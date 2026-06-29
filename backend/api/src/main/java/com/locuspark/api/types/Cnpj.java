package com.locuspark.api.types;

import com.locuspark.api.exception.BusinessException;
import java.util.Objects;

public final class Cnpj {

    private final String value;

    public Cnpj(String rawValue) {
        if (rawValue == null || rawValue.trim().isEmpty()) {
            throw new BusinessException("CNPJ não pode ser nulo ou vazio.");
        }
        String cleaned = rawValue.replaceAll("\\D", "");
        if (cleaned.length() != 14 || isRepeated(cleaned) || !isValidCnpj(cleaned)) {
            throw new BusinessException("CNPJ inválido.");
        }
        this.value = cleaned;
    }

    public String getValue() {
        return value;
    }

    private boolean isRepeated(String cleaned) {
        return cleaned.matches("(\\d)\\1{13}");
    }

    private boolean isValidCnpj(String cleaned) {
        int[] digits = new int[14];
        for (int i = 0; i < 14; i++) {
            digits[i] = Character.getNumericValue(cleaned.charAt(i));
        }

        int sum1 = 0;
        int[] weights1 = {5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
        for (int i = 0; i < 12; i++) {
            sum1 += digits[i] * weights1[i];
        }
        int remainder1 = sum1 % 11;
        int expectedDigit1 = remainder1 < 2 ? 0 : 11 - remainder1;
        if (digits[12] != expectedDigit1) {
            return false;
        }

        int sum2 = 0;
        int[] weights2 = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
        for (int i = 0; i < 13; i++) {
            sum2 += digits[i] * weights2[i];
        }
        int remainder2 = sum2 % 11;
        int expectedDigit2 = remainder2 < 2 ? 0 : 11 - remainder2;
        return digits[13] == expectedDigit2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cnpj cnpj = (Cnpj) o;
        return Objects.equals(value, cnpj.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return String.format("%s.%s.%s/%s-%s",
                value.substring(0, 2),
                value.substring(2, 5),
                value.substring(5, 8),
                value.substring(8, 12),
                value.substring(12, 14)
        );
    }
}
