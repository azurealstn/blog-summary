package com.azurealstn.constructorinjection.inject;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    //유저 생성
    public void saveUser(User user) {
        userRepository.save(user);
    }

    //유저 조회
    public User selectUser(Long id) {
        return userRepository.findById(id);
    }
}
