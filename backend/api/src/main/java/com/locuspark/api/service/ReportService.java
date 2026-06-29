package com.locuspark.api.service;

import com.locuspark.api.dto.response.ReportResponse;
import com.locuspark.api.entity.Ticket;
import com.locuspark.api.enums.PaymentMethod;
import com.locuspark.api.enums.TicketStatus;
import com.locuspark.api.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReportService {

    private final TicketRepository ticketRepository;

    public ReportResponse getCompanyReport(UUID companyId) {
        List<Ticket> tickets = ticketRepository.findAllByCompanyIdAndStatus(companyId, TicketStatus.PAID);

        BigDecimal totalRevenue = tickets.stream()
                .map(Ticket::getTotalAmount)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        long totalServices = tickets.size();

        double averageStayMinutes = tickets.stream()
                .filter(t -> t.getEnteredAt() != null && t.getExitedAt() != null)
                .mapToLong(t -> Duration.between(t.getEnteredAt(), t.getExitedAt()).toMinutes())
                .average()
                .orElse(0.0);

        Map<PaymentMethod, BigDecimal> revenueByPaymentMethod = new EnumMap<>(PaymentMethod.class);
        for (PaymentMethod method : PaymentMethod.values()) {
            revenueByPaymentMethod.put(method, BigDecimal.ZERO);
        }

        tickets.stream()
                .filter(t -> t.getPaymentMethod() != null && t.getTotalAmount() != null)
                .forEach(t -> {
                    PaymentMethod method = t.getPaymentMethod();
                    BigDecimal amount = t.getTotalAmount();
                    revenueByPaymentMethod.put(method, revenueByPaymentMethod.get(method).add(amount));
                });

        return new ReportResponse(totalRevenue, totalServices, averageStayMinutes, revenueByPaymentMethod);
    }
}
