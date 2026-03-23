package com.bapful.fabricore.product.repository;

import com.bapful.fabricore.product.entity.ProductCodeSeq;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductCodeSeqRepository extends JpaRepository<ProductCodeSeq, Long> {
    Optional<ProductCodeSeq> findBySeasonCodeAndCategoryIdAndColorCode(
            String seasonCode,
            Long categoryId,
            String colorCode
    );
}
