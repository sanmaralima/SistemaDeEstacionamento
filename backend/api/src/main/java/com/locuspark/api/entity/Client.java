package com.locuspark.api.entity;

import com.locuspark.api.types.Cpf;
import com.locuspark.api.enums.ClientType;
import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Entity
@Table(name = "clients")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, length = 11)
    private Cpf cpf;

    @Column(nullable = false)
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ClientType type;

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;
}
