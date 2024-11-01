package com.jonathan.bidirectional_streaming_grpc.client;
import com.jonathan.bidirectional_streaming_grpc.proto.ChatMessage;
import com.jonathan.bidirectional_streaming_grpc.proto.ChatResponse;
import com.jonathan.bidirectional_streaming_grpc.proto.ChatServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

public class ChatClient {

    public static void main(String[] args) throws InterruptedException {
        // Build the channel to communicate with the server
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9090)
                .usePlaintext()
                .build();

        // Create an async stub for bidirectional streaming
        ChatServiceGrpc.ChatServiceStub chatServiceStub = ChatServiceGrpc.newStub(channel);

        // Implement the response observer to receive messages from the server
        StreamObserver<ChatMessage> requestObserver = chatServiceStub.startChat(new StreamObserver<ChatResponse>() {
            @Override
            public void onNext(ChatResponse response) {
                // Handle each response from the server
                System.out.println("Server: " + response.getSender() + ": " + response.getMessage());
            }

            @Override
            public void onError(Throwable t) {
                // Handle any errors
                t.printStackTrace();
            }

            @Override
            public void onCompleted() {
                // Server has completed the chat session
                System.out.println("Chat session ended by the server.");
            }
        });

        // Predefined list of messages to send to the server
        String[] messages = {
                "Hi",
                "I need to know my account balance",
                "5050", // OTP
                "Thanks!"
        };

        // Loop through and send each message to the server
        for (String message : messages) {
            ChatMessage chatMessage = ChatMessage.newBuilder()
                    .setSender("User")
                    .setMessage(message)
                    .build();

            // Send the message to the server
            requestObserver.onNext(chatMessage);

            // Sleep for a short duration to simulate delay between messages
            Thread.sleep(1000);  // 1-second delay between messages
        }

        // After sending all messages, mark the request as completed
        requestObserver.onCompleted();

        // Shutdown the channel after completion
        channel.shutdown();
    }
}
