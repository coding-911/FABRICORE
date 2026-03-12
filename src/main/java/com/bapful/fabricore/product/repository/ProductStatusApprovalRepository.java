package com.bapful.fabricore.product.repository;

import com.bapful.fabricore.product.entity.ProductStatusApproval;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductStatusApprovalRepository extends JpaRepository<ProductStatusApproval, Long> {
    List<ProductStatusApproval> findByProductStatusIdOrderByApprovalOrderAsc(Long productStatusId);
}
