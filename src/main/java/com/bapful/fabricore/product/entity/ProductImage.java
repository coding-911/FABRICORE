package com.bapful.fabricore.product.entity;

import com.bapful.fabricore.global.common.audit.BaseAudit;
import com.bapful.fabricore.product.enums.ProductImageType;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "product_image")
@Getter
@NoArgsConstructor
public class ProductImage extends BaseAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Enumerated(EnumType.STRING)
    @Column(name = "image_type", nullable = false, length = 30)
    private ProductImageType imageType;

    @Column(name = "image_url", nullable = false, length = 500)
    private String imageUrl;

    @Column(name = "sort_order")
    private Integer sortOrder;

    @Builder
    public ProductImage(
            Product product,
            ProductImageType imageType,
            String imageUrl,
            Integer sortOrder
    ) {
        this.product = product;
        this.imageType = imageType;
        this.imageUrl = imageUrl;
        this.sortOrder = sortOrder;
    }

    public void updateImageInfo(ProductImageType imageType, String imageUrl, Integer sortOrder) {
        this.imageType = imageType;
        this.imageUrl = imageUrl;
        this.sortOrder = sortOrder;
    }
}
