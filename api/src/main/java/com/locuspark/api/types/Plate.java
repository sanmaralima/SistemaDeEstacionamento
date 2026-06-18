package com.locuspark.api.types;

import com.locuspark.api.exception.BusinessException;
import java.util.Objects;

public final class Plate {

    private final String value;

    public Plate(String rawValue) {
        if (rawValue == null || rawValue.trim().isEmpty()) {
            throw new BusinessException("Placa não pode ser nula ou vazia.");
        }
        String cleaned = rawValue.replace(" ", "").replace("-", "").toUpperCase();
        
        boolean matchesTraditional = cleaned.matches("^[A-Z]{3}[0-9]{4}$");
        boolean matchesMercosul = cleaned.matches("^[A-Z]{3}[0-9][A-Z][0-9]{2}$");
        
        if (!matchesTraditional && !matchesMercosul) {
            throw new BusinessException("Placa inválida.");
        }
        this.value = cleaned;
    }

    public boolean isMercosul() {
        return value.matches("^[A-Z]{3}[0-9][A-Z][0-9]{2}$");
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Plate plate = (Plate) o;
        return Objects.equals(value, plate.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        if (!isMercosul()) {
            return value.substring(0, 3) + "-" + value.substring(3);
        }
        return value;
    }
}
