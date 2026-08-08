package com.example.demo.repository;

import com.example.demo.model.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {
    // JpaRepository provides CRUD operations: findAll(), findById(), save(), deleteById(), etc.
}
