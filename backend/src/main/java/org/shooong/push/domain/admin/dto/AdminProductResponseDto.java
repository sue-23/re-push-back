package org.shooong.push.domain.admin.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Setter
public class AdminProductResponseDto {
    private String mainDepartment;
    private String subDepartment;
    private List<AdminProductDto> products;
    private int totalPages;
    private long totalElements;
    private int number;
    private int size;
    private boolean isLast;

    public AdminProductResponseDto(String mainDepartment, String subDepartment, Page<AdminProductDto> productPage) {
        this.mainDepartment = mainDepartment;
        this.subDepartment = subDepartment;
        this.products = productPage.getContent();
        this.totalPages = productPage.getTotalPages();
        this.totalElements = productPage.getTotalElements();
        this.number = productPage.getNumber();
        this.size = productPage.getSize();
        this.isLast = productPage.isLast();
    }
}