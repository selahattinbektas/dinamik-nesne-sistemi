import { Component, EventEmitter, Input, Output } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { EntityData } from '../../../../core/models/entities.models';
import { MetaData, PropertyItem } from '../../../../core/models/metadata.models';

@Component({
  selector: 'app-entities-form',
  templateUrl: './entities-form.component.html'
})
export class EntitiesFormComponent {
  @Input({ required: true }) formGroup!: FormGroup;
  @Input({ required: true }) propertiesForm!: FormGroup;
  @Input({ required: true }) entities: EntityData[] = [];
  @Input({ required: true }) metadataList: MetaData[] = [];
  @Input({ required: true }) propertyItems: PropertyItem[] = [];
  @Input() isLoading = false;
  @Input() errorMessage = '';

  @Output() submitForm = new EventEmitter<void>();
  @Output() metaDataChange = new EventEmitter<string>();
  @Output() viewClassification = new EventEmitter<EntityData>();
  @Output() viewUiContent = new EventEmitter<EntityData>();
}
