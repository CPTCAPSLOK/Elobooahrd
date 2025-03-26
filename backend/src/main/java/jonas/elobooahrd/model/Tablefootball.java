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
 * Represents a Table Football (Foosball) game in the Eloboard system.
 * Extends the base Game class with table football specific functionality.
 */
@Data
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Tablefootball extends Game {
    private int maxGoals = 10; // Maximum number of goals to win a match
    private boolean allowTeams = false; // Whether team play is allowed
    private List<Match> matchHistory = new ArrayList<>(); // History of matches played
    private Map<UUID, Team> teams = new HashMap<>(); // Teams for this game

    /**
     * Represents a match in table football
     */
    @Data
    @NoArgsConstructor
    @ToString
    public static class Match {
        private UUID id = UUID.randomUUID();
        private UUID team1Id; // Can be a single player ID or a team ID
        private UUID team2Id; // Can be a single player ID or a team ID
        private int team1Score;
        private int team2Score;
        private LocalDateTime timestamp = LocalDateTime.now();
        private UUID winnerId;
        private boolean isTeamMatch = false;

        /**
         * Creates a new match with the given players and scores
         */
        public Match(UUID player1Id, UUID player2Id, int player1Score, int player2Score) {
            this.team1Id = player1Id;
            this.team2Id = player2Id;
            this.team1Score = player1Score;
            this.team2Score = player2Score;
            this.winnerId = player1Score > player2Score ? player1Id : player2Id;
            this.isTeamMatch = false;
        }
        
        /**
         * Creates a new team match with the given teams and scores
         */
        public Match(UUID team1Id, UUID team2Id, int team1Score, int team2Score, boolean isTeamMatch) {
            this.team1Id = team1Id;
            this.team2Id = team2Id;
            this.team1Score = team1Score;
            this.team2Score = team2Score;
            this.winnerId = team1Score > team2Score ? team1Id : team2Id;
            this.isTeamMatch = isTeamMatch;
        }

        /**
         * Updates the winner based on current scores
         */
        public void updateWinner() {
            this.winnerId = team1Score > team2Score ? team1Id : team2Id;
        }

        /**
         * Sets team 1's score and updates the winner
         */
        public void setTeam1Score(int team1Score) {
            this.team1Score = team1Score;
            updateWinner();
        }

        /**
         * Sets team 2's score and updates the winner
         */
        public void setTeam2Score(int team2Score) {
            this.team2Score = team2Score;
            updateWinner();
        }
    }

    /**
     * Creates a new table football game with default settings
     */
    public Tablefootball(String name, String description) {
        super(name, description);
    }

    /**
     * Creates a new table football game with custom settings
     */
    public Tablefootball(String name, String description, int initialEloRating, int kFactor, 
                         int maxGoals, boolean allowTeams) {
        super(name, description, initialEloRating, kFactor);
        this.maxGoals = maxGoals;
        this.allowTeams = allowTeams;
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
     * Records a match with specific scores between individual players
     */
    public void recordMatch(Player player1, Player player2, int player1Score, int player2Score) {
        // Create and add match to history
        Match match = new Match(player1.getId(), player2.getId(), player1Score, player2Score);
        matchHistory.add(match);
        
        // Determine winner and loser
        Player winner = player1Score > player2Score ? player1 : player2;
        Player loser = player1Score > player2Score ? player2 : player1;
        
        // Update Elo ratings
        recordMatch(winner, loser);
    }
    
    /**
     * Records a match result between two teams and updates their players' Elo ratings
     * @param winnerTeam The winning team
     * @param loserTeam The losing team
     */
    public void recordTeamMatch(Team winnerTeam, Team loserTeam) {
        // Calculate average team ratings
        int winnerTeamRating = winnerTeam.getTeamEloRating(getId());
        int loserTeamRating = loserTeam.getTeamEloRating(getId());
        
        // Calculate Elo changes for each player in the winning team
        for (Player player : winnerTeam.getPlayers()) {
            int playerRating = player.getEloRating(getId());
            int eloChange = calculateEloChange(playerRating, loserTeamRating, 1.0);
            player.updateEloRating(getId(), eloChange);
        }
        
        // Calculate Elo changes for each player in the losing team
        for (Player player : loserTeam.getPlayers()) {
            int playerRating = player.getEloRating(getId());
            int eloChange = calculateEloChange(playerRating, winnerTeamRating, 0.0);
            player.updateEloRating(getId(), eloChange);
        }
    }
    
    /**
     * Records a team match with specific scores
     */
    public void recordTeamMatch(Team team1, Team team2, int team1Score, int team2Score) {
        // Ensure teams are registered with this game
        registerTeam(team1);
        registerTeam(team2);
        
        // Create and add match to history
        Match match = new Match(team1.getId(), team2.getId(), team1Score, team2Score, true);
        matchHistory.add(match);
        
        // Determine winner and loser teams
        Team winnerTeam = team1Score > team2Score ? team1 : team2;
        Team loserTeam = team1Score > team2Score ? team2 : team1;
        
        // Update Elo ratings
        recordTeamMatch(winnerTeam, loserTeam);
    }
    
    /**
     * Creates a new team for this game
     * @param name The name of the team
     * @param players The players in the team
     * @return The created team
     */
    public Team createTeam(String name, List<Player> players) {
        Team team = new Team(name, getId(), players);
        teams.put(team.getId(), team);
        return team;
    }
    
    /**
     * Registers an existing team with this game
     * @param team The team to register
     * @return The registered team
     */
    public Team registerTeam(Team team) {
        if (!teams.containsKey(team.getId())) {
            teams.put(team.getId(), team);
        }
        return team;
    }
    
    /**
     * Gets a team by its ID
     * @param teamId The ID of the team
     * @return The team, or null if not found
     */
    public Team getTeam(UUID teamId) {
        return teams.get(teamId);
    }
    
    /**
     * Gets all teams for this game
     * @return A list of all teams
     */
    public List<Team> getAllTeams() {
        return new ArrayList<>(teams.values());
    }
}
