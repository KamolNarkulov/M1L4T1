package com.example.m2l4t1.service;

import com.example.m2l4t1.dto.ApiResponse;
import com.example.m2l4t1.dto.CardDto;
import com.example.m2l4t1.entity.Card;
import com.example.m2l4t1.repository.CardRepository;
import com.example.m2l4t1.security.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CardService {
    @Autowired
    CardRepository repository;
    @Autowired
    JwtProvider jwtProvider;

    public ApiResponse add(CardDto cardDto, HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getHeader("Authorization");
        token = token.substring(7);
        String userName = jwtProvider.getUserNameFromToken(token);
        Card card = new Card();
        card.setUsername(userName);
        card.setNumber(cardDto.getNumber());
        card.setExpired_date(cardDto.getExpired_date());
        repository.save(card);
        return new ApiResponse("added", true);
    }

    public ApiResponse edit(Integer id, CardDto cardDto, HttpServletRequest httpServletRequest) {

        String token = httpServletRequest.getHeader("Authorization");
        token = token.substring(7);
        String userName = jwtProvider.getUserNameFromToken(token);
        Optional<Card> optionalCard = repository.findById(id);

        if (!optionalCard.isPresent()) return new ApiResponse("Card ID not Found", false);
        if (!userName.equals(optionalCard.get().getUsername()))
            return new ApiResponse("Username not found", false);

        Card card = optionalCard.get();
        card.setNumber(cardDto.getNumber());
        card.setExpired_date(cardDto.getExpired_date());
        repository.save(card);
        return new ApiResponse("edited", true);
    }

    public ApiResponse delete(Integer id, HttpServletRequest httpServletRequest) {

        String token = httpServletRequest.getHeader("Authorization");
        token = token.substring(7);
        String userName = jwtProvider.getUserNameFromToken(token);
        try {
            Optional<Card> optionalCard = repository.findById(id);
            if (!optionalCard.isPresent()) return new ApiResponse("Card ID Not Found", false);
            if (!userName.equals(optionalCard.get().getUsername()))
                return new ApiResponse("Username Not Found", false);
            repository.deleteById(id);
            return new ApiResponse("deleted.", true);
        } catch (Exception e) {
            return new ApiResponse("Error", false);
        }
    }

    public List<Card> get(HttpServletRequest httpServletRequest) {

        String token = httpServletRequest.getHeader("Authorization");
        token = token.substring(7);
        String userName = jwtProvider.getUserNameFromToken(token);
        List<Card> userCards = new ArrayList<>();
        List<Card> all = repository.findAll();
        for (Card card : all) {
            if (card.getUsername().equals(userName))
                userCards.add(card);
        }
        return userCards;
    }


    public Card getOne(Integer id, HttpServletRequest httpServletRequest) {

        String token = httpServletRequest.getHeader("Authorization");
        token = token.substring(7);
        String userName = jwtProvider.getUserNameFromToken(token);
        Optional<Card> optionalCard = repository.findById(id);
        if (!optionalCard.isPresent()) return null;
        if (!userName.equals(optionalCard.get().getUsername()))
            return null;
        return optionalCard.orElse(null);
    }
}
