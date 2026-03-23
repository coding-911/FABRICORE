package com.bapful.fabricore.product.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "product_code_sequence")
@Getter
@Setter
@NoArgsConstructor
public class ProductCodeSeq {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "season_code", nullable = false)
    private String seasonCode;

    @Column(name = "category_id", nullable = false)
    private Long categoryId;

    @Column(name = "color_code", nullable = false)
    private String colorCode;

    @Column(name = "next_seq", nullable = false)
    private Long nextSeq;

    public Long getAndIncrease() {
        Long current = this.nextSeq;
        this.nextSeq++;
        return current;
    }
}
