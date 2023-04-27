package org.itiszakk.server.balance;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.itiszakk.server.exception.BalanceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Service
public class BalanceServiceImpl implements BalanceService {

    private final static Logger logger = LoggerFactory.getLogger(BalanceServiceImpl.class);

    private final BalanceRepository repository;

    private final LoadingCache<Long, ReadWriteLock> lockCache = CacheBuilder.newBuilder()
            .maximumSize(1000)
            .expireAfterAccess(30, TimeUnit.MINUTES)
            .build(
                    new CacheLoader<>() {
                        public ReadWriteLock load(Long key) {
                            return new ReentrantReadWriteLock();
                        }
                    }
            );

    private final LoadingCache<Long, Optional<Long>> balanceCache = CacheBuilder.newBuilder()
            .maximumSize(1000)
            .expireAfterAccess(30, TimeUnit.MINUTES)
            .build(
                    new CacheLoader<>() {
                        public Optional<Long> load(Long key) {
                            return repository.findById(key).map(Balance::getAmount);
                        }
                    }
            );

    public BalanceServiceImpl(BalanceRepository balanceRepository) {
        this.repository = balanceRepository;
    }

    @Override
    public Optional<Long> getBalance(Long id) {
        lockCache.getUnchecked(id).readLock().lock();
        try {
            return balanceCache.getUnchecked(id);
        } finally {
            lockCache.getUnchecked(id).readLock().unlock();
        }
    }

    @Override
    public void changeBalance(Long id, Long amount) {
        lockCache.getUnchecked(id).writeLock().lock();
        try {
            Long cachedAmount = balanceCache.getUnchecked(id)
                    .orElseThrow(() -> new BalanceNotFoundException(id));
            repository.save(new Balance(id, cachedAmount + amount));
            balanceCache.refresh(id);
        } finally {
            lockCache.getUnchecked(id).writeLock().unlock();
        }
    }
}
