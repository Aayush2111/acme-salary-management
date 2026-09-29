export interface DepartmentSalary {
  department: string;
  employeeCount: number;
  averageSalary: number;
}

export interface CountrySalary {
  country: string;
  employeeCount: number;
  averageSalary: number;
}

export interface SalarySummary {
  totalEmployees: number;
  averageSalary: number;
  minimumSalary: number;
  maximumSalary: number;
}

export interface SalaryDistribution {
  salaryRange: string;
  employeeCount: number;
}
