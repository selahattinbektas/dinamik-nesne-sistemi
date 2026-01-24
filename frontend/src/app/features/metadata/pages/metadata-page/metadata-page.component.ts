import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { FormBuilder, Validators } from '@angular/forms';
import { MetadataApiService } from '../../../../core/api/metadata-api.service';
import { EEntityType } from '../../../../core/models/enums';
import { MetaData } from '../../../../core/models/metadata.models';

@Component({
  selector: 'app-metadata-page',
  templateUrl: './metadata-page.component.html'
})
export class MetadataPageComponent implements OnInit {
  metadataList: MetaData[] = [];
  isLoading = false;
  errorMessage = '';
  entityTypes = Object.values(EEntityType);
  isEditDialogOpen = false;
  editingMetaData: MetaData | null = null;

  form = this.fb.group({
    name: ['', Validators.required],
    entityType: [EEntityType.CUSTOM, Validators.required]
  });

  searchForm = this.fb.group({
    name: ['', [Validators.required, Validators.minLength(2)]]
  });

  editForm = this.fb.group({
    name: ['', Validators.required],
    entityType: [EEntityType.CUSTOM, Validators.required]
  });

  constructor(
    private readonly fb: FormBuilder,
    private readonly metadataApi: MetadataApiService,
    private readonly router: Router
  ) {}

  ngOnInit(): void {
    this.loadMetadata();
  }

  loadMetadata(): void {
    this.isLoading = true;
    this.errorMessage = '';
    this.metadataApi.getAllMetaData().subscribe({
      next: (data) => {
        this.metadataList = data;
        this.isLoading = false;
      },
      error: () => {
        this.errorMessage = 'Metadata listesi alınamadı.';
        this.isLoading = false;
      }
    });
  }

  deleteMetaData(name: string): void {
    const confirmed = window.confirm(`'${name}' kaydı silinecektir. Emin misiniz?`);
    if (!confirmed) {
      return;
    }

    this.isLoading = true;
    this.errorMessage = '';
    this.metadataApi.deleteMetaData(name).subscribe({
      next: () => {
        this.metadataList = this.metadataList.filter((item) => item.name !== name);
        this.isLoading = false;
      },
      error: () => {
        this.errorMessage = `'${name}' kaydı silinemedi.`;
        this.isLoading = false;
      }
    });
  }

  searchMetaData(): void {
    if (this.searchForm.invalid) {
      this.searchForm.markAllAsTouched();
      return;
    }

    const name = this.searchForm.value.name ?? '';
    this.isLoading = true;
    this.errorMessage = '';
    this.metadataApi.getMetaData(name).subscribe({
      next: (data) => {
        this.metadataList = [data];
        this.isLoading = false;
      },
      error: () => {
        this.metadataList = [];
        this.isLoading = false;
      }
    });
  }

  resetSearch(): void {
    this.searchForm.reset({ name: '' });
    this.loadMetadata();
  }

  openEditDialog(metaData: MetaData): void {
    this.editingMetaData = metaData;
    this.editForm.reset({
      name: metaData.name,
      entityType: metaData.entityType ?? EEntityType.CUSTOM
    });
    this.isEditDialogOpen = true;
  }

  closeEditDialog(): void {
    this.isEditDialogOpen = false;
    this.editingMetaData = null;
  }

  updateMetaData(): void {
    if (!this.editingMetaData) {
      return;
    }

    if (this.editForm.invalid) {
      this.editForm.markAllAsTouched();
      return;
    }

    const payload = {
      name: this.editForm.value.name ?? this.editingMetaData.name,
      entityType: this.editForm.value.entityType ?? this.editingMetaData.entityType,
      propertyItemList: this.editingMetaData.propertyItemList ?? []
    };

    this.isLoading = true;
    this.errorMessage = '';
    const originalName = this.editingMetaData.name;
    this.metadataApi.updateMetaData(originalName, payload).subscribe({
      next: (updated) => {
        const updatedList = this.metadataList.map((item) =>
          item.name === originalName ? updated : item
        );
        if (!updatedList.some((item) => item.name === updated.name)) {
          updatedList.push(updated);
        }
        this.metadataList = updatedList;
        this.isLoading = false;
        this.closeEditDialog();
      },
      error: () => {
        this.errorMessage = `'${originalName}' kaydı güncellenemedi.`;
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
      entityType: this.form.value.entityType ?? EEntityType.CUSTOM,
      propertyItemList: []
    };

    this.metadataApi.createMetaData(payload).subscribe({
      next: () => {
        this.form.reset({ name: '', entityType: EEntityType.CUSTOM });
        this.loadMetadata();
      },
      error: () => {
        this.errorMessage = 'Metadata oluşturulamadı.';
      }
    });
  }

  onManagePropertyItems(metaData: MetaData): void {
    this.router.navigate(['/metadata/property-items', metaData.name]);
  }
}
