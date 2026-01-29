import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { EmptyStateComponent } from './components/empty-state/empty-state.component';
import { UiContentTypeLabelPipe } from './pipes/ui-content-type-label.pipe';

@NgModule({
    declarations: [EmptyStateComponent, UiContentTypeLabelPipe],
    imports: [CommonModule],
    exports: [EmptyStateComponent, UiContentTypeLabelPipe]
})
export class SharedModule {}
