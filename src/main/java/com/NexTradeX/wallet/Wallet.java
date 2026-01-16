package com.NexTradeX.wallet;

import com.NexTradeX.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "wallets", indexes = {
        @Index(name = "idx_user_type", columnList = "user_id,wallet_type", unique = true)
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Wallet {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private WalletType walletType;
    
    @Column(nullable = false, precision = 19, scale = 8)
    private BigDecimal balance = BigDecimal.ZERO;
    
    @Column(nullable = false, precision = 19, scale = 8)
    private BigDecimal lockedFunds = BigDecimal.ZERO;
    
    @Column(precision = 19, scale = 8)
    private BigDecimal unrealizedPnL = BigDecimal.ZERO;
    
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
    
    @Column(nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();
    
    public BigDecimal getAvailableBalance() {
        return balance.subtract(lockedFunds);
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
