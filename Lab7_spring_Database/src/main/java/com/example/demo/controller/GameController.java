package com.example.demo.controller;

import com.example.demo.model.Game;
import com.example.demo.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/games")
public class GameController {

    private final GameService gameService;

    // Constructor Injection according to GRASP Controller & DI principles
    @Autowired
    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    // GET /games - List all games
    @GetMapping({"", "/"})
    public String listGames(Model model) {
        List<Game> games = gameService.getAllGames();
        model.addAttribute("games", games);
        return "games/list";
    }

    // GET /games/add - Show add game form
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("game", new Game());
        return "games/add";
    }

    // POST /games/save - Save new game
    @PostMapping("/save")
    public String saveGame(@ModelAttribute("game") Game game, RedirectAttributes redirectAttributes) {
        gameService.saveGame(game);
        redirectAttributes.addFlashAttribute("message", "เพิ่มข้อมูลเกมสำเร็จเรียบร้อยแล้ว!");
        return "redirect:/games";
    }

    // GET /games/edit/{id} - Show edit game form
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        Game game = gameService.getGameById(id);
        if (game == null) {
            return "redirect:/games";
        }
        model.addAttribute("game", game);
        return "games/edit";
    }

    // POST /games/update/{id} - Update game
    @PostMapping("/update/{id}")
    public String updateGame(@PathVariable("id") Long id, @ModelAttribute("game") Game game, RedirectAttributes redirectAttributes) {
        game.setId(id);
        gameService.saveGame(game);
        redirectAttributes.addFlashAttribute("message", "อัปเดตข้อมูลเกมสำเร็จเรียบร้อยแล้ว!");
        return "redirect:/games";
    }

    // GET /games/delete/{id} - Show delete confirmation page
    @GetMapping("/delete/{id}")
    public String showDeleteForm(@PathVariable("id") Long id, Model model) {
        Game game = gameService.getGameById(id);
        if (game == null) {
            return "redirect:/games";
        }
        model.addAttribute("game", game);
        return "games/delete";
    }

    // POST /games/delete/{id} - Confirm delete game
    @PostMapping("/delete/{id}")
    public String deleteGame(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        gameService.deleteGame(id);
        redirectAttributes.addFlashAttribute("message", "ลบข้อมูลเกมออกจากระบบเรียบร้อยแล้ว!");
        return "redirect:/games";
    }
}
