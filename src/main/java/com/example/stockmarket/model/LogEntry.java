package com.example.stockmarket.model;

public class LogEntry {

    private String type;
    private String wallet_id;
    private String stock_name;

    public LogEntry() {} // needed for Jackson

    public LogEntry(String type, String wallet_id, String stock_name) {
        this.type = type;
        this.wallet_id = wallet_id;
        this.stock_name = stock_name;
    }

    public String getType() { return type; }
    public String getWallet_id() { return wallet_id; }
    public String getStock_name() { return stock_name; }

    public void setType(String type) { this.type = type; }
    public void setWallet_id(String wallet_id) { this.wallet_id = wallet_id; }
    public void setStock_name(String stock_name) { this.stock_name = stock_name; }
}