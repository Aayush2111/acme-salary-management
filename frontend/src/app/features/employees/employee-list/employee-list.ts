import { ChangeDetectorRef, Component, DestroyRef, OnInit, inject } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatTableModule } from '@angular/material/table';
import { MatSortModule, Sort } from '@angular/material/sort';
import { MatPaginatorModule, PageEvent } from '@angular/material/paginator';
import { MatDialog } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { debounceTime, distinctUntilChanged } from 'rxjs';

import { EmployeeService } from '../../../core/services/employee';
import { SalaryService } from '../../../core/services/salary';
import { Employee } from '../../../core/models/employee.model';
import { PageResponse } from '../../../core/models/page-response.model';
import { ConfirmDialog } from '../../../shared/confirm-dialog/confirm-dialog';

@Component({
  selector: 'app-employee-list',
  standalone: true,
  imports: [
    CommonModule,
    RouterLink,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatButtonModule,
    MatIconModule,
    MatTableModule,
    MatSortModule,
    MatPaginatorModule
  ],
  templateUrl: './employee-list.html',
  styleUrl: './employee-list.scss'
})
export class EmployeeListComponent implements OnInit {

  private readonly employeeService = inject(EmployeeService);
  private readonly salaryService = inject(SalaryService);
  private readonly cdRef = inject(ChangeDetectorRef);
  private readonly destroyRef = inject(DestroyRef);
  private readonly dialog = inject(MatDialog);
  private readonly snackBar = inject(MatSnackBar);

  employees: Employee[] = [];
  displayedColumns = ['employeeNumber', 'name', 'email', 'department', 'country', 'jobTitle', 'salary', 'actions'];

  departments: string[] = [];
  countries: string[] = [];

  loading = false;
  errorMessage = '';

  currentPage = 0;
  pageSize = 10;
  pageSizeOptions = [10, 25, 50, 100];
  totalElements = 0;

  sortField = '';
  sortDirection: 'asc' | 'desc' | '' = '';

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

  onSortChange(sort: Sort): void {
    this.sortField = sort.direction ? sort.active : '';
    this.sortDirection = sort.direction;
    this.currentPage = 0;
    this.loadEmployees();
  }

  onPageChange(event: PageEvent): void {
    this.currentPage = event.pageIndex;
    this.pageSize = event.pageSize;
    this.loadEmployees();
  }

  deleteEmployee(employee: Employee): void {
    const dialogRef = this.dialog.open(ConfirmDialog, {
      data: {
        title: 'Delete employee',
        message: `Are you sure you want to delete ${employee.firstName} ${employee.lastName} (${employee.employeeNumber})? This cannot be undone.`,
        confirmLabel: 'Delete'
      }
    });

    dialogRef.afterClosed().subscribe((confirmed) => {
      if (!confirmed) {
        return;
      }

      this.employeeService.deleteEmployee(employee.id).subscribe({
        next: () => {
          this.snackBar.open('Employee deleted successfully.', 'Close', { duration: 3000 });

          if (this.employees.length === 1 && this.currentPage > 0) {
            this.currentPage -= 1;
          }

          this.loadEmployees();
        },
        error: () => {
          this.snackBar.open('Unable to delete employee.', 'Close', { duration: 3000 });
          this.cdRef.detectChanges();
        }
      });
    });
  }

  loadEmployees(): void {
    this.loading = true;
    this.errorMessage = '';

    const search = this.filterForm.controls.search.value;
    const department = this.filterForm.controls.department.value;
    const country = this.filterForm.controls.country.value;

    const sort = this.sortField && this.sortDirection
      ? `${this.sortField},${this.sortDirection}`
      : undefined;

    this.employeeService
      .getEmployees(
        this.currentPage,
        this.pageSize,
        search || undefined,
        department || undefined,
        country || undefined,
        sort
      )
      .subscribe({
        next: (response: PageResponse<Employee>) => {
          this.employees = response.content;
          this.totalElements = response.totalElements;

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
}
