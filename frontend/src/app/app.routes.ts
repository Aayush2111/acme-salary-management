import { Routes } from '@angular/router';
import { EmployeeListComponent } from './features/employees/employee-list/employee-list';

export const routes: Routes = [
  {
    path: '',
    redirectTo: 'employees',
    pathMatch: 'full'
  },
  {
    path: 'employees',
    component: EmployeeListComponent
  }
];