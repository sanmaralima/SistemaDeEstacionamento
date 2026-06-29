package com.locuspark.api.types;

import com.locuspark.api.exception.BusinessException;
import java.util.Objects;

public final class Cpf {

    private final String value;

    public Cpf(String rawValue) {
        if (rawValue == null || rawValue.trim().isEmpty()) {
            throw new BusinessException("CPF não pode ser nulo ou vazio.");
        }
        String cleaned = rawValue.replaceAll("\\D", "");
        if (cleaned.length() != 11 || isRepeated(cleaned) || !isValidCpf(cleaned)) {
            throw new BusinessException("CPF inválido.");
        }
        this.value = cleaned;
    }

    public String getValue() {
        return value;
    }

    private boolean isRepeated(String cleaned) {
        return cleaned.matches("(\\d)\\1{10}");
    }

    private boolean isValidCpf(String cleaned) {
        int[] digits = new int[11];
        for (int i = 0; i < 11; i++) {
            digits[i] = Character.getNumericValue(cleaned.charAt(i));
        }

        // Primeiro dígito verificador
        int sum1 = 0;
        for (int i = 0; i < 9; i++) {
            sum1 += digits[i] * (10 - i);
        }
        int remainder1 = sum1 % 11;
        int expectedDigit1 = remainder1 < 2 ? 0 : 11 - remainder1;
        if (digits[9] != expectedDigit1) {
            return false;
        }

        // Segundo dígito verificador
        int sum2 = 0;
        for (int i = 0; i < 10; i++) {
            sum2 += digits[i] * (11 - i);
        }
        int remainder2 = sum2 % 11;
        int expectedDigit2 = remainder2 < 2 ? 0 : 11 - remainder2;
        return digits[10] == expectedDigit2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cpf cpf = (Cpf) o;
        return Objects.equals(value, cpf.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return String.format("%s.%s.%s-%s",
                value.substring(0, 3),
                value.substring(3, 6),
                value.substring(6, 9),
                value.substring(9, 11)
        );
    }
}
