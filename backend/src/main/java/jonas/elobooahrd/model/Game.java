package jonas.elobooahrd.model;

import java.util.List;
import java.util.UUID;

import jonas.elobooahrd.model.interfaces.GameInterface;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Represents a generic game in the Eloboard system.
 * This is an abstract class that should be extended by specific game implementations.
 */
@Data
@NoArgsConstructor
@ToString
public abstract class Game implements GameInterface {
    private UUID id = UUID.randomUUID();
    private String name;
    private String description;
    private int initialEloRating = 1000;
    private int kFactor = 32; // K-factor for Elo calculation

    /**
     * Creates a new game with the given name and description
     */
    public Game(String name, String description) {
        this.name = name;
        this.description = description;
    }

    /**
     * Creates a new game with the given name, description, initial Elo rating, and K-factor
     */
    public Game(String name, String description, int initialEloRating, int kFactor) {
        this.name = name;
        this.description = description;
        this.initialEloRating = initialEloRating;
        this.kFactor = kFactor;
    }

    /**
     * Calculates the Elo rating change for a match result
     * @param playerRating The current rating of the player
     * @param opponentRating The current rating of the opponent
     * @param score The score (1 for win, 0.5 for draw, 0 for loss)
     * @return The change in Elo rating
     */
    @Override
    public int calculateEloChange(int playerRating, int opponentRating, double score) {
        double expectedScore = 1.0 / (1.0 + Math.pow(10, (opponentRating - playerRating) / 400.0));
        return (int) Math.round(kFactor * (score - expectedScore));
    }

    /**
     * Records a match result between two players and updates their Elo ratings
     * This method should be implemented by specific game types
     */
    @Override
    public abstract void recordMatch(Player winner, Player loser);
    
    /**
     * Records a match result between multiple winners and losers
     * Default implementation processes each winner against each loser
     * @param winners List of winning players
     * @param losers List of losing players
     */
    @Override
    public void recordMatch(List<Player> winners, List<Player> losers) {
        // Calculate average ratings for winners and losers
        int avgWinnerRating = calculateAverageRating(winners);
        int avgLoserRating = calculateAverageRating(losers);
        
        // Update each winner's rating
        for (Player winner : winners) {
            int winnerRating = winner.getEloRating(getId());
            int eloChange = calculateEloChange(winnerRating, avgLoserRating, 1.0);
            winner.updateEloRating(getId(), eloChange);
        }
        
        // Update each loser's rating
        for (Player loser : losers) {
            int loserRating = loser.getEloRating(getId());
            int eloChange = calculateEloChange(loserRating, avgWinnerRating, 0.0);
            loser.updateEloRating(getId(), eloChange);
        }
    }
    
    /**
     * Records a match result between two teams
     * Default implementation uses the team's players as winners and losers
     * @param winnerTeam The winning team
     * @param loserTeam The losing team
     */
    @Override
    public void recordTeamMatch(Team winnerTeam, Team loserTeam) {
        recordMatch(winnerTeam.getPlayers(), loserTeam.getPlayers());
    }
    
    /**
     * Calculates the average Elo rating for a list of players
     * @param players The list of players
     * @return The average Elo rating
     */
    protected int calculateAverageRating(List<Player> players) {
        if (players == null || players.isEmpty()) {
            return initialEloRating;
        }
        
        int totalRating = 0;
        for (Player player : players) {
            totalRating += player.getEloRating(getId());
        }
        
        return totalRating / players.size();
    }
}
