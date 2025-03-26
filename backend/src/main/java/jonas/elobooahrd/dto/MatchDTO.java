package jonas.elobooahrd.dto;

import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for Match data.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MatchDTO {
    private UUID gameId;
    private List<UUID> winnerIds;
    private List<UUID> loserIds;
    private List<Integer> winnerScores; // Optional, for games that track scores
    private List<Integer> loserScores; // Optional, for games that track scores
    private boolean isTeamMatch = false;
    private UUID winnerTeamId; // Only used if isTeamMatch is true
    private UUID loserTeamId; // Only used if isTeamMatch is true
}
