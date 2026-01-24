import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';

import { MetadataRoutingModule } from './metadata-routing.module';
import { MetadataPageComponent } from './pages/metadata-page/metadata-page.component';
import { MetadataFormComponent } from './components/metadata-form/metadata-form.component';
import { PropertyItemsPageComponent } from './pages/property-items-page/property-items-page.component';
import { SharedModule } from '../../shared/shared.module';

@NgModule({
  declarations: [MetadataPageComponent, MetadataFormComponent, PropertyItemsPageComponent],
  imports: [CommonModule, ReactiveFormsModule, MetadataRoutingModule, SharedModule]
})
export class MetadataModule {}
