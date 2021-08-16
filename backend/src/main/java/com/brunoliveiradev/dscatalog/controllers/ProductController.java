package com.brunoliveiradev.dscatalog.controllers;

import com.brunoliveiradev.dscatalog.dto.ProductDto;
import com.brunoliveiradev.dscatalog.services.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping(value = "/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<Page<ProductDto>> findAll(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "linesPerPage", defaultValue = "12") Integer linesPerPage,
            @RequestParam(value = "direction", defaultValue = "ASC") String direction,
            @RequestParam(value = "orderBy", defaultValue = "name") String orderBy
            ){
        PageRequest pageRequest = PageRequest.of(page, linesPerPage, Sort.Direction.valueOf(direction), orderBy);

        Page<ProductDto> products = productService.findAllPaged(pageRequest);

        return ResponseEntity.ok().body(products);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<ProductDto> findById(@PathVariable Long id) {
        ProductDto product = productService.findById(id);
        return ResponseEntity.ok().body(product);
    }

    @PostMapping
    public ResponseEntity<ProductDto> insert(@RequestBody @Valid ProductDto createdProductDto) {
        createdProductDto = productService.insert(createdProductDto);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(createdProductDto.getId()).toUri();

        return ResponseEntity.created(uri).body(createdProductDto);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<ProductDto> update(@RequestBody @Valid ProductDto updatedProductDto, @PathVariable Long id) {
        updatedProductDto = productService.update(updatedProductDto, id);

        return ResponseEntity.ok().body(updatedProductDto);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        productService.delete(id);
        // Código 204: Operação ok, com o corpo vazio
        return ResponseEntity.noContent().build();
    }
}
