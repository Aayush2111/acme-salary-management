import { ChangeDetectorRef, Component, DestroyRef, OnInit, inject } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { CommonModule } from '@angular/common';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { debounceTime, distinctUntilChanged } from 'rxjs';

import { EmployeeService } from '../../../core/services/employee';
import { SalaryService } from '../../../core/services/salary';
import { Employee } from '../../../core/models/employee.model';
import { PageResponse } from '../../../core/models/page-response.model';

@Component({
  selector: 'app-employee-list',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatButtonModule,
    MatIconModule
  ],
  templateUrl: './employee-list.html',
  styleUrl: './employee-list.scss'
})
export class EmployeeListComponent implements OnInit {

  private readonly employeeService = inject(EmployeeService);
  private readonly salaryService = inject(SalaryService);
  private readonly cdRef = inject(ChangeDetectorRef);
  private readonly destroyRef = inject(DestroyRef);

  employees: Employee[] = [];

  departments: string[] = [];
  countries: string[] = [];

  loading = false;
  errorMessage = '';

  currentPage = 0;
  pageSize = 10;
  totalElements = 0;
  totalPages = 0;

  filterForm = new FormGroup({
    search: new FormControl(''),
    department: new FormControl(''),
    country: new FormControl('')
  });

  ngOnInit(): void {
    this.loadFilterOptions();
    this.loadEmployees();

    this.filterForm.controls.search.valueChanges
      .pipe(
        debounceTime(300),
        distinctUntilChanged(),
        takeUntilDestroyed(this.destroyRef)
      )
      .subscribe(() => this.applyFilters());

    this.filterForm.controls.department.valueChanges
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe(() => this.applyFilters());

    this.filterForm.controls.country.valueChanges
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe(() => this.applyFilters());
  }

  private loadFilterOptions(): void {
    this.salaryService.getSalaryByDepartment().subscribe({
      next: (results) => {
        this.departments = results.map((result) => result.department).sort();
        this.cdRef.detectChanges();
      },
      error: (error) => console.error('DEPARTMENT OPTIONS ERROR:', error)
    });

    this.salaryService.getSalaryByCountry().subscribe({
      next: (results) => {
        this.countries = results.map((result) => result.country).sort();
        this.cdRef.detectChanges();
      },
      error: (error) => console.error('COUNTRY OPTIONS ERROR:', error)
    });
  }

  private applyFilters(): void {
    this.currentPage = 0;
    this.loadEmployees();
  }

  clearFilters(): void {
    this.filterForm.reset({ search: '', department: '', country: '' });
  }

  loadEmployees(): void {
    this.loading = true;
    this.errorMessage = '';

    const search = this.filterForm.controls.search.value;
    const department = this.filterForm.controls.department.value;
    const country = this.filterForm.controls.country.value;

    this.employeeService
      .getEmployees(
        this.currentPage,
        this.pageSize,
        search || undefined,
        department || undefined,
        country || undefined
      )
      .subscribe({
        next: (response: PageResponse<Employee>) => {
          this.employees = response.content;
          this.totalElements = response.totalElements;
          this.totalPages = response.totalPages;

          this.loading = false;
          this.cdRef.detectChanges();
        },
        error: (error) => {
          console.error('EMPLOYEE API ERROR:', error);

          this.errorMessage = 'Unable to load employees.';
          this.loading = false;
          this.cdRef.detectChanges();
        }
      });
  }

  nextPage(): void {
    if (this.currentPage < this.totalPages - 1) {
      this.currentPage++;
      this.loadEmployees();
    }
  }

  previousPage(): void {
    if (this.currentPage > 0) {
      this.currentPage--;
      this.loadEmployees();
    }
  }
}
