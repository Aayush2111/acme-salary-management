import { Routes } from '@angular/router';
import { EmployeeListComponent } from './features/employees/employee-list/employee-list';
import { AddEmployeeComponent } from './features/employees/add-employee/add-employee';

export const routes: Routes = [
  {
    path: '',
    redirectTo: 'employees',
    pathMatch: 'full'
  },
  {
    path: 'employees',
    component: EmployeeListComponent
  },
  {
    path: 'employees/new',
    component: AddEmployeeComponent
  }
];