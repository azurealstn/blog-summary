package com.azurealstn.myspringmvc1.modelattribute;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.ArrayList;
import java.util.List;

@Controller
public class AnotherModelAttribute {

    @ModelAttribute("list")
    public List<String> list() {
        List<String> list = new ArrayList<>();
        list.add("키보드");
        list.add("마우스");
        list.add("모니터");
        return list;
    }

    @GetMapping("/ex1")
    public String ex1(TestDto reqDto, Model model) {
        return "hello/test";
    }
    @GetMapping("/ex2")
    public String ex2(TestDto reqDto, Model model) {
        return "hello/test";
    }
    @GetMapping("/ex3")
    public String ex3(TestDto reqDto, Model model) {
        return "hello/test";
    }
}
