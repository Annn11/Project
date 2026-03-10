package org.example;

import java.time.LocalDate;
import java.time.Period;

public final class Student extends Person {
    private final String studentRecordId;
    private int course;
    private String group;
    private int admissionYear;
    private StudyForm studyForm;
    private StudentStatus status;
    private Department department;
    private Specialty specialty;

    public Student(
            String id,
            String lastName,
            String firstName,
            String middleName,
            LocalDate birthDate,
            String email,
            String phone,
            String studentRecordId,
            int course,
            String group,
            int admissionYear,
            StudyForm studyForm,
            StudentStatus status,
            Department department,
            Specialty specialty
    ) {
        super(id, lastName, firstName, middleName, birthDate, email, phone);
        this.studentRecordId = studentRecordId;
        this.course = course;
        this.group = group;
        this.admissionYear = admissionYear;
        this.studyForm = studyForm;
        this.status = status;
        this.department = department;
        this.specialty = specialty;
    }

    public String getStudentRecordId() {
        return studentRecordId;
    }

    public int getCourse() {
        return course;
    }

    public void setCourse(int course) {
        this.course = course;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public int getAdmissionYear() {
        return admissionYear;
    }

    public void setAdmissionYear(int admissionYear) {
        this.admissionYear = admissionYear;
    }

    public StudyForm getStudyForm() {
        return studyForm;
    }

    public void setStudyForm(StudyForm studyForm) {
        this.studyForm = studyForm;
    }

    public StudentStatus getStatus() {
        return status;
    }

    public void setStatus(StudentStatus status) {
        this.status = status;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Specialty getSpecialty() {
        return specialty;
    }

    public void setSpecialty(Specialty specialty) {
        this.specialty = specialty;
    }

    public int getAge() {
        if (getBirthDate() == null) {
            return 0;
        }
        return Period.between(getBirthDate(), LocalDate.now()).getYears();
    }

    public int getYearsOfStudy() {
        return LocalDate.now().getYear() - admissionYear;
    }

    @Override
    public String toString() {
        String depName = department == null ? "-" : department.getName();
        String specialtyName = specialty == null ? "-" : specialty.getName();
        String facultyName = "-";

        if (specialty != null && specialty.getFaculty() != null) {
            facultyName = specialty.getFaculty().getName();
        }

        return getPib()
                + " | студ.квиток/залікова: " + studentRecordId
                + " | " + getEmail()
                + " | тел: " + getPhone()
                + " | нар.: " + getBirthDate()
                + " | вік: " + getAge()
                + " | курс: " + course
                + " | група: " + group
                + " | вступ: " + admissionYear
                + " | форма: " + (studyForm == null ? "-" : studyForm.getLabel())
                + " | статус: " + (status == null ? "-" : status.getLabel())
                + " | кафедра: " + depName
                + " | факультет: " + facultyName
                + " | спеціальність: " + specialtyName;
    }
}