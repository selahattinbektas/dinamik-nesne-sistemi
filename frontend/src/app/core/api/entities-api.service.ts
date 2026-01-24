import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { EntityData, EntityDataDTO, EntityPropertyValueDTO } from '../models/entities.models';

@Injectable({
  providedIn: 'root'
})
export class EntitiesApiService {
  private readonly baseUrl = `${environment.apiBaseUrl}/api/entities`;

  constructor(private readonly http: HttpClient) {}

  createEntity(metaDataName: string, payload: EntityDataDTO): Observable<EntityData> {
    return this.http.post<EntityData>(`${this.baseUrl}/${metaDataName}`, payload);
  }

  getEntity(id: string): Observable<EntityData> {
    return this.http.get<EntityData>(`${this.baseUrl}/${id}`);
  }

  updateEntity(id: string, payload: EntityDataDTO): Observable<EntityData> {
    return this.http.put<EntityData>(`${this.baseUrl}/${id}`, payload);
  }

  deleteEntity(id: string): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }

  getEntitiesByMetaData(metaDataName: string): Observable<EntityData[]> {
    return this.http.get<EntityData[]>(`${this.baseUrl}/metadata/${metaDataName}`);
  }

  getEntities(): Observable<EntityData[]> {
    return this.http.get<EntityData[]>(this.baseUrl);
  }

  addPropertyToEntity(entityId: string, payload: EntityPropertyValueDTO): Observable<EntityData> {
    return this.addPropertiesToEntity(entityId, [payload]);
  }

  addPropertiesToEntity(entityId: string, payload: EntityPropertyValueDTO[]): Observable<EntityData> {
    return this.http.post<EntityData>(`${this.baseUrl}/${entityId}/properties`, payload);
  }

  removePropertyFromEntity(entityId: string, propertyId: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${entityId}/properties/${propertyId}`);
  }

  getPropertiesOfEntity(entityId: string): Observable<Record<string, unknown>> {
    return this.http.get<Record<string, unknown>>(`${this.baseUrl}/${entityId}/properties`);
  }
}
