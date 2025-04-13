package ru.shilaev.investorchestrator.dto.controller.PortfolioController;

import java.time.Instant;

public record GetSandboxAccountsResponseDto(
        String id,
        String name,
        int status,
        Instant openDate,
        Instant CloseDate,
        int accessLevel
) {
}
