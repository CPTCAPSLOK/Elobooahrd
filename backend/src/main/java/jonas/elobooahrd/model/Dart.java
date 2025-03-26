package jonas.elobooahrd.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Represents a Dart game in the Eloboard system.
 * Extends the base Game class with dart specific functionality.
 */
@Data
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Dart extends Game {
    private int startingScore = 501; // Default starting score for a dart game (501)
    private boolean doubleOut = true; // Whether players need to finish on a double
    private List<Match> matchHistory = new ArrayList<>(); // History of matches played
    private Map<UUID, List<Integer>> playerScoreHistory = new HashMap<>(); // Track score history for each player
    
    /**
     * Represents a dart game variant
     */
    public enum DartVariant {
        X01,        // 301, 501, 701, etc.
        CRICKET,    // Cricket scoring
        AROUND_THE_CLOCK,
        SHANGHAI,
        KILLER
    }
    
    private DartVariant variant = DartVariant.X01;

    /**
     * Represents a match in dart
     */
    @Data
    @NoArgsConstructor
    @ToString
    public static class Match {
        private UUID id = UUID.randomUUID();
        private UUID player1Id;
        private UUID player2Id;
        private List<Integer> player1Scores = new ArrayList<>(); // List of scores for each round
        private List<Integer> player2Scores = new ArrayList<>();
        private int player1RemainingScore;
        private int player2RemainingScore;
        private int player1Darts = 0; // Total number of darts thrown
        private int player2Darts = 0;
        private LocalDateTime timestamp = LocalDateTime.now();
        private UUID winnerId;
        private boolean completed = false;
        private int startingScore;
        private DartVariant variant;
        
        /**
         * Creates a new dart match with the given players
         */
        public Match(UUID player1Id, UUID player2Id, int startingScore, DartVariant variant) {
            this.player1Id = player1Id;
            this.player2Id = player2Id;
            this.player1RemainingScore = startingScore;
            this.player2RemainingScore = startingScore;
            this.startingScore = startingScore;
            this.variant = variant;
        }
        
        /**
         * Records a score for player 1
         * @param score The score achieved in this round
         * @param dartsThrown The number of darts thrown in this round
         * @return true if the player has won, false otherwise
         */
        public boolean recordPlayer1Score(int score, int dartsThrown) {
            player1Scores.add(score);
            player1Darts += dartsThrown;
            player1RemainingScore -= score;
            
            if (player1RemainingScore == 0) {
                winnerId = player1Id;
                completed = true;
                return true;
            }
            
            return false;
        }
        
        /**
         * Records a score for player 2
         * @param score The score achieved in this round
         * @param dartsThrown The number of darts thrown in this round
         * @return true if the player has won, false otherwise
         */
        public boolean recordPlayer2Score(int score, int dartsThrown) {
            player2Scores.add(score);
            player2Darts += dartsThrown;
            player2RemainingScore -= score;
            
            if (player2RemainingScore == 0) {
                winnerId = player2Id;
                completed = true;
                return true;
            }
            
            return false;
        }
        
        /**
         * Gets the average score per dart for player 1
         * @return The average score per dart
         */
        public double getPlayer1Average() {
            if (player1Darts == 0) return 0;
            int totalScore = startingScore - player1RemainingScore;
            return (double) totalScore / player1Darts * 3; // Multiply by 3 to get the standard 3-dart average
        }
        
        /**
         * Gets the average score per dart for player 2
         * @return The average score per dart
         */
        public double getPlayer2Average() {
            if (player2Darts == 0) return 0;
            int totalScore = startingScore - player2RemainingScore;
            return (double) totalScore / player2Darts * 3; // Multiply by 3 to get the standard 3-dart average
        }
    }

    /**
     * Creates a new dart game with default settings (501, double out)
     */
    public Dart(String name, String description) {
        super(name, description);
    }

    /**
     * Creates a new dart game with custom settings
     */
    public Dart(String name, String description, int initialEloRating, int kFactor, 
                int startingScore, boolean doubleOut, DartVariant variant) {
        super(name, description, initialEloRating, kFactor);
        this.startingScore = startingScore;
        this.doubleOut = doubleOut;
        this.variant = variant;
    }

    /**
     * Records a match result between two players and updates their Elo ratings
     */
    @Override
    public void recordMatch(Player winner, Player loser) {
        // Calculate Elo changes
        int winnerRating = winner.getEloRating(getId());
        int loserRating = loser.getEloRating(getId());
        
        int winnerEloChange = calculateEloChange(winnerRating, loserRating, 1.0);
        int loserEloChange = calculateEloChange(loserRating, winnerRating, 0.0);
        
        // Update player ratings
        winner.updateEloRating(getId(), winnerEloChange);
        loser.updateEloRating(getId(), loserEloChange);
    }
    
    /**
     * Records a match result between multiple winners and losers
     * This implementation uses the default behavior from Game class
     */
    @Override
    public void recordMatch(List<Player> winners, List<Player> losers) {
        super.recordMatch(winners, losers);
    }
    
    /**
     * Records a match result between two teams
     * Dart does not support team play, so this method throws an exception
     */
    @Override
    public void recordTeamMatch(Team winnerTeam, Team loserTeam) {
        throw new UnsupportedOperationException("Dart does not support team play");
    }
    
    /**
     * Creates a new match between two players
     * @param player1 The first player
     * @param player2 The second player
     * @return The created match
     */
    public Match createMatch(Player player1, Player player2) {
        Match match = new Match(player1.getId(), player2.getId(), startingScore, variant);
        matchHistory.add(match);
        
        // Initialize score history for players if not already done
        playerScoreHistory.putIfAbsent(player1.getId(), new ArrayList<>());
        playerScoreHistory.putIfAbsent(player2.getId(), new ArrayList<>());
        
        return match;
    }
    
    /**
     * Records a score for a player in an ongoing match
     * @param match The match
     * @param player The player
     * @param opponent The opponent
     * @param score The score achieved in this round
     * @param dartsThrown The number of darts thrown in this round
     * @return true if the player has won, false otherwise
     */
    public boolean recordScore(Match match, Player player, Player opponent, int score, int dartsThrown) {
        if (match.isCompleted()) {
            throw new IllegalStateException("Match is already completed");
        }
        
        UUID playerId = player.getId();
        
        // Record the score in the player's history
        List<Integer> scoreHistory = playerScoreHistory.get(playerId);
        if (scoreHistory != null) {
            scoreHistory.add(score);
        }
        
        boolean isWinner = false;
        
        // Record the score in the match
        if (playerId.equals(match.getPlayer1Id())) {
            isWinner = match.recordPlayer1Score(score, dartsThrown);
        } else if (playerId.equals(match.getPlayer2Id())) {
            isWinner = match.recordPlayer2Score(score, dartsThrown);
        } else {
            throw new IllegalArgumentException("Player is not part of this match");
        }
        
        // If the match is completed, update Elo ratings
        if (isWinner) {
            recordMatch(player, opponent);
        }
        
        return isWinner;
    }
    
    /**
     * Gets the match history for a specific player
     * @param playerId The ID of the player
     * @return A list of matches involving the player
     */
    public List<Match> getPlayerMatches(UUID playerId) {
        return matchHistory.stream()
                .filter(match -> match.getPlayer1Id().equals(playerId) || match.getPlayer2Id().equals(playerId))
                .toList();
    }
    
    /**
     * Gets the score history for a specific player
     * @param playerId The ID of the player
     * @return A list of scores for the player, or an empty list if not found
     */
    public List<Integer> getPlayerScoreHistory(UUID playerId) {
        return playerScoreHistory.getOrDefault(playerId, new ArrayList<>());
    }
    
    /**
     * Gets the average score per dart for a player across all matches
     * @param playerId The ID of the player
     * @return The average score per dart
     */
    public double getPlayerAverageScore(UUID playerId) {
        List<Match> playerMatches = getPlayerMatches(playerId);
        if (playerMatches.isEmpty()) {
            return 0;
        }
        
        double totalAverage = 0;
        int matchCount = 0;
        
        for (Match match : playerMatches) {
            if (match.getPlayer1Id().equals(playerId)) {
                totalAverage += match.getPlayer1Average();
                matchCount++;
            } else if (match.getPlayer2Id().equals(playerId)) {
                totalAverage += match.getPlayer2Average();
                matchCount++;
            }
        }
        
        return matchCount > 0 ? totalAverage / matchCount : 0;
    }
}
