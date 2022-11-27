package com.infoshareacademy.DTO;

import com.infoshareacademy.entity.recipe.Meal;
import com.infoshareacademy.entity.recipe.Recipe;
import com.infoshareacademy.entity.recipe.RecipeAllergens;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.io.Serializable;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecipeAllergensDto implements Serializable {
    private Long id;
    private boolean chocolate;
    private boolean nuts;
    private boolean eggs;
    private boolean strawberries;
    private boolean shellfish;
    private boolean dairy;
    @Pattern(regexp = "^[A-Za-z]*$", message = "Pole \"Inne alergeny\" musi być tekstem!")
    private String other;
    private boolean meatEater;
    private boolean isVegan;
    private boolean isVegetarian;
    private RecipeDto recipe;

    /**
     * A DTO for the {@link Recipe} entity
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RecipeDto implements Serializable {
        private Long recipeId;
        @NotEmpty
        private String name;
        @NotEmpty
        private String description;
        @Min(1)
        @Max(120)
        private int preparationTime;
        private Meal meal;
        private Long userId;
    }
}