package com.locuspark.api.infrastructure.converter;

import com.locuspark.api.types.Plate;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class PlateConverter implements AttributeConverter<Plate, String> {

    @Override
    public String convertToDatabaseColumn(Plate attribute) {
        return attribute != null ? attribute.getValue() : null;
    }

    @Override
    public Plate convertToEntityAttribute(String dbData) {
        return dbData != null ? new Plate(dbData) : null;
    }
}
