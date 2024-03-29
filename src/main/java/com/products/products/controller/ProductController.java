package com.products.products.controller;

import com.products.products.dto.PageResponse;
import com.products.products.dto.ProductDto;
import com.products.products.service.ProductService;
import com.products.products.utils.ConstantsUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Contrôleur gérant les produits
 */
@RestController
@RequestMapping("api/products")
@RequiredArgsConstructor
@Tag(name = "Products", description = "Endpoints for managing products")
public class ProductController {

    private final ProductService productService;

    /**
     * Récupérer une liste paginée de produits
     * @param pageNo Numéro de la page
     * @param pageSize Taille de la page
     * @param sortBy Champ de tri
     * @param sortDir Direction du tri
     * @param title Titre du produit
     * @param description Description du produit
     * @param minPrice Prix minimum
     * @param maxPrice Prix maximum
     * @return Liste paginée de produits
     */
    @GetMapping
    @Operation(
            summary = "Get all products",
            description = "Get all the products",
            tags = {"Products"},
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ProductDto.class)))
                    ),
                    @ApiResponse(
                            description = "Success - Partial content",
                            responseCode = "206",
                            content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ProductDto.class)))
                    ),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
            })
    // @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<PageResponse<ProductDto>> getAllProducts(
            @RequestParam(value = "pageNo", defaultValue = ConstantsUtils.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = ConstantsUtils.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = ConstantsUtils.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = ConstantsUtils.DEFAULT_SORT_DIRECTION, required = false) String sortDir,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "min_price", required = false) Integer minPrice,
            @RequestParam(value = "max_price", required = false) Integer maxPrice
    ) {
        PageResponse<ProductDto> productDtoPageResponse = productService.getAllProducts(pageNo, pageSize, sortBy, sortDir, title, description, minPrice, maxPrice);
        return productDtoPageResponse.getContent().size() < productDtoPageResponse.getTotalElements()
                ? new ResponseEntity<>(productDtoPageResponse, HttpStatus.PARTIAL_CONTENT)
                : ResponseEntity.ok(productDtoPageResponse);
    }

    /**
     * Récupérer une liste de produits paginée par catégorie
     * @param categoryId Id de la catégorie
     * @param pageNo Numéro de la page
     * @param pageSize Taille de la page
     * @param sortBy Champ de tri
     * @param sortDir Direction du tri
     * @return Liste paginée de produits
     */
    @GetMapping("/category/{id}")
    @Operation(
            summary = "Get products by category",
            description = "Get products by category id",
            tags = {"Products"},
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ProductDto.class)))
                    ),
                    @ApiResponse(
                            description = "Success - Partial content",
                            responseCode = "206",
                            content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ProductDto.class)))
                    ),
                    @ApiResponse(
                            description = "Not found",
                            responseCode = "404",
                            content = @Content
                    ),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
            })
    public ResponseEntity<PageResponse<ProductDto>> getAllProductsByCategoryId(
            @Parameter(description = "Category id", example = "1") @PathVariable(name = "id") int categoryId,
            @RequestParam(value = "pageNo", defaultValue = ConstantsUtils.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = ConstantsUtils.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = ConstantsUtils.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = ConstantsUtils.DEFAULT_SORT_DIRECTION, required = false) String sortDir
) {
        PageResponse<ProductDto> productDtoPageResponse = productService.getAllProductsByCategoryId(categoryId, pageNo, pageSize, sortBy, sortDir);
        return productDtoPageResponse.getContent().size() < productDtoPageResponse.getTotalElements()
                ? new ResponseEntity<>(productDtoPageResponse, HttpStatus.PARTIAL_CONTENT)
                : ResponseEntity.ok(productDtoPageResponse);
    }

    /**
     * Récupère un produit par son id
     * @param productId Id du produit
     * @return Produit
     */
    @GetMapping("/{id}")
    @Operation(
            summary = "Get a product",
            description = "Get a product by id",
            tags = {"Products"},
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = @Content(mediaType = "application/json", schema =@Schema(implementation = ProductDto.class))
                    ),
                    @ApiResponse(description = "Not found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
            })
    public ResponseEntity<ProductDto> getProductById(@Parameter(description = "Product id", example = "1") @PathVariable(name = "id") int productId) {
        return ResponseEntity.ok(productService.getProductById(productId));
    }

    /**
     * Créer un produit
     * @param productDto Produit à créer
     * @return Produit créé
     */
    @PostMapping
    @Operation(
            summary = "Create a single product",
            description = "Create a new product",
            tags = {"Products"},
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "201",
                            content = @Content(mediaType = "application/json", schema =@Schema(implementation = ProductDto.class))
                    ),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
            })
    public ResponseEntity<ProductDto> createProduct(@Valid @RequestBody ProductDto productDto) {
        return new ResponseEntity<>(productService.createProduct(productDto), HttpStatus.CREATED);
    }

    /**
     * Mettre à jour un produit
     * @param productDto Produit à mettre à jour
     * @param productId Id du produit à mettre à jour
     * @return Produit mis à jour
     */
    @PutMapping("/{id}")
    @Operation(
            summary = "Update a product",
            description = "Update a new product",
            tags = {"Products"},
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = @Content(mediaType = "application/json", schema =@Schema(implementation = ProductDto.class))
                    ),
                    @ApiResponse(description = "Not found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
            })
    public ResponseEntity<ProductDto> updateProduct(@Valid @RequestBody ProductDto productDto, @Parameter(description = "Product id", example = "1") @PathVariable(name = "id") int productId) {
        return ResponseEntity.ok(productService.updateProduct(productDto, productId));
    }

    /**
     * Supprimer un produit
     * @param productId Id du produit à supprimer
     * @return 204 No Content
     */
    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete a product",
            description = "Delete a product",
            tags = {"Products"},
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "204",
                            content = @Content(mediaType = "application/json", schema =@Schema(implementation = String.class))
                    ),
                    @ApiResponse(description = "Not found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
            })
    public ResponseEntity<?> deleteProduct(@Parameter(description = "Product id", example = "1") @PathVariable(name = "id") int productId) {
        productService.deleteProductById(productId);
        return ResponseEntity.noContent().build();
    }
}
