package com.azurealstn.myspringmvc2.request;

import com.azurealstn.myspringmvc2.Hello;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@Controller
public class RequestBodyJsonController {

    @PostMapping("/request-body-json")
    public HttpEntity<String> requestBodyJson(HttpEntity<Hello> httpEntity) {
        Hello hello = httpEntity.getBody();
        log.info("name={}, age={}", hello.getName(), hello.getAge());
        log.info("headers={}", httpEntity.getHeaders());
        return new HttpEntity<>(HttpStatus.OK.getReasonPhrase());
    }

    @PostMapping("request-body-json-v2")
    public ResponseEntity<HttpStatus> requestBodyJsonV2(RequestEntity<Hello> requestEntity) {
        Hello hello = requestEntity.getBody();
        log.info("name={}, age={}", hello.getName(), hello.getAge());
        log.info("url={}", requestEntity.getUrl());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
