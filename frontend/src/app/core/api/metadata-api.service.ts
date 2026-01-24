import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import {
  MetaData,
  MetaDataClassification,
  MetaDataClassificationDTO,
  MetaDataDTO,
  Option,
  OptionDTO,
  PropertyItem,
  PropertyItemDTO
} from '../models/metadata.models';

@Injectable({
  providedIn: 'root'
})
export class MetadataApiService {
  private readonly baseUrl = `${environment.apiBaseUrl}/api/metadata`;

  constructor(private readonly http: HttpClient) {}

  createMetaData(payload: MetaDataDTO): Observable<MetaData> {
    return this.http.post<MetaData>(this.baseUrl, payload);
  }

  getMetaData(name: string): Observable<MetaData> {
    return this.http.get<MetaData>(`${this.baseUrl}/${name}`);
  }

  updateMetaData(name: string, payload: MetaDataDTO): Observable<MetaData> {
    return this.http.put<MetaData>(`${this.baseUrl}/${name}`, payload);
  }

  deleteMetaData(name: string): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${name}`);
  }

  getAllMetaData(): Observable<MetaData[]> {
    return this.http.get<MetaData[]>(this.baseUrl);
  }

  createPropertyItem(metaDataName: string, payload: PropertyItemDTO): Observable<PropertyItem> {
    return this.http.post<PropertyItem>(`${this.baseUrl}/property-items/${metaDataName}`, payload);
  }

  getPropertyItem(id: number): Observable<PropertyItem> {
    return this.http.get<PropertyItem>(`${this.baseUrl}/property-items/${id}`);
  }

  updatePropertyItem(id: number, payload: PropertyItemDTO): Observable<PropertyItem> {
    return this.http.put<PropertyItem>(`${this.baseUrl}/property-items/${id}`, payload);
  }

  deletePropertyItem(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/property-items/${id}`);
  }

  getPropertyItemsByMetaData(metaDataName: string): Observable<PropertyItem[]> {
    return this.http.get<PropertyItem[]>(`${this.baseUrl}/property-items/metadata/${metaDataName}`);
  }

  createOption(payload: OptionDTO): Observable<Option> {
    return this.http.post<Option>(`${this.baseUrl}/options`, payload);
  }

  getOption(value: string): Observable<Option> {
    return this.http.get<Option>(`${this.baseUrl}/options/${value}`);
  }

  updateOption(value: string, payload: OptionDTO): Observable<Option> {
    return this.http.put<Option>(`${this.baseUrl}/options/${value}`, payload);
  }

  deleteOption(value: string): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/options/${value}`);
  }

  getAllOptions(): Observable<Option[]> {
    return this.http.get<Option[]>(`${this.baseUrl}/options`);
  }

  addPropertyToMetaData(metaDataName: string, payload: PropertyItemDTO): Observable<PropertyItem> {
    return this.http.post<PropertyItem>(`${this.baseUrl}/${metaDataName}/properties`, payload);
  }

  removePropertyFromMetaData(metaDataName: string, propertyId: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${metaDataName}/properties/${propertyId}`);
  }

  getPropertiesOfMetaData(metaDataName: string): Observable<PropertyItem[]> {
    return this.http.get<PropertyItem[]>(`${this.baseUrl}/${metaDataName}/properties`);
  }

  createClassification(payload: MetaDataClassificationDTO): Observable<MetaDataClassification> {
    return this.http.post<MetaDataClassification>(`${this.baseUrl}/classifications`, payload);
  }

  getClassification(id: string): Observable<MetaDataClassification> {
    return this.http.get<MetaDataClassification>(`${this.baseUrl}/classifications/${id}`);
  }

  updateClassification(id: string, payload: MetaDataClassificationDTO): Observable<MetaDataClassification> {
    return this.http.put<MetaDataClassification>(`${this.baseUrl}/classifications/${id}`, payload);
  }

  getAllClassifications(): Observable<MetaDataClassification[]> {
    return this.http.get<MetaDataClassification[]>(`${this.baseUrl}/classifications`);
  }

  filterByClassification(classification: string): Observable<MetaDataClassification[]> {
    return this.http.get<MetaDataClassification[]>(`${this.baseUrl}/classifications/filter/${classification}`);
  }

  deleteClassification(id: string): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/classifications/${id}`);
  }
}
