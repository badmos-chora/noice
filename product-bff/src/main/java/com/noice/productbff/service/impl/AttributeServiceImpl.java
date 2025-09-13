package com.noice.productbff.service.impl;

import com.noice.productbff.entity.ProductAttribute;
import com.noice.productbff.entity.ProductAttributeValue;
import com.noice.productbff.exception.NoiceBusinessLogicException;
import com.noice.productbff.projection.ProductAttributeNameId;
import com.noice.productbff.repository.ProductAttributeRepository;
import com.noice.productbff.service.interfaces.AttributeService;
import lombok.AllArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AttributeServiceImpl implements AttributeService {

    private ProductAttributeRepository attributeRepository;

    @Override
    public List<ProductAttributeNameId> listAttributeDefinition() {
        List<ProductAttributeNameId> productAttributeNameIdList = attributeRepository.findAllBy(ProductAttributeNameId.class);
        return productAttributeNameIdList == null ? new ArrayList<>() : productAttributeNameIdList;
    }

    @Override
    public void addAttributeDefinition(@NonNull String name, @NonNull Set<String> values) {
        ProductAttribute productAttribute = new ProductAttribute();
        productAttribute.setName(name);
        Set<ProductAttributeValue> productAttributeValuesList = new HashSet<>();
        values.forEach(value -> {
            ProductAttributeValue productAttributeValue = new ProductAttributeValue();
            productAttributeValue.setValue(value);
            productAttributeValue.setAttribute(productAttribute);
            productAttributeValuesList.add(productAttributeValue);
        });
        productAttribute.setValues(productAttributeValuesList);
        attributeRepository.save(productAttribute);
    }

    @Override
    @Transactional
    public void updateAttributeDefinition(@NonNull Long id , @NonNull String name ,@NonNull Set<String> values) {
        ProductAttribute productAttribute = attributeRepository.findById(id).orElseThrow( () -> new NoiceBusinessLogicException("No attribute with id " + id + " exists"));
        productAttribute.setName(name.trim());
        if(values.isEmpty()) {
            productAttribute.getValues().clear();
            return;
        }
        Set<ProductAttributeValue> presentAttributeValuesList = productAttribute.getValues();
        Set<String> currentValues = presentAttributeValuesList.stream().map(ProductAttributeValue::getValue).collect(Collectors.toSet());
        presentAttributeValuesList.removeIf(value -> !values.contains(value.getValue()));
        values.stream()
                .filter( v -> !currentValues.contains(v))
                .forEach( v -> {
                    ProductAttributeValue productAttributeValue = new ProductAttributeValue();
                    productAttributeValue.setValue(v.trim());
                    productAttributeValue.setAttribute(productAttribute);
                    presentAttributeValuesList.add(productAttributeValue);
                });
    }

    @Override
    public void deleteAttributeDefinition(@NonNull Long id) {
        //only soft delete
        attributeRepository.updateActiveStatus(Boolean.FALSE);
    }


}