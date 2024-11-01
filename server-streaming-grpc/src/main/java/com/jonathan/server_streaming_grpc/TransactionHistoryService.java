package com.jonathan.server_streaming_grpc;

import com.jonathan.server_streaming_grpc.proto.TransactionServiceGrpc;
import com.jonathan.server_streaming_grpc.proto.AccountRequest;
import com.jonathan.server_streaming_grpc.proto.TransactionDetailList;
import com.jonathan.server_streaming_grpc.proto.TransactionDetail;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@GrpcService
public class TransactionHistoryService extends TransactionServiceGrpc.TransactionServiceImplBase {

    @Override
    public void streamTransactions(AccountRequest request, StreamObserver<TransactionDetailList> responseObserver) {
        // Assuming you have a method to fetch transaction details based on the duration
        // in days
        List<Transaction> transactions = fetchTransactions(request.getDurationInDays());
        int batchSize = 3; // How many transactions to send at once

        for (int i = 0; i < transactions.size(); i += batchSize) {
            int endIndex = Math.min(i + batchSize, transactions.size());
            List<Transaction> batchTransactions = transactions.subList(i, endIndex);

            TransactionDetailList.Builder transactionDetailListBuilder = TransactionDetailList.newBuilder();

            for (Transaction transaction : batchTransactions) {
                TransactionDetail transactionDetail = createTransactionDetailFromTransaction(transaction);
                transactionDetailListBuilder.addTransactionDetails(transactionDetail);
            }
            TransactionDetailList transactionDetailList = transactionDetailListBuilder.build();

            responseObserver.onNext(transactionDetailList);

            // Delay between sending batches (if necessary)
            // You can adjust this based on your requirements
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                responseObserver.onError(e);
                return;
            }
        }

        responseObserver.onCompleted();
    }

    private TransactionDetail createTransactionDetailFromTransaction(Transaction transaction) {
        return TransactionDetail.newBuilder()
                .setTransactionId(transaction.getId())
                .setTransactionType(transaction.getType())
                .setTransactionAmount(transaction.getAmount())
                .build();
    }

    private List<Transaction> fetchTransactions(int durationInDays) {
        List<Transaction> transactions = new ArrayList<>();

        // Mock data for transaction details
        Transaction transaction1 = new Transaction("1", "Deposit", 100.0f);
        Transaction transaction2 = new Transaction("2", "Withdrawal", 50.0f);
        Transaction transaction3 = new Transaction("3", "Transfer", 75.0f);
        Transaction transaction4 = new Transaction("4", "Deposit", 200.0f);
        Transaction transaction5 = new Transaction("5", "Withdrawal", 30.0f);

        transactions.addAll(Arrays.asList(transaction1, transaction2, transaction3, transaction4, transaction5));

        return transactions;
    }
}