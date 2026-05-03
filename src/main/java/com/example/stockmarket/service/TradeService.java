package com.example.stockmarket.service;

import com.example.stockmarket.repository.StockRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;


@Service
public class TradeService {

    private final StockRepository repo;
    private final AuditService audit;

    public TradeService(StockRepository repo, AuditService audit) {
        this.repo = repo;
        this.audit = audit;
    }

    public void trade(String walletId, String stock, String type) {

        if (!repo.bankHasStock(stock)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Stock does not exist");
        }

        if ("buy".equalsIgnoreCase(type)) {

            boolean bankOk = repo.bankDecrement(stock);
            if (!bankOk) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Stock unavailable in bank");
            }

            repo.walletIncrement(walletId, stock);

            audit.log("buy", walletId, stock);
            return;
        }

        if ("sell".equalsIgnoreCase(type)) {

            boolean walletOk = repo.walletDecrement(walletId, stock);
            if (!walletOk) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No stock in wallet");
            }

            repo.bankIncrement(stock);

            audit.log("sell", walletId, stock);
            return;
        }

        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid type");
    }
}