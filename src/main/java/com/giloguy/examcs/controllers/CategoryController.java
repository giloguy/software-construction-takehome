package com.giloguy.examcs.controllers;

import jakarta.validation.constraints.NotBlank;
import com.giloguy.examcs.models.Category;
import com.giloguy.examcs.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import com.giloguy.examcs.payloads.CategoryRequest;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/categories")
@SecurityRequirement(name = "bearerAuth")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @NotBlank
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Category> addCategory(@Valid @RequestBody CategoryRequest category) {
        Category categoryEntity = new Category(category.getName());
        Category savedCategory = categoryService.saveCategory(categoryEntity);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedCategory.getId())
                .toUri();

        return ResponseEntity.created(location).body(savedCategory);
    }

    @GetMapping
    public ResponseEntity<?> getCategories(@RequestParam(value = "name", required = false) String name) {
        List<Category> categories;
        Optional<Category> category;

        if (name != null) {
            category = categoryService.getCategoryByName(name);
            return category.map(value -> ResponseEntity.ok().body(value)).orElseGet(() -> ResponseEntity.notFound().build());
        } else {
            categories = categoryService.getAllCategories();
            return ResponseEntity.ok().body(categories);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long id) {
        Optional<Category> category = categoryService.getCategoryById(id);

        if (category.isPresent()) {
            categoryService.deleteCategory(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
