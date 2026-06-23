package com.locuspark.api.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "tariff_configurations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TariffConfiguration {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID) // Ajustado para UUID conforme o padrão do projeto
    private UUID id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @Column(nullable = true) // Agora opcional
    private Integer toleranceMinutes;

    @Column(name = "first_hour_value", nullable = true, precision = 10, scale = 2) // Agora opcional
    private BigDecimal firstHourValue;

    @Column(name = "additional_fraction_value", nullable = true, precision = 10, scale = 2) // Agora opcional
    private BigDecimal additionalFractionValue;

    @Column(name = "overnight_fee", nullable = true, precision = 10, scale = 2) // Agora opcional
    private BigDecimal overnightFee;

    @Column(name = "lost_ticket_fee", nullable = true, precision = 10, scale = 2) // Agora opcional
    private BigDecimal lostTicketFee;
}