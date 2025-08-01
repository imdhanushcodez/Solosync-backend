package com.dhanush.SoloSync.Controller;

import com.dhanush.SoloSync.Dto.CategoryDTO;
import com.dhanush.SoloSync.Service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    @PostMapping
    public ResponseEntity<CategoryDTO> saveCategories(@RequestBody CategoryDTO categoryDTO){
        CategoryDTO savedCategoryDto = categoryService.saveCategory(categoryDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCategoryDto);
    }

    @GetMapping
    public ResponseEntity<List<CategoryDTO>> getCategoriesForCurrentUser(){
        return ResponseEntity.ok(categoryService.getCategoriesForCurrentUser());
    }

    @GetMapping("/{type}")
    public ResponseEntity<List<CategoryDTO>> getCategoriesByTypeForCurrentUsers(@PathVariable String type){
        return ResponseEntity.ok(categoryService.getCategoriesByTypeForCurrentUser(type));
    }

    @PostMapping("/{categoryId}")
    public ResponseEntity<CategoryDTO> updateCategory(@PathVariable Long categoryId,@RequestBody CategoryDTO categoryDTO){
        categoryDTO = categoryService.updateCategory(categoryId,categoryDTO);
        return ResponseEntity.ok(categoryDTO);
    }

}
