package com.gigigo.orchextra.core.domain.entities.menus;

import com.gigigo.orchextra.core.domain.entities.elements.Element;
import java.util.List;

public class MenuContent {

    private String id;
    private String slug;
    private List<Element> elements;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public List<Element> getElements() {
        return elements;
    }

    public void setElements(List<Element> elements) {
        this.elements = elements;
    }
}
