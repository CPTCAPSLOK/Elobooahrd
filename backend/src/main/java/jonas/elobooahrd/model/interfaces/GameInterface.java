package jonas.elobooahrd.model.interfaces;

import java.util.List;
import java.util.UUID;

import jonas.elobooahrd.model.Player;
import jonas.elobooahrd.model.Team;

/**
 * Interface for all game types in the Eloboard system.
 * Defines common methods that all games must implement.
 */
public interface GameInterface {
    
    /**
     * Gets the unique ID of the game
     * @return The game's UUID
     */
    UUID getId();
    
    /**
     * Gets the name of the game
     * @return The game's name
     */
    String getName();
    
    /**
     * Gets the description of the game
     * @return The game's description
     */
    String getDescription();
    
    /**
     * Gets the initial Elo rating for new players
     * @return The initial Elo rating
     */
    int getInitialEloRating();
    
    /**
     * Gets the K-factor used for Elo calculations
     * @return The K-factor
     */
    int getKFactor();
    
    /**
     * Calculates the Elo rating change for a match result
     * @param playerRating The current rating of the player
     * @param opponentRating The current rating of the opponent
     * @param score The score (1 for win, 0.5 for draw, 0 for loss)
     * @return The change in Elo rating
     */
    int calculateEloChange(int playerRating, int opponentRating, double score);
    
    /**
     * Records a match result between two players and updates their Elo ratings
     * @param winner The winning player
     * @param loser The losing player
     */
    void recordMatch(Player winner, Player loser);
    
    /**
     * Records a match result between multiple winners and losers
     * @param winners List of winning players
     * @param losers List of losing players
     */
    void recordMatch(List<Player> winners, List<Player> losers);
    
    /**
     * Records a match result between two teams
     * @param winnerTeam The winning team
     * @param loserTeam The losing team
     */
    void recordTeamMatch(Team winnerTeam, Team loserTeam);
}
