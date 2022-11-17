package com.infoshareacademy.controller;

import com.infoshareacademy.DTO.FoodPreferencesDto;
import com.infoshareacademy.service.FoodPreferencesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class FoodPreferencesController {

    private final FoodPreferencesService foodPreferencesService;

    @Autowired
    public FoodPreferencesController(FoodPreferencesService foodPreferencesService) {
        this.foodPreferencesService = foodPreferencesService;
    }

    @GetMapping("/food-preferences")
    public String listFoodPreferences(Model model) {
        model.addAttribute("getfoodpreferences", foodPreferencesService.getFoodPreferences());
        return "food-preferences";
    }

    @GetMapping("/food-preferences/set-foodpreferences")
    public String createFoodPreferencesForm(Model model) {
        model.addAttribute("foodpreferences", foodPreferencesService.checkIfFoodPreferencesIsSet());
        return "set-food-preferences";
    }

    @PostMapping("/food-preferences")
    public String saveFoodPreferences(@ModelAttribute("foodpreferences") FoodPreferencesDto foodPreferencesDto) {
        foodPreferencesService.setFoodPreferences(foodPreferencesDto);
        return "redirect:/food-preferences";
    }

}