package com.gigigo.orchextra.core.data.dto.menus;


import com.gigigo.orchextra.core.data.dto.elements.ApiElement;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class ApiMenuContent {

    @SerializedName("_id")
    private String id;

    @SerializedName("slug")
    private String slug;

    @SerializedName("elements")
    private List<ApiElement> elements;

    public String getId() {
        return id;
    }

    public String getSlug() {
        return slug;
    }

    public List<ApiElement> getElements() {
        return elements;
    }
}
