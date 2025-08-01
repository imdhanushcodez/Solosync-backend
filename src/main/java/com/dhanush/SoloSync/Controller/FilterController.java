package com.dhanush.SoloSync.Controller;

import com.dhanush.SoloSync.Dto.ExpenseDTO;
import com.dhanush.SoloSync.Dto.FilterDTO;
import com.dhanush.SoloSync.Dto.IncomeDTO;
import com.dhanush.SoloSync.Service.ExpenseService;
import com.dhanush.SoloSync.Service.IncomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/filter")
public class FilterController {

    @Autowired
    IncomeService incomeService;
    @Autowired
    ExpenseService expenseService;

    @PostMapping
    public ResponseEntity<?> filterTransactions(@RequestBody FilterDTO filterDTO){

        //preparing the data or validation
        LocalDate startDate = filterDTO.getStartDate() != null ? filterDTO.getStartDate():LocalDate.MIN;
        LocalDate endDate = filterDTO.getEndDate() != null ? filterDTO.getEndDate():LocalDate.now();
        String keyword = filterDTO.getKeyword() != null ? filterDTO.getKeyword():"";
        String sortField = filterDTO.getSortField() != null ? filterDTO.getSortField() : "date";
        Sort.Direction direction = "desc".equalsIgnoreCase(filterDTO.getSortOrder()) ? Sort.Direction.DESC:Sort.Direction.ASC;
        Sort sort = Sort.by(direction,sortField);

        if("income".equalsIgnoreCase(filterDTO.getType())){
            List<IncomeDTO> incomeDTOS = incomeService.filterIncomes(startDate,endDate,keyword,sort);
            return ResponseEntity.ok(incomeDTOS);
        }
        else if("expense".equalsIgnoreCase(filterDTO.getType())){
            List<ExpenseDTO> expenseDTOS = expenseService.filterExpenses(startDate,endDate,keyword,sort);
            return ResponseEntity.ok(expenseDTOS);
        }
        else{
            return ResponseEntity.badRequest().body("Invalid type. Must be 'income' or 'expense' ");
        }
    }

}
