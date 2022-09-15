package com.infoshareacademy.repository;

import com.infoshareacademy.entity.shopping_list.ShoppingList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShoppingListRepository extends JpaRepository <ShoppingList, Long>{
}
