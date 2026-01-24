import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { UiContent, UiContentDTO } from '../models/ui-content.models';

@Injectable({
  providedIn: 'root'
})
export class UiContentApiService {
  private readonly baseUrl = `${environment.apiBaseUrl}/api/ui-contents`;

  constructor(private readonly http: HttpClient) {}

  createUiContent(payload: UiContentDTO): Observable<UiContent> {
    return this.http.post<UiContent>(this.baseUrl, payload);
  }

  getUiContent(name: string): Observable<UiContent> {
    return this.http.get<UiContent>(`${this.baseUrl}/${name}`);
  }

  updateUiContent(name: string, payload: UiContentDTO): Observable<UiContent> {
    return this.http.put<UiContent>(`${this.baseUrl}/${name}`, payload);
  }

  deleteUiContent(name: string): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${name}`);
  }

  getAllUiContents(): Observable<UiContent[]> {
    return this.http.get<UiContent[]>(this.baseUrl);
  }

  addProperty(uiContentName: string, propertyId: number): Observable<number[]> {
    return this.http.post<number[]>(`${this.baseUrl}/${uiContentName}/properties/${propertyId}`, {});
  }

  removeProperty(uiContentName: string, propertyId: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${uiContentName}/properties/${propertyId}`);
  }

  getProperties(uiContentName: string): Observable<number[]> {
    return this.http.get<number[]>(`${this.baseUrl}/${uiContentName}/properties`);
  }
}
