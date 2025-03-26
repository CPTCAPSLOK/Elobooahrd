import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatChipsModule } from '@angular/material/chips';
import { PlayerService } from '../../services/player.service';
import { GameService } from '../../services/game.service';
import { Player } from '../../models/player.model';
import { Game } from '../../models/game.model';

@Component({
  selector: 'app-players',
  standalone: true,
  imports: [
    CommonModule,
    MatCardModule,
    MatTableModule,
    MatButtonModule,
    MatIconModule,
    MatProgressSpinnerModule,
    MatChipsModule
  ],
  templateUrl: './players.component.html',
  styleUrl: './players.component.scss'
})
export class PlayersComponent implements OnInit {
  players: Player[] = [];
  games: Game[] = [];
  displayedColumns: string[] = ['name', 'eloRatings'];
  loading = true;
  error = false;
  errorMessage = '';
  gamesLoading = true;
  gamesError = false;

  constructor(
    private playerService: PlayerService,
    private gameService: GameService
  ) {}

  ngOnInit(): void {
    this.loadPlayers();
    this.loadGames();
  }

  loadPlayers(): void {
    this.loading = true;
    this.error = false;
    this.errorMessage = '';
    
    this.playerService.getAllPlayers().subscribe({
      next: (players) => {
        this.players = players;
        this.loading = false;
      },
      error: (err) => {
        console.error('Error loading players', err);
        this.error = true;
        this.loading = false;
        this.errorMessage = `Failed to load players: ${err.message || 'Unknown error'}`;
      }
    });
  }

  loadGames(): void {
    this.gamesLoading = true;
    this.gamesError = false;
    
    this.gameService.getAllGames().subscribe({
      next: (games) => {
        this.games = games;
        this.gamesLoading = false;
      },
      error: (err) => {
        console.error('Error loading games', err);
        this.gamesError = true;
        this.gamesLoading = false;
      }
    });
  }

  getGameName(gameId: string): string {
    const game = this.games.find(g => g.id === gameId);
    return game ? game.name : 'Unknown Game';
  }
  
  getObjectKeys(obj: any): string[] {
    return Object.keys(obj || {});
  }
  
  retryLoading(): void {
    this.loadPlayers();
    this.loadGames();
  }
}
