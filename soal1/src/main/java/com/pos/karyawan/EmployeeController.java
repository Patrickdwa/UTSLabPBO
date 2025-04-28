package com.pos.karyawan;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.geometry.Insets;

public class EmployeeController {
    @FXML private TableView<Employee> employeeTable;
    @FXML private TableView<LogEntry> logTable;
    @FXML private Button addEmployeeBtn;
    @FXML private TextField searchField;
    @FXML private Button searchBtn;

    private Company company;
    private ObservableList<Employee> employeeList;
    private ObservableList<LogEntry> logList;

    public void initialize() {
        // Inisialisasi
        company = new Company();

        // 3 Sampel
        company.addEmployee(new Employee("EMP001", "Andre Smite", "Chief Executive Officer", 645016), "System");
        company.addEmployee(new Employee("EMP002", "Simon Rousseau", "Director", 157000), "System");
        company.addEmployee(new Employee("EMP003", "Klein Moretti", "Designer", 120000), "System");

        // Set up tabel employee(karyawan)
        employeeList = FXCollections.observableArrayList(company.getAllEmployees());
        employeeTable.setItems(employeeList);

        // Menambah tombol action tiap baris
        TableColumn<Employee, String> actionsCol = (TableColumn<Employee, String>) employeeTable.getColumns().get(4);
        actionsCol.setCellFactory(param -> new TableCell<>() {
            private final Button editBtn = new Button("Edit");
            private final Button deleteBtn = new Button("Delete");

            {
                editBtn.setOnAction(event -> {
                    Employee employee = getTableView().getItems().get(getIndex());
                    showEditDialog(employee);
                });

                deleteBtn.setOnAction(event -> {
                    Employee employee = getTableView().getItems().get(getIndex());
                    if (company.removeEmployee(employee.getId(), "Admin")) {
                        refreshTables();
                    } else {
                        showAlert("Error", "Failed to delete employee");
                    }
                });
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox buttons = new HBox(5, editBtn, deleteBtn);
                    setGraphic(buttons);
                }
            }
        });

        // Set up log table
        logList = FXCollections.observableArrayList(company.getAllLogs());
        logTable.setItems(logList);

        // Add employee button action
        addEmployeeBtn.setOnAction(event -> showAddDialog());

        // Search button action
        searchBtn.setOnAction(event -> {
            String searchTerm = searchField.getText().trim();
            if (!searchTerm.isEmpty()) {
                employeeList.setAll(company.searchEmployeesByName(searchTerm));
            } else {
                employeeList.setAll(company.getAllEmployees());
            }
        });
    }

    private void showAddDialog() {
        // Create a dialog
        Dialog<Employee> dialog = new Dialog<>();
        dialog.setTitle("Add New Employee");
        dialog.setHeaderText("Enter employee details");

        // Set the button types
        ButtonType addButton = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButton, ButtonType.CANCEL);

        // Create the form
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField idField = new TextField();
        idField.setPromptText("ID");
        TextField nameField = new TextField();
        nameField.setPromptText("Name");
        TextField positionField = new TextField();
        positionField.setPromptText("Position");
        TextField salaryField = new TextField();
        salaryField.setPromptText("Salary");

        grid.add(new Label("ID:"), 0, 0);
        grid.add(idField, 1, 0);
        grid.add(new Label("Name:"), 0, 1);
        grid.add(nameField, 1, 1);
        grid.add(new Label("Position:"), 0, 2);
        grid.add(positionField, 1, 2);
        grid.add(new Label("Salary:"), 0, 3);
        grid.add(salaryField, 1, 3);

        dialog.getDialogPane().setContent(grid);

        // Menambah karyawan ketika tombol sudah di-click
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButton) {
                try {
                    String id = idField.getText();
                    String name = nameField.getText();
                    String position = positionField.getText();
                    double salary = Double.parseDouble(salaryField.getText());

                    return new Employee(id, name, position, salary);
                } catch (NumberFormatException e) {
                    showAlert("Error", "Please enter a valid salary");
                    return null;
                }
            }
            return null;
        });

        // Menunjukan dialog and memproses hasil
        dialog.showAndWait().ifPresent(employee -> {
            if (company.addEmployee(employee, "Admin")) {
                refreshTables();
            } else {
                showAlert("Error", "Failed to add employee (ID might already exist or salary is invalid)");
            }
        });
    }

    private void showEditDialog(Employee employee) {
        // Membuat dialog
        Dialog<Employee> dialog = new Dialog<>();
        dialog.setTitle("Edit Employee");
        dialog.setHeaderText("Edit employee details");

        // Menentukan Tipe button
        ButtonType saveButton = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButton, ButtonType.CANCEL);

        // Membuat form
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField idField = new TextField(employee.getId());
        idField.setDisable(true);
        TextField nameField = new TextField(employee.getName());
        TextField positionField = new TextField(employee.getPosition());
        TextField salaryField = new TextField(String.valueOf(employee.getSalary()));

        grid.add(new Label("ID:"), 0, 0);
        grid.add(idField, 1, 0);
        grid.add(new Label("Name:"), 0, 1);
        grid.add(nameField, 1, 1);
        grid.add(new Label("Position:"), 0, 2);
        grid.add(positionField, 1, 2);
        grid.add(new Label("Salary:"), 0, 3);
        grid.add(salaryField, 1, 3);

        dialog.getDialogPane().setContent(grid);

        // Save Button Di-CLICK --> Mennjukan hasil yang diupdate
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButton) {
                try {
                    String name = nameField.getText();
                    String position = positionField.getText();
                    double salary = Double.parseDouble(salaryField.getText());

                    employee.setName(name);
                    employee.setPosition(position);
                    employee.setSalary(salary);

                    return employee;
                } catch (NumberFormatException e) {
                    showAlert("Error", "Please enter a valid salary");
                    return null;
                }
            }
            return null;
        });

        //Menunjukan dialog dan memproses hasil
        dialog.showAndWait().ifPresent(updatedEmployee -> {
            if (company.updateEmployeeSalary(updatedEmployee.getId(), updatedEmployee.getSalary(), "Admin") &&
                    company.updateEmployeePosition(updatedEmployee.getId(), updatedEmployee.getPosition(), "Admin")) {
                refreshTables();
            } else {
                showAlert("Error", "Failed to update employee");
            }
        });
    }

    private void refreshTables() {
        employeeList.setAll(company.getAllEmployees());
        logList.setAll(company.getAllLogs());
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}