package org.example;

public class Specialty {
    private final String code;
    private final String name;
    private final Faculty faculty;

    public Specialty(String code, String name, Faculty faculty) {
        this.code = code;
        this.name = name;
        this.faculty = faculty;
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

    @Override
    public String toString() {
        return name + " (" + code + ")";
    }
}