package jonas.elobooahrd.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jonas.elobooahrd.dto.MatchDTO;
import jonas.elobooahrd.model.Game;
import jonas.elobooahrd.model.GameContainer;
import jonas.elobooahrd.model.Player;
import jonas.elobooahrd.model.Tablefootball;
import jonas.elobooahrd.model.Team;

/**
 * Service for managing matches in the Eloboard system.
 */
@Service
public class MatchService {
    
    private GameContainer gameContainer;
    
    @Autowired
    public MatchService(GameService gameService) {
        this.gameContainer = gameService.getGameContainer();
    }
    
    /**
     * Record a match result
     * 
     * @param matchDTO The match data
     * @return True if the match was recorded successfully
     */
    public boolean recordMatch(MatchDTO matchDTO) {
        Game game = gameContainer.getGame(matchDTO.getGameId());
        if (game == null) {
            return false;
        }
        
        if (matchDTO.isTeamMatch()) {
            return recordTeamMatch(matchDTO, game);
        } else {
            return recordPlayerMatch(matchDTO, game);
        }
    }
    
    /**
     * Record a match between individual players
     * 
     * @param matchDTO The match data
     * @param game The game
     * @return True if the match was recorded successfully
     */
    private boolean recordPlayerMatch(MatchDTO matchDTO, Game game) {
        List<Player> winners = new ArrayList<>();
        List<Player> losers = new ArrayList<>();
        
        // Get winner players
        for (UUID playerId : matchDTO.getWinnerIds()) {
            Player player = gameContainer.getPlayer(playerId);
            if (player == null) {
                return false;
            }
            winners.add(player);
        }
        
        // Get loser players
        for (UUID playerId : matchDTO.getLoserIds()) {
            Player player = gameContainer.getPlayer(playerId);
            if (player == null) {
                return false;
            }
            losers.add(player);
        }
        
        // Handle special case for Tablefootball with scores
        if (game instanceof Tablefootball && matchDTO.getWinnerScores() != null 
                && matchDTO.getLoserScores() != null && winners.size() == 1 && losers.size() == 1) {
            ((Tablefootball) game).recordMatch(
                winners.get(0), 
                losers.get(0), 
                matchDTO.getWinnerScores().get(0), 
                matchDTO.getLoserScores().get(0)
            );
            return true;
        }
        
        // For multiple players or other game types
        game.recordMatch(winners, losers);
        return true;
    }
    
    /**
     * Record a match between teams
     * 
     * @param matchDTO The match data
     * @param game The game
     * @return True if the match was recorded successfully
     */
    private boolean recordTeamMatch(MatchDTO matchDTO, Game game) {
        // This is a simplified implementation
        // In a real application, you would need to retrieve teams from a repository
        
        // For now, we'll create temporary teams with the players
        List<Player> winnerPlayers = new ArrayList<>();
        List<Player> loserPlayers = new ArrayList<>();
        
        // Get winner players
        for (UUID playerId : matchDTO.getWinnerIds()) {
            Player player = gameContainer.getPlayer(playerId);
            if (player == null) {
                return false;
            }
            winnerPlayers.add(player);
        }
        
        // Get loser players
        for (UUID playerId : matchDTO.getLoserIds()) {
            Player player = gameContainer.getPlayer(playerId);
            if (player == null) {
                return false;
            }
            loserPlayers.add(player);
        }
        
        Team winnerTeam = new Team("Winner Team", matchDTO.getGameId());
        Team loserTeam = new Team("Loser Team", matchDTO.getGameId());
        
        winnerPlayers.forEach(winnerTeam::addPlayer);
        loserPlayers.forEach(loserTeam::addPlayer);
        
        game.recordTeamMatch(winnerTeam, loserTeam);
        return true;
    }
}
