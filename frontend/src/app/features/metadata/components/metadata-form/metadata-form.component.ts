import { Component, EventEmitter, Input, Output } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { MetaData } from '../../../../core/models/metadata.models';
import { EEntityType } from '../../../../core/models/enums';


@Component({
  selector: 'app-metadata-form',
  templateUrl: './metadata-form.component.html'
})
export class MetadataFormComponent {
  @Input({ required: true }) formGroup!: FormGroup;
  @Input({ required: true }) searchFormGroup!: FormGroup;
  @Input({ required: true }) entityTypes: EEntityType[] = [];
  @Input({ required: true }) entityTypeLabelMap!: Record<EEntityType, string>;
  @Input({ required: true }) metadataList: MetaData[] = [];
  @Input() isLoading = false;
  @Input() errorMessage = '';

  @Output() submitForm = new EventEmitter<void>();
  @Output() deleteMetaData = new EventEmitter<string>();
  @Output() editMetaData = new EventEmitter<MetaData>();
  @Output() searchMetaData = new EventEmitter<void>();
  @Output() resetSearch = new EventEmitter<void>();
  @Output() managePropertyItems = new EventEmitter<MetaData>();
}
