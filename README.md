## gRPC Implemetation (Unary, Client Streaming, Server Streaming, Bidirectional Streaming)
 
### Unary gRPC Demonstration

#### Project Overview
This project demonstrates a simple **unary gRPC call**. The unary call is a one-to-one communication between the client and server, where the client sends a single request, and the server responds with a single response.

#### Proto File
The `proto3` syntax defines a service for retrieving a client's account balance.

- **`AccountRequest`**: Represents the request message from the client, containing the account number.
- **`AccountBalanceResponse`**: The server's response containing the account balance for the provided account number.
- **`AccountBalanceService`**: Defines the `GetAccountBalance` RPC method, which takes an `AccountRequest` and returns an `AccountBalanceResponse`.

#### Server-Side Implementation
The `BankAccountBalanceService` class implements the gRPC service using the `AccountBalanceServiceImplBase` class generated from the proto file. In the `getAccountBalance` method:
- It takes the `AccountRequest`, processes the request, and sends back an `AccountBalanceResponse` with a balance value of `100`.
- The response is streamed back to the client using `responseObserver`.

#### Client-Side Implementation
The client is directly created within the same project and connects to the gRPC server using the `ManagedChannel`.
- It makes a call to the `getAccountBalance` method of the gRPC service, providing the account number.
- The response contains the account number and balance, which is printed on the client-side.

---

### Client Streaming gRPC Demonstration

#### Project Overview
This project demonstrates **client-side streaming**, where the client sends multiple chunks of data to the server, and the server processes the streamed data after the client completes sending the data.

#### Proto File
The `proto3` syntax defines the client streaming gRPC service for uploading an address proof document in chunks.

- **`AddressProofRequest`**: Represents each chunk of the address proof being streamed to the server.
- **`AddressProofResponse`**: The server's response indicating the success of the file upload.
- **`BankService`**: Defines the `UploadAddressProof` RPC method that accepts a stream of `AddressProofRequest` and returns a single `AddressProofResponse` when the stream is completed.

#### Server-Side Implementation
The `ProcessAddressService` class implements the `uploadAddressProof` method, processing each chunk of the PDF document sent by the client.

- The `UploadAddressProofObserver` collects the streamed chunks, reassembles the file, and writes it to the server's local file system.
- Once the file is fully uploaded, the server responds with a success message.

#### Client-Side Implementation
The client is directly created within the same project, the client reads a PDF file in chunks and sends each chunk to the server using a streaming request.
- After sending the file, the client closes the stream, and the server responds with a message indicating whether the file upload was successful.

---

### Server Streaming gRPC Demonstration

#### Project Overview
This project demonstrates **server-side streaming**, where the client sends a single request, and the server responds with multiple messages, streaming data back to the client in chunks.

#### Proto File
The `proto3` syntax defines a service for retrieving a list of transactions over a specific time period.

- **`AccountRequest`**: Represents the request message from the client, containing the account number and duration in days.
- **`TransactionDetail`**: Represents the details of a single transaction.
- **`TransactionDetailList`**: A repeated field that holds multiple transaction details.
- **`TransactionService`**: Defines the `streamTransactions` RPC method that streams `TransactionDetailList` back to the client.

#### Server-Side Implementation
The `TransactionHistoryService` class handles the server-side logic for the `streamTransactions` method.
- It simulates a list of transactions based on the requested duration.
- The server sends the transactions back to the client in batches, simulating a delay between batches.

#### Client-Side Implementation
The client is directly created within the same project, this client sends a single request and receives a stream of transaction details in response.
- The client prints out each batch of transactions as they are streamed from the server.

---

### Bidirectional Streaming gRPC Demonstration

#### Project Overview
This project demonstrates **bidirectional streaming**, where both the client and server can send multiple messages in a stream concurrently.

#### Proto File
The `proto3` syntax defines a chat service for bidirectional communication between a client and server.

- **`ChatMessage`**: Represents the messages exchanged between the client and server.
- **`ChatResponse`**: The server's response to the client's message.
- **`ChatService`**: Defines the `StartChat` RPC method, which supports bidirectional streaming.

#### Server-Side Implementation
The `ChatServiceServer` class implements the bidirectional streaming service.
- The server receives messages from the client, processes them, and responds based on predefined rules (e.g., responding with account balance if OTP is provided).

#### Client-Side Implementation
The client is directly created within the same project, it sends a series of chat messages to the server, and the server responds to each message.
- The interaction is continuous until the client or server ends the communication.
- The client observes server responses and processes them in real-time during the chat session.

---

