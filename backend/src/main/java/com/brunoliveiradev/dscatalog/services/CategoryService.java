package com.brunoliveiradev.dscatalog.services;

import com.brunoliveiradev.dscatalog.dto.CategoryDto;
import com.brunoliveiradev.dscatalog.model.Category;
import com.brunoliveiradev.dscatalog.repositories.CategoryRepository;
import com.brunoliveiradev.dscatalog.services.exceptions.DataBaseException;
import com.brunoliveiradev.dscatalog.services.exceptions.ResourceNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Transactional(readOnly = true)
    public Page<CategoryDto> findAllPaged(PageRequest pageRequest) {
        Page<Category> categories = categoryRepository.findAll(pageRequest);
        return categories.map(CategoryDto::new);
    }

    @Transactional(readOnly = true)
    public CategoryDto findById(Long id) {
        //findById faz uma consulta ao banco de dados
        Optional<Category> categoryOptional = categoryRepository.findById(id);
        Category entity = categoryOptional.orElseThrow(() -> new ResourceNotFoundException("Entity not found"));
        return new CategoryDto(entity);
    }

    @Transactional
    public CategoryDto insert(CategoryDto categoryDto) {
        Category entity = new Category();
        entity.setName(categoryDto.getName());
        entity = categoryRepository.save(entity);

        return new CategoryDto(entity);
    }

    @Transactional
    public CategoryDto update(CategoryDto updatedCategoryDto, Long id) {
        // getOne não faz uma consulta desnecessária ao banco, ele cria algo temporário, sendo mais performático
        try {
            Category entity = categoryRepository.getOne(id);
            entity.setName(updatedCategoryDto.getName());
            entity = categoryRepository.save(entity);

            return new CategoryDto(entity);
        } catch (EntityNotFoundException exception) {
            throw new ResourceNotFoundException("Id: " + id + " not found!");
        }
    }

    public void delete(Long id) {
        try{
            categoryRepository.deleteById(id);
        } catch (EmptyResultDataAccessException exception) {
            throw new ResourceNotFoundException("ID: " + id + " not found!");

        } catch (DataIntegrityViolationException exception) {
            throw new DataBaseException("Integrity violation");
        }
    }
}
