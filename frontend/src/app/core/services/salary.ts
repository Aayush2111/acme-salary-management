import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { DepartmentSalary, CountrySalary, SalarySummary, SalaryDistribution } from '../models/salary.model';

@Injectable({
  providedIn: 'root'
})
export class SalaryService {

  private readonly http = inject(HttpClient);

  private readonly apiUrl = 'http://localhost:8080/api/salary';

  getSalarySummary(): Observable<SalarySummary> {
    return this.http.get<SalarySummary>(`${this.apiUrl}/summary`);
  }

  getSalaryByDepartment(): Observable<DepartmentSalary[]> {
    return this.http.get<DepartmentSalary[]>(`${this.apiUrl}/by-department`);
  }

  getSalaryByCountry(): Observable<CountrySalary[]> {
    return this.http.get<CountrySalary[]>(`${this.apiUrl}/by-country`);
  }

  getSalaryDistribution(): Observable<SalaryDistribution[]> {
    return this.http.get<SalaryDistribution[]>(`${this.apiUrl}/distribution`);
  }
}
