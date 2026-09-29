import { ChangeDetectorRef, Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';

import { SalaryService } from '../../../core/services/salary';
import {
  CountrySalary,
  DepartmentSalary,
  SalaryDistribution,
  SalarySummary
} from '../../../core/models/salary.model';

@Component({
  imports: [CommonModule, MatCardModule],
  selector: 'app-dashboard',
  styleUrl: './dashboard.scss',
  templateUrl: './dashboard.html',
})
export class Dashboard implements OnInit {

  private readonly salaryService = inject(SalaryService);
  private readonly cdRef = inject(ChangeDetectorRef);

  summary: SalarySummary | null = null;
  byDepartment: DepartmentSalary[] = [];
  byCountry: CountrySalary[] = [];
  distribution: SalaryDistribution[] = [];

  loading = false;
  errorMessage = '';

  ngOnInit(): void {
    this.loading = true;
    this.errorMessage = '';

    this.salaryService.getSalarySummary().subscribe({
      next: (summary) => {
        this.summary = summary;
        this.cdRef.detectChanges();
      },
      error: () => this.onLoadError()
    });

    this.salaryService.getSalaryByDepartment().subscribe({
      next: (results) => {
        this.byDepartment = [...results].sort((a, b) => b.employeeCount - a.employeeCount);
        this.cdRef.detectChanges();
      },
      error: () => this.onLoadError()
    });

    this.salaryService.getSalaryByCountry().subscribe({
      next: (results) => {
        this.byCountry = [...results].sort((a, b) => b.employeeCount - a.employeeCount);
        this.cdRef.detectChanges();
      },
      error: () => this.onLoadError()
    });

    this.salaryService.getSalaryDistribution().subscribe({
      next: (results) => {
        this.distribution = results;
        this.loading = false;
        this.cdRef.detectChanges();
      },
      error: () => this.onLoadError()
    });
  }

  maxDepartmentCount(): number {
    return Math.max(1, ...this.byDepartment.map((d) => d.employeeCount));
  }

  maxCountryCount(): number {
    return Math.max(1, ...this.byCountry.map((c) => c.employeeCount));
  }

  maxDistributionCount(): number {
    return Math.max(1, ...this.distribution.map((d) => d.employeeCount));
  }

  private onLoadError(): void {
    this.errorMessage = 'Unable to load salary analytics.';
    this.loading = false;
    this.cdRef.detectChanges();
  }
}
