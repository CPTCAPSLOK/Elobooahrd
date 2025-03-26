package jonas.elobooahrd.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jonas.elobooahrd.model.interfaces.TeamInterface;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Represents a team of players in the Eloboard system.
 */
@Data
@NoArgsConstructor
@ToString
public class Team implements TeamInterface {
    private UUID id = UUID.randomUUID();
    private String name;
    private List<Player> players = new ArrayList<>();
    private UUID gameId; // The game this team is associated with

    /**
     * Creates a new team with the given name for a specific game
     */
    public Team(String name, UUID gameId) {
        this.name = name;
        this.gameId = gameId;
    }

    /**
     * Creates a new team with the given name and initial players for a specific game
     */
    public Team(String name, UUID gameId, List<Player> players) {
        this.name = name;
        this.gameId = gameId;
        this.players = new ArrayList<>(players);
    }

    @Override
    public boolean addPlayer(Player player) {
        if (!players.contains(player)) {
            return players.add(player);
        }
        return false;
    }

    @Override
    public boolean removePlayer(UUID playerId) {
        return players.removeIf(player -> player.getId().equals(playerId));
    }

    @Override
    public int getTeamEloRating(UUID gameId) {
        // Calculate the average Elo rating of all players for this game
        if (players.isEmpty()) {
            return 1000; // Default rating for empty teams
        }
        
        int totalRating = players.stream()
                .mapToInt(player -> player.getEloRating(gameId))
                .sum();
        
        return totalRating / players.size();
    }
    
    /**
     * Gets the team's Elo rating for its associated game
     * @return The team's Elo rating
     */
    public int getTeamEloRating() {
        return getTeamEloRating(gameId);
    }
}
