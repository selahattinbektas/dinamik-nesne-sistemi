import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ClassificationsPageComponent } from './pages/classifications-page/classifications-page.component';

const routes: Routes = [
  {
    path: '',
    component: ClassificationsPageComponent
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ClassificationsRoutingModule {}
