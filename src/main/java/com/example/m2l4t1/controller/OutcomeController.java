package com.example.m2l4t1.controller;

import com.example.m2l4t1.dto.ApiResponse;
import com.example.m2l4t1.dto.OutcomeDto;
import com.example.m2l4t1.service.OutcomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/outcome")
public class OutcomeController {

    @Autowired
    OutcomeService outcomeService;

    @PostMapping
    public HttpEntity<ApiResponse> add(@RequestBody OutcomeDto outcomeDto, HttpServletRequest httpServletRequest) {
        ApiResponse apiResponse = outcomeService.add(outcomeDto, httpServletRequest);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @GetMapping
    public ResponseEntity<?> get(HttpServletRequest httpServletRequest) {
        return ResponseEntity.ok(outcomeService.getAll(httpServletRequest));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOne(@PathVariable Integer id, HttpServletRequest httpServletRequest) {
        return ResponseEntity.ok(outcomeService.getOne(id, httpServletRequest));
    }
}
