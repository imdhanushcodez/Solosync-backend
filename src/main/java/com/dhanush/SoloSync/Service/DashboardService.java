package com.dhanush.SoloSync.Service;

import com.dhanush.SoloSync.Dto.ExpenseDTO;
import com.dhanush.SoloSync.Dto.IncomeDTO;
import com.dhanush.SoloSync.Dto.RecentTransactionDTO;
import com.dhanush.SoloSync.Model.ProfileEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


import static java.util.stream.Stream.concat;

@Service
public class DashboardService {

    @Autowired
    IncomeService incomeService;
    @Autowired
    ExpenseService expenseService;
    @Autowired
    ProfileService profileService;

    public Map<String,Object> getDashboardData(){
        ProfileEntity profileEntity = profileService.getCurrentProfile();
        Map<String,Object> returnValue = new LinkedHashMap<>();
        List<IncomeDTO> latestIncomes = incomeService.getLatest5IncomeForCurrentUser();
        List<ExpenseDTO> latestExpenses = expenseService.getLatest5ExpensesForCurrentUser();
        List<RecentTransactionDTO> recentTransactionDTOS = concat(latestIncomes.stream().map(income -> RecentTransactionDTO.builder()
                .id(income.getId())
                .name(income.getName())
                .profileId(profileEntity.getId())
                .icon(income.getIcon())
                .amount(income.getAmount())
                .date(income.getDate())
                .createdAt(income.getCreatedAt())
                .updatedAt(income.getUpdatedAt())
                .type("income")
                .build()),
                latestExpenses.stream().map(expens -> RecentTransactionDTO.builder()
                        .id(expens.getId())
                        .profileId(profileEntity.getId())
                        .icon(expens.getIcon())
                        .name(expens.getName())
                        .amount(expens.getAmount())
                        .date(expens.getDate())
                        .createdAt(expens.getCreatedAt())
                        .updatedAt(expens.getUpdatedAt())
                        .type("expense")
                        .build())).
                sorted((a,b) -> {
                    int cmp = b.getDate().compareTo(a.getDate());
                    if(cmp == 0 && a.getCreatedAt() != null && b.getCreatedAt() != null){
                        return b.getCreatedAt().compareTo(a.getCreatedAt());
                    }
                    return cmp;
                }).collect(Collectors.toList());

        returnValue.put("totalBalance",incomeService.getTotalIncomeForCurrentUser().subtract(expenseService.getTotalExpensesForCurrentUser()));
        returnValue.put("totalIncome",incomeService.getTotalIncomeForCurrentUser());
        returnValue.put("totalExpense",expenseService.getTotalExpensesForCurrentUser());
        returnValue.put("recent5Expenses",latestExpenses);
        returnValue.put("recent5Incomes",latestIncomes);
        returnValue.put("recentTransactions",recentTransactionDTOS);

        return returnValue;

    }


}
