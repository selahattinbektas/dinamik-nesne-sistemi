import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { EntitiesApiService } from '../../../../core/api/entities-api.service';
import { MetadataApiService } from '../../../../core/api/metadata-api.service';
import { EntityData, EntityPropertyValueDTO } from '../../../../core/models/entities.models';
import { EComponentType } from '../../../../core/models/enums';
import { PropertyItem } from '../../../../core/models/metadata.models';

@Component({
  selector: 'app-entity-properties-page',
  templateUrl: './entity-properties-page.component.html'
})
export class EntityPropertiesPageComponent implements OnInit {
  entityId = '';
  entity: EntityData | null = null;
  metaDataName = '';
  propertyItems: PropertyItem[] = [];
  availablePropertyItems: PropertyItem[] = [];
  assignedProperties: Array<{ item: PropertyItem; value: unknown }> = [];
  propertiesForm: FormGroup = this.fb.group({});
  editForm = this.fb.group({
    value: this.fb.control<unknown>('')
  });
  isEditDialogOpen = false;
  editPropertyItem: PropertyItem | null = null;
  isLoading = false;
  errorMessage = '';

  constructor(
    private readonly fb: FormBuilder,
    private readonly route: ActivatedRoute,
    private readonly router: Router,
    private readonly entitiesApi: EntitiesApiService,
    private readonly metadataApi: MetadataApiService
  ) {}

  ngOnInit(): void {
    this.entityId = this.route.snapshot.paramMap.get('id') ?? '';
    if (!this.entityId) {
      this.errorMessage = 'Entity bulunamadı.';
      return;
    }
    this.loadEntity();
  }

  submitProperties(): void {
    this.errorMessage = '';
    const payload = this.availablePropertyItems
      .filter((item) => item.id !== undefined && item.id !== null)
      .map((item) => ({
        item,
        value: this.normalizeValue(item, this.propertiesForm.get(this.getPropertyKey(item))?.value)
      }))
      .filter(({ item, value }) => !this.isEmptyValue(item, value))
      .map(({ item, value }) => ({
        propertyId: item.id as number,
        value
      }));

    if (!payload.length) {
      return;
    }

    this.isLoading = true;
    this.entitiesApi.addPropertiesToEntity(this.entityId, payload).subscribe({
      next: () => {
        this.refreshEntity();
      },
      error: () => {
        this.errorMessage = 'Property eklenemedi.';
        this.isLoading = false;
      }
    });
  }

  openEditDialog(item: PropertyItem, value: unknown): void {
    this.editPropertyItem = item;
    this.editForm.reset({ value });
    this.isEditDialogOpen = true;
  }

  closeEditDialog(): void {
    this.isEditDialogOpen = false;
    this.editPropertyItem = null;
  }

  updateProperty(): void {
    if (!this.editPropertyItem?.id) {
      return;
    }

    this.isLoading = true;
    const payload: EntityPropertyValueDTO = {
      propertyId: this.editPropertyItem.id,
      value: this.normalizeValue(this.editPropertyItem, this.editForm.value.value)
    };
    this.entitiesApi.addPropertiesToEntity(this.entityId, [payload]).subscribe({
      next: () => {
        this.closeEditDialog();
        this.refreshEntity();
      },
      error: () => {
        this.errorMessage = 'Property güncellenemedi.';
        this.isLoading = false;
      }
    });
  }

  deleteProperty(item: PropertyItem): void {
    if (!item.id) {
      return;
    }
    this.isLoading = true;
    this.entitiesApi.removePropertyFromEntity(this.entityId, item.id).subscribe({
      next: () => {
        this.refreshEntity();
      },
      error: () => {
        this.errorMessage = 'Property silinemedi.';
        this.isLoading = false;
      }
    });
  }

  goBack(): void {
    this.router.navigate(['/entities']);
  }

  private loadEntity(): void {
    this.isLoading = true;
    this.entitiesApi.getEntity(this.entityId).subscribe({
      next: (entity) => {
        this.entity = entity;
        this.metaDataName = entity.metaDataName ?? '';
        if (!this.metaDataName) {
          this.errorMessage = 'Entity metadata bilgisi bulunamadı.';
          this.isLoading = false;
          return;
        }
        this.loadPropertyItems();
      },
      error: () => {
        this.errorMessage = 'Entity bilgisi alınamadı.';
        this.isLoading = false;
      }
    });
  }

  private loadPropertyItems(): void {
    this.metadataApi.getPropertiesOfMetaData(this.metaDataName).subscribe({
      next: (items) => {
        this.propertyItems = items;
        this.syncProperties();
        this.isLoading = false;
      },
      error: () => {
        this.errorMessage = 'Metadata property listesi alınamadı.';
        this.isLoading = false;
      }
    });
  }

  private refreshEntity(): void {
    this.entitiesApi.getEntity(this.entityId).subscribe({
      next: (entity) => {
        this.entity = entity;
        this.syncProperties();
        this.isLoading = false;
      },
      error: () => {
        this.errorMessage = 'Entity bilgisi alınamadı.';
        this.isLoading = false;
      }
    });
  }

  private syncProperties(): void {
    const properties = this.entity?.properties ?? {};
    this.assignedProperties = this.propertyItems
      .filter((item) => this.hasProperty(properties, item))
      .map((item) => ({
        item,
        value: properties[this.getPropertyKey(item)]
      }));
    this.availablePropertyItems = this.propertyItems.filter((item) => !this.hasProperty(properties, item));
    this.buildPropertiesForm(this.availablePropertyItems);
  }

  private buildPropertiesForm(items: PropertyItem[]): void {
    const group: Record<string, unknown> = {};
    items.forEach((item) => {
      const key = this.getPropertyKey(item);
      let initialValue: unknown = '';
      if (item.type === EComponentType.CHECKBOX) {
        initialValue = false;
      } else if (item.type === EComponentType.NUMBER) {
        initialValue = null;
      } else if (item.type === EComponentType.SELECT || item.type === EComponentType.RADIO) {
        initialValue = '';
      }
      group[key] = this.fb.control(initialValue);
    });
    this.propertiesForm = this.fb.group(group);
  }

  private normalizeValue(item: PropertyItem, value: unknown): unknown {
    if (item.type === EComponentType.NUMBER) {
      return value === null || value === '' ? null : Number(value);
    }
    if (item.type === EComponentType.CHECKBOX) {
      return Boolean(value);
    }
    return value ?? '';
  }

  private isEmptyValue(item: PropertyItem, value: unknown): boolean {
    if (item.type === EComponentType.CHECKBOX) {
      return value !== true;
    }
    return value === null || value === undefined || value === '';
  }

  getPropertyKey(item: PropertyItem): string {
    return item.itemName || String(item.id ?? '');
  }

  private hasProperty(properties: Record<string, unknown>, item: PropertyItem): boolean {
    const key = this.getPropertyKey(item);
    return Object.prototype.hasOwnProperty.call(properties, key);
  }
}
