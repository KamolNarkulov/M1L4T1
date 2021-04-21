package com.example.m2l4t1.service;

import com.example.m2l4t1.dto.ApiResponse;
import com.example.m2l4t1.dto.OutcomeDto;
import com.example.m2l4t1.entity.Card;
import com.example.m2l4t1.entity.Income;
import com.example.m2l4t1.entity.Outcome;
import com.example.m2l4t1.repository.CardRepository;
import com.example.m2l4t1.repository.IncomeRepository;
import com.example.m2l4t1.repository.OutcomeRepository;
import com.example.m2l4t1.security.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class OutcomeService {
    @Autowired
    OutcomeRepository outcomeRepository;
    @Autowired
    CardRepository cardRepository;
    @Autowired
    JwtProvider jwtProvider;
    @Autowired
    IncomeRepository incomeRepository;

    public ApiResponse add(OutcomeDto outcomeDto, HttpServletRequest httpServletRequest) {

        Optional<Card> optionalCardTo = cardRepository.findById(outcomeDto.getTo_card_id());

        if (!optionalCardTo.isPresent()) return new ApiResponse("error", false);

        Optional<Card> optionalCardFrom = cardRepository.findById(outcomeDto.getFrom_card_id());

        if (!optionalCardFrom.isPresent()) return new ApiResponse("error", false);

        Card cardFrom = optionalCardFrom.get();
        Card cardTo = optionalCardTo.get();

        String token = httpServletRequest.getHeader("Authorization");
        token = token.substring(7);
        String userName = jwtProvider.getUserNameFromToken(token);
        if (!userName.equals(cardFrom.getUsername())) return
                new ApiResponse("card ID error", false);

        Double balance = cardFrom.getBalance();
        Double totalAmount = outcomeDto.getAmount() + (outcomeDto.getAmount() / 100 * outcomeDto.getCommission_amount());
        if (balance < totalAmount) return new ApiResponse("Balance is not enough", false);


        Outcome outcome = new Outcome();
        Income income = new Income();

        outcome.setAmount(outcomeDto.getAmount());
        outcome.setCommission_amount(outcome.getCommission_amount());
        outcome.setFrom_card_id(cardFrom);
        outcome.setTo_card_id(cardTo);
        outcome.setDate(new Date());
        outcomeRepository.save(outcome);

        income.setAmount(outcomeDto.getAmount());
        income.setDate(new Date());
        income.setFrom_card_id(cardFrom);
        income.setTo_card_id(cardTo);
        incomeRepository.save(income);

        cardFrom.setBalance(balance - totalAmount);
        cardTo.setBalance(cardTo.getBalance() + outcomeDto.getAmount());
        cardRepository.save(cardFrom);
        cardRepository.save(cardTo);


        return new ApiResponse("success", true);
    }


    public List<Outcome> getAll(HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getHeader("Authorization");
        token = token.substring(7);
        String userName = jwtProvider.getUserNameFromToken(token);
        List<Outcome> userOutcome = new ArrayList<>();

        List<Outcome> all = outcomeRepository.findAll();
        for (Outcome outcome : all) {
            if (outcome.getFrom_card_id().getUsername().equals(userName))
                userOutcome.add(outcome);
        }
        return userOutcome;
    }

    public ApiResponse getOne(Integer id, HttpServletRequest httpServletRequest) {

        String token = httpServletRequest.getHeader("Authorization");
        token = token.substring(7);
        String userName = jwtProvider.getUserNameFromToken(token);
        Optional<Outcome> optionalOutcome = outcomeRepository.findById(id);

        if (!optionalOutcome.isPresent()) return new ApiResponse("outcome id error", false);

        if (!userName.equals(optionalOutcome.get().getFrom_card_id().getUsername()))
            return new ApiResponse("username error", false);
        return new ApiResponse("success", true, optionalOutcome.get());
    }
}
