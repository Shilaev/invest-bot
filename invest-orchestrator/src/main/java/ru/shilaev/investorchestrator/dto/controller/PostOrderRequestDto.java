package ru.shilaev.investorchestrator.dto.controller;

public record PostOrderRequestDto(
        String accountId,
        long quantity,
        String instrumentId
) { }
