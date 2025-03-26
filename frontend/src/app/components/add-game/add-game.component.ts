import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatIconModule } from '@angular/material/icon';
import { RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';
import { GameService } from '../../services/game.service';
import { Router } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Game } from '../../models/game.model';

@Component({
  selector: 'app-add-game',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatButtonModule,
    MatCardModule,
    MatProgressSpinnerModule,
    MatIconModule,
    RouterLink
  ],
  templateUrl: './add-game.component.html',
  styleUrls: ['./add-game.component.scss']
})
export class AddGameComponent implements OnInit {
  gameForm!: FormGroup;
  submitting = false;

  constructor(
    private fb: FormBuilder,
    private gameService: GameService,
    private router: Router,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {
    this.gameForm = this.fb.group({
      name: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(50)]],
      description: ['', [Validators.required, Validators.maxLength(200)]],
      type: ['', Validators.required],
      initialEloRating: [1200, [Validators.required, Validators.min(100), Validators.max(3000)]],
      kfactor: [32, [Validators.required, Validators.min(10), Validators.max(40)]]
    });
  }

  onSubmit(): void {
    if (this.gameForm.valid) {
      this.submitting = true;
      
      // Prepare the game data from the form
      const gameData: Game = {
        id: '', // Will be assigned by the backend
        name: this.gameForm.value.name,
        description: this.gameForm.value.description,
        type: this.gameForm.value.type,
        initialEloRating: this.gameForm.value.initialEloRating,
        kfactor: this.gameForm.value.kfactor
      };
      
      // Call the service to create the game
      this.gameService.createGame(gameData).subscribe({
        next: () => {
          this.submitting = false;
          this.snackBar.open('Game created successfully!', 'Close', {
            duration: 3000,
            panelClass: ['success-snackbar']
          });
          this.router.navigate(['/games']);
        },
        error: (error) => {
          this.submitting = false;
          console.error('Error creating game:', error);
          this.snackBar.open('Failed to create game. Please try again.', 'Close', {
            duration: 5000,
            panelClass: ['error-snackbar']
          });
        }
      });
    }
  }
}
