export interface Game {
  id: string;
  name: string;
  description: string;
  initialEloRating: number;
  type: string;
  kfactor: number;
}
