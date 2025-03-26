import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { GameService } from '../../services/game.service';
import { Game } from '../../models/game.model';

@Component({
  selector: 'app-games',
  standalone: true,
  imports: [
    CommonModule,
    MatCardModule,
    MatTableModule,
    MatButtonModule,
    MatIconModule,
    MatProgressSpinnerModule
  ],
  templateUrl: './games.component.html',
  styleUrl: './games.component.scss'
})
export class GamesComponent implements OnInit {
  games: Game[] = [];
  displayedColumns: string[] = ['name', 'description', 'type', 'initialEloRating', 'kFactor'];
  loading = true;
  error = false;
  errorMessage = '';

  constructor(private gameService: GameService) {}

  ngOnInit(): void {
    this.loadGames();
  }

  loadGames(): void {
    this.loading = true;
    this.error = false;
    this.errorMessage = '';
    
    this.gameService.getAllGames().subscribe({
      next: (data) => {
        this.games = data;
        this.loading = false;
      },
      error: (err) => {
        this.loading = false;
        this.error = true;
        this.errorMessage = `Failed to load games: ${err.message || 'Unknown error'}`;
        console.error('Error loading games', err);
      }
    });
  }

  retryLoading(): void {
    this.loadGames();
  }
}
