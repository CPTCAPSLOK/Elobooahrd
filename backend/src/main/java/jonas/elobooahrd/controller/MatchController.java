package jonas.elobooahrd.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jonas.elobooahrd.dto.MatchDTO;
import jonas.elobooahrd.service.MatchService;

/**
 * REST controller for managing matches in the Eloboard system.
 */
@RestController
@RequestMapping("/api/matches")
public class MatchController {

    @Autowired
    private MatchService matchService;

    /**
     * POST /api/matches : Record a new match
     * 
     * @param matchDTO The match data
     * @return Success status
     */
    @PostMapping
    public ResponseEntity<Void> recordMatch(@RequestBody MatchDTO matchDTO) {
        boolean success = matchService.recordMatch(matchDTO);
        
        if (success) {
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
