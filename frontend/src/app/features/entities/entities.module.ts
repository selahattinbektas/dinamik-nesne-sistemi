import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';

import { EntitiesRoutingModule } from './entities-routing.module';
import { EntitiesPageComponent } from './pages/entities-page/entities-page.component';
import { EntitiesFormComponent } from './components/entities-form/entities-form.component';
import { EntityPropertiesPageComponent } from './pages/entity-properties-page/entity-properties-page.component';
import { SharedModule } from '../../shared/shared.module';

@NgModule({
  declarations: [EntitiesPageComponent, EntitiesFormComponent, EntityPropertiesPageComponent],
  imports: [CommonModule, ReactiveFormsModule, EntitiesRoutingModule, SharedModule]
})
export class EntitiesModule {}
