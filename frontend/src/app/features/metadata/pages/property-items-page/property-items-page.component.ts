import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { MetadataApiService } from '../../../../core/api/metadata-api.service';
import { EComponentType, EPropertyItemType } from '../../../../core/models/enums';
import { MetaData, Option, PropertyItem } from '../../../../core/models/metadata.models';

@Component({
  selector: 'app-property-items-page',
  templateUrl: './property-items-page.component.html'
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
  propertyItemTypes = Object.values(EPropertyItemType);
  editingItem: PropertyItem | null = null;
  options: Option[] = [];

  form = this.fb.group({
    itemName: ['', Validators.required],
    title: ['', Validators.required],
    type: [EComponentType.TEXT, Validators.required],
    unit: [''],
    min: [null as number | null],
    max: [null as number | null],
    defaultValue: [''],
    groupProperty: [false],
    readOnly: [false],
    showContextMenu: [false],
    propertyItemType: [EPropertyItemType.BASE, Validators.required],
    selectedVisible: [true],
    toValidatorClass: [''],
    fromValidatorClass: [''],
    options: [[] as Option[]]
  });

  editForm = this.fb.group({
    itemName: ['', Validators.required],
    title: ['', Validators.required],
    type: [EComponentType.TEXT, Validators.required],
    unit: [''],
    min: [null as number | null],
    max: [null as number | null],
    defaultValue: [''],
    groupProperty: [false],
    readOnly: [false],
    showContextMenu: [false],
    propertyItemType: [EPropertyItemType.BASE, Validators.required],
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

    const selectedType = this.form.value.type ?? EComponentType.TEXT;
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
      propertyItemType: this.form.value.propertyItemType ?? EPropertyItemType.BASE,
      selectedVisible: this.form.value.selectedVisible ?? true,
      options: selectedType === EComponentType.SELECT ? (this.form.value.options ?? []) : []
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
          type: EComponentType.TEXT,
          unit: '',
          min: null,
          max: null,
          defaultValue: '',
          groupProperty: false,
          readOnly: false,
          showContextMenu: false,
          propertyItemType: EPropertyItemType.BASE,
          selectedVisible: true,
          toValidatorClass: '',
          fromValidatorClass: '',
          options: []
        });
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
      type: item.type ?? EComponentType.TEXT,
      unit: item.unit ?? '',
      min: item.min ?? null,
      max: item.max ?? null,
      defaultValue: item.defaultValue ?? '',
      groupProperty: item.groupProperty ?? false,
      readOnly: item.readOnly ?? false,
      showContextMenu: item.showContextMenu ?? false,
      propertyItemType: item.propertyItemType ?? EPropertyItemType.BASE,
      selectedVisible: item.selectedVisible ?? true,
      toValidatorClass: item.toValidatorClass ?? '',
      fromValidatorClass: item.fromValidatorClass ?? '',
      options: this.mapSelectedOptions(item.options ?? [])
    });
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
      options: selectedType === EComponentType.SELECT ? (this.editForm.value.options ?? []) : []
    };

    this.isSaving = true;
    this.errorMessage = '';
    this.metadataApi.updatePropertyItem(this.editingItem.id ?? 0, payload).subscribe({
      next: (updated) => {
        this.propertyItems = this.propertyItems.map((existing) =>
          existing.id === updated.id ? updated : existing
        );
        this.isSaving = false;
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
}
