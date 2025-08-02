import { Routes } from '@angular/router';
import { Home } from './pages/home/home';
import { NotFound } from './components/not-found/not-found';
import { Simulation } from './pages/simulation/simulation/simulation';

export const routes: Routes = [
  {
    path: "home",
    title: "home",
    component: Home,
  },

  {
    path: "simulation",
    title: "simulation",
    component: Simulation,
  },

  {
    path: "",
    redirectTo: "/home",
    pathMatch: "full",
  },

  {
    path: "**",
    component: NotFound,
  }
];
