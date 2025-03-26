package jonas.elobooahrd.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Container class for managing multiple games in the Eloboard system.
 * This class serves as a registry for all games and provides methods to access and manage them.
 */
@Data
@NoArgsConstructor
@ToString
public class GameContainer {
    private UUID id = UUID.randomUUID();
    private String name = "Default Game Container";
    private Map<UUID, Game> games = new HashMap<>();
    private List<Player> players = new ArrayList<>();

    /**
     * Creates a new game container with the given name
     */
    public GameContainer(String name) {
        this.name = name;
    }

    /**
     * Adds a game to the container
     * @param game The game to add
     * @return The added game
     */
    public Game addGame(Game game) {
        games.put(game.getId(), game);
        return game;
    }

    /**
     * Removes a game from the container
     * @param gameId The ID of the game to remove
     * @return The removed game, or null if not found
     */
    public Game removeGame(UUID gameId) {
        return games.remove(gameId);
    }

    /**
     * Gets a game by its ID
     * @param gameId The ID of the game to get
     * @return The game, or null if not found
     */
    public Game getGame(UUID gameId) {
        return games.get(gameId);
    }

    /**
     * Gets all games in the container
     * @return A list of all games
     */
    public List<Game> getAllGames() {
        return new ArrayList<>(games.values());
    }

    /**
     * Gets games of a specific type
     * @param gameClass The class of the game type
     * @return A list of games of the specified type
     */
    public <T extends Game> List<T> getGamesByType(Class<T> gameClass) {
        return games.values().stream()
                .filter(game -> gameClass.isInstance(game))
                .map(game -> gameClass.cast(game))
                .collect(Collectors.toList());
    }

    /**
     * Adds a player to the container
     * @param player The player to add
     * @return The added player
     */
    public Player addPlayer(Player player) {
        players.add(player);
        return player;
    }

    /**
     * Removes a player from the container
     * @param playerId The ID of the player to remove
     * @return true if the player was removed, false otherwise
     */
    public boolean removePlayer(UUID playerId) {
        return players.removeIf(player -> player.getId().equals(playerId));
    }

    /**
     * Gets a player by their ID
     * @param playerId The ID of the player to get
     * @return The player, or null if not found
     */
    public Player getPlayer(UUID playerId) {
        return players.stream()
                .filter(player -> player.getId().equals(playerId))
                .findFirst()
                .orElse(null);
    }

    /**
     * Gets all players in the container
     * @return A list of all players
     */
    public List<Player> getAllPlayers() {
        return new ArrayList<>(players);
    }

    /**
     * Gets the leaderboard for a specific game
     * @param gameId The ID of the game
     * @return A list of players sorted by their Elo rating for the game (highest first)
     */
    public List<Player> getLeaderboard(UUID gameId) {
        return players.stream()
                .sorted((p1, p2) -> Integer.compare(p2.getEloRating(gameId), p1.getEloRating(gameId)))
                .collect(Collectors.toList());
    }

    /**
     * Records a match result for a specific game
     * @param gameId The ID of the game
     * @param player1Id The ID of the first player
     * @param player2Id The ID of the second player
     * @param player1Score The score of the first player
     * @param player2Score The score of the second player
     * @return true if the match was recorded successfully, false otherwise
     */
    public boolean recordMatch(UUID gameId, UUID player1Id, UUID player2Id, int player1Score, int player2Score) {
        Game game = getGame(gameId);
        Player player1 = getPlayer(player1Id);
        Player player2 = getPlayer(player2Id);
        
        if (game == null || player1 == null || player2 == null) {
            return false;
        }
        
        if (game instanceof Tablefootball) {
            ((Tablefootball) game).recordMatch(player1, player2, player1Score, player2Score);
            return true;
        } else {
            // Determine winner and loser based on scores
            Player winner = player1Score > player2Score ? player1 : player2;
            Player loser = player1Score > player2Score ? player2 : player1;
            
            game.recordMatch(winner, loser);
            return true;
        }
    }
}
