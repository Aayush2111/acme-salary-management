import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatSnackBar } from '@angular/material/snack-bar';

import { EmployeeService } from '../../../core/services/employee';
import { Employee } from '../../../core/models/employee.model';

@Component({
  selector: 'app-employee-form',
  standalone: true,
  imports: [
    CommonModule,
    RouterLink,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule
  ],
  templateUrl: './employee-form.html',
  styleUrl: './employee-form.scss'
})
export class EmployeeFormComponent implements OnInit {

  private readonly formBuilder = inject(FormBuilder);
  private readonly employeeService = inject(EmployeeService);
  private readonly router = inject(Router);
  private readonly route = inject(ActivatedRoute);
  private readonly snackBar = inject(MatSnackBar);

  employeeId: number | null = null;
  isEditMode = false;

  loading = false;
  submitting = false;
  errorMessage = '';

  employeeForm = this.formBuilder.group({
    employeeNumber: ['', Validators.required],
    firstName: ['', Validators.required],
    lastName: ['', Validators.required],
    email: ['', [Validators.required, Validators.email]],
    department: ['', Validators.required],
    country: ['', Validators.required],
    jobTitle: ['', Validators.required],
    salary: [null as number | null, [Validators.required, Validators.min(0.01)]],
    currency: ['', [Validators.required, Validators.pattern(/^[A-Za-z]{3}$/)]]
  });

  ngOnInit(): void {
    const idParam = this.route.snapshot.paramMap.get('id');

    if (idParam) {
      this.employeeId = Number(idParam);
      this.isEditMode = true;
      this.loadEmployee(this.employeeId);
    }
  }

  private loadEmployee(id: number): void {
    this.loading = true;
    this.errorMessage = '';

    this.employeeService.getEmployeeById(id).subscribe({
      next: (employee) => {
        this.employeeForm.patchValue(employee);
        this.loading = false;
      },
      error: () => {
        this.errorMessage = 'Unable to load employee.';
        this.loading = false;
      }
    });
  }

  onSubmit(): void {
    if (this.employeeForm.invalid) {
      this.employeeForm.markAllAsTouched();
      return;
    }

    this.submitting = true;
    this.errorMessage = '';

    const formValue = this.employeeForm.getRawValue();
    const employee: Omit<Employee, 'id'> = {
      employeeNumber: formValue.employeeNumber!,
      firstName: formValue.firstName!,
      lastName: formValue.lastName!,
      email: formValue.email!,
      department: formValue.department!,
      country: formValue.country!,
      jobTitle: formValue.jobTitle!,
      salary: formValue.salary!,
      currency: formValue.currency!.toUpperCase()
    };

    const request = this.isEditMode
      ? this.employeeService.updateEmployee(this.employeeId!, employee)
      : this.employeeService.createEmployee(employee);

    request.subscribe({
      next: () => {
        this.snackBar.open(
          `Employee ${this.isEditMode ? 'updated' : 'created'} successfully.`,
          'Close',
          { duration: 3000 }
        );
        this.router.navigate(['/employees']);
      },
      error: (error) => {
        this.errorMessage = error?.error?.message ||
          `Unable to ${this.isEditMode ? 'update' : 'create'} employee.`;
        this.submitting = false;
      }
    });
  }
}
