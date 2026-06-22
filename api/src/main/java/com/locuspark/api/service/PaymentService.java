package com.locuspark.api.service;

import com.locuspark.api.entity.*;
import com.locuspark.api.enums.DiscountType;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;

@Service
public class PaymentService {

    public BigDecimal calculateStayAmount(
            Ticket ticket,
            LocalDateTime exitTime,
            TariffConfiguration tariff,
            PricingConfiguration plan) {

        Duration duration = Duration.between(ticket.getEnteredAt(), exitTime);
        long totalMinutes = duration.toMinutes();

        if (totalMinutes <= tariff.getToleranceMinutes()) {
            return BigDecimal.ZERO;
        }

        long totalHours = (long) Math.ceil(totalMinutes / 60.0);
        if (totalHours >= plan.getDailyTriggerHours()) {
            return applyPartnershipDiscount(plan.getDailyValue(), ticket.getPartnership(), totalMinutes, tariff);
        }

        BigDecimal finalAmount = BigDecimal.ZERO;

        finalAmount = finalAmount.add(tariff.getFirstHourValue());
        long remainingMinutes = totalMinutes - 60;

        if (remainingMinutes > 0) {
            long fractionsOf15 = (long) Math.ceil(remainingMinutes / 15.0);
            BigDecimal additionalCost = tariff.getAdditionalFractionValue()
                    .multiply(BigDecimal.valueOf(fractionsOf15));
            finalAmount = finalAmount.add(additionalCost);
        }

        return applyPartnershipDiscount(finalAmount, ticket.getPartnership(), totalMinutes, tariff);
    }

    private BigDecimal applyPartnershipDiscount(BigDecimal originalAmount, Partnership partnership, long totalMinutes, TariffConfiguration tariff) {
        BigDecimal calculatedAmount = originalAmount;

        if (partnership == null) {
            return calculatedAmount.compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : calculatedAmount;
        }

        if (partnership.getDiscountType() == DiscountType.FIXED_VALUE) {
            calculatedAmount = calculatedAmount.subtract(partnership.getValue());
            return calculatedAmount.compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : calculatedAmount;
        }

        if (partnership.getDiscountType() == DiscountType.PERCENTAGE) {
            BigDecimal discountFactor = partnership.getValue().divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP);
            BigDecimal discountAmount = calculatedAmount.multiply(discountFactor);
            calculatedAmount = calculatedAmount.subtract(discountAmount);
            return calculatedAmount.compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : calculatedAmount;
        }

        if (partnership.getDiscountType() == DiscountType.FREE_HOURS) {
            long freeMinutes = partnership.getValue().longValue() * 60;
            long continuousMinutes = totalMinutes - freeMinutes;

            if (continuousMinutes <= tariff.getToleranceMinutes()) {
                return BigDecimal.ZERO;
            }

            BigDecimal costPerHour = tariff.getFirstHourValue();
            BigDecimal discount = costPerHour.multiply(partnership.getValue());
            calculatedAmount = calculatedAmount.subtract(discount);
            return calculatedAmount.compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : calculatedAmount;
        }

        return calculatedAmount.compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : calculatedAmount;
    }
}