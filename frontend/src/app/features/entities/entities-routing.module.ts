import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { EntitiesPageComponent } from './pages/entities-page/entities-page.component';
import { EntityPropertiesPageComponent } from './pages/entity-properties-page/entity-properties-page.component';

const routes: Routes = [
  {
    path: ':id/properties',
    component: EntityPropertiesPageComponent
  },
  {
    path: '',
    component: EntitiesPageComponent
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class EntitiesRoutingModule {}
