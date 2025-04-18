package ru.shilaev.investorchestrator.dto.controller.SandboxPortfolioController;

public record SandboxPayInResponseDto(
        String accountId,
        String currency,
        float balance
) {
}
