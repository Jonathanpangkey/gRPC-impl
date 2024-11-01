package com.jonathan.server_streaming_grpc.Client;

import com.jonathan.server_streaming_grpc.proto.AccountRequest;
import com.jonathan.server_streaming_grpc.proto.TransactionDetailList;
import com.jonathan.server_streaming_grpc.proto.TransactionServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class GrpcClient {

    public static void main(String[] args) throws InterruptedException {
        // Establish a connection to the gRPC server
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9090)
                .usePlaintext()  // Disable SSL for development purposes
                .build();

        // Create a non-blocking (asynchronous) stub for server-streaming
        TransactionServiceGrpc.TransactionServiceStub asyncStub = TransactionServiceGrpc.newStub(channel);

        // Prepare the request
        AccountRequest request = AccountRequest.newBuilder()
                .setAccountNumber("1234567890")
                .setDurationInDays(7)
                .build();

        // Use CountDownLatch to wait for the stream to complete
        CountDownLatch latch = new CountDownLatch(1);

        // Call the server-streaming method and define a StreamObserver to handle the stream
        asyncStub.streamTransactions(request, new StreamObserver<TransactionDetailList>() {

            @Override
            public void onNext(TransactionDetailList transactionDetailList) {
                // This method is called for each response from the server
                System.out.println("Received Transaction Details: ");
                transactionDetailList.getTransactionDetailsList().forEach(transactionDetail -> {
                    System.out.println("Transaction ID: " + transactionDetail.getTransactionId());
                    System.out.println("Transaction Type: " + transactionDetail.getTransactionType());
                    System.out.println("Transaction Amount: " + transactionDetail.getTransactionAmount());
                });
            }

            @Override
            public void onError(Throwable t) {
                // Handle any errors that occur during the streaming
                System.err.println("Error occurred: " + t.getMessage());
                latch.countDown();
            }

            @Override
            public void onCompleted() {
                // This method is called when the server has completed sending the stream
                System.out.println("All transactions have been received.");
                latch.countDown();
            }
        });

        // Keep the client running until the stream is complete
        latch.await(1, TimeUnit.MINUTES);

        // Shut down the channel
        channel.shutdown();
    }
}
