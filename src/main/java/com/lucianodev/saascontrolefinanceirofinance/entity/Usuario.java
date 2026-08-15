package com.lucianodev.saascontrolefinanceirofinance.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(length = 120, nullable = false)
    private String nome;

    @Column(length = 160, nullable = false, unique = true)
    private String email;

    @Column(length = 255, nullable = false)
    private String senhaHash;

    @Column(nullable = false)
    private Boolean emailVerificado = false;

    @Column(length = 3, nullable = false)
    private String moeda = "BRL";

    @Column(nullable = false)
    private String fusoHorario = "America/Sao_Paulo";

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    @PrePersist
    private void prePersist() {
        if (this.criadoEm == null) {
            this.criadoEm = LocalDateTime.now();
        }
    }
}