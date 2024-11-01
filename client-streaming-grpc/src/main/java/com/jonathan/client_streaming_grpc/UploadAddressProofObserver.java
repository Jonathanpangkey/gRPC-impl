package com.jonathan.client_streaming_grpc;

import com.jonathan.client_streaming_grpc.proto.AddressProofRequest;
import com.jonathan.client_streaming_grpc.proto.AddressProofResponse;
import io.grpc.stub.StreamObserver;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class UploadAddressProofObserver implements StreamObserver<AddressProofRequest> {

    private String accountHolderName;
    private String accountNumber;
    private ByteArrayOutputStream pdfBytes = new ByteArrayOutputStream();
    private final StreamObserver<AddressProofResponse> responseObserver;

    public UploadAddressProofObserver(StreamObserver<AddressProofResponse> responseObserver) {
        this.responseObserver = responseObserver;
    }

    @Override
    public void onNext(AddressProofRequest request) {
        // Retrieve data from each request chunk
        if (request.hasField(request.getDescriptor().findFieldByName("account_holder_name"))) {
            accountHolderName = request.getAccountHolderName();
        }
        if (request.hasField(request.getDescriptor().findFieldByName("account_number"))) {
            accountNumber = request.getAccountNumber();
        }
        if (request.hasField(request.getDescriptor().findFieldByName("pdf_file"))) {
            try {
                // Append the received bytes from the PDF file
                pdfBytes.write(request.getPdfFile().toByteArray());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onError(Throwable t) {
        // Handle any errors that occur during streaming
    }

    @Override
    public void onCompleted() {
        // Process the complete PDF file
        // Implement the required logic to handle the address proof document
        // You can write the byte array stored in pdfBytes to a file or store it in a
        // database, etc.
        try {
            FileOutputStream fileOutputStream = new FileOutputStream("./pdf/address_proof.pdf");
            pdfBytes.writeTo(fileOutputStream);
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Create and send a response message
        AddressProofResponse response = AddressProofResponse.newBuilder().setSuccess(true)
                .setMessage("Address proof document uploaded successfully for " + accountHolderName
                        + " having account number " + accountNumber)
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}