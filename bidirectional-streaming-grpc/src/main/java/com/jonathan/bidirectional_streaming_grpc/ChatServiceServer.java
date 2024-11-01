package com.jonathan.bidirectional_streaming_grpc;


import com.jonathan.bidirectional_streaming_grpc.proto.ChatMessage;
import com.jonathan.bidirectional_streaming_grpc.proto.ChatResponse;
import com.jonathan.bidirectional_streaming_grpc.proto.ChatServiceGrpc;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class ChatServiceServer extends ChatServiceGrpc.ChatServiceImplBase {

    @Override
    public StreamObserver<ChatMessage> startChat(StreamObserver<ChatResponse> responseObserver) {
        return new ChatStreamObserver(responseObserver);
    }
}
