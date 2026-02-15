package org.example;

/** Форма навчання студента. */
public enum StudyForm {
    BUDGET("бюджет"),
    CONTRACT("контракт");

    private final String label;

    StudyForm(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
