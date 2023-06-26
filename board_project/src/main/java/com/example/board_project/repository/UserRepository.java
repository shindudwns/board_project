package com.example.board_project.repository;

import com.example.board_project.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Integer> {
     Optional<User> findByLoginId(String loginId);
}
