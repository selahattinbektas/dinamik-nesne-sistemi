import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import {
  FormBuilder,
  FormControl,
  Validators
} from '@angular/forms';
import { MetadataApiService } from '../../../../core/api/metadata-api.service';
import { EEntityType } from '../../../../core/models/enums';
import { MetaData } from '../../../../core/models/metadata.models';
import { EEntityTypeLabelMap } from '../../../../core/models/enums.labels';

type MetadataFormControls = {
  name: FormControl<string>;
  entityType: FormControl<EEntityType | null>;
};

@Component({
  selector: 'app-metadata-page',
  templateUrl: './metadata-page.component.html'
})
export class MetadataPageComponent implements OnInit {
  metadataList: MetaData[] = [];
  isLoading = false;
  errorMessage = '';
  entityTypes = Object.values(EEntityType);
  entityTypeLabelMap = EEntityTypeLabelMap;
  isEditDialogOpen = false;
  editingMetaData: MetaData | null = null;
  isValidationDialogOpen = false;
  validationMessages: string[] = [];


  form = this.fb.group<MetadataFormControls>({
    name: new FormControl('', {
      nonNullable: true,
      validators: [Validators.required]
    }),
    entityType: new FormControl<EEntityType | null>(null, {
      validators: [Validators.required]
    })
  });


  editForm = this.fb.group<MetadataFormControls>({
    name: new FormControl('', {
      nonNullable: true,
      validators: [Validators.required]
    }),
    entityType: new FormControl<EEntityType | null>(null, {
      validators: [Validators.required]
    })
  });


  searchForm = this.fb.group({
    name: ['', [Validators.required, Validators.minLength(2)]]
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

    this.metadataApi.searchMetaData(name).subscribe({
      next: (data) => {
        this.metadataList = data;
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
      entityType: metaData.entityType ?? null
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

    const value = this.editForm.getRawValue();
    const originalName = this.editingMetaData.name;

    const payload = {
      name: value.name,
      entityType: value.entityType!, // required olduğu için garanti
      propertyItemList: this.editingMetaData.propertyItemList ?? []
    };

    this.isLoading = true;
    this.errorMessage = '';

    this.metadataApi.updateMetaData(originalName, payload).subscribe({
      next: (updated) => {
        this.metadataList = this.metadataList.map((item) =>
          item.name === originalName ? updated : item
        );
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
      this.validationMessages = this.getCreateValidationMessages();
      this.isValidationDialogOpen = true;
      return;
    }

    this.isValidationDialogOpen = false;
    this.validationMessages = [];
    const value = this.form.getRawValue();

    const payload = {
      name: value.name,
      entityType: value.entityType!, // required olduğu için garanti
      propertyItemList: []
    };

    this.metadataApi.createMetaData(payload).subscribe({
      next: () => {
        this.form.reset({ name: '', entityType: null });
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

  closeValidationDialog(): void {
      this.isValidationDialogOpen = false;
    }

    private getCreateValidationMessages(): string[] {
      const messages: string[] = [];
      const nameControl = this.form.controls.name;
      const entityTypeControl = this.form.controls.entityType;

      if (nameControl.errors?.['required']) {
        messages.push('Metadata adı zorunludur.');
      }

      if (entityTypeControl.errors?.['required']) {
        messages.push('Lütfen bir entity tipi seçiniz.');
      }

      return messages;
    }
}
