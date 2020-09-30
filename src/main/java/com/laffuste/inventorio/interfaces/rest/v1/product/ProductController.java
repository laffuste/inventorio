package com.laffuste.inventorio.interfaces.rest.v1.product;

import com.google.common.base.Stopwatch;
import com.laffuste.inventorio.domain.product.Product;
import com.laffuste.inventorio.domain.product.ProductManagementService;
import com.laffuste.inventorio.infrastructure.exception.ModelNotFoundException;
import com.laffuste.inventorio.infrastructure.product.SearchProductCriteria;
import com.laffuste.inventorio.interfaces.rest.v1.product.dto.CreateProductDto;
import com.laffuste.inventorio.interfaces.rest.v1.product.dto.ProductDto;
import com.laffuste.inventorio.interfaces.rest.v1.product.dto.ProductMapper;
import com.laffuste.inventorio.interfaces.rest.v1.product.dto.SearchProductRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Tag(name = "products", description = "view and manage products")
@RestController
@RequestMapping(path = "v1/products", produces = {"application/json"})
@AllArgsConstructor
@Slf4j
public class ProductController {

    private final ProductManagementService productService;

    private final ProductMapper productMapper = ProductMapper.INSTANCE;

    @Operation(summary = "List or search products", description = "Retrieves products of the inventory. If no filters are passed, returns all products.")
    @GetMapping
    public Page<ProductDto> searchProducts(SearchProductRequest request) {
        SearchProductCriteria criteria = productMapper.toCriteria(request);
        Page<Product> pageDtos = productService.search(criteria, request.getPage());
        return pageDtos.map(productMapper::toDto);
    }


    @Operation(summary = "Create a new product", description = "Creates a new product in the inventory.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product created"),
            @ApiResponse(responseCode = "404", description = "Product Category doesn't exist")
    })
    @PostMapping
    public ProductDto createProduct(@RequestBody @Valid CreateProductDto productDto) throws ModelNotFoundException {
        Stopwatch watch = Stopwatch.createStarted();
        Product product = productMapper.toModel(productDto);
        Product newProduct = productService.create(product);
        log.info("Product {} created in {}: {}", newProduct.getName(), watch, newProduct);
        return productMapper.toDto(newProduct);
    }

    @Operation(summary = "Find a product", description = "Retrieves a product by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product returned"),
            @ApiResponse(responseCode = "404", description = "Product doesn't exist")
    })
    @GetMapping("/{id}")
    public ProductDto getProduct(@PathVariable Long id) throws ModelNotFoundException {
        Product product = productService.findById(id);
        return productMapper.toDto(product);
    }

    @Operation(summary = "Update a product", description = "Update a product by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product updated"),
            @ApiResponse(responseCode = "404", description = "Product Category doesn't exist")
    })
    @PutMapping("/{id}")
    public ProductDto updateProduct(@PathVariable Long id, @RequestBody @Valid CreateProductDto productDto) throws ModelNotFoundException {
        Product product = productMapper.toModel(productDto);
        Product updatedProduct = productService.update(product.withId(id));
        return productMapper.toDto(updatedProduct);
    }


    @Operation(summary = "Delete a product", description = "Deletes a product by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product deleted"),
            @ApiResponse(responseCode = "404", description = "Product doesn't exist")
    })
    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id) throws ModelNotFoundException {
        productService.delete(id);
    }


}
