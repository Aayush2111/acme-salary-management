import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

import { Employee } from '../models/employee.model';
import { PageResponse } from '../models/page-response.model';

@Injectable({
  providedIn: 'root'
})
export class EmployeeService {

  private readonly http = inject(HttpClient);

  private readonly apiUrl = 'http://localhost:8080/api/employees';

  getEmployees(
    page: number = 0,
    size: number = 10,
    search?: string,
    department?: string,
    country?: string,
    sort?: string
  ): Observable<PageResponse<Employee>> {

    let params = new HttpParams()
      .set('page', page)
      .set('size', size);

    if (search) {
      params = params.set('search', search);
    }

    if (department) {
      params = params.set('department', department);
    }

    if (country) {
      params = params.set('country', country);
    }

    if (sort) {
      params = params.set('sort', sort);
    }

    return this.http.get<PageResponse<Employee>>(
      this.apiUrl,
      { params }
    );
  }

  getEmployeeById(id: number): Observable<Employee> {
    return this.http.get<Employee>(`${this.apiUrl}/${id}`);
  }

  createEmployee(employee: Omit<Employee, 'id'>): Observable<Employee> {
    return this.http.post<Employee>(this.apiUrl, employee);
  }

  updateEmployee(
    id: number,
    employee: Omit<Employee, 'id'>
  ): Observable<Employee> {
    return this.http.put<Employee>(
      `${this.apiUrl}/${id}`,
      employee
    );
  }

  deleteEmployee(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}