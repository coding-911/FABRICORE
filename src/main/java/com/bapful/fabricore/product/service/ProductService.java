package com.bapful.fabricore.product.service;

import com.bapful.fabricore.product.dto.request.ProductCreateRequest;
import com.bapful.fabricore.product.dto.request.ReviewerCreateRequest;
import com.bapful.fabricore.product.entity.Product;
import com.bapful.fabricore.product.entity.ProductCodeSeq;
import com.bapful.fabricore.product.entity.ProductStatus;
import com.bapful.fabricore.product.enums.LaunchStatus;
import com.bapful.fabricore.product.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductStatusRepository productStatusRepository;
    private final ProductCodeSeqRepository productCodeSeqRepository;

    // 상품 등록
    public Product createProduct(ProductCreateRequest request, UUID requestedBy) {

        // 상품 코드(비즈니스) 자동 생성
        String productCode = generateProductCode(request.getSeasonCode(), request.getCategoryId(), request.getColorCode());

        // 상품 객체 생성
        Product product = Product.builder()
                .productCode(productCode)
                .productName(request.getProductName())
                .seasonCode(request.getSeasonCode())
                .categoryId(request.getCategoryId())
                .colorCode(request.getColorCode())
                .genderType(request.getGenderType())
                .launchStatus(LaunchStatus.REVIEWING)
                .description(request.getDescription())
                .build();

        productRepository.save(product);

        // TODO 사진 등록 로직 구현 필요
//        if (request.images() != null && !request.images().isEmpty()) {
//            List<ProductImage> images = request.images().stream()
//                    .map(image -> ProductImage.builder()
//                            .product(product)
//                            .imageType(image.imageType())
//                            .imageUrl(image.imageUrl())
//                            .sortOrder(image.sortOrder())
//                            .build())
//                    .toList();
//
//            productImageRepository.saveAll(images);
//        }

        // 상품 출시 단계 객체 생성
        ProductStatus productStatus = ProductStatus.builder()
                .product(product)
                .fromStatus(LaunchStatus.PLANNING)
                .toStatus(LaunchStatus.REVIEWING)
                .statusChangedAt(LocalDateTime.now())
                .build();

        productStatusRepository.save(productStatus);

        return product;

    }

    // 상품 코드 자동 생성 method
    public String generateProductCode(String seasonCode, Long categoryId, String colorCode) {

        // product_code_seq 테이블에서 [시즌코드&카테고리ID]에 해당하는 시퀀스가 있을 때
        // [시즌코드&카테고리ID]에 해당하는 시퀀스가 없을 때
        ProductCodeSeq sequence = productCodeSeqRepository
                .findBySeasonCodeAndCategoryIdAndColorCode(seasonCode, categoryId, colorCode)
                .orElseGet(() -> {
                    ProductCodeSeq newSeq = new ProductCodeSeq();
                    newSeq.setSeasonCode(seasonCode);
                    newSeq.setCategoryId(categoryId);
                    newSeq.setNextSeq(1L);
                    newSeq.setColorCode(colorCode);
                    return productCodeSeqRepository.save(newSeq);
                });

        Long seq = sequence.getAndIncrease();

        return seasonCode +
                categoryId +
                String.format("%04d", seq)
                + colorCode;
    }


    // 상품 목록 조회
    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    // 상품 상세 조회
    public Product getProduct(Long productId) {
        return productRepository.findById(productId).orElseThrow(() -> new IllegalArgumentException("해당하는 상품이 없습니다."));
    }

    // 상품 출시 단계 변경 (결재 없이)
    public void changeStatusManually(Long productId, LaunchStatus launchStatus) {

        Product product = productRepository.findById(productId).orElseThrow(()-> new IllegalArgumentException("해당하는 상품이 없습니다."));

        ProductStatus productStatus = ProductStatus.builder()
                .product(product)
                .fromStatus(product.getLaunchStatus())
                .toStatus(launchStatus)
                .build();

        LocalDateTime now = LocalDateTime.now();
        productStatus.setStatusChangedAt(now);
        productStatusRepository.save(productStatus);

        product.changeLaunchStatus(launchStatus);
        product.setUpdatedAt(now);
        productRepository.save(product);

        log.info("[PRODUCT-STATUS] 변경된 상품 출시 단계 변경 완료:  productId={}, from={}, to={}",
                product.getId(),
                productStatus.getFromStatus(),
                productStatus.getToStatus());

    }
}
