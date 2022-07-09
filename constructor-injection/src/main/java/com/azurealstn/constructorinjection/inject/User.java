package com.azurealstn.constructorinjection.inject;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
public class User {

    private Long id;
    private String username;
    private int age;
}
