import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { MetadataPageComponent } from './pages/metadata-page/metadata-page.component';
import { PropertyItemsPageComponent } from './pages/property-items-page/property-items-page.component';

const routes: Routes = [
  {
    path: '',
    component: MetadataPageComponent
  },
  {
    path: 'property-items/:metaDataName',
    component: PropertyItemsPageComponent
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class MetadataRoutingModule {}
