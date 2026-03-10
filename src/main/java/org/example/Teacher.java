package org.example;

import java.time.LocalDate;

/**
 * Викладач:
 *  - посада
 *  - науковий ступінь
 *  - вчене звання
 *  - дата прийняття на роботу
 *  - ставка/навантаження
 */
public final class Teacher extends Person {
    private String position;
    private String scientificDegree;
    private String academicTitle;
    private LocalDate hireDate;
    private double workload;

    // Додатково: кафедра (посилання)
    private Department department;

    public Teacher(String id,
                   String lastName,
                   String firstName,
                   String middleName,
                   LocalDate birthDate,
                   String email,
                   String phone,
                   String position,
                   String scientificDegree,
                   String academicTitle,
                   LocalDate hireDate,
                   double workload,
                   Department department) {
        super(id, lastName, firstName, middleName, birthDate, email, phone);
        this.position = position;
        this.scientificDegree = scientificDegree;
        this.academicTitle = academicTitle;
        this.hireDate = hireDate;
        this.workload = workload;
        this.department = department;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getScientificDegree() {
        return scientificDegree;
    }

    public void setScientificDegree(String scientificDegree) {
        this.scientificDegree = scientificDegree;
    }

    public String getAcademicTitle() {
        return academicTitle;
    }

    public void setAcademicTitle(String academicTitle) {
        this.academicTitle = academicTitle;
    }

    public LocalDate getHireDate() {
        return hireDate;
    }

    public void setHireDate(LocalDate hireDate) {
        this.hireDate = hireDate;
    }

    public double getWorkload() {
        return workload;
    }

    public void setWorkload(double workload) {
        this.workload = workload;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    @Override
    public String toString() {
        String dep = (department == null) ? "-" : department.getName();
        return getPib() +
                " | " + position +
                " | ступінь: " + (scientificDegree == null ? "-" : scientificDegree) +
                " | звання: " + (academicTitle == null ? "-" : academicTitle) +
                " | прийнятий: " + hireDate +
                " | ставка: " + workload +
                " | кафедра: " + dep;
    }
}
