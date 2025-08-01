package com.dhanush.SoloSync.Service;

import com.dhanush.SoloSync.Dto.ExpenseDTO;
import com.dhanush.SoloSync.Model.CategoryEntity;
import com.dhanush.SoloSync.Model.ExpenseEntity;
import com.dhanush.SoloSync.Model.ProfileEntity;
import com.dhanush.SoloSync.Repository.CategoryRepository;
import com.dhanush.SoloSync.Repository.ExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class ExpenseService {

    @Autowired
    CategoryService categoryService;
    @Autowired
    ExpenseRepository expenseRepository;
    @Autowired
    ProfileService profileService;
    @Autowired
    CategoryRepository categoryRepository;


    //add a new expense to the database
    public ExpenseDTO addExpense(ExpenseDTO DTO)
    {
        ProfileEntity profileEntity = profileService.getCurrentProfile();
        CategoryEntity categoryEntity = categoryRepository.findById(DTO.getCategoryId()).orElseThrow(() -> new RuntimeException("Category not found"));
        ExpenseEntity expense = toEntity(DTO,profileEntity,categoryEntity);
        expense = expenseRepository.save(expense);
        return toDTO(expense);
    }

    //Retrieve all expenses for the current month/based on the date between
    public List<ExpenseDTO> getCurrentMonthExpensesForCurrentUser(){
        ProfileEntity entity = profileService.getCurrentProfile();
        LocalDate now  = LocalDate.now();
        LocalDate startDate = now.withDayOfMonth(1);
        LocalDate endDate = now.withDayOfMonth(now.lengthOfMonth());
        List<ExpenseEntity> expenseEntities = expenseRepository.findByProfileIdAndDateBetween(entity.getId(),startDate,endDate);
        return expenseEntities.stream().map(this::toDTO).toList();
    }

    //dete expenses by id for current user
    public void deleteExpenseById(Long id){
        ProfileEntity entity = profileService.getCurrentProfile();
        ExpenseEntity expense = expenseRepository.findById(id).orElseThrow(()-> new RuntimeException("Expense not found"));

        if(!expense.getProfile().getId().equals(entity.getId())){
            throw new RuntimeException("Unauthorized to delete the expense");
        }
        expenseRepository.delete(expense);
    }

    //get latest 5 expenses for current user
    public List<ExpenseDTO> getLatest5ExpensesForCurrentUser(){
        ProfileEntity entity = profileService.getCurrentProfile();
        List<ExpenseEntity> expenseEntities = expenseRepository.findTop5ByProfileIdOrderByDateDesc(entity.getId());
        return expenseEntities.stream().map(this::toDTO).toList();
    }
    //get the total sum of expenses
    public BigDecimal getTotalExpensesForCurrentUser(){
        ProfileEntity entity = profileService.getCurrentProfile();
        BigDecimal sumOfExpenses = expenseRepository.findTotalExpenseByProfileId(entity.getId());
        return sumOfExpenses != null ? sumOfExpenses:BigDecimal.ZERO;

    }

    //filter expenses
    public List<ExpenseDTO> filterExpenses(LocalDate startDate, LocalDate endDate, String keyword, Sort sort){
        ProfileEntity profile = profileService.getCurrentProfile();
        List<ExpenseEntity> list = expenseRepository.findByProfileIdAndDateBetweenAndNameContainingIgnoreCase(profile.getId(), startDate,endDate,keyword,sort);
        return list.stream().map(this::toDTO).toList();
    }

    //Notifications
    public List<ExpenseDTO> getExpensesForUserDate(Long profileId,LocalDate date){
        List<ExpenseEntity> list = expenseRepository.findByProfileIdAndDate(profileId,date);
        return list.stream().map(this::toDTO).toList();
    }







    //helper methods
    private ExpenseEntity toEntity(ExpenseDTO DTO, ProfileEntity profile, CategoryEntity category){
        return ExpenseEntity.builder()
                .name(DTO.getName())
                .icon(DTO.getIcon())
                .amount(DTO.getAmount())
                .date(DTO.getDate())
                .profile(profile)
                .category(category)
                .build();
    }

    private ExpenseDTO toDTO(ExpenseEntity entity){
        return ExpenseDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .icon(entity.getIcon())
                .categoryId(entity.getCategory() != null ? entity.getCategory().getId():null)
                .categoryName(entity.getCategory() != null ? entity.getCategory().getName():"N/A")
                .amount(entity.getAmount())
                .date(entity.getDate())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }


}
