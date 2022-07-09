package com.azurealstn.constructorinjection.inject;

import org.springframework.stereotype.Repository;

@Repository
public interface MemoryRepository {

    void save(User user);

    User findById(Long userId);
}
