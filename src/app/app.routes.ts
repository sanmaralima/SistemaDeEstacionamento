import { Routes } from '@angular/router';
import { Dashboard } from './pages/dashboard/dashboard';
import { ControleVagas } from './pages/controle-vagas/controle-vagas';
import { Historico } from './pages/historico/historico';

export const routes: Routes = [
  { path: '', component: Dashboard },
  { path: 'controle-vagas', component: ControleVagas },
  { path: 'historico', component: Historico },
  { path: '**', redirectTo: '' }
];