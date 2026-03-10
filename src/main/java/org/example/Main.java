package org.example;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;
import java.util.UUID;

public class Main {
    private static final UniversityService service = new UniversityService();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            printMenu();

            switch (scanner.nextLine().trim()) {
                case "1":
                    addStudentMenu();
                    break;
                case "2":
                    printStudents(service.getAllStudents());
                    break;
                case "3":
                    updateStudentMenu();
                    break;
                case "4":
                    deleteStudentMenu();
                    break;
                case "5":
                    searchByNameMenu();
                    break;
                case "6":
                    searchByCourseMenu();
                    break;
                case "7":
                    searchByGroupMenu();
                    break;
                case "8":
                    reportsMenu();
                    break;
                case "9":
                    showUniversityStructure();
                    break;
                case "0":
                    System.out.println("До побачення!");
                    return;
                default:
                    System.out.println("Помилка: невірний пункт меню.");
            }
        }
    }

    private static void printMenu() {
        System.out.println("\n--- Registry: Checkpoint 2 ---");
        System.out.println("Університет: " + service.getUniversity().getFullName()
                + " (" + service.getUniversity().getShortName() + "), "
                + service.getUniversity().getCity());
        System.out.println("1. Додати студента");
        System.out.println("2. Список усіх студентів");
        System.out.println("3. Оновити студента");
        System.out.println("4. Видалити студента");
        System.out.println("5. Пошук за ПІБ");
        System.out.println("6. Пошук за курсом");
        System.out.println("7. Пошук за групою");
        System.out.println("8. Звіти");
        System.out.println("9. Показати факультети / кафедри / спеціальності");
        System.out.println("0. Вихід");
        System.out.print("Вибір: ");
    }

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

        Faculty faculty = readFaculty();
        if (faculty == null) return;

        Specialty specialty = readSpecialty(faculty);
        if (specialty == null) return;

        Department dept = getDefaultDepartmentByFaculty(faculty);
        if (dept == null) {
            System.out.println("Помилка: для цього факультету не знайдено кафедру.");
            return;
        }

        String id = UUID.randomUUID().toString();

        Student student = new Student(
                id,
                lastName,
                firstName,
                middleName,
                birthDate,
                email,
                phone,
                recordId,
                course,
                group,
                admissionYear,
                studyForm,
                status,
                dept,
                specialty
        );

        service.addStudent(student);

        System.out.println("Студента додано!");
        System.out.println("id=" + id);
        System.out.println("Факультет: " + faculty.getName());
        System.out.println("Кафедра: " + dept.getName());
        System.out.println("Спеціальність: " + specialty.getName());
    }

    private static void updateStudentMenu() {
        if (service.getAllStudents().isEmpty()) {
            System.out.println("Немає студентів для оновлення.");
            return;
        }

        printStudents(service.getAllStudents());
        System.out.print("Введіть id студента для оновлення: ");
        String id = scanner.nextLine().trim();

        Optional<Student> opt = service.findStudentById(id);
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
        String groupStr = scanner.nextLine().trim();
        if (!groupStr.isEmpty()) {
            if (!UniversityService.isValidGroup(groupStr)) {
                System.out.println("Помилка: некоректна група.");
                return;
            }
            newGroup = groupStr;
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
        System.out.print("Новий статус (1=навчається, 2=академвідпустка, 3=відрахований, Enter=без змін): ");
        String stStr = scanner.nextLine().trim();
        if (!stStr.isEmpty()) {
            newStatus = parseStudentStatus(stStr);
            if (newStatus == null) {
                System.out.println("Помилка: невірний статус.");
                return;
            }
        }

        boolean updated = service.updateStudent(
                id,
                newLast,
                newFirst,
                newMiddle,
                newCourse,
                newGroup,
                newAdmissionYear,
                newForm,
                newStatus
        );

        if (!updated) {
            System.out.println("Не вдалося оновити студента.");
            return;
        }

        System.out.print("Змінити спеціальність? (y/n): ");
        String changeSpecialty = scanner.nextLine().trim();

        if (changeSpecialty.equalsIgnoreCase("y")) {
            Faculty faculty = readFaculty();
            if (faculty == null) return;

            Specialty specialty = readSpecialty(faculty);
            if (specialty == null) return;

            Department dept = getDefaultDepartmentByFaculty(faculty);
            if (dept == null) {
                System.out.println("Помилка: для цього факультету не знайдено кафедру.");
                return;
            }

            s.setSpecialty(specialty);
            s.setDepartment(dept);

            System.out.println("Спеціальність і кафедру оновлено.");
        }

        System.out.println("Оновлено!");
    }

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

    private static void searchByNameMenu() {
        System.out.print("Введіть частину ПІБ для пошуку: ");
        String query = scanner.nextLine();
        printStudents(service.findStudentsByName(query));
    }

    private static void searchByCourseMenu() {
        Integer course = readCourse("Введіть курс 1-6 для пошуку: ");
        if (course != null) {
            printStudents(service.getStudentsByCourse(course));
        }
    }

    private static void searchByGroupMenu() {
        String group = readGroup("Введіть групу для пошуку: ");
        if (group != null) {
            printStudents(service.getStudentsByGroup(group));
        }
    }

    private static void reportsMenu() {
        System.out.println("\n--- Звіти ---");

        System.out.println("1) Кількість студентів по курсах:");
        Map<Integer, Long> byCourse = service.reportCountByCourse();
        if (byCourse.isEmpty()) {
            System.out.println("(нема даних)");
        } else {
            byCourse.forEach((course, cnt) ->
                    System.out.println("Курс " + course + ": " + cnt));
        }

        System.out.println("\n2) Кількість студентів по групах:");
        Map<String, Long> byGroup = service.reportCountByGroup();
        if (byGroup.isEmpty()) {
            System.out.println("(нема даних)");
        } else {
            byGroup.forEach((group, cnt) ->
                    System.out.println("Група " + group + ": " + cnt));
        }

        System.out.println("\n3) Кількість студентів по факультетах:");
        List<Student> students = service.getAllStudents();
        if (students.isEmpty()) {
            System.out.println("(нема даних)");
        } else {
            students.stream()
                    .filter(s -> s.getSpecialty() != null && s.getSpecialty().getFaculty() != null)
                    .collect(java.util.stream.Collectors.groupingBy(
                            s -> s.getSpecialty().getFaculty().getName(),
                            java.util.TreeMap::new,
                            java.util.stream.Collectors.counting()
                    ))
                    .forEach((faculty, cnt) -> System.out.println(faculty + ": " + cnt));
        }

        System.out.println("\n4) Кількість студентів по спеціальностях:");
        if (students.isEmpty()) {
            System.out.println("(нема даних)");
        } else {
            students.stream()
                    .filter(s -> s.getSpecialty() != null)
                    .collect(java.util.stream.Collectors.groupingBy(
                            s -> s.getSpecialty().getName(),
                            java.util.TreeMap::new,
                            java.util.stream.Collectors.counting()
                    ))
                    .forEach((specialty, cnt) -> System.out.println(specialty + ": " + cnt));
        }
    }

    private static void showUniversityStructure() {
        System.out.println("\n--- Структура університету ---");

        List<Faculty> faculties = service.getUniversity().getFaculties();
        if (faculties.isEmpty()) {
            System.out.println("Факультети відсутні.");
            return;
        }

        for (int i = 0; i < faculties.size(); i++) {
            Faculty faculty = faculties.get(i);
            System.out.println((i + 1) + ". " + faculty.getName() + " (" + faculty.getShortName() + ")");

            System.out.println("   Кафедри:");
            if (faculty.getDepartments().isEmpty()) {
                System.out.println("   - немає");
            } else {
                for (Department dept : faculty.getDepartments()) {
                    System.out.println("   - " + dept.getName());
                }
            }

            System.out.println("   Спеціальності:");
            if (faculty.getSpecialties().isEmpty()) {
                System.out.println("   - немає");
            } else {
                for (Specialty specialty : faculty.getSpecialties()) {
                    System.out.println("   - " + specialty.getName() + " (" + specialty.getCode() + ")");
                }
            }

            System.out.println();
        }
    }

    private static void printStudents(List<Student> list) {
        if (list.isEmpty()) {
            System.out.println("Список порожній.");
            return;
        }

        for (Student s : list) {
            System.out.println("id=" + s.getId() + " | " + s);
        }
    }

    private static Faculty readFaculty() {
        List<Faculty> faculties = service.getUniversity().getFaculties();

        if (faculties.isEmpty()) {
            System.out.println("Помилка: факультети відсутні.");
            return null;
        }

        System.out.println("\nОберіть факультет:");
        for (int i = 0; i < faculties.size(); i++) {
            Faculty faculty = faculties.get(i);
            System.out.println((i + 1) + ". " + faculty.getName() + " (" + faculty.getShortName() + ")");
        }

        System.out.print("Ваш вибір: ");
        String input = scanner.nextLine().trim();

        try {
            int index = Integer.parseInt(input);
            if (index < 1 || index > faculties.size()) {
                System.out.println("Помилка: неправильний номер факультету.");
                return null;
            }
            return faculties.get(index - 1);
        } catch (NumberFormatException e) {
            System.out.println("Помилка: введіть номер факультету.");
            return null;
        }
    }

    private static Specialty readSpecialty(Faculty faculty) {
        List<Specialty> specialties = faculty.getSpecialties();

        if (specialties.isEmpty()) {
            System.out.println("Помилка: на факультеті немає спеціальностей.");
            return null;
        }

        System.out.println("\nОберіть спеціальність:");
        for (int i = 0; i < specialties.size(); i++) {
            Specialty specialty = specialties.get(i);
            System.out.println((i + 1) + ". " + specialty.getName() + " (" + specialty.getCode() + ")");
        }

        System.out.print("Ваш вибір: ");
        String input = scanner.nextLine().trim();

        try {
            int index = Integer.parseInt(input);
            if (index < 1 || index > specialties.size()) {
                System.out.println("Помилка: неправильний номер спеціальності.");
                return null;
            }
            return specialties.get(index - 1);
        } catch (NumberFormatException e) {
            System.out.println("Помилка: введіть номер спеціальності.");
            return null;
        }
    }

    private static Department getDefaultDepartmentByFaculty(Faculty faculty) {
        if (faculty == null || faculty.getDepartments().isEmpty()) {
            return null;
        }
        return faculty.getDepartments().get(0);
    }

    private static String readNonBlank(String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = scanner.nextLine();
            if (!UniversityService.isBlank(s)) {
                return s.trim();
            }
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
            if (parts.length == 3) {
                return String.join(" ", parts);
            }

            System.out.println("Помилка: ПІБ має бути з 3 слів (Прізвище Ім'я По батькові).");
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
        if (form == null) {
            System.out.println("Помилка: введіть 1 або 2.");
        }
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
        if (st == null) {
            System.out.println("Помилка: введіть 1, 2 або 3.");
        }
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