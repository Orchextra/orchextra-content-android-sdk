package com.gigigo.orchextra.core.domain.entities.elements;

import java.util.List;

public class Element {

    private ElementSegmentation segmentation;
    private ElementSectionView sectionView;
    private String slug;
    private String elementUrl;
    private List<String> tags;

    public ElementSegmentation getSegmentation() {
        return segmentation;
    }

    public void setSegmentation(ElementSegmentation segmentation) {
        this.segmentation = segmentation;
    }

    public ElementSectionView getSectionView() {
        return sectionView;
    }

    public void setSectionView(ElementSectionView sectionView) {
        this.sectionView = sectionView;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getElementUrl() {
        return elementUrl;
    }

    public void setElementUrl(String elementUrl) {
        this.elementUrl = elementUrl;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public List<String> getTags() {
        return tags;
    }
}
