package org.example;

import java.time.LocalDate;

/**
 * Студент:
 *  - ідентифікатор студента/залікова (studentRecordId)
 *  - курс (1-6)
 *  - група
 *  - рік вступу
 *  - форма навчання (бюджет/контракт)
 *  - статус (навчається/академвідпустка/відрахований)
 */
public final class Student extends Person {
    private final String studentRecordId;
    private int course;
    private String group;
    private int admissionYear;
    private StudyForm studyForm;
    private StudentStatus status;

    // Додатково: посилання на кафедру (не суперечить вимогам, допомагає ієрархії)
    private Department department;

    public Student(String id,
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
                   Department department) {
        super(id, lastName, firstName, middleName, birthDate, email, phone);
        this.studentRecordId = studentRecordId;
        this.course = course;
        this.group = group;
        this.admissionYear = admissionYear;
        this.studyForm = studyForm;
        this.status = status;
        this.department = department;
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

    @Override
    public String toString() {
        String depName = (department == null) ? "-" : department.getName();
        return getPib() +
                " | студ.квиток/залікова: " + studentRecordId +
                " | " + getEmail() +
                " | тел: " + getPhone() +
                " | нар.: " + getBirthDate() +
                " | курс: " + course +
                " | група: " + group +
                " | вступ: " + admissionYear +
                " | форма: " + (studyForm == null ? "-" : studyForm.getLabel()) +
                " | статус: " + (status == null ? "-" : status.getLabel()) +
                " | кафедра: " + depName;
    }
}
