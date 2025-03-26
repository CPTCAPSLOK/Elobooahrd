package jonas.elobooahrd.dto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for Game entities.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameDTO {
    private UUID id;
    private String name;
    private String description;
    private int initialEloRating = 1000;
    private int kFactor = 32;
    private String type; // Stores the type of game (e.g., "Tablefootball", "Dart")
}
