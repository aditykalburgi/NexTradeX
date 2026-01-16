package com.NexTradeX.margin;

import com.NexTradeX.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MarginPositionRepository extends JpaRepository<MarginPosition, Long> {
    List<MarginPosition> findAllByUser(User user);
    List<MarginPosition> findAllByUserAndStatus(User user, String status);
    List<MarginPosition> findAllByUserAndSymbol(User user, String symbol);
    Optional<MarginPosition> findByUserAndSymbol(User user, String symbol);
    Optional<MarginPosition> findByIdAndUser(Long positionId, User user);
}
