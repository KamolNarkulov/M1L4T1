package com.example.m2l4t1.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OutcomeDto {
    private Integer from_card_id;

    private Integer to_card_id;

    private double amount;

    private double commission_amount;
}
