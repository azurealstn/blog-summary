package com.azurealstn.myspringmvc1.requestparam;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class RequestParamEx {

    @GetMapping("/param1")
    public String param1(
            @RequestParam("name") String name,
            @RequestParam("age") int age
    ) {
        log.info("name={}, age={}", name, age);
        return "OK";
    }

    @GetMapping("/param2")
    public String param2(
            @RequestParam String name,
            @RequestParam int age
    ) {
        log.info("name={}, age={}", name, age);
        return "OK";
    }

    @GetMapping("/param3")
    public String param3(String name, int age) {
        log.info("name={}, age={}", name, age);
        return "OK";
    }

    @PostMapping("/param4")
    public String param4(
            @RequestParam String name,
            @RequestParam Integer age
    ) {
        log.info("name={}, age={}", name, age);
        return "OK";
    }
}
