package com.locuspark.api.infrastructure.converter;

import com.locuspark.api.types.Cnpj;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class CnpjConverter implements AttributeConverter<Cnpj, String> {

    @Override
    public String convertToDatabaseColumn(Cnpj attribute) {
        return attribute != null ? attribute.getValue() : null;
    }

    @Override
    public Cnpj convertToEntityAttribute(String dbData) {
        return dbData != null ? new Cnpj(dbData) : null;
    }
}
