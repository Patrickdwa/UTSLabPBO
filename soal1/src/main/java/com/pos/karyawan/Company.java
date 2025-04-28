package com.pos.karyawan;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Company {
    private Map<String, Employee> employees;
    private List<LogEntry> activityLog;

    public Company() {
        employees = new HashMap<>();
        activityLog = new ArrayList<>();
    }

    // AddEmployee dengan validitas --> tidak ada kesamaan ID & tidak ada gaji minus
    public boolean addEmployee(Employee employee, String user) {
        if (employees.containsKey(employee.getId())) {
            return false; // ID already exists
        }

        if (employee.getSalary() < 0) {
            return false; // Invalid salary
        }

        employees.put(employee.getId(), employee);
        logActivity(user, "Added employee: " + employee.getName() + " (ID: " + employee.getId() + ")"); //save ke log
        return true;
    }

    // Remove employee(karyawan)
    public boolean removeEmployee(String id, String user) {
        if (!employees.containsKey(id)) {
            return false; // ID doesn't exist
        }

        Employee removed = employees.remove(id);
        logActivity(user, "Removed employee: " + removed.getName() + " (ID: " + id + ")"); //save ke log
        return true;
    }

    // Update posisi employee(karyawan)
    public boolean updateEmployeePosition(String id, String newPosition, String user) {
        if (!employees.containsKey(id)) {
            return false; // ID doesn't exist
        }

        Employee employee = employees.get(id);
        employee.setPosition(newPosition);
        logActivity(user, "Updated position for " + employee.getName() + " (ID: " + id + ") to " + newPosition);
        return true;
    }

    // Update employee salary
    public boolean updateEmployeeSalary(String id, double newSalary, String user) {
        if (!employees.containsKey(id)) {
            return false; // ID doesn't exist
        }

        if (newSalary < 0) {
            return false; // Invalid salary
        }

        Employee employee = employees.get(id);
        employee.setSalary(newSalary);
        logActivity(user, "Updated salary for " + employee.getName() + " (ID: " + id + ") to " + newSalary);
        return true;
    }

    // Mendapatkan data semua employee(karyawan)
    public List<Employee> getAllEmployees() {
        return new ArrayList<>(employees.values());
    }

    // Mendapatkan data semua log
    public List<LogEntry> getAllLogs() {
        return new ArrayList<>(activityLog);
    }

    // Search employees(karyawan) dengan ID
    public Employee getEmployeeById(String id) {
        return employees.get(id);
    }

    // Search employees(karyawan) dengan name (partial match)
    public List<Employee> searchEmployeesByName(String name) {
        List<Employee> result = new ArrayList<>();
        for (Employee emp : employees.values()) {
            if (emp.getName().toLowerCase().contains(name.toLowerCase())) {
                result.add(emp);
            }
        }
        return result;
    }

    // Log activity
    private void logActivity(String user, String action) {
        activityLog.add(new LogEntry(user, action));
    }
}