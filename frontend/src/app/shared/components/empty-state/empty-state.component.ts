import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-empty-state',
  templateUrl: './empty-state.component.html'
})
export class EmptyStateComponent {
  @Input() message = 'Henüz kayıt yok.';
}
