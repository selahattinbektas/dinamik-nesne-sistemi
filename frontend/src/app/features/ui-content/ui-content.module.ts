import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';

import { UiContentRoutingModule } from './ui-content-routing.module';
import { UiContentPageComponent } from './pages/ui-content-page/ui-content-page.component';
import { UiContentFormComponent } from './components/ui-content-form/ui-content-form.component';
import { SharedModule } from '../../shared/shared.module';

@NgModule({
  declarations: [UiContentPageComponent, UiContentFormComponent],
  imports: [CommonModule, ReactiveFormsModule, UiContentRoutingModule, SharedModule]
})
export class UiContentModule {}
