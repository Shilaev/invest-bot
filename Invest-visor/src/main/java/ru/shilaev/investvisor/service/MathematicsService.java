package ru.shilaev.investvisor.service;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import ru.shilaev.investvisor.AnalyticsApi;
import ru.shilaev.investvisor.AnalyticsApi.AnalyticsResult;
import ru.shilaev.investvisor.AnalyticsApi.NumberArray;
import ru.shilaev.investvisor.DataProcessingGrpc;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class MathematicsService {

    private final DataProcessingGrpc.DataProcessingBlockingStub dataProcessingBlockingStub;

    public AnalyticsResult trySendNumbers() {
        NumberArray numberArray = NumberArray.newBuilder()
                .addNumbers(2500.0)
                .addNumbers(3600.0)
                .addNumbers(3640.0)
                .addNumbers(2540.0)
                .addNumbers(4500.0)
                .addNumbers(1000.0)
                .addNumbers(2055.0)
                .addNumbers(2055.0)
                .addNumbers(2055.0)
                .addNumbers(2055.0)
                .build();

        AnalyticsResult analyticsResult = dataProcessingBlockingStub.processNumbers(numberArray);

        return analyticsResult;
    }
}
