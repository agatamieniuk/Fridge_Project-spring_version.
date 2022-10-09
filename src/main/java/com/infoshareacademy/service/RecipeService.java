package com.infoshareacademy.service;

import com.infoshareacademy.entity.recipe.Meal;
import com.infoshareacademy.entity.recipe.RecipeAllegrens;
import com.infoshareacademy.repository.RecipeAllergensRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.infoshareacademy.entity.recipe.Recipe;
import com.infoshareacademy.repository.RecipeRepository;
import java.util.List;

@Service
public class RecipeService {

    private final RecipeRepository recipeRepository;

    private final RecipeAllergensRepository allergensRepository;

    @Autowired
    public RecipeService(RecipeRepository recipeRepository, RecipeAllergensRepository allergensRepository) {
        this.recipeRepository = recipeRepository;
        this.allergensRepository = allergensRepository;
    }

    public List<Recipe> getAllRecipe() {
        return recipeRepository.findAll();
    }

    public Page<Recipe> getSearchRecipe(String keyword, Pageable pageable) {
        if (keyword != null) {
            return new PageImpl<>(recipeRepository.findRecipeBy(keyword));
        }
        return recipeRepository.findAll(pageable);
    }

    public Recipe getRecipeById(Long id) {
        Recipe recipe = new Recipe();
        if (recipeRepository.findById(id).isPresent()) recipe = recipeRepository.findById(id).get();
        return recipe;
    }

    public Page<Recipe> getRecipesByCanFilterByMeal(Meal meal, Pageable pageable) {
        if (meal != null) {
            List<Recipe> recipeList = recipeRepository.findAll();

            switch (meal) {

                case BREAKFAST:
                    recipeList = recipeList.stream()
                            .filter(recipe -> recipe.getMeal().equals(Meal.BREAKFAST))
                            .toList();
                    break;
                case LUNCH:
                    recipeList = recipeList.stream()
                            .filter(recipe -> recipe.getMeal().equals(Meal.LUNCH))
                            .toList();
                    break;
                case SUPPER:
                    recipeList = recipeList.stream()
                            .filter(recipe -> recipe.getMeal().equals(Meal.SUPPER))
                            .toList();
                    break;
                case DINNER:
                    recipeList = recipeList.stream()
                            .filter(recipe -> recipe.getMeal().equals(Meal.DINNER))
                            .toList();
                    break;
                case ALL:
                    recipeList = recipeRepository.findAll();
                    break;
            }

            return new PageImpl<>(recipeList);
        } else return recipeRepository.findAll(pageable);
    }

    public void saveRecipeAllergens(Long id, RecipeAllegrens allergens) {
        Recipe recipe = new Recipe();
        if (recipeRepository.findById(id).isPresent()) recipe = recipeRepository.findById(id).get();
        RecipeAllegrens existingAllergens = new RecipeAllegrens();
        if (allergensRepository.findById(recipe.getRecipeAllegrens().getId()).isPresent()) {
            existingAllergens = allergensRepository.findById(recipe.getRecipeAllegrens().getId()).get();
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

        }
    }

    public Recipe saveRecipe(Recipe recipe) {
        recipe.getProductList().forEach(x -> x.setRecipe(recipe));
        recipe.getRecipeAllegrens().setRecipe(recipe);
        return recipeRepository.save(recipe);
    }


    public void updateRecipe(Long recipeId, Recipe recipe) {

        Recipe existingRecipe = new Recipe();
        if (recipeRepository.findById(recipeId).isPresent()) existingRecipe = recipeRepository.findById(recipeId).get();
        existingRecipe.setRecipeId(recipeId);
        existingRecipe.setName(recipe.getName());
        existingRecipe.setDescription(recipe.getDescription());
        existingRecipe.setPreparationTime(recipe.getPreparationTime());
        existingRecipe.setRecipeAllegrens(recipe.getRecipeAllegrens());
        existingRecipe.setMeal(recipe.getMeal());
        recipeRepository.save(existingRecipe);
    }

    public void deleteRecipeById(Long id) {
        recipeRepository.deleteById(id);
    }

}