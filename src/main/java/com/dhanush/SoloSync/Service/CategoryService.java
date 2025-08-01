package com.dhanush.SoloSync.Service;

import com.dhanush.SoloSync.Dto.CategoryDTO;
import com.dhanush.SoloSync.Model.CategoryEntity;
import com.dhanush.SoloSync.Model.ProfileEntity;
import com.dhanush.SoloSync.Repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    ProfileService profileService;
    @Autowired
    CategoryRepository categoryRepository;

    public CategoryDTO saveCategory(CategoryDTO categoryDTO){

        ProfileEntity profileEntity = profileService.getCurrentProfile();
        if(categoryRepository.existsByNameAndProfileId(categoryDTO.getName(), profileEntity.getId())){
            throw new RuntimeException("Category with this name already exists");
        }
        CategoryEntity newEntity = toEntity(categoryDTO,profileEntity);
        newEntity = categoryRepository.save(newEntity);
        return toDTO(newEntity);
    }

    //get categories for current user
    public List<CategoryDTO> getCategoriesForCurrentUser(){
        ProfileEntity profileEntity = profileService.getCurrentProfile();
        List<CategoryEntity> categories = categoryRepository.findByProfileId(profileEntity.getId());
        return categories.stream().map(this::toDTO).toList();

    }

    //get categories type for current users;
    public List<CategoryDTO> getCategoriesByTypeForCurrentUser(String type){
        ProfileEntity profileEntity = profileService.getCurrentProfile();
        List<CategoryEntity> categories = categoryRepository.findByTypeAndProfileId(type,profileEntity.getId());
        return categories.stream().map(this::toDTO).toList();

    }

    //Update the category
    public CategoryDTO updateCategory(Long categoryId,CategoryDTO DTO){
        ProfileEntity profileEntity = profileService.getCurrentProfile();
        CategoryEntity entity = categoryRepository.findByIdAndProfileId(categoryId, profileEntity.getId())
                .orElseThrow(() -> new RuntimeException("Category not found or not accessible"));
        entity.setName(DTO.getName());
        entity.setIcon(DTO.getIcon());
        entity.setType(DTO.getType());
        entity = categoryRepository.save(entity);
        return toDTO(entity);

    }








    //HELPER METHODS
    private CategoryEntity toEntity(CategoryDTO categoryDTO, ProfileEntity profileEntity){
        return CategoryEntity.builder()
                .name(categoryDTO.getName())
                .icon(categoryDTO.getIcon())
                .profile(profileEntity)
                .type(categoryDTO.getType())
                .build();
    }

    private CategoryDTO toDTO(CategoryEntity entity){
        return CategoryDTO.builder()
                .id(entity.getId())
                .profileId(entity.getProfile() != null ? entity.getProfile().getId():null)
                .icon(entity.getIcon())
                .createdAt(entity.getCreatedAt())
                .updateAt(entity.getUpdatedAt())
                .type(entity.getType())
                .name(entity.getName())
                .build();
    }


}
