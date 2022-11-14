package com.infoshareacademy.service;

import com.infoshareacademy.DTO.RecipeAllergensDto;
import com.infoshareacademy.DTO.RecipeDto;
import com.infoshareacademy.entity.product.ProductInFridge;
import com.infoshareacademy.entity.recipe.Meal;
import com.infoshareacademy.entity.recipe.Recipe;
import com.infoshareacademy.entity.recipe.RecipeAllergens;
import com.infoshareacademy.repository.RecipeAllergensRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.infoshareacademy.repository.RecipeRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class RecipeService {

    private final RecipeRepository recipeRepository;

    private final RecipeAllergensRepository allergensRepository;

    private final FridgeService fridgeService;

    private static Logger LOGGER = LogManager.getLogger(RecipeService.class.getName());

    private final ModelMapper modelMapper;

    @Autowired
    public RecipeService(RecipeRepository recipeRepository, RecipeAllergensRepository allergensRepository, FridgeService fridgeService, ModelMapper modelMapper) {
        this.recipeRepository = recipeRepository;
        this.allergensRepository = allergensRepository;
        this.fridgeService = fridgeService;
        this.modelMapper = modelMapper;
    }


    public List<RecipeDto> getAllRecipe() {
        return recipeRepository.findAll().stream().map(recipeDto -> modelMapper.map(recipeDto, RecipeDto.class)).toList();
    }

    public Page<RecipeDto> getSearchRecipe(String keyword, Pageable pageable) {
        if (keyword != null) {
            return new PageImpl<>(recipeRepository.findRecipeBy(keyword).stream().map(recipeDto -> modelMapper.map(recipeDto, RecipeDto.class)).collect(Collectors.toList()));
        }
        return new PageImpl<>(recipeRepository.findAll(pageable).stream().map(recipeDto -> modelMapper.map(recipeDto, RecipeDto.class)).collect(Collectors.toList()));
    }

    public RecipeDto getRecipeById(Long id) {
        RecipeDto recipe = new RecipeDto();
        if (recipeRepository.findById(id).isPresent())
            recipe = modelMapper.map(recipeRepository.findById(id).get(), RecipeDto.class);
        return recipe;
    }

    @Transactional
    public Page<RecipeDto> getRecipesByCanFilterByMeal(Meal meal, Pageable pageable) {
        Page<Recipe> pageRecipes = recipeRepository.findAll(pageable);
        if (meal != null && meal != Meal.ALL) {
            pageRecipes = recipeRepository.findRecipeByMeal(meal, pageable);
        }
        Page<RecipeDto> recipeDtos = pageRecipes.map(recipe -> modelMapper.map(recipe, RecipeDto.class));
        return recipeDtos;
    }

    public void saveRecipeAllergens(Long id, RecipeAllergensDto allergens) {
        Recipe recipe = new Recipe();
        recipe.setUserId(fridgeService.getDEFAULT_FRIDGE_ID());
        if (recipeRepository.findById(id).isPresent()) recipe = recipeRepository.findById(id).get();
        RecipeAllergens existingAllergens;
        if (allergensRepository.findById(recipe.getRecipeAllergens().getId()).isPresent()) {
            existingAllergens = allergensRepository.findById(recipe.getRecipeAllergens().getId()).get();
            existingAllergens.setRecipe(recipe);
            existingAllergens.setChocolate(allergens.isChocolate());
            existingAllergens.setDairy(allergens.isDairy());
            existingAllergens.setEggs(allergens.isEggs());
            existingAllergens.setMeatEater(allergens.isMeatEater());
            existingAllergens.setNuts(allergens.isNuts());
            existingAllergens.setOther(allergens.getOther());
            existingAllergens.setShellfish(allergens.isShellfish());
            existingAllergens.setStrawberries(allergens.isStrawberries());
            existingAllergens.setVegan(allergens.isVegan());
            existingAllergens.setVegetarian(allergens.isVegetarian());
            allergensRepository.save(existingAllergens);
            LOGGER.info("Zapisano preferencje żywieniowe!");

        }
    }

    public void saveRecipe(RecipeDto recipeDto) {
        Recipe recipe = modelMapper.map(recipeDto, Recipe.class);
        recipe.getProductList().forEach(x -> x.setRecipe(recipe));
        recipe.getRecipeAllergens().setRecipe(recipe);
        recipe.setUserId(fridgeService.getDEFAULT_FRIDGE_ID());
        LOGGER.info("Zapisano przepis: " + recipe.getName());
        recipeRepository.save(recipe);
    }


    public void updateRecipe(Long recipeId, RecipeDto recipeDto) {
        Recipe recipe = modelMapper.map(recipeDto, Recipe.class);
        Recipe existingRecipe = new Recipe();
        existingRecipe.setUserId(fridgeService.getDEFAULT_FRIDGE_ID());
        if (recipeRepository.findById(recipeId).isPresent()) existingRecipe = recipeRepository.findById(recipeId).get();
        existingRecipe.setRecipeId(recipeId);
        existingRecipe.setName(recipe.getName());
        existingRecipe.setDescription(recipe.getDescription());
        existingRecipe.setPreparationTime(recipe.getPreparationTime());
        existingRecipe.setMeal(recipe.getMeal());
        recipeRepository.save(existingRecipe);
        LOGGER.info("Zaktualizowano przepis: " + recipe.getName());
    }

    @Transactional
    public void deleteRecipeById(Long id) {
        recipeRepository.deleteByRecipeId(id);
    }

    public void deleteAllRecipes() {
        recipeRepository.deleteAll();
    }

    public List<RecipeDto> getRecipesWithProductsToLowerCase() {
        List<RecipeDto> recipes = getAllRecipe().stream()
                .peek(recipe -> recipe.getProductList()
                        .forEach(productRecipe -> productRecipe
                                .setProductName(productRecipe.getProductName().toLowerCase()))).toList();
        return recipes;
    }

    public Map<RecipeDto, List<RecipeDto.ProductRecipeDto>> mapRecipeProducts() {
        Map<RecipeDto, List<RecipeDto.ProductRecipeDto>> mapRecipesProducts = getRecipesWithProductsToLowerCase().stream()
                .collect(Collectors.toMap(Function.identity(), RecipeDto::getProductList));
        return mapRecipesProducts;
    }

    public Page<RecipeDto> getRecipeByProductsInFridge(Pageable pageable) {

        //TODO: zmienić wczytywanie przepisów po ID
        Map<String, ProductInFridge> productsInFridge = fridgeService.mapProductsInFridgeWithNameAsKey();
        List<RecipeDto> filteredRecipies = new ArrayList<>();
        Map<RecipeDto, List<RecipeDto.ProductRecipeDto>> mapRecipesProducts = mapRecipeProducts();

        for (Map.Entry<RecipeDto, List<RecipeDto.ProductRecipeDto>> entry : mapRecipesProducts.entrySet()) {
            List<RecipeDto.ProductRecipeDto> tempRecipeList = entry.getValue();
            int matchScore = tempRecipeList.size();
            for (RecipeDto.ProductRecipeDto productRecipe : tempRecipeList) {
                ProductInFridge productInFridge = productsInFridge.get(productRecipe.getProductName());

                if (productInFridge != null
                        && productInFridge.getAmount() >= productRecipe.getAmount()) {
                    matchScore--;
                }
            }
            if (matchScore == 0) {
                filteredRecipies.add(entry.getKey());
            }
        }
        return new PageImpl<>(filteredRecipies);
    }

}