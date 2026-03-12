package com.bapful.fabricore.product.entity;

import com.bapful.fabricore.global.common.audit.BaseAudit;
import com.bapful.fabricore.product.enums.GenderType;
import com.bapful.fabricore.product.enums.LaunchStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Table(name = "product")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product extends BaseAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;

    @Column(name = "product_code", unique = true, nullable = false, length = 30)
    private String productCode;

    @Column(name = "product_name", nullable = false, length = 200)
    private String productName;

    @Column(name = "category_id", nullable = false)
    private Long categoryId;

    @Column(name = "season_code", nullable = false, length = 20)
    private String seasonCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender_type", length = 20)
    private GenderType genderType;

    @Enumerated(EnumType.STRING)
    @Column(name = "launch_status", nullable = false, length = 30)
    private LaunchStatus launchStatus;

    @Lob
    @Column(name = "description")
    private String description;

    @Builder
    public Product(
            String productCode,
            String productName,
            Long categoryId,
            String seasonCode,
            GenderType genderType,
            LaunchStatus launchStatus,
            String description
    ) {
        this.productCode = productCode;
        this.productName = productName;
        this.categoryId = categoryId;
        this.seasonCode = seasonCode;
        this.genderType = genderType;
        this.launchStatus = launchStatus;
        this.description = description;
    }

    public void updateBasicInfo(
            String productCode,
            String productName,
            Long categoryId,
            String seasonCode,
            GenderType genderType,
            String description
    ) {
        this.productCode = productCode;
        this.productName = productName;
        this.categoryId = categoryId;
        this.seasonCode = seasonCode;
        this.genderType = genderType;
        this.description = description;
    }

    public void changeLaunchStatus(LaunchStatus launchStatus) {
        this.launchStatus = launchStatus;
    }

    public void changeToProducing() {
        this.launchStatus = LaunchStatus.PRODUCING;
    }

    public void changeToOnSale() {
        this.launchStatus = LaunchStatus.ON_SALE;
    }

}
