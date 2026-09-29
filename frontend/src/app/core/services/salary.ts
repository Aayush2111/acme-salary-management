import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { DepartmentSalary, CountrySalary } from '../models/salary.model';

@Injectable({
  providedIn: 'root'
})
export class SalaryService {

  private readonly http = inject(HttpClient);

  private readonly apiUrl = 'http://localhost:8080/api/salary';

  getSalaryByDepartment(): Observable<DepartmentSalary[]> {
    return this.http.get<DepartmentSalary[]>(`${this.apiUrl}/by-department`);
  }

  getSalaryByCountry(): Observable<CountrySalary[]> {
    return this.http.get<CountrySalary[]>(`${this.apiUrl}/by-country`);
  }
}
