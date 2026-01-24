import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { forkJoin } from 'rxjs';
import { EntitiesApiService } from '../../../../core/api/entities-api.service';
import { MetadataApiService } from '../../../../core/api/metadata-api.service';
import { UiContentApiService } from '../../../../core/api/ui-content-api.service';
import { EntityData } from '../../../../core/models/entities.models';
import { EComponentType, EUiContentType } from '../../../../core/models/enums';
import { MetaData, MetaDataClassification, PropertyItem } from '../../../../core/models/metadata.models';
import { UiContent } from '../../../../core/models/ui-content.models';

@Component({
  selector: 'app-entities-page',
  templateUrl: './entities-page.component.html'
})
export class EntitiesPageComponent implements OnInit {
  entities: EntityData[] = [];
  isLoading = false;
  errorMessage = '';
  metadataList: MetaData[] = [];
  propertyItems: PropertyItem[] = [];
  isClassificationDialogOpen = false;
  isUiContentDialogOpen = false;
  classificationDetail: MetaDataClassification | null = null;
  uiContentDetails: Array<{
    name: string;
    type: EUiContentType;
    properties: Array<{ name: string; value: unknown }>;
  }> = [];

  propertiesForm = this.fb.group({});

  form = this.fb.group({
    id: ['', Validators.required],
    metaDataName: ['', Validators.required],
    properties: this.propertiesForm
  });

  constructor(
    private readonly fb: FormBuilder,
    private readonly entitiesApi: EntitiesApiService,
    private readonly metadataApi: MetadataApiService,
    private readonly uiContentApi: UiContentApiService
  ) {}

  ngOnInit(): void {
    this.loadMetaData();
    this.loadEntities();
    this.form.get('metaDataName')?.valueChanges.subscribe((value) => {
      if (value) {
        this.onMetaDataChange(value);
      } else {
        this.propertyItems = [];
        this.buildPropertiesForm([]);
      }
    });
  }

  submit(): void {
    this.errorMessage = '';

    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    const properties = this.buildPropertiesPayload();

    const payload = {
      id: this.form.value.id ?? '',
      metaDataName: this.form.value.metaDataName ?? '',
      properties
    };

    this.entitiesApi.createEntity(payload.metaDataName, payload).subscribe({
      next: () => {
        this.form.reset({ id: '', metaDataName: payload.metaDataName });
        this.buildPropertiesForm(this.propertyItems);
        this.loadEntities();
      },
      error: () => {
        this.errorMessage = 'Entity oluşturulamadı.';
      }
    });
  }

  loadEntities(): void {
    this.isLoading = true;
    this.entitiesApi.getEntities().subscribe({
      next: (data) => {
        this.entities = data;
        this.isLoading = false;
      },
      error: () => {
        this.errorMessage = 'Entity listesi alınamadı.';
        this.isLoading = false;
      }
    });
  }

  onMetaDataChange(metaDataName: string): void {
    const selected = this.metadataList.find((item) => item.name === metaDataName);
    this.propertyItems = selected?.propertyItemList ?? [];
    this.buildPropertiesForm(this.propertyItems);
  }

  openClassificationDialog(entity: EntityData): void {
    this.errorMessage = '';
    const metaDataName = entity.metaDataName ?? '';
    if (!metaDataName) {
      this.errorMessage = 'Entity metadata bilgisi bulunamadı.';
      return;
    }
    this.isLoading = true;
    this.metadataApi.getAllClassifications().subscribe({
      next: (data) => {
        this.classificationDetail = data.find((item) => item.metaDataName === metaDataName) ?? null;
        if (!this.classificationDetail) {
          this.errorMessage = 'Classification bulunamadı.';
        }
        this.isClassificationDialogOpen = true;
        this.isLoading = false;
      },
      error: () => {
        this.errorMessage = 'Classification bilgisi alınamadı.';
        this.isLoading = false;
      }
    });
  }

  closeClassificationDialog(): void {
    this.isClassificationDialogOpen = false;
  }

  openUiContentDialog(entity: EntityData): void {
    this.errorMessage = '';
    const metaDataName = entity.metaDataName ?? '';
    if (!metaDataName) {
      this.errorMessage = 'Entity metadata bilgisi bulunamadı.';
      return;
    }
    this.isLoading = true;
    forkJoin({
      metaData: this.metadataApi.getMetaData(metaDataName),
      uiContents: this.uiContentApi.getAllUiContents()
    }).subscribe({
      next: ({ metaData, uiContents }) => {
        this.uiContentDetails = this.buildUiContentDetails(entity, metaData.propertyItemList ?? [], uiContents);
        this.isUiContentDialogOpen = true;
        this.isLoading = false;
      },
      error: () => {
        this.errorMessage = 'UI Content bilgisi alınamadı.';
        this.isLoading = false;
      }
    });
  }

  closeUiContentDialog(): void {
    this.isUiContentDialogOpen = false;
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

  private buildPropertiesForm(items: PropertyItem[]): void {
    const group: Record<string, unknown> = {};
    items.forEach((item) => {
      let initialValue: unknown = '';
      if (item.type === EComponentType.CHECKBOX) {
        initialValue = false;
      } else if (item.type === EComponentType.NUMBER) {
        initialValue = null;
      } else if (item.type === EComponentType.SELECT || item.type === EComponentType.RADIO) {
        initialValue = item.options?.[0]?.value ?? '';
      }
      group[item.itemName] = this.fb.control(initialValue);
    });
    this.propertiesForm = this.fb.group(group);
    this.form.setControl('properties', this.propertiesForm);
  }

  private buildPropertiesPayload(): Record<string, unknown> {
    const raw = (this.form.value.properties ?? {}) as Record<string, unknown>;
    const payload: Record<string, unknown> = {};
    this.propertyItems.forEach((item) => {
      const value = raw[item.itemName];
      if (item.type === EComponentType.NUMBER) {
        payload[item.itemName] = value === null || value === '' ? null : Number(value);
      } else if (item.type === EComponentType.CHECKBOX) {
        payload[item.itemName] = Boolean(value);
      } else {
        payload[item.itemName] = value ?? '';
      }
    });
    return payload;
  }

  private buildUiContentDetails(
    entity: EntityData,
    propertyItems: PropertyItem[],
    uiContents: UiContent[]
  ): Array<{ name: string; type: EUiContentType; properties: Array<{ name: string; value: unknown }> }> {
    const properties = entity.properties ?? {};
    const details = uiContents.map((content) => {
      const matchedItems = propertyItems.filter((item) => (content.itemIdList ?? []).includes(item.id ?? -1));
      if (!matchedItems.length) {
        return null;
      }
      return {
        name: content.name,
        type: content.type,
        properties: matchedItems.map((item) => ({
          name: item.itemName || item.title || String(item.id ?? ''),
          value: properties[item.itemName || String(item.id ?? '')]
        }))
      };
    });
    return details.filter((item) => item !== null) as Array<{
      name: string;
      type: EUiContentType;
      properties: Array<{ name: string; value: unknown }>;
    }>;
  }
}
