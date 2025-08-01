package com.dhanush.SoloSync.Service;



import com.dhanush.SoloSync.Dto.ExpenseDTO;
import com.dhanush.SoloSync.Dto.IncomeDTO;
import com.dhanush.SoloSync.Model.CategoryEntity;

import com.dhanush.SoloSync.Model.ExpenseEntity;
import com.dhanush.SoloSync.Model.IncomeEntity;
import com.dhanush.SoloSync.Model.ProfileEntity;
import com.dhanush.SoloSync.Repository.CategoryRepository;
import com.dhanush.SoloSync.Repository.IncomeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class IncomeService {

    @Autowired
    CategoryService categoryService;
    @Autowired
    IncomeRepository incomeRepository;
    @Autowired
    ProfileService profileService;
    @Autowired
    CategoryRepository categoryRepository;

    //add the incomes
    public IncomeDTO addIncome(IncomeDTO DTO)
    {
        ProfileEntity profileEntity = profileService.getCurrentProfile();
        CategoryEntity categoryEntity = categoryRepository.findById(DTO.getCategoryId()).orElseThrow(() -> new RuntimeException("Category not found"));
        IncomeEntity income = toEntity(DTO,profileEntity,categoryEntity);
        income = incomeRepository.save(income);
        return toDTO(income);
    }

    //Retrive the incomes for current profile by startdate and endDate
    public List<IncomeDTO> getCurrentMonthIncomesForCurrentUser(){
        ProfileEntity entity = profileService.getCurrentProfile();
        LocalDate now  = LocalDate.now();
        LocalDate startDate = now.withDayOfMonth(1);
        LocalDate endDate = now.withDayOfMonth(now.lengthOfMonth());
        List<IncomeEntity> incomeEntities = incomeRepository.findByProfileIdAndDateBetween(entity.getId(),startDate,endDate);
        return incomeEntities.stream().map(this::toDTO).toList();
    }

    //dete income by id for current user
    public void deleteIncomeById(Long id){
        ProfileEntity entity = profileService.getCurrentProfile();
        IncomeEntity income = incomeRepository.findById(id).orElseThrow(()-> new RuntimeException("Income not found"));
        if(!income.getProfile().getId().equals(entity.getId())){
            throw new RuntimeException("Unauthorized to delete the income");
        }
        incomeRepository.delete(income);
    }

    //get latest 5 incomes for current user
    public List<IncomeDTO> getLatest5IncomeForCurrentUser(){
        ProfileEntity entity = profileService.getCurrentProfile();
        List<IncomeEntity> incomeEntities = incomeRepository.findTop5ByProfileIdOrderByDateDesc(entity.getId());
        return incomeEntities.stream().map(this::toDTO).toList();
    }
    //get the total sum of incomes
    public BigDecimal getTotalIncomeForCurrentUser(){
        ProfileEntity entity = profileService.getCurrentProfile();
        BigDecimal sumOfIncomes = incomeRepository.findTotalIncomeByProfileId(entity.getId());
        return sumOfIncomes != null ? sumOfIncomes:BigDecimal.ZERO;

    }

    //filter expenses
    public List<IncomeDTO> filterIncomes(LocalDate startDate, LocalDate endDate, String keyword, Sort sort){
        ProfileEntity profile = profileService.getCurrentProfile();
        List<IncomeEntity> list = incomeRepository.findByProfileIdAndDateBetweenAndNameContainingIgnoreCase(profile.getId(), startDate,endDate,keyword,sort);
        return list.stream().map(this::toDTO).toList();
    }





    //helper methods
    private IncomeEntity toEntity(IncomeDTO DTO, ProfileEntity profile, CategoryEntity category){
        return IncomeEntity.builder()
                .name(DTO.getName())
                .icon(DTO.getIcon())
                .amount(DTO.getAmount())
                .date(DTO.getDate())
                .profile(profile)
                .category(category)
                .build();
    }

    private IncomeDTO toDTO(IncomeEntity entity){
        return IncomeDTO.builder()
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
