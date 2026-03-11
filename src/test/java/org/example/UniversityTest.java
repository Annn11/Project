package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UniversityTest {

    // Тест 1: Валідація коректного курсу (межові значення 1 та 6)
    @Test
    void testValidCourse() {
        assertTrue(UniversityService.isValidCourse(1), "Курс 1 має бути валідним");
        assertTrue(UniversityService.isValidCourse(6), "Курс 6 має бути валідним");
    }

    // Тест 2: Валідація некоректного курсу (більше 6)
    @Test
    void testInvalidCourseTooHigh() {
        assertFalse(UniversityService.isValidCourse(7), "Курс 7 не може бути валідним");
    }

    // Тест 3: Валідація некоректного курсу (менше 1)
    @Test
    void testInvalidCourseTooLow() {
        assertFalse(UniversityService.isValidCourse(0), "Курс 0 не може бути валідним");
    }

    // Тест 4: Перевірка парсингу статусів зі стрічки
    @Test
    void testParseStudentStatusKnown() {
        assertEquals(StudentStatus.STUDYING, Main.parseStudentStatus("1"));
        assertEquals(StudentStatus.EXPELLED, Main.parseStudentStatus("3"));
    }

    // Тест 5: Перевірка обробки невідомого статусу
    @Test
    void testParseStudentStatusUnknown() {
        assertNull(Main.parseStudentStatus("99"), "Невідомий статус має повертати null");
    }
}
