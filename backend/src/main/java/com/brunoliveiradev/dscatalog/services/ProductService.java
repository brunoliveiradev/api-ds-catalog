package com.brunoliveiradev.dscatalog.services;

import com.brunoliveiradev.dscatalog.dto.ProductDto;
import com.brunoliveiradev.dscatalog.model.Product;
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

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
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
//        Acresentar os demais campos além do setName
//        entity.setName(productDto.getName());
        entity = productRepository.save(entity);

        return new ProductDto(entity);
    }

    @Transactional
    public ProductDto update(ProductDto updatedProductDto, Long id) {
        // getOne não faz uma consulta desnecessária ao banco, ele cria algo temporário, sendo mais performático
        try {
            Product entity = productRepository.getOne(id);
//             Acresentar os demais campos além do setName
//            entity.setName(updatedProductDto.getName());
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
}
