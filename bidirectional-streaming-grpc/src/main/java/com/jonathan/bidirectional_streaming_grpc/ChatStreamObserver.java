package com.jonathan.bidirectional_streaming_grpc;
import com.jonathan.bidirectional_streaming_grpc.proto.ChatServiceGrpc;
import com.jonathan.bidirectional_streaming_grpc.proto.ChatMessage;
import com.jonathan.bidirectional_streaming_grpc.proto.ChatResponse;
import io.grpc.stub.StreamObserver;

public class ChatStreamObserver implements StreamObserver<ChatMessage> {

    private final StreamObserver<ChatResponse> responseObserver;

    public ChatStreamObserver(StreamObserver<ChatResponse> responseObserver) {
        this.responseObserver = responseObserver;
    }

    @Override
    public void onNext(ChatMessage chatMessage) {
        String sender = chatMessage.getSender();
        String message = chatMessage.getMessage();

        ChatResponse.Builder response = ChatResponse.newBuilder();

        if (message.equals("Hi")) {
            response.setSender("Server").setMessage("Hello how can I help you today.");
        } else if (message.equals("I need to know my account balance")) {
            response.setSender("Server").setMessage("Ok. Please type the OTP received on your registered phone");
        } else if (message.equals("5050")) {
            response.setSender("Server").setMessage("Ok. The account balance is 300");
        } else {
            response.setSender("Server").setMessage("Invalid OTP");
        }

        responseObserver.onNext(response.build());
    }

    @Override
    public void onError(Throwable t) {
        t.printStackTrace();
    }

    @Override
    public void onCompleted() {
        responseObserver.onCompleted();
    }
}