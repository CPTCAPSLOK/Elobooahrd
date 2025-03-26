import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, catchError, throwError } from 'rxjs';
import { Player } from '../models/player.model';

@Injectable({
  providedIn: 'root'
})
export class PlayerService {
  private apiUrl = '/api/players';  // Using relative path for proxy

  constructor(private http: HttpClient) { }

  getAllPlayers(): Observable<Player[]> {
    return this.http.get<Player[]>(this.apiUrl)
      .pipe(
        catchError(error => {
          console.error('Error fetching players', error);
          return throwError(() => new Error('Failed to fetch players. Please try again later.'));
        })
      );
  }

  getPlayer(id: string): Observable<Player> {
    return this.http.get<Player>(`${this.apiUrl}/${id}`)
      .pipe(
        catchError(error => {
          console.error(`Error fetching player with id ${id}`, error);
          return throwError(() => new Error('Failed to fetch player. Please try again later.'));
        })
      );
  }

  getPlayersByGame(gameId: string): Observable<Player[]> {
    return this.http.get<Player[]>(`${this.apiUrl}/game/${gameId}`)
      .pipe(
        catchError(this.handleError)
      );
  }

  createPlayer(player: Player): Observable<Player> {
    return this.http.post<Player>(this.apiUrl, player)
      .pipe(
        catchError(error => {
          console.error('Error creating player', error);
          return throwError(() => new Error('Failed to create player. Please try again later.'));
        })
      );
  }

  private handleError(error: HttpErrorResponse) {
    console.error('API Error:', error);
    return throwError(() => error);
  }
}
