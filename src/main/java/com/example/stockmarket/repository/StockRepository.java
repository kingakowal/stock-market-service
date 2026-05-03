package com.example.stockmarket.repository;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.data.redis.core.RedisCallback;

@Repository
public class StockRepository {

    private final StringRedisTemplate redis;

    public StockRepository(StringRedisTemplate redis) {
        this.redis = redis;
    }

    private static final String BANK_KEY = "bank";
    private static final String WALLET_PREFIX = "wallet:";

    private static final String DECR_IF_GT_ZERO =
            "local v = redis.call('HGET', KEYS[1], ARGV[1]) " +
            "if not v then return -1 end " +
            "if tonumber(v) <= 0 then return -1 end " +
            "return redis.call('HINCRBY', KEYS[1], ARGV[1], -1)";

    private static final String INCR =
            "return redis.call('HINCRBY', KEYS[1], ARGV[1], 1)";

    public boolean bankDecrement(String stock) {

        RedisCallback<Long> callback = connection ->
                connection.scriptingCommands().eval(
                        DECR_IF_GT_ZERO.getBytes(),
                        org.springframework.data.redis.connection.ReturnType.INTEGER,
                        1,
                        BANK_KEY.getBytes(),
                        stock.getBytes()
                );

        Long result = redis.execute(callback);

        return result != null && result >= 0;
    }

    public void bankIncrement(String stock) {
        redis.opsForHash().increment(BANK_KEY, stock, 1);
    }

    public boolean walletDecrement(String walletId, String stock) {
        String key = WALLET_PREFIX + walletId;
        RedisCallback<Long> callback = connection ->
                connection.scriptingCommands().eval(
                        DECR_IF_GT_ZERO.getBytes(),
                        org.springframework.data.redis.connection.ReturnType.INTEGER,
                        1,
                        key.getBytes(),
                        stock.getBytes()
                );
        Long result = redis.execute(callback);
        return result != null && result >= 0;
    }

    public void walletIncrement(String walletId, String stock) {
        redis.opsForHash().increment(WALLET_PREFIX + walletId, stock, 1);
    }

    public boolean bankHasStock(String stock) {
        Object val = redis.opsForHash().get(BANK_KEY, stock);
        return val != null;
    }
}