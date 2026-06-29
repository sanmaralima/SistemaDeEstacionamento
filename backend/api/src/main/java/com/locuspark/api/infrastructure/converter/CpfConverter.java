package com.locuspark.api.infrastructure.converter;

import com.locuspark.api.types.Cpf;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class CpfConverter implements AttributeConverter<Cpf, String> {

    @Override
    public String convertToDatabaseColumn(Cpf attribute) {
        return attribute != null ? attribute.getValue() : null;
    }

    @Override
    public Cpf convertToEntityAttribute(String dbData) {
        return dbData != null ? new Cpf(dbData) : null;
    }
}
