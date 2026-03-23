package com.bapful.fabricore.global.approval.repository;

import com.bapful.fabricore.global.approval.entity.ApprovalLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ApprovalLineRepository extends JpaRepository<ApprovalLine, Long> {

    List<ApprovalLine> findByApprovalDocumentIdOrderByApprovalOrderAsc(Long approvalDocumentId);
    Optional<ApprovalLine> findByApprovalDocumentIdAndApproverId(Long approvalDocumentId, UUID approverId);

}
