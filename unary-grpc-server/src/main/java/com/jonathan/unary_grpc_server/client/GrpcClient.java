package com.jonathan.unary_grpc_server.client;

import com.jonathan.unary_grpc_server.proto.AccountBalanceServiceGrpc.AccountBalanceServiceBlockingStub;
import com.jonathan.unary_grpc_server.proto.AccountRequest;
import com.jonathan.unary_grpc_server.proto.AccountBalanceResponse;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import static com.jonathan.unary_grpc_server.proto.AccountBalanceServiceGrpc.newBlockingStub;

public class GrpcClient {

    public static void main(String[] args) {
        // Create a channel connecting to the gRPC server
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9090)
                .usePlaintext()  // Disable SSL for development
                .build();

        // Create a blocking stub (synchronous client)
        AccountBalanceServiceBlockingStub stub = newBlockingStub(channel);

        // Prepare a request with the account number
        AccountRequest request = AccountRequest.newBuilder()
                .setAccountNumber("1234567890")
                .build();

        // Make the unary gRPC call and get the response
        AccountBalanceResponse response = stub.getAccountBalance(request);

        // Print the response from the server
        System.out.println("Account Number: " + response.getAccountNumber());
        System.out.println("Account Balance: " + response.getBalance());

        // Shut down the channel after use
        channel.shutdown();
    }
}
