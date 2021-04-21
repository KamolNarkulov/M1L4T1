package com.example.m2l4t1.controller;

import com.example.m2l4t1.service.IncomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/income")
public class IncomeController {

    @Autowired
    IncomeService incomeService;

    @GetMapping
    public ResponseEntity<?> get(HttpServletRequest httpServletRequest){
        return ResponseEntity.ok(incomeService.get(httpServletRequest));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?>getOne(@PathVariable Integer id, HttpServletRequest httpServletRequest){
        return ResponseEntity.ok(incomeService.getOne(id, httpServletRequest));
    }
}
