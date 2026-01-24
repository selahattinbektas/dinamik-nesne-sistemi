import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { UiContentPageComponent } from './pages/ui-content-page/ui-content-page.component';

const routes: Routes = [
  {
    path: '',
    component: UiContentPageComponent
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class UiContentRoutingModule {}
