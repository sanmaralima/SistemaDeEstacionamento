package com.locuspark.api.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "pricing_configurations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PricingConfiguration {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @Column(name = "daily_trigger_hours", nullable = false)
    private Integer dailyTriggerHours; // Ex: Após 6 horas, vira diária

    @Column(name = "daily_value", nullable = false, precision = 10, scale = 2)
    private BigDecimal dailyValue;

    @Column(name = "monthly_base_value", nullable = false, precision = 10, scale = 2)
    private BigDecimal monthlyBaseValue;
}