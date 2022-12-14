package com.infoshareacademy.controller;

import com.infoshareacademy.entity.fridge.Fridge;
import com.infoshareacademy.entity.product.ProductInFridge;
import com.infoshareacademy.repository.FridgeRepository;
import com.infoshareacademy.service.FridgeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
@RequestMapping("/fridge")
public class FridgeController {

    private final FridgeService fridgeService;
    private final FridgeRepository fridgeRepository;

    @Autowired
    public FridgeController(FridgeService fridgeService, FridgeRepository fridgeRepository) {
        this.fridgeService = fridgeService;
        this.fridgeRepository = fridgeRepository;
    }

    @GetMapping
    public String productsInFridge(Model model) {
        model.addAttribute("productsinfridge", fridgeService.getAllProductsFromFridge());
        return "fridge";
    }

    @GetMapping("/product")
    public String addProductsToFridgeForm(Model model, Fridge fridge) {
        fridgeService.addProductsToFridgeForm(fridge);
        model.addAttribute("fridge", fridge);
        return "addproductstofridge";
    }

    @PostMapping("/product")
    public String saveFridge(@Valid @ModelAttribute("fridge") Fridge fridge, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "addproductstofridge";
        }
        fridge.setFridgeId(fridgeService.getUserId());
        fridgeService.saveFridge(fridge);
        return "redirect:/fridge";
    }

    @PostMapping(value = "/product", params = {"addProduct"})
    public String addProduct(@ModelAttribute("fridge") Fridge fridge) {
        fridge.addProduct(new ProductInFridge());
        return "addproductstofridge";
    }

    @PostMapping(value = "/product", params = {"removeProduct"})
    public String removeProduct(@ModelAttribute("fridge") Fridge fridge,
                                HttpServletRequest request) {
        int index = Integer.parseInt(request.getParameter("removeProduct"));
        fridge.getProductsInFridge().remove(index);
        return "addproductstofridge";
    }
}
