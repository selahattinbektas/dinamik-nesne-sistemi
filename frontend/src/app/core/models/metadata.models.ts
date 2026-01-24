import { EComponentType, EEntityType, EPropertyItemType } from './enums';

export interface OptionDTO {
  value: string;
  label: string;
}

export interface Option extends OptionDTO {}

export interface PropertyItemDTO {
  type: EComponentType;
  id?: number;
  itemName: string;
  title: string;
  unit?: string;
  options: OptionDTO[];
  min?: number;
  max?: number;
  defaultValue?: string;
  groupProperty: boolean;
  readOnly: boolean;
  toValidatorClass?: string;
  fromValidatorClass?: string;
  showContextMenu: boolean;
  propertyItemType: EPropertyItemType;
  selectedVisible: boolean;
}

export interface PropertyItem extends PropertyItemDTO {
  options: Option[];
}

export interface MetaDataDTO {
  name: string;
  entityType: EEntityType;
  propertyItemList: PropertyItemDTO[];
}

export interface MetaData extends MetaDataDTO {
  propertyItemList: PropertyItem[];
}

export interface MetaDataClassificationDTO {
  id?: string;
  drawClassification: string;
  classification: string;
  metaDataName: string;
  metaDataCode: string;
  symbolCode: string;
  unitType: string;
  undeletable: boolean;
}

export interface MetaDataClassification extends MetaDataClassificationDTO {}
