import { Component, OnInit } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { MetadataApiService } from '../../../../core/api/metadata-api.service';
import { MetaData, MetaDataClassification } from '../../../../core/models/metadata.models';

@Component({
  selector: 'app-classifications-page',
  templateUrl: './classifications-page.component.html'
})
export class ClassificationsPageComponent implements OnInit {
  classifications: MetaDataClassification[] = [];
  metadataList: MetaData[] = [];
  isLoading = false;
  errorMessage = '';
  isEditDialogOpen = false;
  editClassificationId = '';
  isDeleteErrorDialogOpen = false;
  deleteErrorReason = '';

  form = this.fb.group({
    drawClassification: ['', Validators.required],
    classification: ['', Validators.required],
    metaDataName: ['', Validators.required],
    metaDataCode: ['', Validators.required],
    symbolCode: ['', Validators.required],
    unitType: ['', Validators.required],
    undeletable: [false]
  });

  editForm = this.fb.group({
    drawClassification: ['', Validators.required],
    classification: ['', Validators.required],
    metaDataName: ['', Validators.required],
    metaDataCode: ['', Validators.required],
    symbolCode: ['', Validators.required],
    unitType: ['', Validators.required],
    undeletable: [false]
  });

  constructor(private readonly fb: FormBuilder, private readonly metadataApi: MetadataApiService) {}

  ngOnInit(): void {
    this.loadMetaData();
    this.loadClassifications();
  }

  submit(): void {
    this.errorMessage = '';
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    this.isLoading = true;
    const payload = {
      drawClassification: this.form.value.drawClassification ?? '',
      classification: this.form.value.classification ?? '',
      metaDataName: this.form.value.metaDataName ?? '',
      metaDataCode: this.form.value.metaDataCode ?? '',
      symbolCode: this.form.value.symbolCode ?? '',
      unitType: this.form.value.unitType ?? '',
      undeletable: Boolean(this.form.value.undeletable)
    };
    this.metadataApi.createClassification(payload).subscribe({
      next: () => {
        this.form.reset({
          drawClassification: '',
          classification: '',
          metaDataName: '',
          metaDataCode: '',
          symbolCode: '',
          unitType: '',
          undeletable: false
        });
        this.loadClassifications();
      },
      error: () => {
        this.errorMessage = 'Classification oluşturulamadı.';
        this.isLoading = false;
      }
    });
  }

  openEditDialog(item: MetaDataClassification): void {
    this.editClassificationId = item.id ?? '';
    this.editForm.reset({
      drawClassification: item.drawClassification ?? '',
      classification: item.classification ?? '',
      metaDataName: item.metaDataName ?? '',
      metaDataCode: item.metaDataCode ?? '',
      symbolCode: item.symbolCode ?? '',
      unitType: item.unitType ?? '',
      undeletable: Boolean(item.undeletable)
    });
    this.isEditDialogOpen = true;
  }

  closeEditDialog(): void {
    this.isEditDialogOpen = false;
    this.editClassificationId = '';
  }

  updateClassification(): void {
    this.errorMessage = '';
    if (this.editForm.invalid || !this.editClassificationId) {
      this.editForm.markAllAsTouched();
      return;
    }
    this.isLoading = true;
    const payload = {
      drawClassification: this.editForm.value.drawClassification ?? '',
      classification: this.editForm.value.classification ?? '',
      metaDataName: this.editForm.value.metaDataName ?? '',
      metaDataCode: this.editForm.value.metaDataCode ?? '',
      symbolCode: this.editForm.value.symbolCode ?? '',
      unitType: this.editForm.value.unitType ?? '',
      undeletable: Boolean(this.editForm.value.undeletable)
    };
    this.metadataApi.updateClassification(this.editClassificationId, payload).subscribe({
      next: (updated) => {
        this.classifications = this.classifications.map((item) =>
          item.id === updated.id ? updated : item
        );
        this.closeEditDialog();
        this.isLoading = false;
      },
      error: () => {
        this.errorMessage = 'Classification güncellenemedi.';
        this.isLoading = false;
      }
    });
  }

  deleteClassification(item: MetaDataClassification): void {
    if (!item.id) {
      return;
    }
    this.isLoading = true;
    this.metadataApi.deleteClassification(item.id).subscribe({
      next: () => {
        this.classifications = this.classifications.filter((row) => row.id !== item.id);
        this.isLoading = false;
      },
      error: (error) => {
              this.deleteErrorReason = this.getDeleteErrorReason(error);
              this.isDeleteErrorDialogOpen = true;
              this.isLoading = false;
      }
    });
  }

  closeDeleteErrorDialog(): void {
      this.isDeleteErrorDialogOpen = false;
      this.deleteErrorReason = '';
    }

  private loadMetaData(): void {
    this.metadataApi.getAllMetaData().subscribe({
      next: (data) => {
        this.metadataList = data;
      },
      error: () => {
        this.errorMessage = 'Metadata listesi alınamadı.';
      }
    });
  }

  private loadClassifications(): void {
    this.metadataApi.getAllClassifications().subscribe({
      next: (data) => {
        this.classifications = data;
        this.isLoading = false;
      },
      error: () => {
        this.errorMessage = 'Classification listesi alınamadı.';
        this.isLoading = false;
      }
    });
  }

    private getDeleteErrorReason(error: unknown): string {
      if (error instanceof HttpErrorResponse) {
        const payload = error.error;
        if (typeof payload === 'string' && payload.trim()) {
          return this.formatDeleteErrorReason(payload);
        }
        if (payload && typeof payload === 'object') {
          const message = (payload as { message?: string }).message;
          if (typeof message === 'string' && message.trim()) {
            return this.formatDeleteErrorReason(message);
          }
          const errorText = (payload as { error?: string }).error;
          if (typeof errorText === 'string' && errorText.trim()) {
            return this.formatDeleteErrorReason(errorText);
          }
        }
        if (error.message) {
          return this.formatDeleteErrorReason(error.message);
        }
      }
      if (error && typeof error === 'object' && 'message' in error) {
        const message = (error as { message?: string }).message;
        if (typeof message === 'string' && message.trim()) {
          return this.formatDeleteErrorReason(message);
        }
      }
      return 'Silme işlemi başarısız.';
    }

    private formatDeleteErrorReason(message: string): string {
      const normalized = message.trim();
      if (!normalized) {
        return 'Silme işlemi başarısız.';
      }
      if (normalized.toLowerCase().includes('undeletable')) {
        return 'Bu kayıt silinemez (undeletable).';
      }
      return normalized;
    }
}
