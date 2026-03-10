package org.example;

import java.util.ArrayList;
import java.util.List;

/**
 * Університет:
 *  - повна назва
 *  - скорочена назва
 *  - місто
 *  - адреса
 */
public class University {
    private final String fullName;
    private final String shortName;
    private final String city;
    private final String address;
    private final List<Faculty> faculties = new ArrayList<>();

    public University(String fullName, String shortName, String city, String address) {
        this.fullName = fullName;
        this.shortName = shortName;
        this.city = city;
        this.address = address;
    }

    public String getFullName() {
        return fullName;
    }

    public String getShortName() {
        return shortName;
    }

    public String getCity() {
        return city;
    }

    public String getAddress() {
        return address;
    }

    public List<Faculty> getFaculties() {
        return faculties;
    }
}
