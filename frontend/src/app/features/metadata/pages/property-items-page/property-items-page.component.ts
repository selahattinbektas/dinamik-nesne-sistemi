import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { MetadataApiService } from '../../../../core/api/metadata-api.service';
import {
  EComponentType,
  EOptionsPropertyItemType,
  EPropertyItemType
} from '../../../../core/models/enums';
import { EComponentTypeLabelMap } from '../../../../core/models/enums.labels';
import { MetaData, Option, PropertyItem } from '../../../../core/models/metadata.models';

@Component({
  selector: 'app-property-items-page',
  templateUrl: './property-items-page.component.html',
  styleUrls: ['./property-items-page.component.css']
})
export class PropertyItemsPageComponent implements OnInit {
  metaDataName = '';
  metaDataEntityType = '';
  propertyItems: PropertyItem[] = [];
  isLoading = false;
  isSaving = false;
  isEditDialogOpen = false;
  errorMessage = '';
  componentTypes = Object.values(EComponentType);
  componentTypeLabels = EComponentTypeLabelMap;
  propertyItemTypes = Object.values(EPropertyItemType);
  editingItem: PropertyItem | null = null;
  options: Option[] = [];
  optionsSearch = '';
  editOptionsSearch = '';

  form = this.fb.group({
    itemName: ['', Validators.required],
    title: ['', Validators.required],
    type: [EComponentType.TextInputComponent, Validators.required],
    unit: [''],
    min: [null as number | null],
    max: [null as number | null],
    defaultValue: [''],
    groupProperty: [false],
    readOnly: [false],
    showContextMenu: [false],
    propertyItemType: [EPropertyItemType.DEFAULT, Validators.required],
    selectedVisible: [true],
    toValidatorClass: [''],
    fromValidatorClass: [''],
    options: [[] as Option[]]
  });

  editForm = this.fb.group({
    itemName: ['', Validators.required],
    title: ['', Validators.required],
    type: [EComponentType.TextInputComponent, Validators.required],
    unit: [''],
    min: [null as number | null],
    max: [null as number | null],
    defaultValue: [''],
    groupProperty: [false],
    readOnly: [false],
    showContextMenu: [false],
    propertyItemType: [EPropertyItemType.DEFAULT, Validators.required],
    selectedVisible: [true],
    toValidatorClass: [''],
    fromValidatorClass: [''],
    options: [[] as Option[]]
  });

  constructor(
    private readonly fb: FormBuilder,
    private readonly metadataApi: MetadataApiService,
    private readonly route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    const metaDataName = this.route.snapshot.paramMap.get('metaDataName');
    this.metaDataName = metaDataName ?? '';
    if (!this.metaDataName) {
      this.errorMessage = 'Metadata adı bulunamadı.';
      return;
    }

    this.loadMetaData();
    this.loadPropertyItems();
    this.loadOptions();
  }

  loadMetaData(): void {
    this.errorMessage = '';
    this.metadataApi.getMetaData(this.metaDataName).subscribe({
      next: (data: MetaData) => {
        this.metaDataEntityType = data.entityType ?? '';
      },
      error: () => {
        this.errorMessage = 'Metadata bilgisi alınamadı.';
      }
    });
  }

  loadPropertyItems(): void {
    this.isLoading = true;
    this.errorMessage = '';
    this.metadataApi.getPropertyItemsByMetaData(this.metaDataName).subscribe({
      next: (items) => {
        this.propertyItems = items;
        this.isLoading = false;
      },
      error: () => {
        this.errorMessage = 'Property item listesi alınamadı.';
        this.isLoading = false;
      }
    });
  }

  loadOptions(): void {
    this.metadataApi.getAllOptions().subscribe({
      next: (options) => {
        this.options = options;
      },
      error: () => {
        this.errorMessage = 'Option listesi alınamadı.';
      }
    });
  }

