package org.example;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;

public class Main {
    private static final UniversityService service = new UniversityService();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            printMenu();
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> addStudentMenu();
                case "2" -> printStudents(service.getAllStudents());
                case "3" -> updateStudentMenu();
                case "4" -> deleteStudentMenu();
                case "5" -> searchByNameMenu();
                case "6" -> searchByCourseMenu();
                case "7" -> searchByGroupMenu();
                case "8" -> reportsMenu();
                case "0" -> {
                    System.out.println("До побачення!");
                    return;
                }
                default -> System.out.println("Помилка: невірний пункт меню.");
            }
        }
    }

    private static void printMenu() {
        System.out.println("\n--- Registry: Checkpoint 1 (мін. дані сутностей) ---");
        System.out.println("Університет: " + service.getUniversity().getFullName() +
                " (" + service.getUniversity().getShortName() + ")" +
                ", " + service.getUniversity().getCity());
        System.out.println("1. Додати студента (Create)");
        System.out.println("2. Список всіх студентів (Read)");
        System.out.println("3. Оновити студента (Update)");
        System.out.println("4. Видалити студента (Delete)");
        System.out.println("5. Пошук за ПІБ");
        System.out.println("6. Пошук за курсом");
        System.out.println("7. Пошук за групою");
        System.out.println("8. Звіти (1-2 базові)");
        System.out.println("0. Вихід");
        System.out.print("Вибір: ");
    }

    // ---------------------- Create ----------------------

    private static void addStudentMenu() {
        String pib = readPib("Введіть ПІБ (3 слова): ");
        String[] parts = pib.split("\\s+");
        String lastName = parts[0];
        String firstName = parts[1];
        String middleName = parts[2];

        LocalDate birthDate = readDate("Дата народження (YYYY-MM-DD): ");
        if (birthDate == null) return;

        String email = readNonBlank("Email: ");
        String phone = readNonBlank("Телефон: ");
        String recordId = readNonBlank("Номер студ.квитка/залікової: ");

        Integer course = readCourse("Курс (1-6): ");
        if (course == null) return;

        String group = readGroup("Група (наприклад IPZ-1): ");
        if (group == null) return;

        Integer admissionYear = readYear("Рік вступу (наприклад 2025): ");
        if (admissionYear == null) return;

        StudyForm studyForm = readStudyForm("Форма навчання (1=бюджет, 2=контракт): ");
        if (studyForm == null) return;

        StudentStatus status = readStudentStatus("Статус (1=навчається, 2=академвідпустка, 3=відрахований): ");
        if (status == null) return;

        Department dept = getDefaultDepartment();

        String id = UUID.randomUUID().toString();
        service.addStudent(new Student(
                id,
                lastName, firstName, middleName,
                birthDate,
                email,
                phone,
                recordId,
                course,
                group,
                admissionYear,
                studyForm,
                status,
                dept
        ));
        System.out.println("Студента додано! id=" + id + " | кафедра: " + dept.getName());
    }

    // ---------------------- Update ----------------------

    private static void updateStudentMenu() {
        if (service.getAllStudents().isEmpty()) {
            System.out.println("Немає студентів для оновлення.");
            return;
        }

        printStudents(service.getAllStudents());
        System.out.print("Введіть id студента для оновлення: ");
        String id = scanner.nextLine().trim();

        var opt = service.findStudentById(id);
        if (opt.isEmpty()) {
            System.out.println("Студента з таким id не знайдено.");
            return;
        }

        Student s = opt.get();
        System.out.println("Залиште поле порожнім, щоб не змінювати.");

        System.out.print("Нове прізвище (зараз: " + s.getLastName() + "): ");
        String newLast = scanner.nextLine().trim();
        if (newLast.isEmpty()) newLast = null;

        System.out.print("Нове ім'я (зараз: " + s.getFirstName() + "): ");
        String newFirst = scanner.nextLine().trim();
        if (newFirst.isEmpty()) newFirst = null;

        System.out.print("Нове по батькові (зараз: " + s.getMiddleName() + "): ");
        String newMiddle = scanner.nextLine().trim();
        if (newMiddle.isEmpty()) newMiddle = null;

        Integer newCourse = null;
        System.out.print("Новий курс 1-6 (зараз: " + s.getCourse() + "): ");
        String courseStr = scanner.nextLine().trim();
        if (!courseStr.isEmpty()) {
            try {
                int c = Integer.parseInt(courseStr);
                if (!UniversityService.isValidCourse(c)) {
                    System.out.println("Помилка: курс має бути 1..6.");
                    return;
                }
                newCourse = c;
            } catch (NumberFormatException e) {
                System.out.println("Помилка: введіть число для курсу.");
                return;
            }
        }

        String newGroup = null;
        System.out.print("Нова група (зараз: " + s.getGroup() + "): ");
        String groupStr = scanner.nextLine();
        if (!groupStr.isBlank()) {
            if (!UniversityService.isValidGroup(groupStr)) {
                System.out.println("Помилка: некоректна група.");
                return;
            }
            newGroup = groupStr.trim();
        }


        Integer newAdmissionYear = null;
        System.out.print("Новий рік вступу (зараз: " + s.getAdmissionYear() + "): ");
        String yearStr = scanner.nextLine().trim();
        if (!yearStr.isEmpty()) {
            try {
                int y = Integer.parseInt(yearStr);
                if (!UniversityService.isValidYear(y)) {
                    System.out.println("Помилка: некоректний рік.");
                    return;
                }
                newAdmissionYear = y;
            } catch (NumberFormatException e) {
                System.out.println("Помилка: введіть число для року.");
                return;
            }
        }

        StudyForm newForm = null;
        System.out.print("Нова форма (1=бюджет, 2=контракт, Enter=без змін): ");
        String formStr = scanner.nextLine().trim();
        if (!formStr.isEmpty()) {
            newForm = parseStudyForm(formStr);
            if (newForm == null) {
                System.out.println("Помилка: невірна форма.");
                return;
            }
        }

        StudentStatus newStatus = null;
        System.out.print("Новий статус (1=навчається, 2=академвідпустка, 3=відрах., Enter=без змін): ");
        String stStr = scanner.nextLine().trim();
        if (!stStr.isEmpty()) {
            newStatus = parseStudentStatus(stStr);
            if (newStatus == null) {
                System.out.println("Помилка: невірний статус.");
                return;
            }
        }

        service.updateStudent(id, newLast, newFirst, newMiddle, newCourse, newGroup, newAdmissionYear, newForm, newStatus);
        System.out.println("Оновлено!");
    }

    // ---------------------- Delete ----------------------

    private static void deleteStudentMenu() {
        if (service.getAllStudents().isEmpty()) {
            System.out.println("Немає студентів для видалення.");
            return;
        }

        printStudents(service.getAllStudents());
        System.out.print("Введіть id студента для видалення: ");
        String id = scanner.nextLine().trim();

        boolean deleted = service.deleteStudentById(id);
        System.out.println(deleted ? "Видалено!" : "Студента з таким id не знайдено.");
    }

    // ---------------------- Search ----------------------

    private static void searchByNameMenu() {
        System.out.print("Введіть частину імені для пошуку: ");
        String query = scanner.nextLine();
        printStudents(service.findStudentsByName(query));
    }

    private static void searchByCourseMenu() {
        Integer course = readCourse("Введіть курс 1-6 для пошуку: ");
        if (course == null) return;
        printStudents(service.getStudentsByCourse(course));
    }

    private static void searchByGroupMenu() {
        String group = readGroup("Введіть групу для пошуку: ");
        if (group == null) return;
        printStudents(service.getStudentsByGroup(group));
    }

    // ---------------------- Reports ----------------------

    private static void reportsMenu() {
        System.out.println("\n--- Звіти ---");
        System.out.println("1) Кількість студентів по курсах:");
        var byCourse = service.reportCountByCourse();
        if (byCourse.isEmpty()) {
            System.out.println("(нема даних)");
        } else {
            byCourse.forEach((course, cnt) -> System.out.println("Курс " + course + ": " + cnt));
        }

        System.out.println("\n2) Кількість студентів по групах:");
        var byGroup = service.reportCountByGroup();
        if (byGroup.isEmpty()) {
            System.out.println("(нема даних)");
        } else {
            byGroup.forEach((group, cnt) -> System.out.println("Група " + group + ": " + cnt));
        }
    }

    // ---------------------- Helpers ----------------------

    private static void printStudents(List<Student> list) {
        if (list.isEmpty()) {
            System.out.println("Список порожній.");
            return;
        }
        for (Student s : list) {
            System.out.println("id=" + s.getId() + " | " + s);
        }
    }

    private static Department getDefaultDepartment() {
        // У нас в сервісі створена базова структура НаУКМА -> ФІ -> Кафедра ІПЗ
        return service.getUniversity().getFaculties().get(0).getDepartments().get(0);
    }

    private static String readNonBlank(String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = scanner.nextLine();
            if (!UniversityService.isBlank(s)) return s.trim();
            System.out.println("Помилка: поле не може бути порожнім.");
        }
    }

    private static String readPib(String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = scanner.nextLine();
            if (UniversityService.isBlank(s)) {
                System.out.println("Помилка: поле не може бути порожнім.");
                continue;
            }
            String[] parts = s.trim().split("\\s+");
            if (parts.length != 3) {
                System.out.println("Помилка: ПІБ має бути з 3 слів (Прізвище Ім'я По-батькові).");
                continue;
            }
            return String.join(" ", parts);
        }
    }

    private static LocalDate readDate(String prompt) {
        System.out.print(prompt);
        String s = scanner.nextLine().trim();
        if (s.isEmpty()) {
            System.out.println("Помилка: дата не може бути порожньою.");
            return null;
        }
        try {
            return LocalDate.parse(s);
        } catch (DateTimeParseException e) {
            System.out.println("Помилка: формат дати має бути YYYY-MM-DD.");
            return null;
        }
    }

    private static Integer readYear(String prompt) {
        System.out.print(prompt);
        String s = scanner.nextLine().trim();
        try {
            int y = Integer.parseInt(s);
            if (!UniversityService.isValidYear(y)) {
                System.out.println("Помилка: некоректний рік.");
                return null;
            }
            return y;
        } catch (NumberFormatException e) {
            System.out.println("Помилка: введіть число для року.");
            return null;
        }
    }

    private static StudyForm readStudyForm(String prompt) {
        System.out.print(prompt);
        String s = scanner.nextLine().trim();
        StudyForm form = parseStudyForm(s);
        if (form == null) System.out.println("Помилка: введіть 1 або 2.");
        return form;
    }

    private static StudyForm parseStudyForm(String s) {
        return switch (s) {
            case "1" -> StudyForm.BUDGET;
            case "2" -> StudyForm.CONTRACT;
            default -> null;
        };
    }

    private static StudentStatus readStudentStatus(String prompt) {
        System.out.print(prompt);
        String s = scanner.nextLine().trim();
        StudentStatus st = parseStudentStatus(s);
        if (st == null) System.out.println("Помилка: введіть 1, 2 або 3.");
        return st;
    }

    private static StudentStatus parseStudentStatus(String s) {
        return switch (s) {
            case "1" -> StudentStatus.STUDYING;
            case "2" -> StudentStatus.ACADEMIC_LEAVE;
            case "3" -> StudentStatus.EXPELLED;
            default -> null;
        };
    }

    private static Integer readCourse(String prompt) {
        System.out.print(prompt);
        String str = scanner.nextLine().trim();
        try {
            int c = Integer.parseInt(str);
            if (!UniversityService.isValidCourse(c)) {
                System.out.println("Помилка: курс має бути 1..6.");
                return null;
            }
            return c;
        } catch (NumberFormatException e) {
            System.out.println("Помилка: введіть число для курсу.");
            return null;
        }
    }

    private static String readGroup(String prompt) {
        System.out.print(prompt);
        String group = scanner.nextLine();
        if (!UniversityService.isValidGroup(group)) {
            System.out.println("Помилка: некоректна група.");
            return null;
        }
        return group.trim();
    }
}
