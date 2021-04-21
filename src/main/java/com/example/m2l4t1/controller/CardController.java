package com.example.m2l4t1.controller;

import com.example.m2l4t1.dto.ApiResponse;
import com.example.m2l4t1.dto.CardDto;
import com.example.m2l4t1.entity.Card;
import com.example.m2l4t1.security.JwtProvider;
import com.example.m2l4t1.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RequestMapping("/api/card")
@RestController
public class CardController {
    @Autowired
    CardService cardService;
    @Autowired
    JwtProvider jwtProvider;

    @PostMapping
    public HttpEntity<ApiResponse> add(@RequestBody CardDto cardDto, HttpServletRequest httpServletRequest) {
        ApiResponse apiResponse = cardService.add(cardDto, httpServletRequest);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 209).body(apiResponse);
    }

    @GetMapping
    public ResponseEntity<?> get(HttpServletRequest httpServletRequest) {
        return ResponseEntity.ok(cardService.get(httpServletRequest));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Card> getOne(@PathVariable Integer id, HttpServletRequest httpServletRequest) {
        return ResponseEntity.ok(cardService.getOne(id, httpServletRequest));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> edit(@RequestBody CardDto cardDto,
                                            @PathVariable Integer id, HttpServletRequest httpServletRequest) {
        ApiResponse apiResponse = cardService.edit(id, cardDto, httpServletRequest);
        if (apiResponse.isSuccess()) {
            return ResponseEntity.status(200).body(apiResponse);
        } else {
            return ResponseEntity.status(409).body(apiResponse);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id, HttpServletRequest httpServletRequest) {
        ApiResponse response = cardService.delete(id, httpServletRequest);
        return ResponseEntity.status(response.isSuccess() ? 200 : 409).body(response);
    }

}
