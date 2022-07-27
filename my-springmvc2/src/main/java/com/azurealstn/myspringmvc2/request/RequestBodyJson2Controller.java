package com.azurealstn.myspringmvc2.request;

import com.azurealstn.myspringmvc2.Hello;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
public class RequestBodyJson2Controller {

    @ResponseBody
    @PostMapping("/request-body-json2")
    public String requestBodyJson2(@RequestBody Hello hello) {
        log.info("name={}, age={}", hello.getName(), hello.getAge());
        return "OK";
    }

    @ResponseBody
    @GetMapping("/request-body-json2-v2")
    public Hello requestBodyJson2V2(@ModelAttribute Hello hello) {
        log.info("name={}, age={}", hello.getName(), hello.getAge());
        return hello;
    }
}
