export interface Player {
  id: string;
  name: string;
  eloRatings: { [gameId: string]: number };
}
