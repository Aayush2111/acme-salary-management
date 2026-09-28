import { ChangeDetectorRef, Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { EmployeeService } from '../../../core/services/employee';
import { Employee } from '../../../core/models/employee.model';
import { PageResponse } from '../../../core/models/page-response.model';

@Component({
  selector: 'app-employee-list',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './employee-list.html',
  styleUrl: './employee-list.scss'
})
export class EmployeeListComponent implements OnInit {

  private readonly employeeService = inject(EmployeeService);
  private readonly cdRef = inject(ChangeDetectorRef);

  employees: Employee[] = [];

  loading = false;
  errorMessage = '';

  currentPage = 0;
  pageSize = 10;
  totalElements = 0;
  totalPages = 0;

  ngOnInit(): void {
    this.loadEmployees();
  }

  loadEmployees(): void {
    this.loading = true;
    this.errorMessage = '';

    this.employeeService
      .getEmployees(this.currentPage, this.pageSize)
      .subscribe({
        next: (response: PageResponse<Employee>) => {

          console.log('EMPLOYEE API RESPONSE:', response);

          this.employees = response.content;
          this.totalElements = response.totalElements;
          this.totalPages = response.totalPages;

          console.log('EMPLOYEES:', this.employees);
          console.log('TOTAL:', this.totalElements);

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