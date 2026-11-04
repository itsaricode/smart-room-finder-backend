package azh.bkd.demo.service;

import azh.bkd.demo.model.Department;
import azh.bkd.demo.repository.DepartmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    public DepartmentService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    // Create Department
    public String addDepartment(Department department) {

        if (departmentRepository.existsByDepartmentName(department.getDepartmentName())) {
            return "Department already exists!";
        }

        departmentRepository.save(department);

        return "Department added successfully!";
    }

    // Get All Departments
    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

}