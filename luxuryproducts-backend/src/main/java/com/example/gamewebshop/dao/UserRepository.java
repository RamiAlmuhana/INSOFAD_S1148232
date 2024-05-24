package com.example.gamewebshop.dao;

import com.example.gamewebshop.models.CustomUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<CustomUser, Long> {
    CustomUser findByEmail(String email);
}
