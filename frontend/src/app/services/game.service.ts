import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, catchError, throwError } from 'rxjs';
import { Game } from '../models/game.model';

@Injectable({
  providedIn: 'root'
})
export class GameService {
  private apiUrl = '/api/games';  // Using relative path for proxy

  constructor(private http: HttpClient) { }

  getAllGames(): Observable<Game[]> {
    return this.http.get<Game[]>(this.apiUrl)
      .pipe(
        catchError(error => {
          console.error('Error fetching games', error);
          return throwError(() => new Error('Failed to fetch games. Please try again later.'));
        })
      );
  }

  getGame(id: string): Observable<Game> {
    return this.http.get<Game>(`${this.apiUrl}/${id}`)
      .pipe(
        catchError(error => {
          console.error(`Error fetching game with id ${id}`, error);
          return throwError(() => new Error('Failed to fetch game. Please try again later.'));
        })
      );
  }

  createGame(game: Game): Observable<Game> {
    return this.http.post<Game>(this.apiUrl, game)
      .pipe(
        catchError(error => {
          console.error('Error creating game', error);
          return throwError(() => new Error('Failed to create game. Please try again later.'));
        })
      );
  }
}
