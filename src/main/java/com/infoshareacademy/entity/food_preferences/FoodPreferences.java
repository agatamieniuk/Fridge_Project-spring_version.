package com.infoshareacademy.entity.food_preferences;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Pattern;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FoodPreferences {

    @Id
    private Long id;
    private boolean shellfish;
    private boolean chocolate;
    private boolean nuts;
    private boolean eggs;
    private boolean strawberries;
    private boolean dairy;
    @Pattern(regexp = "^[A-Za-z]*$", message = "Pole \"Inne alergeny\" musi być tekstem!")
    private String other;
    private boolean meatEater;
    private boolean isVegan;
    private boolean isVegetarian;

}