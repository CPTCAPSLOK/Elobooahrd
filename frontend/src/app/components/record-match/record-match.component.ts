import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatSelectModule } from '@angular/material/select';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatIconModule } from '@angular/material/icon';
import { Router, RouterLink } from '@angular/router';
import { MatchService } from '../../services/match.service';
import { GameService } from '../../services/game.service';
import { PlayerService } from '../../services/player.service';
import { Match, MatchResult } from '../../models/match.model';
import { Game } from '../../models/game.model';
import { Player } from '../../models/player.model';

@Component({
  selector: 'app-record-match',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatSelectModule,
    MatSnackBarModule,
    MatProgressSpinnerModule,
    MatIconModule,
    RouterLink
  ],
  templateUrl: './record-match.component.html',
  styleUrl: './record-match.component.scss'
})
export class RecordMatchComponent implements OnInit {
  matchForm: FormGroup;
  submitting = false;
  loading = true;
  error = false;
  errorMessage = '';
  
  games: Game[] = [];
  players: Player[] = [];

  constructor(
    private fb: FormBuilder,
    private matchService: MatchService,
    private gameService: GameService,
    private playerService: PlayerService,
    private snackBar: MatSnackBar,
    private router: Router
  ) {
    this.matchForm = this.fb.group({
      gameId: ['', Validators.required],
      player1Id: ['', Validators.required],
      player2Id: ['', Validators.required],
      player1Score: [0, [Validators.required, Validators.min(0)]],
      player2Score: [0, [Validators.required, Validators.min(0)]]
    });
  }

  ngOnInit(): void {
    this.loadGames();
    this.loadPlayers();
  }

  loadGames(): void {
    this.gameService.getAllGames().subscribe({
      next: (games) => {
        this.games = games;
        this.checkLoading();
      },
      error: (error) => {
        console.error('Error loading games', error);
        this.error = true;
        this.errorMessage = 'Failed to load games. Please try again later.';
        this.loading = false;
      }
    });
  }

  loadPlayers(): void {
    this.playerService.getAllPlayers().subscribe({
      next: (players) => {
        this.players = players;
        this.checkLoading();
      },
      error: (error) => {
        console.error('Error loading players', error);
        this.error = true;
        this.errorMessage = 'Failed to load players. Please try again later.';
        this.loading = false;
      }
    });
  }

  checkLoading(): void {
    if (this.games.length > 0 && this.players.length > 0) {
      this.loading = false;
    }
  }

  onSubmit(): void {
    if (this.matchForm.valid) {
      const player1Id = this.matchForm.value.player1Id;
      const player2Id = this.matchForm.value.player2Id;
      
      if (player1Id === player2Id) {
        this.snackBar.open('Players must be different', 'Close', { duration: 3000 });
        return;
      }
      
      if (this.matchForm.value.player1Score === 0 && this.matchForm.value.player2Score === 0) {
        this.snackBar.open('At least one player must score points', 'Close', { duration: 3000 });
        return;
      }
      
      this.submitting = true;
      
      // Determine winner and loser based on scores
      let winnerIds: string[] = [];
      let loserIds: string[] = [];
      let winnerScores: number[] = [];
      let loserScores: number[] = [];
      
      if (this.matchForm.value.player1Score > this.matchForm.value.player2Score) {
        winnerIds.push(player1Id);
        loserIds.push(player2Id);
        winnerScores.push(this.matchForm.value.player1Score);
        loserScores.push(this.matchForm.value.player2Score);
      } else {
        winnerIds.push(player2Id);
        loserIds.push(player1Id);
        winnerScores.push(this.matchForm.value.player2Score);
        loserScores.push(this.matchForm.value.player1Score);
      }
      
      const match: Match = {
        gameId: this.matchForm.value.gameId,
        winnerIds: winnerIds,
        loserIds: loserIds,
        winnerScores: winnerScores,
        loserScores: loserScores,
        isTeamMatch: false
      };

      this.matchService.recordMatch(match).subscribe({
        next: (result) => {
          this.submitting = false;
          this.snackBar.open('Match recorded successfully!', 'Close', { duration: 3000 });
          this.router.navigate(['/players']);
        },
        error: (error) => {
          this.submitting = false;
          this.snackBar.open(`Error recording match: ${error.message}`, 'Close', {
            duration: 5000
          });
        }
      });
    }
  }

  retryLoading(): void {
    this.loading = true;
    this.error = false;
    this.errorMessage = '';
    this.loadGames();
    this.loadPlayers();
  }
}
