package com.products.products.service;

import com.products.products.dto.BrandDto;
import com.products.products.entity.Brand;
import com.products.products.mapper.BrandMapper;
import com.products.products.repository.BrandRepository;
import com.products.products.service.impl.BrandServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;

/**
 * Classe de test pour le service BrandService
 */
@ExtendWith(MockitoExtension.class)
public class BrandServiceImplTest {

    @Mock
    BrandRepository brandRepository;
    @Mock
    BrandMapper brandMapper;
    @InjectMocks
    BrandServiceImpl brandService;

    /**
     * Test de la méthode getAllBrands
     */
    @Test
    void brandService_getAllBrand_returnAllBrand() {
        Brand brand = Brand.builder()
                .id(1)
                .name("name")
                .build();

        BrandDto brandDto = BrandDto.builder()
                .id(1)
                .name("name")
                .build();

        when(brandRepository.findAll()).thenReturn(Collections.singletonList(brand));

        List<BrandDto> brandDtoList = brandService.getAllBrands();

        Assertions.assertThat(brandDtoList).isNotNull();
        Assertions.assertThat(brandDtoList)
                .isNotEmpty()
                .size().isEqualTo(1);
    }
}
