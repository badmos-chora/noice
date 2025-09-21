package com.noice.productbff.utils;


import com.github.slugify.Slugify;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Locale;

public class GeneralUtils {

    private static final Slugify SLUGIFY = Slugify.builder()
            .lowerCase(true)
            .transliterator(true)
            .underscoreSeparator(false)
            .locale(Locale.ENGLISH)
            .customReplacement("&", "and")
            .customReplacement("+", "plus")
            .customReplacement("/", "-")
            .customReplacement(":", "-")
            .customReplacement("#", "")
            .build();

    public static Pageable getPageable(Integer page, Integer size, String sortBy, String sortDirection) {
        return PageRequest.of(page,size,Sort.Direction.valueOf(sortDirection), sortBy);
    }

    public static String slugify(String slug) {
        return SLUGIFY.slugify(slug);
    }

}
