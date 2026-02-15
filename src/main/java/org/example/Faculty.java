package org.example;

import java.util.ArrayList;
import java.util.List;

/**
 * Факультет:
 *  - унікальний код
 *  - назва
 *  - скорочена назва
 *  - декан (посилання на викладача)
 *  - контакти
 */
public class Faculty {
    private final String code;
    private final String name;
    private final String shortName;
    private Teacher dean;
    private String contacts;
    private final List<Department> departments = new ArrayList<>();

    public Faculty(String code, String name, String shortName, Teacher dean, String contacts) {
        this.code = code;
        this.name = name;
        this.shortName = shortName;
        this.dean = dean;
        this.contacts = contacts;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getShortName() {
        return shortName;
    }

    public Teacher getDean() {
        return dean;
    }

    public void setDean(Teacher dean) {
        this.dean = dean;
    }

    public String getContacts() {
        return contacts;
    }

    public void setContacts(String contacts) {
        this.contacts = contacts;
    }

    public List<Department> getDepartments() {
        return departments;
    }
}
