package org.example.controllers;

import org.example.dao.CoinsDAO;
import org.example.models.ReadXlsx;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;


@Controller
@RequestMapping("/coins")
public class CoinsController {

    private final CoinsDAO coinsDAO;

    @Autowired
    public CoinsController(CoinsDAO coinsDAO) {
        this.coinsDAO = coinsDAO;
    }

    @GetMapping("/update_db")
    public String updateDb(Model model) {
//        model.addAttribute("readXlsx", new ReadXlsx());
        return "update_db";
    }

//    @PostMapping("/update_db")
//    public String addInf(@ModelAttribute("readXlsx") ReadXlsx readXlsx) {
//        System.out.println(readXlsx.toString());
//        coinsDAO.save(readXlsx.readXlsx());
//        return "redirect:/coins";
//    }

    @PostMapping("/coins/update_db")
    public String handleFileUpload(@RequestParam("file") MultipartFile file) {
        if (!file.isEmpty()) {
            try {
                ReadXlsx readXlsx = new ReadXlsx(file);
                coinsDAO.save(readXlsx.readXlsx());

                System.out.println("File is saved under: well ");
                return "redirect:/coins";

            } catch (Exception e) {
                e.printStackTrace();
                return "File upload is failed: " + e.getMessage();
            }
        } else {
            return "File upload is failed: File is empty";
        }
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

    @GetMapping("/query")
    public String queryCoins(Model model, @RequestParam("query") String query, @RequestParam("value") String value) {
        model.addAttribute("coins", coinsDAO.showQuery(query, value));
        return "/coins";
    }

    @GetMapping("/{id}")
    public String showCoin(@PathVariable("id") String partNumber, Model model) {
        model.addAttribute("coin", coinsDAO.showCoin(partNumber));
        return "/show_coin";
    }


}
