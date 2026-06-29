package com.locuspark.api.dto.response;

import com.locuspark.api.enums.PaymentMethod;
import java.math.BigDecimal;
import java.util.Map;

public record ReportResponse(
        BigDecimal totalRevenue,
        long totalServices,
        double averageStayMinutes,
        Map<PaymentMethod, BigDecimal> revenueByPaymentMethod
) {}
