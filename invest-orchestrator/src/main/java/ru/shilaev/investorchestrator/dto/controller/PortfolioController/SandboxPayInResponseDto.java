package ru.shilaev.investorchestrator.dto.controller.PortfolioController;

public record SandboxPayInResponseDto(
        String accountId,
        String currency,
        float balance
) {
}
