package com.microfinance.financeapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CollectionsController {

    @GetMapping("/collections")
    public String collectionsRedirect() {
        return "redirect:/weekly-collection";
    }
}
