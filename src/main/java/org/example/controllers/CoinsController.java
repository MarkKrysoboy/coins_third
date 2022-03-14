package org.example.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/coins")
public class CoinsController {
    @GetMapping("/admin/update_db")
    public String updateDb(){
        return "update_db";
    }

    @GetMapping("/admin")
    public String admin(){
        return "admin";
    }

    @GetMapping("/")
    public String allCoins(){
        return "coins";
    }
}
