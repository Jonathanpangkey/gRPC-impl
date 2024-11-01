package com.jonathan.unary_grpc_server.service;
import com.jonathan.unary_grpc_server.proto.AccountBalanceServiceGrpc.AccountBalanceServiceImplBase;
import io.grpc.stub.StreamObserver;
import com.jonathan.unary_grpc_server.proto.AccountRequest;
import com.jonathan.unary_grpc_server.proto.AccountBalanceResponse;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class BankAccountBalanceService extends AccountBalanceServiceImplBase {

    @Override
    public void getAccountBalance(AccountRequest request,
                                  StreamObserver<AccountBalanceResponse> responseObserver) {

        AccountBalanceResponse empResp = AccountBalanceResponse.newBuilder()
                .setAccountNumber(request.getAccountNumber()).setBalance(100).build();

        // set the response object
        responseObserver.onNext(empResp);

        // mark process is completed
        responseObserver.onCompleted();
    }
}
