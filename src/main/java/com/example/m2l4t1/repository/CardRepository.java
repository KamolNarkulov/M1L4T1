package com.example.m2l4t1.repository;

import com.example.m2l4t1.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardRepository extends JpaRepository<Card, Integer> {
    boolean existsByNumber(long number);
}
