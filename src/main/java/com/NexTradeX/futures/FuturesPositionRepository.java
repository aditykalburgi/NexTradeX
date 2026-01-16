package com.NexTradeX.futures;

import com.NexTradeX.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FuturesPositionRepository extends JpaRepository<FuturesPosition, Long> {
    List<FuturesPosition> findAllByUser(User user);
    List<FuturesPosition> findAllByUserAndStatus(User user, PositionStatus status);
    List<FuturesPosition> findAllByUserAndSymbol(User user, String symbol);
    Optional<FuturesPosition> findByUserAndSymbolAndPositionMode(User user, String symbol, PositionMode positionMode);
    Optional<FuturesPosition> findByIdAndUser(Long positionId, User user);
}
