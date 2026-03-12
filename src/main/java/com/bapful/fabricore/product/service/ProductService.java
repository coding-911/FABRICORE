package com.bapful.fabricore.product.service;

import com.bapful.fabricore.product.dto.request.ProductCreateRequest;
import com.bapful.fabricore.product.dto.request.ReviewerCreateRequest;
import com.bapful.fabricore.product.entity.Product;
import com.bapful.fabricore.product.entity.ProductCodeSeq;
import com.bapful.fabricore.product.entity.ProductStatus;
import com.bapful.fabricore.product.entity.ProductStatusApproval;
import com.bapful.fabricore.product.enums.LaunchStatus;
import com.bapful.fabricore.product.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductStatusRepository productStatusRepository;
    private final ProductStatusApprovalRepository productStatusApprovalRepository;
    private final ProductImageRepository productImageRepository;
    private final ProductCodeSeqRepository productCodeSeqRepository;

    // 상품 등록
    public Product createProduct(ProductCreateRequest request, UUID requestedBy) {

        // 상품 코드(비즈니스) 자동 생성
        String productCode = generateProductCode(request.getSeasonCode(), request.getCategoryId());

        // 상품 객체 생성
        Product product = Product.builder()
                .productCode(productCode)
                .productName(request.getProductName())
                .categoryId(request.getCategoryId())
                .seasonCode(request.getSeasonCode())
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

        // 상품 상태 객체 생성
        ProductStatus productStatus = ProductStatus.builder()
                .product(product)
                .fromStatus(LaunchStatus.PLANNING)
                .toStatus(LaunchStatus.REVIEWING)
                .requestedBy(requestedBy)
                .requestedAt(LocalDateTime.now())
                .comment("상품 최초 등록에 따른 출시 검토 요청")
                .build();
        productStatus.startApproval();
        productStatusRepository.save(productStatus);

        // 상품 상태 결재 라인 객체 List 생성
        createApprovals(productStatus, request.getReviewers());

        return product;

    }

    // 상품 코드 자동 생성 method
    public String generateProductCode(String seasonCode, Long categoryId) {

        // product_code_seq 테이블에서 [시즌코드&카테고리ID]에 해당하는 시퀀스가 있을 때
        // [시즌코드&카테고리ID]에 해당하는 시퀀스가 없을 때
        ProductCodeSeq sequence = productCodeSeqRepository
                .findBySeasonCodeAndCategoryId(seasonCode, categoryId)
                .orElseGet(() -> {
                    ProductCodeSeq newSeq = new ProductCodeSeq();
                    newSeq.setSeasonCode(seasonCode);
                    newSeq.setCategoryId(categoryId);
                    newSeq.setNextSeq(1L);
                    return productCodeSeqRepository.save(newSeq);
                });

        Long seq = sequence.getAndIncrease();

        return seasonCode +
                categoryId +
                String.format("%04d", seq);
    }

    // 결재 라인 목록 생성 method
    private void createApprovals(ProductStatus productStatus, List<ReviewerCreateRequest> reviewers) {

        if (reviewers == null || reviewers.isEmpty()) {
            throw new IllegalArgumentException("결재자는 최소 1명 이상이어야 합니다.");
        }

        List<ProductStatusApproval> approvals = reviewers.stream()
                .distinct()
                .map(reviewer -> ProductStatusApproval.builder()
                        .productStatus(productStatus)
                        .approvedBy(reviewer.getApprovedBy())
                        .approvalOrder(reviewer.getApprovalOrder())
                        .build())
                .toList();

        productStatusApprovalRepository.saveAll(approvals);
    }
}
