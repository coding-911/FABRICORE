package com.bapful.fabricore.product.repository;

import com.bapful.fabricore.product.entity.ProductStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductStatusRepository extends JpaRepository<ProductStatus, Long> {
    List<ProductStatus> findByProductIdOrderByRequestedAtDesc(Long productId);
    Optional<ProductStatus> findByApprovalDocumentId(Long approvalDocumentId);
}
