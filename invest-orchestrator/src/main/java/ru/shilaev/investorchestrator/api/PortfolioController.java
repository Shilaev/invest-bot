package ru.shilaev.investorchestrator.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.shilaev.investorchestrator.dto.controller.PortfolioController.*;
import ru.shilaev.investorchestrator.dto.controller.PostOrderRequestDto;
import ru.shilaev.investorchestrator.service.PortfolioControllerService;
import ru.shilaev.investorchestrator.service.ConvertService;
import ru.tinkoff.piapi.contract.v1.*;
import ru.tinkoff.piapi.contract.v1.SandboxServiceGrpc.SandboxServiceBlockingStub;

import java.util.UUID;

@RestController
@RequestMapping("portfolio/api")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PortfolioController {

    //todo: удалить после вынесения логики в PortfolioControllerService
    private final SandboxServiceBlockingStub sandboxServiceBlockingStub;
    private final PortfolioControllerService portfolioControllerService;
    private final ConvertService convertService;
    private static final Logger logger = LoggerFactory.getLogger(PortfolioController.class);

    //Зарегистрировать счет
    @PostMapping("open-account")
    public Mono<OpenAccountResponseDto> openAccount(@Valid @RequestBody OpenAccountRequestDto openAccountRequestDto) {
        OpenAccountResponseDto openAccountResponseDto = portfolioControllerService.openAccount(openAccountRequestDto);

        logger.info("Account: {}\nwith id: {} registered successfully",
                openAccountRequestDto.accountTitle(), openAccountResponseDto.accountId());
        return Mono.just(openAccountResponseDto);
    }

    //Закрыть счет
    @PostMapping("close-account")
    public void closeAccount(@Valid @RequestBody CloseAccountRequestDto closeAccountRequestDto) {
        logger.info("Account with id: {} closed successfully", closeAccountRequestDto.accountId());
        portfolioControllerService.closeAccount(closeAccountRequestDto);
    }

    //Получить список счетов
    @GetMapping("get-accounts")
    public Flux<GetSandboxAccountsResponseDto> getAccounts(@Valid @RequestBody GetSandboxAccountsRequestDto getSandboxAccountsRequestDto) {
        return portfolioControllerService.getAccounts(getSandboxAccountsRequestDto);
    }

    //Внести деньги на счет
    @PostMapping("pay-in")
    public Mono<SandboxPayInResponseDto> payIn(@Valid @RequestBody SandboxPayInRequestDto requestDto) {
        return portfolioControllerService.payIn(requestDto);
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
