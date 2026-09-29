import { Routes } from '@angular/router';
import { EmployeeListComponent } from './features/employees/employee-list/employee-list';
import { EmployeeFormComponent } from './features/employees/employee-form/employee-form';

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
    component: EmployeeFormComponent
  },
  {
    path: 'employees/:id/edit',
    component: EmployeeFormComponent
  }
];