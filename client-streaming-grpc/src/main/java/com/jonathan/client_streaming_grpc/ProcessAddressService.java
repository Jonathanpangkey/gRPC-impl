package com.jonathan.client_streaming_grpc;
import com.jonathan.client_streaming_grpc.proto.AddressProofRequest;
import com.jonathan.client_streaming_grpc.proto.AddressProofResponse;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import com.jonathan.client_streaming_grpc.proto.BankServiceGrpc.BankServiceImplBase;
@GrpcService
public class ProcessAddressService extends BankServiceImplBase {

    @Override
    public StreamObserver<AddressProofRequest> uploadAddressProof(
            StreamObserver<AddressProofResponse> responseObserver) {

        return new UploadAddressProofObserver(responseObserver);
    }

}