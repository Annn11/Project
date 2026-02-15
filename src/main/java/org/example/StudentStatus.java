package org.example;

/** Статус навчання студента. */
public enum StudentStatus {
    STUDYING("навчається"),
    ACADEMIC_LEAVE("академвідпустка"),
    EXPELLED("відрахований");

    private final String label;

    StudentStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
