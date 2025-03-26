package jonas.elobooahrd.dto;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for Player entities.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlayerDTO {
    private UUID id;
    private String name;
    private Map<UUID, Integer> eloRatings = new HashMap<>();
}
