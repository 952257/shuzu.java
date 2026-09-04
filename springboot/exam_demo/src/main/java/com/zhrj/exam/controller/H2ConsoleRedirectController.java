package com.zhrj.exam.controller;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Profile("h2")
@Controller
public class H2ConsoleRedirectController {

    @GetMapping("/h2-console")
    public String redirect() {
        return "redirect:/h2-console/";
    }
}
