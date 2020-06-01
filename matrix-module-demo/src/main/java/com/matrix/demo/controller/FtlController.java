package com.matrix.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author WangCheng
 */
@Controller
@RequestMapping("/ftl")
public class FtlController {

    @GetMapping("/demo.html")
    public String demo() {
        return "demo";
    }
}
