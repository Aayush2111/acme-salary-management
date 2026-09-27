package salary_management.specification;

import org.springframework.data.jpa.domain.Specification;
import salary_management.entity.Employee;

public final class EmployeeSpecification {

    private EmployeeSpecification() {
    }

    public static Specification<Employee> hasSearchTerm(String search) {
        return (root, query, criteriaBuilder) -> {

            String searchPattern = "%" + search.toLowerCase() + "%";

            return criteriaBuilder.or(
                    criteriaBuilder.like(
                            criteriaBuilder.lower(root.get("firstName")),
                            searchPattern
                    ),
                    criteriaBuilder.like(
                            criteriaBuilder.lower(root.get("lastName")),
                            searchPattern
                    ),
                    criteriaBuilder.like(
                            criteriaBuilder.lower(root.get("employeeNumber")),
                            searchPattern
                    ),
                    criteriaBuilder.like(
                            criteriaBuilder.lower(root.get("email")),
                            searchPattern
                    )
            );
        };
    }

    public static Specification<Employee> hasDepartment(String department) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(
                        criteriaBuilder.lower(root.get("department")),
                        department.toLowerCase()
                );
    }

    public static Specification<Employee> hasCountry(String country) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(
                        criteriaBuilder.lower(root.get("country")),
                        country.toLowerCase()
                );
    }
}