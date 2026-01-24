import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { MetadataApiService } from '../../../../core/api/metadata-api.service';
import { UiContentApiService } from '../../../../core/api/ui-content-api.service';
import { EUiContentType } from '../../../../core/models/enums';
import { UiContent } from '../../../../core/models/ui-content.models';

@Component({
  selector: 'app-ui-content-page',
  templateUrl: './ui-content-page.component.html'
})
export class UiContentPageComponent implements OnInit {
  uiContents: UiContent[] = [];
  isLoading = false;
  isSaving = false;
  isEditDialogOpen = false;
  errorMessage = '';
  contentTypes = Object.values(EUiContentType);
  editingContent: UiContent | null = null;
  propertyItemOptions: { value: number; label: string }[] = [];

  form = this.fb.group({
    name: ['', Validators.required],
    cssClassName: ['', Validators.required],
    type: [EUiContentType.DEFAULT, Validators.required],
    itemIdList: [[] as number[]]
  });

  editForm = this.fb.group({
    name: [{ value: '', disabled: true }, Validators.required],
    cssClassName: ['', Validators.required],
    type: [EUiContentType.DEFAULT, Validators.required],
    itemIdList: [[] as number[]]
  });

  constructor(
    private readonly fb: FormBuilder,
    private readonly uiContentApi: UiContentApiService,
    private readonly metadataApi: MetadataApiService
  ) {}

  ngOnInit(): void {
    this.loadUiContents();
    this.loadPropertyItemOptions();
  }

  loadUiContents(): void {
    this.isLoading = true;
    this.errorMessage = '';
    this.uiContentApi.getAllUiContents().subscribe({
      next: (data) => {
        this.uiContents = data;
        this.isLoading = false;
      },
      error: () => {
        this.errorMessage = 'UI content listesi alınamadı.';
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
      name: this.form.value.name ?? '',
      cssClassName: this.form.value.cssClassName ?? '',
      type: this.form.value.type ?? EUiContentType.DEFAULT,
      itemIdList: this.form.value.itemIdList ?? []
    };

    this.uiContentApi.createUiContent(payload).subscribe({
      next: () => {
        this.form.reset({ name: '', cssClassName: '', type: EUiContentType.DEFAULT, itemIdList: [] });
        this.loadUiContents();
      },
      error: () => {
        this.errorMessage = 'UI content oluşturulamadı.';
      }
    });
  }

  deleteUiContent(content: UiContent): void {
    const confirmed = window.confirm(`'${content.name}' kaydı silinecektir. Emin misiniz?`);
    if (!confirmed) {
      return;
    }

    this.isLoading = true;
    this.errorMessage = '';
    this.uiContentApi.deleteUiContent(content.name).subscribe({
      next: () => {
        this.uiContents = this.uiContents.filter((item) => item.name !== content.name);
        this.isLoading = false;
      },
      error: () => {
        this.errorMessage = 'UI content silinemedi.';
        this.isLoading = false;
      }
    });
  }

  openEditDialog(content: UiContent): void {
    this.editingContent = content;
    this.editForm.reset({
      name: content.name,
      cssClassName: content.cssClassName,
      type: content.type ?? EUiContentType.DEFAULT,
      itemIdList: content.itemIdList ?? []
    });
    this.isEditDialogOpen = true;
  }

  closeEditDialog(): void {
    this.isEditDialogOpen = false;
    this.editingContent = null;
  }

  updateUiContent(): void {
    if (!this.editingContent || this.editForm.invalid) {
      this.editForm.markAllAsTouched();
      return;
    }

    const payload = {
      name: this.editingContent.name,
      cssClassName: this.editForm.value.cssClassName ?? this.editingContent.cssClassName,
      type: this.editForm.value.type ?? this.editingContent.type,
      itemIdList: this.editForm.value.itemIdList ?? this.editingContent.itemIdList ?? []
    };

    this.isSaving = true;
    this.errorMessage = '';
    this.uiContentApi.updateUiContent(this.editingContent.name, payload).subscribe({
      next: (updated) => {
        this.uiContents = this.uiContents.map((item) => (item.name === updated.name ? updated : item));
        this.isSaving = false;
        this.closeEditDialog();
      },
      error: () => {
        this.errorMessage = 'UI content güncellenemedi.';
        this.isSaving = false;
      }
    });
  }

  loadPropertyItemOptions(): void {
    this.metadataApi.getAllMetaData().subscribe({
      next: (items) => {
        const options = new Map<number, string>();
        items.forEach((metaData) => {
          metaData.propertyItemList?.forEach((item) => {
            if (item.id !== undefined) {
              options.set(item.id, `${item.id} - ${item.itemName}`);
            }
          });
        });
        this.propertyItemOptions = Array.from(options.entries()).map(([value, label]) => ({
          value,
          label
        }));
      },
      error: () => {
        this.errorMessage = 'Property item listesi alınamadı.';
      }
    });
  }
}
