package com.azurealstn.constructorinjection.inject;

import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class UserRepository implements MemoryRepository {

    private final Map<Long, User> userMap = new ConcurrentHashMap<>();

    @Override
    public void save(User user) {
        userMap.put(user.getId(), user);
    }

    @Override
    public User findById(Long userId) {
        return userMap.get(userId);
    }
}
