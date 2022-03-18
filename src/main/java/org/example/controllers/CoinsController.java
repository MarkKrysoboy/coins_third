package org.example.controllers;

import org.example.dao.CoinsDAO;
import org.example.models.ReadXlsx;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.annotation.MultipartConfig;

@Controller
@RequestMapping("/coins")
public class CoinsController {

    private final CoinsDAO coinsDAO;

    @Autowired
    public CoinsController(CoinsDAO coinsDAO) {this.coinsDAO = coinsDAO;
    }

    @PostMapping("/update_db")
    public String updateDb(@RequestParam("file") MultipartFile file) {
        System.out.println(file.getName());
        ReadXlsx readXlsx = new ReadXlsx(file);
        coinsDAO.save(readXlsx.readXlsx());
        return "redirect:coins";
    }

    @GetMapping("/update_db")
    public String updateDb() {
//        System.out.println("Ghbdtn");
        return "update_db";
    }

    @GetMapping("/admin")
    public String admin() {
        return "admin";
    }

    @GetMapping()
    public String allCoins(Model model) {
        model.addAttribute("coins", coinsDAO.showAll());
        return "/coins";
    }

    @GetMapping("/{id}")
    public String showCoin(@PathVariable("id") String partNumber, Model model) {
        model.addAttribute("coin", coinsDAO.showCoin(partNumber));
        return "/show_coin";
    }


}
