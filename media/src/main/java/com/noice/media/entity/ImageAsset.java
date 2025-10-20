package com.noice.media.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@DiscriminatorValue("IMAGE")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
public class ImageAsset extends Asset {
    @NotNull(message = "image width required") private Integer width;
    @NotNull(message = "image height required") private Integer height;
}

