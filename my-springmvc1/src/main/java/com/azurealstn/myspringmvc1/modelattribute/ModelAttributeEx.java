package com.azurealstn.myspringmvc1.modelattribute;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ModelAttributeEx {

    private final ModelService modelService;

    @PostMapping("/model-attribute")
    public String modelAttribute(TestDto testDto) {
        log.info("name={}, age={}", testDto.getName(), testDto.getAge());
        return "OK";
    }

    @PostMapping("/model-attributeV2")
    public String modelAttributeV2(@ModelAttribute TestDto testDto, Model model) {
        //model.addAttribute("testDto", testDto); 생략 가능
        modelService.save();
        return "OK";
    }

}
