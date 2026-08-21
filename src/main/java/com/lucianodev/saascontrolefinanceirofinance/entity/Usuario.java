package com.lucianodev.saascontrolefinanceirofinance.entity;

import com.lucianodev.saascontrolefinanceirofinance.exception.OperacaoInvalidaException;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
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

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "usuarios_roles",
            joinColumns = @JoinColumn(name = "usuario_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "role_id", nullable = false)
    )
    private Set<Role> roles = new HashSet<>();

    @Column(nullable = false)
    private Boolean ativo = true;

    @PrePersist
    private void prePersist() {
        if (this.criadoEm == null) {
            this.criadoEm = LocalDateTime.now();
        }
    }

    public boolean ehAdmin() {
        return this.roles.stream().anyMatch(roles -> roles.getNome().equals("ADMIN"));
    }

    public void desativar() {
        if (ehAdmin()) {
            throw new OperacaoInvalidaException("Erro! Um Administrador do sistema não pode ser desativado.");
        }
        this.ativo = false;
    }

    public void reativar() {
        this.ativo = true;
    }
}