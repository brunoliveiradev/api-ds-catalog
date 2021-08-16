package com.brunoliveiradev.dscatalog.services;

import com.brunoliveiradev.dscatalog.dto.CategoryDto;
import com.brunoliveiradev.dscatalog.dto.ProductDto;
import com.brunoliveiradev.dscatalog.model.Category;
import com.brunoliveiradev.dscatalog.model.Product;
import com.brunoliveiradev.dscatalog.repositories.CategoryRepository;
import com.brunoliveiradev.dscatalog.repositories.ProductRepository;
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
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @Transactional(readOnly = true)
    public Page<ProductDto> findAllPaged(PageRequest pageRequest) {
        Page<Product> products = productRepository.findAll(pageRequest);
        return products.map(ProductDto::new);
    }

    @Transactional(readOnly = true)
    public ProductDto findById(Long id) {
        //findById faz uma consulta ao banco de dados
        Optional<Product> productOptional = productRepository.findById(id);
        Product entity = productOptional.orElseThrow(() -> new ResourceNotFoundException("Entity not found"));
        return new ProductDto(entity, entity.getCategories());
    }

    @Transactional
    public ProductDto insert(ProductDto productDto) {
        Product entity = new Product();
        copyDtoToEntity(productDto, entity);
        entity = productRepository.save(entity);
        return new ProductDto(entity);
    }

    @Transactional
    public ProductDto update(ProductDto productDto, Long id) {
        // getOne não faz uma consulta desnecessária ao banco, ele cria algo temporário, sendo mais performático
        try {
            Product entity = productRepository.getOne(id);
            copyDtoToEntity(productDto, entity);
            entity = productRepository.save(entity);
            return new ProductDto(entity);
        } catch (EntityNotFoundException exception) {
            throw new ResourceNotFoundException("Id: " + id + " not found!");
        }
    }

    public void delete(Long id) {
        try{
            productRepository.deleteById(id);
        } catch (EmptyResultDataAccessException exception) {
            throw new ResourceNotFoundException("ID: " + id + " not found!");

        } catch (DataIntegrityViolationException exception) {
            throw new DataBaseException("Integrity violation");
        }
    }

    private void copyDtoToEntity(ProductDto productDto, Product entity) {
        entity.setName(productDto.getName());
        entity.setDescription(productDto.getDescription());
        entity.setDate(productDto.getDate());
        entity.setImgUrl(productDto.getImgUrl());
        entity.setPrice(productDto.getPrice());
        //Acessa e limpa as categorias se houver na entidade
        entity.getCategories().clear();

        for (CategoryDto categoryDto : productDto.getCategories()) {
            Category category = categoryRepository.getOne(categoryDto.getId());
            entity.getCategories().add(category);
        }
    }
}
