package com.NexTradeX.market;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class MarketService {
    
    private final CryptoPriceRepository cryptoPriceRepository;
    private final RestTemplate restTemplate;
    
    private static final String COINGECKO_API = "https://api.coingecko.com/api/v3";
    
    public CryptoPrice getPrice(String symbol) {
        return cryptoPriceRepository.findBySymbol(symbol)
                .orElseThrow(() -> new RuntimeException("Price not found for symbol: " + symbol));
    }
    
    public Optional<CryptoPrice> getPriceOptional(String symbol) {
        return cryptoPriceRepository.findBySymbol(symbol);
    }
    
    public List<CryptoPrice> getAllPrices() {
        return cryptoPriceRepository.findAll();
    }
    
    @Transactional
    public CryptoPrice updateOrCreatePrice(String symbol, BigDecimal currentPrice,
                                          BigDecimal highPrice, BigDecimal lowPrice,
                                          BigDecimal openPrice, BigDecimal priceChange24h,
                                          BigDecimal percentChange24h, BigDecimal volume24h,
                                          BigDecimal marketCap) {
        Optional<CryptoPrice> existing = cryptoPriceRepository.findBySymbol(symbol);
        
        CryptoPrice price = existing.orElse(new CryptoPrice());
        price.setSymbol(symbol);
        price.setCurrentPrice(currentPrice);
        price.setHighPrice(highPrice);
        price.setLowPrice(lowPrice);
        price.setOpenPrice(openPrice);
        price.setPriceChange24h(priceChange24h);
        price.setPercentChange24h(percentChange24h);
        price.setVolume24h(volume24h);
        price.setMarketCap(marketCap);
        price.setUpdatedAt(LocalDateTime.now());
        
        CryptoPrice saved = cryptoPriceRepository.save(price);
        log.info("Updated price for {}: {}", symbol, currentPrice);
        return saved;
    }
    
    public CryptoPrice updatePrice(String symbol, BigDecimal currentPrice) {
        CryptoPrice price = cryptoPriceRepository.findBySymbol(symbol)
                .orElseThrow(() -> new RuntimeException("Price not found for symbol: " + symbol));
        
        BigDecimal priceChange = currentPrice.subtract(price.getOpenPrice());
        BigDecimal percentChange = priceChange.divide(price.getOpenPrice(), 4, java.math.RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100));
        
        price.setCurrentPrice(currentPrice);
        price.setPriceChange24h(priceChange);
        price.setPercentChange24h(percentChange);
        price.setUpdatedAt(LocalDateTime.now());
        
        CryptoPrice saved = cryptoPriceRepository.save(price);
        log.debug("Updated price for {}: {}", symbol, currentPrice);
        return saved;
    }
    
    // Sample data initialization
    public void initializeDefaultPrices() {
        if (!cryptoPriceRepository.existsBySymbol("BTCUSDT")) {
            updateOrCreatePrice("BTCUSDT", 
                    BigDecimal.valueOf(43250.50),
                    BigDecimal.valueOf(44000.00),
                    BigDecimal.valueOf(42500.00),
                    BigDecimal.valueOf(43100.00),
                    BigDecimal.valueOf(1250.50),
                    BigDecimal.valueOf(2.97),
                    BigDecimal.valueOf(28_000_000_000L),
                    BigDecimal.valueOf(850_000_000_000L));
        }
        
        if (!cryptoPriceRepository.existsBySymbol("ETHUSDT")) {
            updateOrCreatePrice("ETHUSDT",
                    BigDecimal.valueOf(2280.75),
                    BigDecimal.valueOf(2350.00),
                    BigDecimal.valueOf(2200.00),
                    BigDecimal.valueOf(2250.00),
                    BigDecimal.valueOf(30.75),
                    BigDecimal.valueOf(1.38),
                    BigDecimal.valueOf(15_000_000_000L),
                    BigDecimal.valueOf(273_000_000_000L));
        }
        
        if (!cryptoPriceRepository.existsBySymbol("BNBUSDT")) {
            updateOrCreatePrice("BNBUSDT",
                    BigDecimal.valueOf(618.50),
                    BigDecimal.valueOf(630.00),
                    BigDecimal.valueOf(610.00),
                    BigDecimal.valueOf(615.00),
                    BigDecimal.valueOf(3.50),
                    BigDecimal.valueOf(0.57),
                    BigDecimal.valueOf(3_000_000_000L),
                    BigDecimal.valueOf(94_000_000_000L));
        }
    }
}