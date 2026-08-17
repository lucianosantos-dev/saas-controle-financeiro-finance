package com.lucianodev.saascontrolefinanceirofinance.dto;

import java.time.Instant;

public record CustomErrorDto(
        Instant timestamp,
        Integer status,
        String message,
        String path

) {
}
