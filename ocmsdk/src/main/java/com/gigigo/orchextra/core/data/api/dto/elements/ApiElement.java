package com.gigigo.orchextra.core.data.api.dto.elements;

import java.util.List;

public class ApiElement {

    private List<String> tags;
    private ApiElementSegmentation segmentation;
    private ApiElementSectionView sectionView;
    private String slug;
    private String elementUrl;

    public ApiElementSegmentation getSegmentation() {
        return segmentation;
    }

    public ApiElementSectionView getSectionView() {
        return sectionView;
    }

    public String getSlug() {
        return slug;
    }

    public String getElementUrl() {
        return elementUrl;
    }

    public List<String> getTags() {
        return tags;
    }
}
