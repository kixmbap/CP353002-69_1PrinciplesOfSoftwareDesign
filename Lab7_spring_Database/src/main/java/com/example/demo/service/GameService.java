package com.example.demo.service;

import com.example.demo.model.Game;
import com.example.demo.repository.GameRepository;
import com.example.demo.strategy.DiscountContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GameService {

    private final GameRepository gameRepository;
    private final DiscountContext discountContext;

    // Constructor Injection (DI) according to SOLID & GRASP principles
    @Autowired
    public GameService(GameRepository gameRepository, DiscountContext discountContext) {
        this.gameRepository = gameRepository;
        this.discountContext = discountContext;
    }

    public List<Game> getAllGames() {
        List<Game> games = gameRepository.findAll();
        for (Game game : games) {
            applyDiscountStrategy(game);
        }
        return games;
    }

    public Game getGameById(Long id) {
        Game game = gameRepository.findById(id).orElse(null);
        if (game != null) {
            applyDiscountStrategy(game);
        }
        return game;
    }

    public Game saveGame(Game game) {
        return gameRepository.save(game);
    }

    public void deleteGame(Long id) {
        gameRepository.deleteById(id);
    }

    // Helper method to execute Strategy Pattern calculation
    private void applyDiscountStrategy(Game game) {
        if (game != null && game.getPrice() != null) {
            double finalPrice = discountContext.calculateFinalPrice(game.getDiscountType(), game.getPrice());
            String discountName = discountContext.getDiscountName(game.getDiscountType());
            game.setFinalPrice(finalPrice);
            game.setDiscountName(discountName);
        }
    }
}
