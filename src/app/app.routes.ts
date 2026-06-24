import { Routes } from '@angular/router';
import { Dashboard } from './pages/dashboard/dashboard';
import { Entry } from './pages/entry/entry.component';
import { Exit } from './pages/exit/exit.component';
import { ParkingSpots } from './pages/parking-spots/parking-spots.component';
import { History } from './pages/history/history.component';
import { Reports } from './pages/reports/reports.component';
import { ManageTeam } from './pages/manage-team/manage-team.component';
import { SettingsPrice } from './pages/settings-price/settings-price.component';
import { Profile } from './pages/profile/profile.component';
import { Login } from './pages/auth/login/login';
import { Cadastro } from './pages/auth/cadastro/cadastro';

export const routes: Routes = [
  { path: 'login', component: Login },
  { path: 'cadastro', component: Cadastro },
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  { path: 'dashboard', component: Dashboard },
  { path: 'entry', component: Entry },
  { path: 'exit', component: Exit },
  { path: 'parking-spots', component: ParkingSpots },
  { path: 'history', component: History },
  { path: 'reports', component: Reports },
  { path: 'manage-team', component: ManageTeam },
  { path: 'settings-price', component: SettingsPrice },
  { path: 'profile', component: Profile },
  { path: '**', redirectTo: 'login' }
];