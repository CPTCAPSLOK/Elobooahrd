package jonas.elobooahrd.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jonas.elobooahrd.dto.GameDTO;
import jonas.elobooahrd.service.GameService;

/**
 * REST controller for managing games in the Eloboard system.
 */
@RestController
@RequestMapping("/api/games")
public class GameController {

    @Autowired
    private GameService gameService;

    /**
     * GET /api/games : Get all games
     * 
     * @return List of games
     */
    @GetMapping
    public ResponseEntity<List<GameDTO>> getAllGames() {
        return ResponseEntity.ok(gameService.getAllGames());
    }

    /**
     * GET /api/games/{id} : Get a game by ID
     * 
     * @param id The game ID
     * @return The game
     */
    @GetMapping("/{id}")
    public ResponseEntity<GameDTO> getGame(@PathVariable UUID id) {
        return gameService.getGame(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * POST /api/games : Create a new game
     * 
     * @param gameDTO The game to create
     * @return The created game
     */
    @PostMapping
    public ResponseEntity<GameDTO> createGame(@RequestBody GameDTO gameDTO) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(gameService.createGame(gameDTO));
        } catch (UnsupportedOperationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .build();
        }
    }

    /**
     * PUT /api/games/{id} : Update a game
     * 
     * @param id The game ID
     * @param gameDTO The updated game data
     * @return The updated game
     */
    @PutMapping("/{id}")
    public ResponseEntity<GameDTO> updateGame(
            @PathVariable UUID id, 
            @RequestBody GameDTO gameDTO) {
        gameDTO.setId(id);
        try {
            return ResponseEntity.ok(gameService.updateGame(gameDTO));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * DELETE /api/games/{id} : Delete a game
     * 
     * @param id The game ID
     * @return No content if successful
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGame(@PathVariable UUID id) {
        gameService.deleteGame(id);
        return ResponseEntity.noContent().build();
    }
}
