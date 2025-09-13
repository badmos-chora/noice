package com.noice.productbff.controller;

import com.noice.productbff.dto.AttributeDefinitionRequest;
import com.noice.productbff.service.interfaces.AttributeService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/attribute")
@AllArgsConstructor
public class AttributeController {

    private AttributeService attributeService;

    @GetMapping("/definition")
    @PreAuthorize("hasRole('ADMIN') and hasAuthority('attribute-defination.list')")
    public  ResponseEntity<?> attributeDefinitionList() {
        return ResponseEntity.ok().body(attributeService.listAttributeDefinition());
    }

    @PostMapping("/definition")
    @PreAuthorize("hasRole('ADMIN')  and hasAuthority('attribute-defination.create')")
    public  ResponseEntity<?> attributeDefinitionNew(@Valid @RequestBody AttributeDefinitionRequest attributeDefinitionRequest) {
        attributeService.addAttributeDefinition(attributeDefinitionRequest.getName(), attributeDefinitionRequest.getValues());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/definition/{id}")
    @PreAuthorize("hasRole('ADMIN') and hasAuthority('attribute-defination.update')")
    public  ResponseEntity<?> attributeDefinitionUpdate(@Valid @RequestBody AttributeDefinitionRequest attributeDefinitionRequest, @PathVariable Long id) {
        attributeService.updateAttributeDefinition(id, attributeDefinitionRequest.getName(), attributeDefinitionRequest.getValues());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/definition/{id}")
    @PreAuthorize("hasRole('ADMIN') and hasAuthority('attribute-defination.delete')")
    public  ResponseEntity<?> attributeDefinitionDelete(@PathVariable Long id) {
        attributeService.deleteAttributeDefinition(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }


}
