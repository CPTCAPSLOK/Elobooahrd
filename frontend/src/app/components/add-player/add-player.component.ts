import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { Router, RouterLink } from '@angular/router';
import { PlayerService } from '../../services/player.service';
import { Player } from '../../models/player.model';

@Component({
  selector: 'app-add-player',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatSnackBarModule,
    MatProgressSpinnerModule,
    RouterLink
  ],
  templateUrl: './add-player.component.html',
  styleUrl: './add-player.component.scss'
})
export class AddPlayerComponent {
  playerForm: FormGroup;
  submitting = false;

  constructor(
    private fb: FormBuilder,
    private playerService: PlayerService,
    private snackBar: MatSnackBar,
    private router: Router
  ) {
    this.playerForm = this.fb.group({
      name: ['', [Validators.required, Validators.minLength(2)]]
    });
  }

  onSubmit(): void {
    if (this.playerForm.valid) {
      this.submitting = true;
      
      const newPlayer: Player = {
        id: '', // Will be assigned by the backend
        name: this.playerForm.value.name,
        eloRatings: {}
      };

      this.playerService.createPlayer(newPlayer).subscribe({
        next: (createdPlayer) => {
          this.submitting = false;
          this.snackBar.open(`Player ${createdPlayer.name} created successfully!`, 'Close', {
            duration: 3000
          });
          this.router.navigate(['/players']);
        },
        error: (error) => {
          this.submitting = false;
          this.snackBar.open(`Error creating player: ${error.message}`, 'Close', {
            duration: 5000
          });
        }
      });
    }
  }
}
