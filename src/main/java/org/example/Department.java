package org.example;

import java.util.ArrayList;
import java.util.List;

/**
 * Кафедра/департамент:
 *  - унікальний код
 *  - назва
 *  - факультет (посилання)
 *  - завідувач (посилання на викладача)
 *  - кабінет/локація
 */
public class Department {
    private final String code;
    private final String name;
    private final Faculty faculty;
    private Teacher head;
    private String location;
    private final List<Person> people = new ArrayList<>();

    public Department(String code, String name, Faculty faculty, Teacher head, String location) {
        this.code = code;
        this.name = name;
        this.faculty = faculty;
        this.head = head;
        this.location = location;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public Faculty getFaculty() {
        return faculty;
    }

    public Teacher getHead() {
        return head;
    }

    public void setHead(Teacher head) {
        this.head = head;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public List<Person> getPeople() {
        return people;
    }
}
