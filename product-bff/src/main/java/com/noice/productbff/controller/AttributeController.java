package com.noice.productbff.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/attribute")
@AllArgsConstructor
public class AttributeController {

    private

    @GetMapping({"","/"})
    public ResponseEntity<?> listAttribute(){

    }
}
