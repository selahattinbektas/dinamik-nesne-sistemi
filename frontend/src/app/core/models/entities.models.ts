export interface EntityDataDTO {
  id: string;
  metaDataName: string;
  properties: Record<string, unknown>;
}

export interface EntityData extends EntityDataDTO {
}

export interface EntityPropertyValueDTO {
  propertyId: number;
  value: unknown;
}
