package com.dhanush.SoloSync.Controller;

import com.dhanush.SoloSync.Dto.ExpenseDTO;
import com.dhanush.SoloSync.Service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/expenses")
public class ExpenseController {

    @Autowired
    ExpenseService expenseService;

    @PostMapping
    public ResponseEntity<ExpenseDTO> addExpense(@RequestBody ExpenseDTO DTO)
    {
        ExpenseDTO saved = expenseService.addExpense(DTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping
    public ResponseEntity<List<ExpenseDTO>> getAllExpensesForCurrentUserForCurrentMonth(){
        List<ExpenseDTO> list = expenseService.getCurrentMonthExpensesForCurrentUser();
        return ResponseEntity.status(HttpStatus.OK).body(list);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteExpenseForCurrentUser(@PathVariable Long id){
        expenseService.deleteExpenseById(id);
        return ResponseEntity.status(HttpStatus.OK).body("Delete the expense successfully");
    }

}
