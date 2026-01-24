import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

const routes: Routes = [
  {
    path: '',
    pathMatch: 'full',
    redirectTo: 'metadata'
  },
  {
    path: 'metadata',
    loadChildren: () => import('./features/metadata/metadata.module').then((m) => m.MetadataModule)
  },
  {
    path: 'entities',
    loadChildren: () => import('./features/entities/entities.module').then((m) => m.EntitiesModule)
  },
  {
    path: 'ui-content',
    loadChildren: () => import('./features/ui-content/ui-content.module').then((m) => m.UiContentModule)
  },
  {
    path: 'options',
    loadChildren: () => import('./features/options/options.module').then((m) => m.OptionsModule)
  },
  {
    path: 'classifications',
    loadChildren: () => import('./features/classifications/classifications.module').then((m) => m.ClassificationsModule)
  },
  {
    path: '**',
    redirectTo: 'metadata'
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {}
