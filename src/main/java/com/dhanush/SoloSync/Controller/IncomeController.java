package com.dhanush.SoloSync.Controller;


import com.dhanush.SoloSync.Dto.IncomeDTO;
import com.dhanush.SoloSync.Service.IncomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/incomes")
public class IncomeController {

    @Autowired
    IncomeService incomeService;

    @PostMapping
    public ResponseEntity<IncomeDTO> addIncome(@RequestBody IncomeDTO DTO){
        IncomeDTO saved = incomeService.addIncome(DTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping
    public ResponseEntity<List<IncomeDTO>> getAllIncomesForCurrentUserForCurrentMonth(){
        List<IncomeDTO> list = incomeService.getCurrentMonthIncomesForCurrentUser();
        return ResponseEntity.status(HttpStatus.OK).body(list);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteIncomeForCurrentUser(@PathVariable Long id){
        incomeService.deleteIncomeById(id);
        return ResponseEntity.status(HttpStatus.OK).body("Delete the income successfully");
    }
}
