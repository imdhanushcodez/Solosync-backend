package com.dhanush.SoloSync.Service;

import com.dhanush.SoloSync.Dto.ExpenseDTO;
import com.dhanush.SoloSync.Dto.IncomeDTO;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.stream.IntStream;

@Service
public class ExcelService {

    public void writeIncomesToExcel(OutputStream os, List<IncomeDTO> incomes) throws IOException{
        try(Workbook workbook = new XSSFWorkbook())
        {
            Sheet sheet = workbook.createSheet("Incomes");
            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("S.No");
            header.createCell(1).setCellValue("Name");
            header.createCell(2).setCellValue("Category");
            header.createCell(3).setCellValue("Amount");
            header.createCell(4).setCellValue("Date");
            IntStream.range(0,incomes.size())
                    .forEach(i -> {
                        IncomeDTO income = incomes.get(i);
                        Row row = sheet.createRow(i+1);
                        row.createCell(0).setCellValue(i+1);
                        row.createCell(1).setCellValue(income.getName() != null ? income.getName():"N/A");
                        row.createCell(2).setCellValue(income.getCategoryName() != null ? income.getCategoryName():"N/A");
                        row.createCell(3).setCellValue(income.getAmount() != null ? income.getAmount().toString():"N/A");
                        row.createCell(4).setCellValue(income.getDate() != null ? income.getDate().toString():"N/A");
                    });
            workbook.write(os);
        }
        catch (Exception e){

        }
    }


    public void writeExpenseToExcel(OutputStream os, List<ExpenseDTO> incomes) throws IOException{
        try(Workbook workbook = new XSSFWorkbook())
        {
            Sheet sheet = workbook.createSheet("Expenses");
            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("S.No");
            header.createCell(1).setCellValue("Name");
            header.createCell(2).setCellValue("Category");
            header.createCell(3).setCellValue("Amount");
            header.createCell(4).setCellValue("Date");
            IntStream.range(0,incomes.size())
                    .forEach(i -> {
                        ExpenseDTO income = incomes.get(i);
                        Row row = sheet.createRow(i+1);
                        row.createCell(0).setCellValue(i+1);
                        row.createCell(1).setCellValue(income.getName() != null ? income.getName():"N/A");
                        row.createCell(2).setCellValue(income.getCategoryName() != null ? income.getCategoryName():"N/A");
                        row.createCell(3).setCellValue(income.getAmount() != null ? income.getAmount().toString():"N/A");
                        row.createCell(4).setCellValue(income.getDate() != null ? income.getDate().toString():"N/A");
                    });
            workbook.write(os);
        }
        catch (Exception e){

        }
    }

}
