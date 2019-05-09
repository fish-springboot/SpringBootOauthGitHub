package com.github.fish56.oauthgithub.repository;

import com.github.fish56.oauthgithub.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {
    public User findByToken(String token);
}
