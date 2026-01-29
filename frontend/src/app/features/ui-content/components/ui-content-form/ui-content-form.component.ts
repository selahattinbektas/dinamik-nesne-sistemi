import { Component, EventEmitter, Input, Output } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { UiContent } from '../../../../core/models/ui-content.models';

@Component({
  selector: 'app-ui-content-form',
  templateUrl: './ui-content-form.component.html'
})
export class UiContentFormComponent {
  @Input({ required: true }) formGroup!: FormGroup;
  @Input({ required: true }) contentTypes: string[] = [];
  @Input({ required: true }) uiContents: UiContent[] = [];
  @Input({ required: true }) propertyItemOptions: { value: number; label: string }[] = [];
  @Input() isLoading = false;
  @Input() errorMessage = '';

  @Output() submitForm = new EventEmitter<void>();
  @Output() refresh = new EventEmitter<void>();
  @Output() editUiContent = new EventEmitter<UiContent>();
  @Output() deleteUiContent = new EventEmitter<UiContent>();

  itemDropdownOpen = false;

    isItemSelected(itemId: number): boolean {
      const selectedItems = (this.formGroup.get('itemIdList')?.value as number[] | null) ?? [];
      return selectedItems.includes(itemId);
    }

    toggleItemDropdown(): void {
      this.itemDropdownOpen = !this.itemDropdownOpen;
    }


    toggleItemSelection(itemId: number, checked: boolean): void {
      const control = this.formGroup.get('itemIdList');
      const selectedItems = (control?.value as number[] | null) ?? [];
      const nextItems = checked
        ? Array.from(new Set([...selectedItems, itemId]))
        : selectedItems.filter((id) => id !== itemId);
      control?.setValue(nextItems);
      control?.markAsDirty();
    }
}
