package com.gigigo.orchextra.core.data.api.mappers.elements;

import com.gigigo.orchextra.core.data.api.dto.elements.ApiElementSectionView;
import com.gigigo.orchextra.core.domain.entities.elements.ElementSectionView;
import com.gigigo.ggglib.mappers.ExternalClassToModelMapper;

public class ApiElementSectionViewMapper
        implements ExternalClassToModelMapper<ApiElementSectionView, ElementSectionView> {

    @Override
    public ElementSectionView externalClassToModel(ApiElementSectionView data) {
        ElementSectionView model = new ElementSectionView();

        model.setText(data.getText());
        model.setImageUrl(data.getImageUrl());
        model.setImageThumb(data.getImageThumb());

        return model;
    }
}
