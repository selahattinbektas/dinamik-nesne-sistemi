import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { forkJoin } from 'rxjs';
import { MetadataApiService } from '../../../../core/api/metadata-api.service';
import { MetaData, Option } from '../../../../core/models/metadata.models';
import { EOptionsPropertyItemType } from '../../../../core/models/enums';

interface OptionWithUsage {
  option: Option;
  usageCount: number;
}

@Component({
  selector: 'app-options-page',
  templateUrl: './options-page.component.html'
})
export class OptionsPageComponent implements OnInit {
  options: OptionWithUsage[] = [];
  isLoading = false;
  isSaving = false;
  isEditDialogOpen = false;
  errorMessage = '';
  editingOption: Option | null = null;

  form = this.fb.group({
    value: ['', Validators.required],
    label: ['', Validators.required],
    optionsPropertyItemType: [null as EOptionsPropertyItemType | null, Validators.required]
  });

  editForm = this.fb.group({
    value: [{ value: '', disabled: true }, Validators.required],
    label: ['', Validators.required],
    optionsPropertyItemType: [null as EOptionsPropertyItemType | null, Validators.required]
  });

  optionsPropertyItemTypes = [
      { value: EOptionsPropertyItemType.TeamType, label: 'teamType' },
      { value: EOptionsPropertyItemType.OperationCondition, label: 'operationCondition' },
      { value: EOptionsPropertyItemType.ActiveStatus, label: 'activeStatus' },
      { value: EOptionsPropertyItemType.FreezeStatus, label: 'freezeStatus' }
    ];

  constructor(private readonly fb: FormBuilder, private readonly metadataApi: MetadataApiService) {}

  ngOnInit(): void {
    this.loadOptions();
  }

  loadOptions(): void {
    this.isLoading = true;
    this.errorMessage = '';
    forkJoin({
      options: this.metadataApi.getAllOptions(),
      metadata: this.metadataApi.getAllMetaData()
    }).subscribe({
      next: ({ options, metadata }) => {
        this.options = this.mapOptionsWithUsage(options, metadata);
        this.isLoading = false;
      },
      error: () => {
        this.errorMessage = 'Option listesi alınamadı.';
        this.isLoading = false;
      }
    });
  }

  submit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    const payload = {
      value: this.form.value.value ?? '',
      label: this.form.value.label ?? '',
      optionsPropertyItemType: this.form.value.optionsPropertyItemType ?? undefined
    };

    this.isSaving = true;
    this.errorMessage = '';
    this.metadataApi.createOption(payload).subscribe({
      next: () => {
        this.form.reset({ value: '', label: '', optionsPropertyItemType: null });
        this.loadOptions();
        this.isSaving = false;
      },
      error: () => {
        this.errorMessage = 'Option oluşturulamadı.';
        this.isSaving = false;
      }
    });
  }

  deleteOption(option: Option): void {
    const confirmed = window.confirm(`'${option.value}' kaydı silinecektir. Emin misiniz?`);
    if (!confirmed) {
      return;
    }

    this.isLoading = true;
    this.errorMessage = '';
    this.metadataApi.deleteOption(option.value).subscribe({
      next: () => {
        this.options = this.options.filter((item) => item.option.value !== option.value);
        this.isLoading = false;
      },
      error: () => {
        this.errorMessage = 'Option silinemedi.';
        this.isLoading = false;
      }
    });
  }

  openEditDialog(option: Option): void {
    this.editingOption = option;
    this.editForm.reset({
      value: option.value,
      label: option.label,
      optionsPropertyItemType: option.optionsPropertyItemType ?? null
    });
    this.isEditDialogOpen = true;
  }

  closeEditDialog(): void {
    this.isEditDialogOpen = false;
    this.editingOption = null;
  }

  updateOption(): void {
    if (!this.editingOption || this.editForm.invalid) {
      this.editForm.markAllAsTouched();
      return;
    }

    const payload = {
      value: this.editForm.value.value ?? this.editingOption.value,
      label: this.editForm.value.label ?? this.editingOption.label,
      optionsPropertyItemType: this.editForm.value.optionsPropertyItemType ?? this.editingOption.optionsPropertyItemType
    };

    this.isSaving = true;
    this.errorMessage = '';
    this.metadataApi.updateOption(this.editingOption.value, payload).subscribe({
      next: (updated) => {
        this.options = this.options.map((item) =>
          item.option.value === this.editingOption?.value ? { ...item, option: updated } : item
        );
        this.isSaving = false;
        this.closeEditDialog();
      },
      error: () => {
        this.errorMessage = 'Option güncellenemedi.';
        this.isSaving = false;
      }
    });
  }

  private mapOptionsWithUsage(options: Option[], metadata: MetaData[]): OptionWithUsage[] {
    const usageMap = new Map<string, number>();
    const itemNameMap = new Map<string, Set<string>>();
    metadata.forEach((metaData) => {
      metaData.propertyItemList?.forEach((item) => {
        item.options?.forEach((option) => {
          usageMap.set(option.value, (usageMap.get(option.value) ?? 0) + 1);
        });
      });
    });

    return options.map((option) => ({
      option,
      usageCount: usageMap.get(option.value) ?? 0
    }));
  }

  getOptionTypeLabel(option: Option): string {
      if (!option.optionsPropertyItemType) {
        return '-';
      }
      return this.optionsPropertyItemTypes.find((item) => item.value === option.optionsPropertyItemType)?.label ?? '-';
    }
}