  submit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    const selectedType = this.form.value.type ?? EComponentType.TextInputComponent;
    const resolvedOptions = this.getResolvedOptionsForForm(this.form);
    const payload = {
      itemName: this.form.value.itemName ?? '',
      title: this.form.value.title ?? '',
      type: selectedType,
      unit: this.form.value.unit ?? undefined,
      min: this.form.value.min ?? undefined,
      max: this.form.value.max ?? undefined,
      defaultValue: this.form.value.defaultValue ?? undefined,
      groupProperty: this.form.value.groupProperty ?? false,
      readOnly: this.form.value.readOnly ?? false,
      toValidatorClass: this.form.value.toValidatorClass ?? undefined,
      fromValidatorClass: this.form.value.fromValidatorClass ?? undefined,
      showContextMenu: this.form.value.showContextMenu ?? false,
      propertyItemType: this.form.value.propertyItemType ?? EPropertyItemType.DEFAULT,
      selectedVisible: this.form.value.selectedVisible ?? true,
      options: this.isOptionsComponent(selectedType)
              ? this.mapOptionsForPayload(resolvedOptions)
              : []
    };

    this.isSaving = true;
    this.errorMessage = '';
    this.metadataApi.createPropertyItem(this.metaDataName, payload).subscribe({
      next: (created) => {
        this.propertyItems = [...this.propertyItems, created];
        this.isSaving = false;
        this.form.reset({
          itemName: '',
          title: '',
          type: EComponentType.TextInputComponent,
          unit: '',
          min: null,
          max: null,
          defaultValue: '',
          groupProperty: false,
          readOnly: false,
          showContextMenu: false,
          propertyItemType: EPropertyItemType.DEFAULT,
          selectedVisible: true,
          toValidatorClass: '',
          fromValidatorClass: '',
          options: []
        });
        this.optionsSearch = '';
      },
      error: () => {
        this.errorMessage = 'Property item kaydedilemedi.';
        this.isSaving = false;
      }
    });
  }

  deletePropertyItem(item: PropertyItem): void {
    const confirmed = window.confirm(`'${item.title}' kaydı silinecektir. Emin misiniz?`);
    if (!confirmed) {
      return;
    }

    this.isLoading = true;
    this.errorMessage = '';
    this.metadataApi.deletePropertyItem(item.id ?? 0).subscribe({
      next: () => {
        this.propertyItems = this.propertyItems.filter((existing) => existing.id !== item.id);
        this.isLoading = false;
      },
      error: () => {
        this.errorMessage = 'Property item silinemedi.';
        this.isLoading = false;
      }
    });
  }

  openEditDialog(item: PropertyItem): void {
    this.editingItem = item;
    this.editForm.reset({
      itemName: item.itemName,
      title: item.title,
      type: item.type ?? EComponentType.TextInputComponent,
      unit: item.unit ?? '',
      min: item.min ?? null,
      max: item.max ?? null,
      defaultValue: item.defaultValue ?? '',
      groupProperty: item.groupProperty ?? false,
      readOnly: item.readOnly ?? false,
      showContextMenu: item.showContextMenu ?? false,
      propertyItemType: item.propertyItemType ?? EPropertyItemType.DEFAULT,
      selectedVisible: item.selectedVisible ?? true,
      toValidatorClass: item.toValidatorClass ?? '',
      fromValidatorClass: item.fromValidatorClass ?? '',
      options: this.mapSelectedOptions(item.options ?? [])
    });
    this.editOptionsSearch = '';
    this.isEditDialogOpen = true;
  }

  closeEditDialog(): void {
    this.isEditDialogOpen = false;
    this.editingItem = null;
  }

  updatePropertyItem(): void {
    if (!this.editingItem || this.editForm.invalid) {
      this.editForm.markAllAsTouched();
      return;
    }

    const selectedType = this.editForm.value.type ?? this.editingItem.type;
    const resolvedOptions = this.getResolvedOptionsForForm(this.editForm);
    const payload = {
      itemName: this.editForm.value.itemName ?? this.editingItem.itemName,
      title: this.editForm.value.title ?? this.editingItem.title,
      type: selectedType,
      unit: this.editForm.value.unit ?? undefined,
      min: this.editForm.value.min ?? undefined,
      max: this.editForm.value.max ?? undefined,
      defaultValue: this.editForm.value.defaultValue ?? undefined,
      groupProperty: this.editForm.value.groupProperty ?? false,
      readOnly: this.editForm.value.readOnly ?? false,
      toValidatorClass: this.editForm.value.toValidatorClass ?? undefined,
      fromValidatorClass: this.editForm.value.fromValidatorClass ?? undefined,
      showContextMenu: this.editForm.value.showContextMenu ?? false,
      propertyItemType: this.editForm.value.propertyItemType ?? this.editingItem.propertyItemType,
      selectedVisible: this.editForm.value.selectedVisible ?? true,
      options: this.isOptionsComponent(selectedType)
              ? this.mapOptionsForPayload(resolvedOptions)
              : []
    };

    this.isSaving = true;
    this.errorMessage = '';
    this.metadataApi.updatePropertyItem(this.editingItem.id ?? 0, payload).subscribe({
      next: (updated) => {
        this.propertyItems = this.propertyItems.map((existing) =>
          existing.id === updated.id ? updated : existing
        );
        this.isSaving = false;
        this.editOptionsSearch = '';
        this.closeEditDialog();
      },
      error: () => {
        this.errorMessage = 'Property item güncellenemedi.';
        this.isSaving = false;
      }
    });
  }

  private mapSelectedOptions(selected: Option[]): Option[] {
    if (!selected.length) {
      return [];
    }
    return selected
      .map((selectedOption) => this.options.find((option) => option.value === selectedOption.value))
      .filter((option): option is Option => Boolean(option));
  }

  private isOptionBasedType(type: EComponentType): boolean {
      return [
        EComponentType.SelectComponent,
        EComponentType.RadioGroupComponent,
        EComponentType.SegmentedSelectionComponent
      ].includes(type);
    }

    isOptionType(type: EComponentType | null | undefined): boolean {
        if (!type) {
          return false;
        }
        return this.isOptionBasedType(type);
      }

      shouldShowOptions(formGroup: FormGroup): boolean {
          const type = formGroup.get('type')?.value as EComponentType | null | undefined;
          if (!type || !this.isOptionsComponent(type)) {
            return false;
          }
          return this.getOptionsForForm(formGroup).length > 0;
        }

        isSelectComponent(formGroup: FormGroup): boolean {
          const type = formGroup.get('type')?.value as EComponentType | null | undefined;
          return type === EComponentType.SelectComponent || type === EComponentType.HiddenComponent;
        }

        isSwitchComponent(formGroup: FormGroup): boolean {
          const type = formGroup.get('type')?.value as EComponentType | null | undefined;
          return type === EComponentType.SwitchComponent;
        }

        getOptionsForForm(formGroup: FormGroup): Option[] {
          const itemName = (formGroup.get('itemName')?.value ?? '').toString();

          const optionType = this.mapItemNameToOptionsType(itemName);

          if (!optionType) {
            return [];
          }

            return this.options.filter(option => {
                const optionTypeFromBackend =
                  EOptionsPropertyItemType[
                    option.optionsPropertyItemType as unknown as keyof typeof EOptionsPropertyItemType
                  ];
                return optionTypeFromBackend === optionType;
              });
        }

        getFilteredOptions(formGroup: FormGroup, searchTerm: string): Option[] {
          const normalized = searchTerm.trim().toLowerCase();
          const filteredOptions = this.getOptionsForForm(formGroup);
          if (!normalized) {
            return filteredOptions;
          }
         return filteredOptions.filter((option) => {
            const label = option.label?.toLowerCase() ?? '';
            const value = option.value?.toLowerCase() ?? '';
            return label.includes(normalized) || value.includes(normalized);
          });
        }

        isOptionSelected(formGroup: FormGroup, option: Option): boolean {
          const selected = (formGroup.value.options ?? []) as Option[];
          return selected.some((selectedOption) => selectedOption.value === option.value);
        }

        toggleOptionSelection(formGroup: FormGroup, option: Option): void {
          const selected = (formGroup.value.options ?? []) as Option[];
          const isSelected = selected.some((selectedOption) => selectedOption.value === option.value);
          const nextSelection = isSelected
            ? selected.filter((selectedOption) => selectedOption.value !== option.value)
            : [...selected, option];
          formGroup.get('options')?.setValue(nextSelection);
          formGroup.get('options')?.markAsDirty();
        }

        toggleSingleOptionSelection(formGroup: FormGroup, option: Option, checked: boolean): void {
            if (checked) {
              formGroup.get('options')?.setValue([option]);
            } else {
              formGroup.get('options')?.setValue([]);
            }
            formGroup.get('options')?.markAsDirty();
          }

          setSingleOptionByValue(formGroup: FormGroup, value: string): void {
            const option = this.getOptionsForForm(formGroup).find((item) => item.value === value);
            formGroup.get('options')?.setValue(option ? [option] : []);
            formGroup.get('options')?.markAsDirty();
          }

          getSelectedOptionValue(formGroup: FormGroup): string {
            const selected = (formGroup.value.options ?? []) as Option[];
            return selected[0]?.value ?? '';
          }

          handleOptionContextChange(formGroup: FormGroup): void {
            this.trimInvalidSelections(formGroup);
          }

          private trimInvalidSelections(formGroup: FormGroup): void {
            const allowedOptions = this.getOptionsForForm(formGroup);

            const selected = (formGroup.value.options ?? []) as Option[];
            if (!selected.length) {
              return;
            }
            const filtered = selected.filter((option) =>
              allowedOptions.some((allowed) => allowed.value === option.value)
            );
            if (filtered.length !== selected.length) {
              formGroup.get('options')?.setValue(filtered);
            }
          }

          private getResolvedOptionsForForm(formGroup: FormGroup): Option[] {
            const allowedOptions = this.getOptionsForForm(formGroup);
            if (!allowedOptions.length) {
              return [];
            }
            const selected = (formGroup.value.options ?? []) as Option[];
            const filtered = selected.filter((option) =>
              allowedOptions.some((allowed) => allowed.value === option.value)
            );
            if (this.isSelectComponent(formGroup) || this.isSwitchComponent(formGroup)) {
              return filtered.slice(0, 1);
            }
            return filtered;
          }

          private mapItemNameToOptionsType(itemName: string): EOptionsPropertyItemType | null {
            switch (itemName) {
              case EOptionsPropertyItemType.TEAM_TYPE:
                return EOptionsPropertyItemType['TEAM_TYPE'];
              case EOptionsPropertyItemType.OPERATION_CONDITION:
                return EOptionsPropertyItemType.OPERATION_CONDITION;
              case EOptionsPropertyItemType.ACTIVE_STATUS:
                return EOptionsPropertyItemType.ACTIVE_STATUS;
              case EOptionsPropertyItemType.FREEZE_STATUS:
                return EOptionsPropertyItemType.FREEZE_STATUS;
              default:
                return null;
            }
          }


          private isOptionsComponent(type: EComponentType): boolean {
            return (
              this.isOptionBasedType(type) ||
              type === EComponentType.HiddenComponent ||
              type === EComponentType.SwitchComponent
            );
          }

          private mapOptionsForPayload(options: Option[]): Option[] {
              return options.map((option) => ({
                ...option,
                optionsPropertyItemType: this.normalizeOptionsPropertyItemType(option.optionsPropertyItemType)
              }));
            }

            private normalizeOptionsPropertyItemType(
              value: EOptionsPropertyItemType | string | null | undefined
            ): EOptionsPropertyItemType | undefined {
              if (!value) {
                return undefined;
              }
              if (Object.values(EOptionsPropertyItemType).includes(value as EOptionsPropertyItemType)) {
                return value as EOptionsPropertyItemType;
              }
              switch (value) {
                case 'TEAM_TYPE':
                  return EOptionsPropertyItemType.TEAM_TYPE;
                case 'OPERATION_CONDITION':
                  return EOptionsPropertyItemType.OPERATION_CONDITION;
                case 'ACTIVE_STATUS':
                  return EOptionsPropertyItemType.ACTIVE_STATUS;
                case 'FREEZE_STATUS':
                  return EOptionsPropertyItemType.FREEZE_STATUS;
                default:
                  return undefined;
              }
            }
}
