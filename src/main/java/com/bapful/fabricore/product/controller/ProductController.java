package com.bapful.fabricore.product.controller;

import com.bapful.fabricore.product.dto.request.ProductCreateRequest;
import com.bapful.fabricore.product.entity.Product;
import com.bapful.fabricore.product.enums.LaunchStatus;
import com.bapful.fabricore.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public Product createProduct(@RequestBody ProductCreateRequest request) {
        UUID requesterId = UUID.randomUUID(); // 임시
        return productService.createProduct(request, requesterId);
    }

    @GetMapping
    public List<Product> getProducts() {
        return productService.getProducts();
    }

    @GetMapping("/{product_id}")
    public Product getProduct(@PathVariable("product_id") Long productId) {
        return productService.getProduct(productId);
    }

    // 상품 출시 단계 수동 변경
    @PostMapping("/{product_id}/change-status-manually")
    public Boolean changeStatusManually(
            @PathVariable("product_id") Long productId,
            @RequestParam("launch_status") LaunchStatus launchStatus
    ) {
        //TODO 로그인 id로 변경 필요
        UUID memberId = UUID.randomUUID();
        productService.changeStatusManually(productId, launchStatus);
        return false;
    }


}
