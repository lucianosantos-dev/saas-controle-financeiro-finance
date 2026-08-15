package com.lucianodev.saascontrolefinanceirofinance.entity;

import com.lucianodev.saascontrolefinanceirofinance.enums.TipoVerificacao;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "tokens_verificacao")
public class TokenVerificacao {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(length = 255, nullable = false, unique = true)
    private String token;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoVerificacao tipoVerificacao;

    @Column(nullable = false)
    private LocalDateTime expiraEm;

    @Column
    private LocalDateTime usadoEm;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    public boolean estaExpirado() {
        return LocalDateTime.now().isAfter(this.expiraEm);
    }

    public boolean foiUsado() {
        return this.usadoEm != null;
    }

    public void marcarComoUsado() {
        this.usadoEm = LocalDateTime.now();
    }
}