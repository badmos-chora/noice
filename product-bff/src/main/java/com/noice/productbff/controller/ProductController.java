package com.noice.productbff.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/product",produces = MediaType.APPLICATION_JSON_VALUE)
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
