import { Routes } from '@angular/router';
import { GamesComponent } from './components/games/games.component';
import { PlayersComponent } from './components/players/players.component';
import { AddPlayerComponent } from './components/add-player/add-player.component';
import { AddGameComponent } from './components/add-game/add-game.component';
import { RecordMatchComponent } from './components/record-match/record-match.component';

export const routes: Routes = [
  { path: '', redirectTo: '/games', pathMatch: 'full' },
  { path: 'games', component: GamesComponent },
  { path: 'players', component: PlayersComponent },
  { path: 'add-player', component: AddPlayerComponent },
  { path: 'add-game', component: AddGameComponent },
  { path: 'record-match', component: RecordMatchComponent }
];
