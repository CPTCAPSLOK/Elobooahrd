import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, catchError, throwError } from 'rxjs';
import { Match, MatchResult } from '../models/match.model';

@Injectable({
  providedIn: 'root'
})
export class MatchService {

  constructor(private http: HttpClient) { }

  getAllMatches(): Observable<Match[]> {
    return this.http.get<Match[]>('/api/matches')
      .pipe(
        catchError(error => {
          console.error('Error fetching matches', error);
          return throwError(() => new Error('Failed to fetch matches. Please try again later.'));
        })
      );
  }

  getMatchesByGameId(gameId: string): Observable<Match[]> {
    return this.http.get<Match[]>(`/api/games/${gameId}/matches`)
      .pipe(
        catchError(error => {
          console.error(`Error fetching matches for game ${gameId}`, error);
          return throwError(() => new Error('Failed to fetch matches for this game. Please try again later.'));
        })
      );
  }

  getMatchesByPlayerId(playerId: string): Observable<Match[]> {
    return this.http.get<Match[]>(`/api/players/${playerId}/matches`)
      .pipe(
        catchError(error => {
          console.error(`Error fetching matches for player ${playerId}`, error);
          return throwError(() => new Error('Failed to fetch matches for this player. Please try again later.'));
        })
      );
  }

  recordMatch(match: Match): Observable<any> {
    return this.http.post<any>('/api/matches', match)
      .pipe(
        catchError(error => {
          console.error('Error recording match', error);
          return throwError(() => new Error('Failed to record match. Please try again later.'));
        })
      );
  }
}
