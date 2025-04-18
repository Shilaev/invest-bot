package ru.shilaev.investorchestrator.dto.controller.SandboxPortfolioController;

public record SandboxPayInRequestDto(
        String accountId,
        String currency,
        long value
) {
}
