package org.example;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class UniversityTest {

    // Допоміжний метод, щоб не писати ці 15 параметрів у кожному тесті
    private Student createTestStudent(LocalDate birthDate, int admissionYear) {
        return new Student(
                "1", "Шевченко", "Тарас", "Григорович",
                birthDate, "taras@test.com", "+380991234567",
                "KB-12345", 2, "ІП-21", admissionYear,
                null, StudentStatus.STUDYING, null, null
        );
    }

    // Тест 1: Перевірка розрахунку віку для коректної дати
    @Test
    void testGetAgeWithValidDate() {
        LocalDate birthDate = LocalDate.now().minusYears(20);
        Student student = createTestStudent(birthDate, 2023);
        assertEquals(20, student.getAge(), "Вік має розраховуватись правильно відносно поточної дати");
    }

    // Тест 2: Перевірка віку, якщо дата народження відсутня (null)
    @Test
    void testGetAgeWithNullDate() {
        Student student = createTestStudent(null, 2023);
        assertEquals(0, student.getAge(), "Якщо дата народження null, метод getAge() має повертати 0");
    }

    // Тест 3: Перевірка розрахунку років навчання
    @Test
    void testGetYearsOfStudy() {
        int currentYear = LocalDate.now().getYear();
        int admissionYear = currentYear - 2;
        Student student = createTestStudent(LocalDate.of(2005, 1, 1), admissionYear);
        assertEquals(2, student.getYearsOfStudy(), "Кількість років навчання має розраховуватись правильно");
    }

    // Тест 4: Перевірка роботи сеттерів (зміна стану об'єкта)
    @Test
    void testSettersAndGetters() {
        Student student = createTestStudent(LocalDate.of(2005, 1, 1), 2023);
        student.setCourse(3);
        assertEquals(3, student.getCourse(), "Курс має оновитися на 3");
        student.setStatus(StudentStatus.EXPELLED);
        assertEquals(StudentStatus.EXPELLED, student.getStatus(), "Статус має оновитися на EXPELLED");
    }

    // Тест 5: Валідація некоректного курсу (більше 6)
    @Test
    void testInvalidCourseTooHigh() {
        assertFalse(UniversityService.isValidCourse(7), "Курс 7 не може бути валідним");
    }

    // Тест 6: Перевірка обробки невідомого статусу
    @Test
    void testParseStudentStatusUnknown() {
        assertNull(Main.parseStudentStatus("99"), "Невідомий статус має повертати null");
    }
}
