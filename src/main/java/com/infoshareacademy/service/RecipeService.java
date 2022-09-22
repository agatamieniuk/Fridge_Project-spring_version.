package com.infoshareacademy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.infoshareacademy.entity.recipe.Recipe;
import com.infoshareacademy.other.Json;
import com.infoshareacademy.repository.RecipeRepository;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

@Service
public class RecipeService {

    private RecipeRepository recipeRepository;

    @Autowired
    public RecipeService(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    public List<Recipe> getAllRecipe() {
        return recipeRepository.findAll();
    }

    public List<Recipe> getSearchRecipe(String keyword) {
        if (keyword != null) {
            return recipeRepository.findRecipeBy(keyword);
        }
        return recipeRepository.findAll();
    }

    public Recipe getRecipeById(Long id) {
        return recipeRepository.findById(id).get();
    }

    public Recipe saveRecipe(Recipe recipe) {
        recipe.getProductList().forEach(x -> x.setRecipe(recipe));
        return recipeRepository.save(recipe);
    }


    public void updateRecipe(Long recipeId, Recipe recipe){

        Recipe existingRecipe = recipeRepository.findById(recipeId).get();
        existingRecipe.setRecipeId(recipeId);
        existingRecipe.setName(recipe.getName());
        existingRecipe.setDescription(recipe.getDescription());
        recipeRepository.save(existingRecipe);
    }

    public void deleteRecipeById(Long id) {
        recipeRepository.deleteById(id);
    }

}