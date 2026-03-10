package org.example;

import java.time.LocalDate;

/**
 * Персона (базовий тип):
 *  - унікальний ідентифікатор
 *  - ПІБ (3 частини)
 *  - дата народження
 *  - email
 *  - телефон
 */
public abstract sealed class Person permits Student, Teacher {
    private final String id;
    private String lastName;
    private String firstName;
    private String middleName;
    private LocalDate birthDate;
    private String email;
    private String phone;

    public Person(String id,
                  String lastName,
                  String firstName,
                  String middleName,
                  LocalDate birthDate,
                  String email,
                  String phone) {
        this.id = id;
        this.lastName = lastName;
        this.firstName = firstName;
        this.middleName = middleName;
        this.birthDate = birthDate;
        this.email = email;
        this.phone = phone;
    }

    public String getId() {
        return id;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPib() {
        return (lastName + " " + firstName + " " + middleName).trim();
    }
}
