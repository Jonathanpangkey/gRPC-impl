package com.jonathan.server_streaming_grpc;

public class Transaction {
    private String id;
    private String type;
    private float amount;

    public Transaction() {
    }

    public Transaction(String id, String type, float amount) {
        super();
        this.id = id;
        this.type = type;
        this.amount = amount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }
}
