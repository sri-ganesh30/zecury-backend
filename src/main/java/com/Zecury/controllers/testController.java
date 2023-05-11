package com.Zecury.controllers;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/")
public class testController {
    @GetMapping("/")
    public String test()
    {
        return "Success";
    }
}
