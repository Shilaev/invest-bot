package ru.shilaev.investorchestrator.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;
import ru.shilaev.investorchestrator.dto.controller.PortfolioController.*;
import ru.tinkoff.piapi.contract.v1.*;
import ru.tinkoff.piapi.contract.v1.SandboxServiceGrpc.SandboxServiceBlockingStub;

@Service
@RequiredArgsConstructor
public class PortfolioControllerService {

    private final SandboxServiceBlockingStub sandboxServiceBlockingStub;
    private final UtilsService utilsService;

    public OpenAccountResponseDto openAccount(OpenAccountRequestDto openAccountRequestDto) {
        OpenSandboxAccountRequest openSandboxAccountRequest =
                OpenSandboxAccountRequest.newBuilder()
                        .setName(openAccountRequestDto.accountTitle())
                        .build();

        OpenSandboxAccountResponse openSandboxAccountResponse = sandboxServiceBlockingStub.openSandboxAccount(openSandboxAccountRequest);
        OpenAccountResponseDto openAccountResponseDto = new OpenAccountResponseDto(openSandboxAccountResponse.getAccountId());

        return openAccountResponseDto;
    }

    public void closeAccount(CloseAccountRequestDto closeAccountRequestDto) {
        sandboxServiceBlockingStub.closeSandboxAccount(
                CloseSandboxAccountRequest.newBuilder()
                        .setAccountId(closeAccountRequestDto.accountId())
                        .build()
        );
    }

    public Flux<GetSandboxAccountsResponseDto> getAccounts(GetSandboxAccountsRequestDto getSandboxAccountsRequestDto) {
        return Flux.fromIterable(sandboxServiceBlockingStub.getSandboxAccounts(
                        GetAccountsRequest.newBuilder().setStatusValue(getSandboxAccountsRequestDto.accountStatus()).build()
                ).getAccountsList())
                .parallel(2)
                .runOn(Schedulers.parallel())
                .map(account -> {
                    GetSandboxAccountsResponseDto getSandboxAccountsResponseDto = new GetSandboxAccountsResponseDto(
                            account.getId(),
                            account.getName(),
                            account.getStatus().getNumber(),
                            utilsService.convertTimestampToInstant(account.getOpenedDate()),
                            utilsService.convertTimestampToInstant(account.getClosedDate()),
                            account.getAccessLevel().getNumber()
                    );
                    return getSandboxAccountsResponseDto;
                })
                .sequential();
    }

}
