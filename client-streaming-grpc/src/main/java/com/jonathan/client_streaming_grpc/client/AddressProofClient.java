package com.jonathan.client_streaming_grpc;
import com.jonathan.client_streaming_grpc.proto.AddressProofRequest;
import com.jonathan.client_streaming_grpc.proto.BankServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import java.io.FileInputStream;
import java.io.IOException;
import com.jonathan.client_streaming_grpc.proto.AddressProofResponse;


public class AddressProofClient {

    public static void main(String[] args) {
        // Create a channel to connect to the server
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9090)
                .usePlaintext() // Use plaintext for simplicity
                .build();

        // Create a stub for the service
        BankServiceGrpc.BankServiceStub stub = BankServiceGrpc.newStub(channel);

        // Start the streaming request
        StreamObserver<AddressProofRequest> requestObserver = stub.uploadAddressProof(new StreamObserver<AddressProofResponse>() {
            @Override
            public void onNext(AddressProofResponse value) {
                System.out.println("Server Response: " + value.getMessage());
            }

            @Override
            public void onError(Throwable t) {
                System.err.println("Error: " + t.getMessage());
            }

            @Override
            public void onCompleted() {
                System.out.println("Upload completed.");
            }
        });

        // Read the PDF file and send it in chunks
        String pdfPath = "./inputFile/tes.pdf"; // Specify your PDF file path
        try (FileInputStream fis = new FileInputStream(pdfPath)) {
            byte[] buffer = new byte[1024]; // Buffer for reading chunks
            int bytesRead;

            // Read and send data in chunks
            while ((bytesRead = fis.read(buffer)) != -1) {
                AddressProofRequest request = AddressProofRequest.newBuilder()
                        .setPdfFile(com.google.protobuf.ByteString.copyFrom(buffer, 0, bytesRead))
                        .setAccountHolderName("John Doe") // Add any other required fields
                        .setAccountNumber("1234567890") // Add any other required fields
                        .build();
                requestObserver.onNext(request); // Send chunk to server
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            requestObserver.onCompleted(); // Complete the request
        }

        // Shutdown the channel
        try {
            channel.shutdown().awaitTermination(5, java.util.concurrent.TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
