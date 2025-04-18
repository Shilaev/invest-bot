package ru.shilaev.investorchestrator.dto.controller.PortfolioController;

import java.math.BigDecimal;

public record SandboxPayInRequestDto(
        String accountId,
        String currency,
        long value
) {
}
