package com.dhanush.SoloSync.Controller;

import com.dhanush.SoloSync.Service.ExcelService;
import com.dhanush.SoloSync.Service.ExpenseService;
import com.dhanush.SoloSync.Service.IncomeService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/Excel")
public class ExcelController {

    @Autowired
    ExcelService excelService;
    @Autowired
    IncomeService incomeService;
    @Autowired
    ExpenseService expenseService;

    @GetMapping("/download/income")
    public void downloadIncomeExcel(HttpServletResponse response) throws IOException{
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition","attachment; filename=income.xlsx");
        excelService.writeIncomesToExcel(response.getOutputStream(),incomeService.getCurrentMonthIncomesForCurrentUser());
    }

    @GetMapping("/download/expense")
    public void downloadExpenseExcel(HttpServletResponse response) throws IOException{
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition","attachment; filename=expense.xlsx");
        excelService.writeExpenseToExcel(response.getOutputStream(),expenseService.getCurrentMonthExpensesForCurrentUser());
    }
}
