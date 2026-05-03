# Stock Market Service

A simplified stock market simulation built with Spring Boot, Redis, and Docker.
Supports wallets, a central bank, audit logging, and basic buy/sell stock operations.

## Architecture

* **Spring Boot app** – REST API
* **Redis** – storage for bank, wallets, and audit log
* **Nginx** – reverse proxy / load balancing
* **Docker Compose** – orchestration
* **Stateless app instances** – supports horizontal scaling

---

## Features

* Buy/sell stocks at fixed price (1)
* Wallets created automatically on first use
* Central bank manages stock availability
* Audit log for successful wallet operations only

---

## Run the project

### Start

```bash
PORT=8080 docker compose up --build --scale app=2
```

You can change `PORT` to expose the service on a different local port.

This will:

* Build the application
* Start Redis
* Start multiple app instances
* Expose the API via Nginx on `http://localhost:PORT`

---

## API Overview

### Wallets

#### Buy/Sell stock

```
POST /wallets/{wallet_id}/stocks/{stock_name}
```

```json
{ "type": "buy" | "sell" }
```

* `404` – stock does not exist in bank
* `400` – insufficient stock in bank or wallet
* `200` – success (wallet auto-created if needed)

---

#### Get wallet state

```
GET /wallets/{wallet_id}
```

Response:

```json
{
  "id": "wallet1",
  "stocks": {
    "AAPL": 10,
    "TSLA": 3
  }
}
```

---

#### Get single stock in wallet

```
GET /wallets/{wallet_id}/stocks/{stock_name}
```

Returns:

```
5
```

---

### Bank

#### Get all stocks

```
GET /stocks
```

```json
{
  "stocks": {
    "AAPL": 100,
    "TSLA": 50
  }
}
```

---

#### Set bank stocks

```
POST /stocks
```

```json
{
  "stocks": [
    { "name": "AAPL", "quantity": 100 },
    { "name": "TSLA", "quantity": 50 }
  ]
}
```

---

### Audit Log

```
GET /log
```

Returns only successful wallet operations:

```json
{
  "log": [
    {
      "type": "buy",
      "wallet_id": "w1",
      "stock_name": "AAPL"
    }
  ]
}
```

---

### Chaos Testing

```
POST /chaos
```

