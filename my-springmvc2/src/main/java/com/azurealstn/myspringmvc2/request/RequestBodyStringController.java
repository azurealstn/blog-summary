package com.azurealstn.myspringmvc2.request;

import com.azurealstn.myspringmvc2.Hello;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@Controller
public class RequestBodyStringController {

    private ObjectMapper objectMapper = new ObjectMapper();

    @ResponseBody
    @PostMapping("/request-body-string-test")
    public String requestBodyStringTest(@RequestBody String messageBody) throws JsonProcessingException {
        log.info("messageBody={}", messageBody);
        Hello hello = objectMapper.readValue(messageBody, Hello.class);
        log.info("name={}, age={}", hello.getName(), hello.getAge());
        return "OK";
    }
}
