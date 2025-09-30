package com.noice.productbff.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/product")
@Tag(name = "Product", description = "product operations")
public class ProductController {

    @GetMapping("/unauth")
    public String unAuth(){
        return "unAuth" ;
    }

    @GetMapping("/auth")
    public String Auth() {
        return "Auth ";
    }

}
