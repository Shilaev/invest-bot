package ru.shilaev.investorchestrator.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import ru.shilaev.investorchestrator.dto.controller.PortfolioController.*;
import ru.tinkoff.piapi.contract.v1.*;
import ru.tinkoff.piapi.contract.v1.SandboxServiceGrpc.SandboxServiceBlockingStub;

@Service
@RequiredArgsConstructor
public class SandboxPortfolioControllerService {

    private final SandboxServiceBlockingStub sandboxServiceBlockingStub;
    private final ConvertService convertService;

    //Регистрация счета
    public OpenAccountResponseDto openAccount(OpenAccountRequestDto requestDto) {
        OpenSandboxAccountRequest openSandboxAccountRequest =
                OpenSandboxAccountRequest.newBuilder()
                        .setName(requestDto.accountTitle())
                        .build();

        OpenSandboxAccountResponse openSandboxAccountResponse = sandboxServiceBlockingStub.openSandboxAccount(openSandboxAccountRequest);
        return new OpenAccountResponseDto(openSandboxAccountResponse.getAccountId());
    }

    //Удаление счета
    public void closeAccount(CloseAccountRequestDto requestDto) {
        sandboxServiceBlockingStub.closeSandboxAccount(
                CloseSandboxAccountRequest.newBuilder()
                        .setAccountId(requestDto.accountId())
                        .build()
        );
    }

    //Вывести все зарегистрированные счета
    public Flux<GetSandboxAccountsResponseDto> getAccounts(GetSandboxAccountsRequestDto requestDto) {
        return Flux.fromIterable(sandboxServiceBlockingStub.getSandboxAccounts(
                        GetAccountsRequest.newBuilder().setStatusValue(requestDto.accountStatus()).build()
                ).getAccountsList())
                .parallel(2)
                .runOn(Schedulers.parallel())
                .map(account -> new GetSandboxAccountsResponseDto(
                        account.getId(),
                        account.getName(),
                        account.getStatus().getNumber(),
                        convertService.convertTimestampToInstant(account.getOpenedDate()),
                        convertService.convertTimestampToInstant(account.getClosedDate()),
                        account.getAccessLevel().getNumber()
                ))
                .sequential();
    }

    //Внести деньги на счет
    public Mono<SandboxPayInResponseDto> payIn(SandboxPayInRequestDto requestDto) {
        SandboxPayInResponse sandboxPayInResponse = sandboxServiceBlockingStub.sandboxPayIn(SandboxPayInRequest.newBuilder()
                .setAccountId(requestDto.accountId())
                .setAmount(MoneyValue.newBuilder()
                        //todo: Нужно добавить метод в ConvertService, который будет конвертировать валюты
                        // по умолчанию рублевый счет, поэтому если кладутся доллары. нужно сконвертировать их в рубли
                        // по текущему курсу
                        .setCurrency(requestDto.currency() == null ? "643" : //Код Рубля по умолчанию
                                requestDto.currency())
                        .setUnits(requestDto.value())
                        .build())
                .build()
        );

        //Текущий баланс
        return Mono.just(new SandboxPayInResponseDto(
                requestDto.accountId(),
                convertService.convertCurrencyCodeToString(requestDto.currency()), //Код в наименование валюты
                (float) (sandboxPayInResponse.getBalance().getUnits() +
                        sandboxPayInResponse.getBalance().getNano() / 1_000_000_000.0)
        ));
    }


}
