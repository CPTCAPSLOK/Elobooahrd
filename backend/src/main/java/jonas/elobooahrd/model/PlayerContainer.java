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
 * Container class for managing players in the Eloboard system.
 * This class serves as a registry for all players and provides methods to access and manage them.
 */
@Data
@NoArgsConstructor
@ToString
public class PlayerContainer {
    private UUID id = UUID.randomUUID();
    private String name = "Default Player Container";
    private Map<UUID, Player> players = new HashMap<>();

    /**
     * Creates a new player container with the given name
     */
    public PlayerContainer(String name) {
        this.name = name;
    }

    /**
     * Adds a player to the container
     * @param player The player to add
     * @return The added player
     */
    public Player addPlayer(Player player) {
        players.put(player.getId(), player);
        return player;
    }

    /**
     * Removes a player from the container
     * @param playerId The ID of the player to remove
     * @return The removed player, or null if not found
     */
    public Player removePlayer(UUID playerId) {
        return players.remove(playerId);
    }

    /**
     * Gets a player by their ID
     * @param playerId The ID of the player to get
     * @return The player, or null if not found
     */
    public Player getPlayer(UUID playerId) {
        return players.get(playerId);
    }

    /**
     * Gets a player by their name
     * @param name The name of the player to get
     * @return The player, or null if not found
     */
    public Player getPlayerByName(String name) {
        return players.values().stream()
                .filter(player -> player.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    /**
     * Gets a player by their email
     * @param email The email of the player to get
     * @return The player, or null if not found
     */
    public Player getPlayerByEmail(String email) {
        return players.values().stream()
                .filter(player -> player.getEmail().equalsIgnoreCase(email))
                .findFirst()
                .orElse(null);
    }

    /**
     * Gets all players in the container
     * @return A list of all players
     */
    public List<Player> getAllPlayers() {
        return new ArrayList<>(players.values());
    }

    /**
     * Gets the leaderboard for a specific game
     * @param gameId The ID of the game
     * @return A list of players sorted by their Elo rating for the game (highest first)
     */
    public List<Player> getLeaderboard(UUID gameId) {
        return players.values().stream()
                .sorted((p1, p2) -> Integer.compare(p2.getEloRating(gameId), p1.getEloRating(gameId)))
                .collect(Collectors.toList());
    }

    /**
     * Gets the top N players for a specific game
     * @param gameId The ID of the game
     * @param limit The maximum number of players to return
     * @return A list of the top N players sorted by their Elo rating for the game (highest first)
     */
    public List<Player> getTopPlayers(UUID gameId, int limit) {
        return players.values().stream()
                .sorted((p1, p2) -> Integer.compare(p2.getEloRating(gameId), p1.getEloRating(gameId)))
                .limit(limit)
                .collect(Collectors.toList());
    }

    /**
     * Gets players with Elo ratings above a certain threshold for a specific game
     * @param gameId The ID of the game
     * @param minRating The minimum Elo rating
     * @return A list of players with Elo ratings above the threshold
     */
    public List<Player> getPlayersAboveRating(UUID gameId, int minRating) {
        return players.values().stream()
                .filter(player -> player.getEloRating(gameId) >= minRating)
                .collect(Collectors.toList());
    }

    /**
     * Creates a new player with the given name and email and adds it to the container
     * @param name The name of the player
     * @param email The email of the player
     * @return The created player
     */
    public Player createPlayer(String name, String email) {
        Player player = new Player(name, email);
        return addPlayer(player);
    }
}
