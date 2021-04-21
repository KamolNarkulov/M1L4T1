package com.example.m2l4t1.service;

import com.example.m2l4t1.dto.ApiResponse;
import com.example.m2l4t1.entity.Income;
import com.example.m2l4t1.repository.CardRepository;
import com.example.m2l4t1.repository.IncomeRepository;
import com.example.m2l4t1.repository.OutcomeRepository;
import com.example.m2l4t1.security.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class IncomeService {
    @Autowired
    OutcomeRepository outcomeRepository;
    @Autowired
    CardRepository cardRepository;
    @Autowired
    JwtProvider jwtProvider;
    @Autowired
    IncomeRepository incomeRepository;

    public List<Income> get(HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getHeader("Authorization");
        token = token.substring(7);
        String userName = jwtProvider.getUserNameFromToken(token);
        List<Income> userIncome = new ArrayList<>();

        List<Income> incomeList = incomeRepository.findAll();
        for (Income income : incomeList) {
            if (income.getTo_card_id().getUsername().equals(userName))
                userIncome.add(income);
        }
        return userIncome;
    }

    public ApiResponse getOne(Integer id, HttpServletRequest httpServletRequest) {

        String token = httpServletRequest.getHeader("Authorization");
        token = token.substring(7);
        String userName = jwtProvider.getUserNameFromToken(token);
        Optional<Income> optionalIncome = incomeRepository.findById(id);

        if (!optionalIncome.isPresent()) return new ApiResponse("income id error", false);

        if (!userName.equals(optionalIncome.get().getTo_card_id().getUsername()))
            return new ApiResponse("username error", false);
        return new ApiResponse("success", true, optionalIncome.get());
    }
}
