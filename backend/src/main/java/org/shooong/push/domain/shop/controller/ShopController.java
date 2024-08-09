package org.shooong.push.domain.shop.controller;

import org.shooong.push.domain.product.dto.AllProductDto;
import org.shooong.push.domain.shop.service.ShopService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@Log4j2
@RequiredArgsConstructor
@RequestMapping("/shop")
@RestController
public class ShopController {

    private final ShopService shopService;

    @GetMapping("/all")
    public Slice<AllProductDto> getTotalProduct(@RequestParam("pageNumber") int pageNumber) {

        Pageable pageable = PageRequest.of(pageNumber, 20);
        return shopService.getTotalProduct(pageable);
    }

    @GetMapping("/sub")
    public Slice<AllProductDto> getSubDepartment(@RequestParam("pageNumber") int pageNumber, @RequestParam(value = "subDepartment", required = false) String subDepartment) {

        String[] subDepartments = null;
        if (subDepartment.contains(",")) {
            subDepartments = subDepartment.split(",");
        } else {
            subDepartments = new String[]{subDepartment};
        }

        Pageable pageable = PageRequest.of(pageNumber, 20);
        return shopService.getSubDepartmentFilter(pageable, Arrays.asList(subDepartments));
    }

}
