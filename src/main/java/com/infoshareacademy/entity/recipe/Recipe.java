package com.infoshareacademy.entity.recipe;


import com.infoshareacademy.entity.product.ProductRecipe;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.util.*;

@Entity
@Table(name = "recipes")
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Column(name = "recipe_id")
    private Long recipeId;

    @NotEmpty
    @Column(name = "name")
    private String name;

    @NotEmpty
    @Column(name = "description")
    private String description;

    @Min(1)
    @Max(120)
    @Column(name = "preparation_time")
    private int preparationTime;

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ProductRecipe> productList = new ArrayList<>();

    private boolean vegetarian;
    private boolean vegan;

    public Recipe() {
    }

    public List<ProductRecipe> getProductList() {
        return productList;
    }

    public void addProduct(ProductRecipe product) {
        this.productList.add(product);
        product.setRecipe(this);
    }

    public boolean containsProduct(String s) {
        boolean flag = false;
        for (ProductRecipe product : productList) {
            if (product.getProductName().contains(s)) {
                flag = true;
            }
        }
        return flag;
    }

    public Long getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(Long id) {
        this.recipeId = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPreparationTime() {
        return preparationTime;
    }

    public void setPreparationTime(int preparationTime) {
        this.preparationTime = preparationTime;
    }

    public void setProductList(List<ProductRecipe> productList) {
        this.productList = productList;
    }

    public boolean isVegetarian() {
        return vegetarian;
    }

    public void setVegetarian(boolean vegetarian) {
        this.vegetarian = vegetarian;
    }

    public boolean isVegan() {
        return vegan;
    }

    public void setVegan(boolean vegan) {
        this.vegan = vegan;
    }

    @Override
    public String toString() {
        return "Recipe{" +
                "id=" + recipeId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", preparationTime=" + preparationTime +
                ", productList=" + productList +
                '}';
    }
}
