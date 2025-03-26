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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jonas.elobooahrd.dto.PlayerDTO;
import jonas.elobooahrd.model.Player;
import jonas.elobooahrd.service.PlayerService;

/**
 * REST controller for managing players in the Eloboard system.
 */
@RestController
@RequestMapping("/api/players")
public class PlayerController {

    @Autowired
    private PlayerService playerService;

    /**
     * GET /api/players : Get all players
     * 
     * @param gameId Optional game ID to filter players by game
     * @return List of players
     */
    @GetMapping
    public ResponseEntity<List<PlayerDTO>> getAllPlayers(
            @RequestParam(required = false) UUID gameId) {
        if (gameId != null) {
            return ResponseEntity.ok(playerService.getPlayersByGame(gameId));
        }
        return ResponseEntity.ok(playerService.getAllPlayers());
    }

    /**
     * GET /api/players/{id} : Get a player by ID
     * 
     * @param id The player ID
     * @return The player
     */
    @GetMapping("/{id}")
    public ResponseEntity<PlayerDTO> getPlayer(@PathVariable UUID id) {
        return playerService.getPlayer(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * POST /api/players : Create a new player
     * 
     * @param playerDTO The player to create
     * @return The created player
     */
    @PostMapping
    public ResponseEntity<PlayerDTO> createPlayer(@RequestBody PlayerDTO playerDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(playerService.createPlayer(playerDTO));
    }

    /**
     * PUT /api/players/{id} : Update a player
     * 
     * @param id The player ID
     * @param playerDTO The updated player data
     * @return The updated player
     */
    @PutMapping("/{id}")
    public ResponseEntity<PlayerDTO> updatePlayer(
            @PathVariable UUID id, 
            @RequestBody PlayerDTO playerDTO) {
        playerDTO.setId(id);
        return ResponseEntity.ok(playerService.updatePlayer(playerDTO));
    }

    /**
     * DELETE /api/players/{id} : Delete a player
     * 
     * @param id The player ID
     * @return No content if successful
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlayer(@PathVariable UUID id) {
        playerService.deletePlayer(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * GET /api/players/leaderboard : Get player leaderboard for a game
     * 
     * @param gameId The game ID
     * @return List of players sorted by Elo rating
     */
    @GetMapping("/leaderboard")
    public ResponseEntity<List<PlayerDTO>> getLeaderboard(
            @RequestParam UUID gameId) {
        return ResponseEntity.ok(playerService.getLeaderboard(gameId));
    }
}
