import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';
import { SharedModule } from '../../shared/shared.module';
import { ClassificationsRoutingModule } from './classifications-routing.module';
import { ClassificationsPageComponent } from './pages/classifications-page/classifications-page.component';

@NgModule({
  declarations: [ClassificationsPageComponent],
  imports: [CommonModule, ReactiveFormsModule, SharedModule, ClassificationsRoutingModule]
})
export class ClassificationsModule {}
