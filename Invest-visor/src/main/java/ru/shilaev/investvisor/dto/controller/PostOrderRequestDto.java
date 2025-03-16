package ru.shilaev.investvisor.dto.controller;

public record PostOrderRequestDto(
        String accountId,
        long quantity,
        String instrumentId
) { }
