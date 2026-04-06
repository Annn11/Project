package org.example;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;
import java.util.UUID;

public class Main {

    private static final AuthService auth = new AuthService();
    private static final UniversityService service = new UniversityService();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        loginMenu();
        printMenu();

        while (true) {
            System.out.print("Вибір: ");

            switch (scanner.nextLine().trim()) {
                case "1":
                    if (requirePermission(Permission.EDIT_STUDENTS)) {
                        addStudentMenu();
                    }
                    break;
                case "2":
                    if (requirePermission(Permission.VIEW_STUDENTS)) {
                        printStudents(service.getAllStudents());
                    }
                    break;
                case "3":
                    if (requirePermission(Permission.EDIT_STUDENTS)) {
                        updateStudentMenu();
                    }
                    break;
                case "4":
                    if (requirePermission(Permission.DELETE_STUDENTS)) {
                        deleteStudentMenu();
                    }
                    break;
                case "5":
                    if (requirePermission(Permission.VIEW_STUDENTS)) {
                        searchByNameMenu();
                    }
                    break;
                case "6":
                    if (requirePermission(Permission.VIEW_STUDENTS)) {
                        searchByCourseMenu();
                    }
                    break;
                case "7":
                    if (requirePermission(Permission.VIEW_STUDENTS)) {
                        searchByGroupMenu();
                    }
                    break;
                case "8":
                    if (requirePermission(Permission.VIEW_REPORTS)) {
                        reportsMenu();
                    }
                    break;
                case "9":
                    showUniversityStructure();
                    break;
                case "10":
                    if (requirePermission(Permission.MANAGE_USERS)) {
                        manageUsersMenu();
                    }
                    break;
                case "11":
                    auth.logout();
                    loginMenu();
                    printMenu();
                    break;
                case "12":
                    if (requirePermission(Permission.SAVE_LOAD)) {
                        service.saveStudentsToFile("src/students.txt");
                    }
                    break;
                case "13":
                    if (requirePermission(Permission.SAVE_LOAD)) {
                        service.loadStudentsFromFile("src/students.txt");
                    }
                    break;
                case "99":
                    demoData();
                    break;
                case "m":
                    printMenu();
                    break;
                case "0":
                    System.out.println("До побачення!");
                    return;
                default:
                    System.out.println("Помилка: невірний пункт меню.");
            }
        }
    }

    private static void loginMenu() {
        while (true) {
            System.out.println("\n--- Вхід у систему ---");
            System.out.print("Логін: ");
            String login = scanner.nextLine().trim();

            System.out.print("Пароль: ");
            String password = scanner.nextLine().trim();

            if (auth.login(login, password)) {
                System.out.println("Вхід успішний. Роль: " + auth.getCurrentRole());
                return;
            }

            System.out.println("Невірний логін або пароль. Спробуйте ще раз.");
        }
    }

    private static void printMenu() {
        System.out.println("\n--- Registry: Checkpoint 3 ---");
        System.out.println("Університет: " + service.getUniversity().getFullName()
                + " (" + service.getUniversity().getShortName() + "), "
                + service.getUniversity().getCity());

        if (auth.getCurrentUser() != null) {
            System.out.println("Користувач: " + auth.getCurrentUser().getLogin()
                    + " | роль: " + auth.getCurrentUser().getRole());
        }

        System.out.println("1. Додати студента");
        System.out.println("2. Список усіх студентів");
        System.out.println("3. Оновити студента");
        System.out.println("4. Видалити студента");
        System.out.println("5. Пошук за ПІБ");
        System.out.println("6. Пошук за курсом");
        System.out.println("7. Пошук за групою");
        System.out.println("8. Звіти");
        System.out.println("9. Показати факультети / кафедри / спеціальності");

        if (auth.getCurrentRole() == UserRole.ADMIN) {
            System.out.println("10. Керування користувачами");
        }

        System.out.println("11. Змінити користувача");
        System.out.println("12. Зберегти дані");
        System.out.println("13. Завантажити дані");
        System.out.println("99. Демо (швидке заповнення)");
        System.out.println("0. Вихід");
        System.out.print("Вибір: ");
    }

    private static void addStudentMenu() {
        String lastName = readNonBlank("Прізвище: ");
        String firstName = readNonBlank("Ім'я: ");
        String middleName = readNonBlank("По батькові: ");

        LocalDate birthDate = readDate("Дата народження (YYYY-MM-DD): ");
        String email = readNonBlank("Email: ");
        String phone = readNonBlank("Телефон: ");
        String recordId = readNonBlank("Номер студ.квитка/залікової: ");

        int course = readCourse("Курс (1-6): ");
        String group = readGroup("Група (наприклад IPZ-1): ");
        int admissionYear = readYear("Рік вступу (наприклад 2025): ");
        StudyForm studyForm = readStudyForm("Форма навчання (1=бюджет, 2=контракт): ");
        StudentStatus status = readStudentStatus("Статус (1=навчається, 2=академвідпустка, 3=відрахований): ");

        Faculty faculty = readFaculty();
        Specialty specialty = readSpecialty(faculty);
        Department dept = getDefaultDepartmentByFaculty(faculty);

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

    private static void manageUsersMenu() {
        while (true) {
            System.out.println("\n--- Керування користувачами ---");
            System.out.println("1. Показати всіх користувачів");
            System.out.println("2. Додати користувача");
            System.out.println("3. Видалити користувача");
            System.out.println("4. Змінити роль користувача");
            System.out.println("0. Назад");
            System.out.print("Вибір: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    for (UserAccount u : auth.getAllUsers()) {
                        System.out.println(u);
                    }
                    break;

                case "2":
                    String login = readNonBlank("Логін: ");
                    String password = readNonBlank("Пароль: ");
                    UserRole role = readUserRole();
                    if (role == null) {
                        System.out.println("Помилка: роль не вибрана.");
                        break;
                    }

                    if (auth.addUser(login, password, role)) {
                        System.out.println("Користувача додано.");
                    } else {
                        System.out.println("Не вдалося додати користувача.");
                    }
                    break;

                case "3":
                    String loginToDelete = readNonBlank("Логін користувача для видалення: ");
                    if (auth.removeUser(loginToDelete)) {
                        System.out.println("Користувача видалено.");
                    } else {
                        System.out.println("Не вдалося видалити користувача.");
                    }
                    break;

                case "4":
                    String loginToChange = readNonBlank("Логін користувача: ");
                    UserRole newRole = readUserRole();
                    if (newRole == null) {
                        System.out.println("Помилка: роль не вибрана.");
                        break;
                    }

                    if (auth.changeRole(loginToChange, newRole)) {
                        System.out.println("Роль змінено.");
                    } else {
                        System.out.println("Не вдалося змінити роль.");
                    }
                    break;

                case "0":
                    return;

                default:
                    System.out.println("Помилка: невірний пункт меню.");
            }
        }
    }

    private static UserRole readUserRole() {
        System.out.println("Оберіть роль:");
        System.out.println("1. USER");
        System.out.println("2. MANAGER");
        System.out.println("3. ADMIN");
        System.out.print("Вибір: ");

        String s = scanner.nextLine().trim();

        return switch (s) {
            case "1" -> UserRole.USER;
            case "2" -> UserRole.MANAGER;
            case "3" -> UserRole.ADMIN;
            default -> null;
        };
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

        while (true) {
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
                    continue;
                }
                return faculties.get(index - 1);
            } catch (NumberFormatException e) {
                System.out.println("Помилка: введіть номер факультету.");
            }
        }
    }

    private static Specialty readSpecialty(Faculty faculty) {
        List<Specialty> specialties = faculty.getSpecialties();

        if (specialties.isEmpty()) {
            System.out.println("Помилка: на факультеті немає спеціальностей.");
            return null;
        }

        while (true) {
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
                    continue;
                }
                return specialties.get(index - 1);
            } catch (NumberFormatException e) {
                System.out.println("Помилка: введіть номер спеціальності.");
            }
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

    private static LocalDate readDate(String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = scanner.nextLine().trim();

            if (s.isEmpty()) {
                System.out.println("Помилка: дата не може бути порожньою.");
                continue;
            }

            try {
                return LocalDate.parse(s);
            } catch (DateTimeParseException e) {
                System.out.println("Помилка: формат дати має бути YYYY-MM-DD.");
            }
        }
    }

    private static int readYear(String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = scanner.nextLine().trim();

            try {
                int y = Integer.parseInt(s);
                if (!UniversityService.isValidYear(y)) {
                    System.out.println("Помилка: некоректний рік.");
                    continue;
                }
                return y;
            } catch (NumberFormatException e) {
                System.out.println("Помилка: введіть число для року.");
            }
        }
    }

    private static StudyForm readStudyForm(String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = scanner.nextLine().trim();

            StudyForm form = parseStudyForm(s);
            if (form != null) {
                return form;
            }

            System.out.println("Помилка: введіть 1 або 2.");
        }
    }

    private static StudyForm parseStudyForm(String s) {
        return switch (s) {
            case "1" -> StudyForm.BUDGET;
            case "2" -> StudyForm.CONTRACT;
            default -> null;
        };
    }

    private static StudentStatus readStudentStatus(String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = scanner.nextLine().trim();

            StudentStatus st = parseStudentStatus(s);
            if (st != null) {
                return st;
            }

            System.out.println("Помилка: введіть 1, 2 або 3.");
        }
    }

    private static StudentStatus parseStudentStatus(String s) {
        return switch (s) {
            case "1" -> StudentStatus.STUDYING;
            case "2" -> StudentStatus.ACADEMIC_LEAVE;
            case "3" -> StudentStatus.EXPELLED;
            default -> null;
        };
    }

    private static int readCourse(String prompt) {
        while (true) {
            System.out.print(prompt);
            String str = scanner.nextLine().trim();

            try {
                int c = Integer.parseInt(str);
                if (!UniversityService.isValidCourse(c)) {
                    System.out.println("Помилка: курс має бути 1..6.");
                    continue;
                }
                return c;
            } catch (NumberFormatException e) {
                System.out.println("Помилка: введіть число для курсу.");
            }
        }
    }

    private static String readGroup(String prompt) {
        while (true) {
            System.out.print(prompt);
            String group = scanner.nextLine();

            if (!UniversityService.isValidGroup(group)) {
                System.out.println("Помилка: некоректна група.");
                continue;
            }

            return group.trim();
        }
    }

    private static void demoData() {

        if (!service.getAllStudents().isEmpty()) {
            System.out.println("Демо-дані вже додані.");
            return;
        }

        List<Faculty> faculties = service.getUniversity().getFaculties();

        if (faculties.isEmpty()) {
            System.out.println("Немає факультетів для демо.");
            return;
        }

        Faculty faculty = faculties.get(0);

        if (faculty.getSpecialties().isEmpty() || faculty.getDepartments().isEmpty()) {
            System.out.println("Немає спеціальностей або кафедр.");
            return;
        }

        Specialty specialty = faculty.getSpecialties().get(0);
        Department dept = faculty.getDepartments().get(0);

        service.addStudent(new Student(
                UUID.randomUUID().toString(),
                "Іваненко",
                "Іван",
                "Іванович",
                LocalDate.of(2005, 5, 10),
                "ivan@test.com",
                "0991234567",
                "ST123",
                2,
                "IPZ-21",
                2023,
                StudyForm.BUDGET,
                StudentStatus.STUDYING,
                dept,
                specialty
        ));

        service.addStudent(new Student(
                UUID.randomUUID().toString(),
                "Петренко",
                "Оля",
                "Ігорівна",
                LocalDate.of(2004, 3, 15),
                "olya@test.com",
                "0987654321",
                "ST124",
                3,
                "IPZ-31",
                2022,
                StudyForm.CONTRACT,
                StudentStatus.STUDYING,
                dept,
                specialty
        ));

        service.addStudent(new Student(
                UUID.randomUUID().toString(),
                "Бондар",
                "Дмитро",
                "Ігорович",
                LocalDate.of(2002, 11, 5),
                "dmytro@test.com",
                "0937778899",
                "ST125",
                4,
                "SE-41",
                2021,
                StudyForm.BUDGET,
                StudentStatus.EXPELLED,
                dept,
                specialty
        ));

        System.out.println("✔ Демо-дані додані!");
    }

    private static boolean requirePermission(Permission permission) {
        if (!auth.hasPermission(permission)) {
            System.out.println("Помилка: недостатньо прав доступу.");
            return false;
        }
        return true;
    }
}