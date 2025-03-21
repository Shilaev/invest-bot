package ru.shilaev.investorchestrator.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.shilaev.investorchestrator.dto.controller.PostOrderRequestDto;
import ru.shilaev.investorchestrator.service.UtilsService;
import ru.tinkoff.piapi.contract.v1.*;
import ru.tinkoff.piapi.contract.v1.SandboxServiceGrpc.SandboxServiceBlockingStub;

import java.util.UUID;

@RestController
@RequestMapping("portfolio/api")
@RequiredArgsConstructor
public class PortfolioController {

    private final SandboxServiceBlockingStub sandboxServiceBlockingStub;
    private final UtilsService utilsService;

    // Зарегистрировать счет
    @PostMapping("open-account")
    public Mono<String> openAccount(@Valid @RequestParam String name) {
        OpenSandboxAccountRequest openSandboxAccountRequest =
                OpenSandboxAccountRequest.newBuilder()
                        .setName(name)
                        .build();

        OpenSandboxAccountResponse openSandboxAccountResponse = sandboxServiceBlockingStub.openSandboxAccount(openSandboxAccountRequest);
        String accountId = openSandboxAccountResponse.getAccountId();

        return Mono.just("Account: " + name + "\nwith id: " + accountId + " registered successfully");
    }

    // Получить список счетов
    @GetMapping("get-accounts")
    public Flux<String> getAccounts(@Valid @RequestParam int accountStatus) {
        return Flux.fromIterable(sandboxServiceBlockingStub.getSandboxAccounts(
                GetAccountsRequest.newBuilder().setStatusValue(accountStatus).build()
        ).getAccountsList()).map(account -> {
            String stringBuilder = "==========================================" + "\n" +
                    "ID: " + account.getId() + "\n" +
                    "NAME: " + account.getName() + "\n" +
                    "STATUS: " + account.getStatus() + "\n" +
                    "OPEN DATE: " + utilsService.convertTimestampToInstant(account.getOpenedDate()) + "\n" +
                    "CLOSE DATE: " + utilsService.convertTimestampToInstant(account.getClosedDate()) + "\n" +
                    "ACCESS LEVEL " + account.getAccessLevel() + "\n" +
                    "==========================================" + "\n" +
                    "\n";
            return stringBuilder;
        });
    }

    @PostMapping("close-account")
    public void closeAccount(@Valid @RequestParam String accountId) {
        sandboxServiceBlockingStub.closeSandboxAccount(
                CloseSandboxAccountRequest.newBuilder()
                        .setAccountId(accountId)
                        .build()
        );
    }

    @PostMapping("pay-in")
    public Mono<String> payIn(@RequestParam String accountId,
                              @RequestParam long amount) {
        return Mono.just(String.valueOf(
                sandboxServiceBlockingStub.sandboxPayIn(SandboxPayInRequest.newBuilder()
                        .setAccountId(accountId)
                        .setAmount(MoneyValue.newBuilder()
                                .setCurrency("643")
                                .setUnits(amount)
                                .build())
                        .build()
                ).getBalance().getUnits()));
    }

    @GetMapping("get-withdraw-limits")
    public Mono<String> getWithdrawLimits(@Valid @RequestParam String accountId) {
        WithdrawLimitsResponse sandboxWithdrawLimits = sandboxServiceBlockingStub.getSandboxWithdrawLimits(WithdrawLimitsRequest.newBuilder()
                .setAccountId(accountId)
                .build());
        return Mono.just(String.valueOf(sandboxWithdrawLimits.getMoney(0)));
    }

    @PostMapping("post-order-request")
    public Mono<String> postOrderRequest(@Valid @RequestBody PostOrderRequestDto postOrderRequestDto) {
        PostOrderResponse postOrderResponse = sandboxServiceBlockingStub.postSandboxOrder(
                PostOrderRequest.newBuilder()
                        .setAccountId(postOrderRequestDto.accountId())
                        .setQuantity(postOrderRequestDto.quantity())
                        .setDirection(OrderDirection.ORDER_DIRECTION_BUY)
                        .setOrderType(OrderType.ORDER_TYPE_BESTPRICE)
                        .setOrderId(UUID.randomUUID().toString())
                        .setInstrumentId(postOrderRequestDto.instrumentId())
                        .setPriceType(PriceType.PRICE_TYPE_CURRENCY)
                        .build()
        );

        return Mono.just(postOrderRequestDto.toString());
    }

    @GetMapping("get-portfolio")
    public Mono<String> getPortfolio(@Valid @RequestParam String accountId) {
        PortfolioResponse sandboxPortfolio = sandboxServiceBlockingStub.getSandboxPortfolio(PortfolioRequest.newBuilder()
                .setAccountId(accountId)
                .setCurrency(PortfolioRequest.CurrencyRequest.RUB)
                .build()
        );

        return Mono.just(sandboxPortfolio.toString());
    }

}
