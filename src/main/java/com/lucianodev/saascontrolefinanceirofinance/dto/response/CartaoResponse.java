package com.lucianodev.saascontrolefinanceirofinance.dto.response;

import java.math.BigDecimal;
import java.util.UUID;

public record CartaoResponse(
        UUID id,
        String nome,
        BigDecimal limiteCredito,
        Integer diaFechamento,
        Integer diaVencimento
) {
}
