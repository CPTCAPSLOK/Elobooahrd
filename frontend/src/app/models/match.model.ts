export interface Match {
  id?: string;
  gameId: string;
  winnerIds: string[];
  loserIds: string[];
  winnerScores?: number[];
  loserScores?: number[];
  isTeamMatch?: boolean;
  winnerTeamId?: string;
  loserTeamId?: string;
}

export interface MatchResult {
  matchId: string;
  player1Id: string;
  player2Id: string;
  player1NewRating: number;
  player2NewRating: number;
  ratingChange: number;
}
