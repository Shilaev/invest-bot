package ru.shilaev.investvisor.service;

import io.grpc.stub.StreamObserver;
import org.springframework.grpc.server.service.GrpcService;
import ru.shilaev.investvisor.AnalyticsApi;
import ru.shilaev.investvisor.DataProcessingGrpc;

@GrpcService
public class DataProcessing extends DataProcessingGrpc.DataProcessingImplBase {
    @Override public void processNumbers(AnalyticsApi.NumberArray request, StreamObserver<AnalyticsApi.AnalyticsResult> responseObserver) {
        AnalyticsApi.AnalyticsResult build = AnalyticsApi.AnalyticsResult.newBuilder()
                .setResult(111111111.1)
                .build();
        responseObserver.onNext(build);
        System.out.println(build);
    }
}
