package com.noice.productbff.service.interfaces;

import com.noice.productbff.projection.ProductAttributeNameId;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Set;

public interface AttributeService {
    List<ProductAttributeNameId> listAttributeDefinition();

    void addAttributeDefinition(@NonNull String name,@NonNull Set<String> values);

    void updateAttributeDefinition(@NonNull Long id,@NonNull String name, @NonNull Set<String> values);

    void deleteAttributeDefinition( @NonNull Long id);
}
