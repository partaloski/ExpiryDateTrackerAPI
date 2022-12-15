package com.example.expirydatetrackerapi.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/")
public class HomeController {
    @GetMapping("")
    public String getHomePage(@RequestParam(required = false) String lang){
        if(lang == null || lang.contentEquals("EN")){
            return "home-page-en";
        }
        else{
            return "home-page-mk";
        }
    }
}
