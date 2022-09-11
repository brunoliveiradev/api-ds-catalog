package com.brunoliveiradev.dscatalog.controllers;

import com.brunoliveiradev.dscatalog.dto.CategoryDto;
import com.brunoliveiradev.dscatalog.services.CategoryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping(value = "/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<Page<CategoryDto>> findAll(Pageable pageable) {
        Page<CategoryDto> categories = categoryService.findAllPaged(pageable);

        return ResponseEntity.ok().body(categories);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<CategoryDto> findById(@PathVariable Long id) {
        CategoryDto category = categoryService.findById(id);
        return ResponseEntity.ok().body(category);
    }

    @PostMapping
    public ResponseEntity<CategoryDto> insert(@RequestBody @Valid CategoryDto createdCategoryDto) {
        createdCategoryDto = categoryService.insert(createdCategoryDto);
        //ServletUri extends UriComponentsBuilder com métodos adicionais
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(createdCategoryDto.getId()).toUri();

        return ResponseEntity.created(uri).body(createdCategoryDto);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<CategoryDto> update(@RequestBody @Valid CategoryDto updatedCategoryDto, @PathVariable Long id) {
        updatedCategoryDto = categoryService.update(updatedCategoryDto, id);

        return ResponseEntity.ok().body(updatedCategoryDto);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        categoryService.delete(id);
        // Código 204: Operação ok, com o corpo vazio
        return ResponseEntity.noContent().build();
    }
}
