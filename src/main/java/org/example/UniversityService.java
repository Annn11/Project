package org.example;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * In-memory "сервіс" для CRUD та пошуку.
 */
public class UniversityService {
    private final University university;

    private final List<Student> students = new ArrayList<>();
    private final List<Teacher> teachers = new ArrayList<>();

    public UniversityService() {
        // Університет: НаУКМА (мінімальні поля з методички)
        university = new University(
                "Національний університет \"Києво-Могилянська академія\"",
                "НаУКМА",
                "Київ",
                "вул. Григорія Сковороди, 2, Київ, 04070"
        );

        // Створюємо кафедри/факультет з посиланнями на викладачів (декан/завідувач)
        Teacher dean = new Teacher(
                UUID.randomUUID().toString(),
                "Іваненко", "Іван", "Іванович",
                LocalDate.of(1975, 5, 12),
                "dean@ukma.edu.ua",
                "+380501112233",
                "декан",
                "к.т.н.",
                "доцент",
                LocalDate.of(2010, 9, 1),
                1.0,
                null

        );

        Faculty fict = new Faculty(
                "FICT",
                "Факультет інформатики",
                "ФІ",
                dean,
                "fict@ukma.edu.ua, +38044XXXXXXX"
        );

        Teacher head = new Teacher(
                UUID.randomUUID().toString(),
                "Петренко", "Петро", "Петрович",
                LocalDate.of(1980, 1, 20),
                "head.se@ukma.edu.ua",
                "+380671234567",
                "завідувач кафедри",
                "д.т.н.",
                "професор",
                LocalDate.of(2008, 2, 1),
                1.0,
                null
        );

        Department seDept = new Department(
                "SE",
                "Кафедра інженерії програмного забезпечення",
                fict,
                head,
                "Корпус 3, каб. 312"
        );
        // Тепер підв'яжемо викладачів до кафедри
        dean.setDepartment(seDept);
        head.setDepartment(seDept);

        fict.getDepartments().add(seDept);
        university.getFaculties().add(fict);

        // Додамо викладачів у колекцію (щоб вони існували в реєстрі)
        teachers.add(dean);
        teachers.add(head);
        seDept.getPeople().add(dean);
        seDept.getPeople().add(head);
    }

    public University getUniversity() {
        return university;
    }

    // ---------------------- CRUD: Students ----------------------

    public void addStudent(Student student) {
        students.add(student);
        // Якщо студент має кафедру — підв'язуємо у ієрархію
        if (student.getDepartment() != null) {
            student.getDepartment().getPeople().add(student);
        }
    }

    /** Повертає true якщо видалення відбулось. */
    public boolean deleteStudentById(String id) {
        // При видаленні прибираємо також з департаменту (якщо був)
        Optional<Student> opt = findStudentById(id);
        if (opt.isPresent()) {
            Student s = opt.get();
            if (s.getDepartment() != null) {
                s.getDepartment().getPeople().remove(s);
            }
        }
        return students.removeIf(s -> s.getId().equals(id));
    }

    public Optional<Student> findStudentById(String id) {
        return students.stream().filter(s -> s.getId().equals(id)).findFirst();
    }

    /**
     * Update: змінюємо лише те, що передано не-null.
     */
    public boolean updateStudent(String id,
                                 String newLastName,
                                 String newFirstName,
                                 String newMiddleName,
                                 Integer newCourse,
                                 String newGroup,
                                 Integer newAdmissionYear,
                                 StudyForm newStudyForm,
                                 StudentStatus newStatus) {
        Optional<Student> opt = findStudentById(id);
        if (opt.isEmpty()) return false;
        Student s = opt.get();

        if (newLastName != null) s.setLastName(newLastName);
        if (newFirstName != null) s.setFirstName(newFirstName);
        if (newMiddleName != null) s.setMiddleName(newMiddleName);
        if (newCourse != null) s.setCourse(newCourse);
        if (newGroup != null) s.setGroup(newGroup);
        if (newAdmissionYear != null) s.setAdmissionYear(newAdmissionYear);
        if (newStudyForm != null) s.setStudyForm(newStudyForm);
        if (newStatus != null) s.setStatus(newStatus);

        return true;
    }

    public List<Student> getAllStudents() {
        return new ArrayList<>(students);
    }

    // ---------------------- CRUD: Teachers (мінімально) ----------------------

    public void addTeacher(Teacher teacher) {
        teachers.add(teacher);
    }

    public List<Teacher> getAllTeachers() {
        return new ArrayList<>(teachers);
    }

    // ---------------------- Search ----------------------

    public List<Student> findStudentsByName(String namePart) {
        String q = safeLower(namePart);
        return students.stream()
                .filter(s -> safeLower(s.getPib()).contains(q))
                .sorted(Comparator.comparing(Student::getPib))
                .collect(Collectors.toList());
    }

    public List<Student> getStudentsByCourse(int course) {
        return students.stream()
                .filter(s -> s.getCourse() == course)
                .sorted(Comparator.comparing(Student::getPib))
                .collect(Collectors.toList());
    }

    public List<Student> getStudentsByGroup(String group) {
        String g = safeLower(group);
        return students.stream()
                .filter(s -> safeLower(s.getGroup()).equals(g))
                .sorted(Comparator.comparing(Student::getPib))
                .collect(Collectors.toList());
    }

    // ---------------------- Reports (1-2 базові) ----------------------

    /** Звіт 1: скільки студентів на кожному курсі. */
    public Map<Integer, Long> reportCountByCourse() {
        return students.stream()
                .collect(Collectors.groupingBy(Student::getCourse, TreeMap::new, Collectors.counting()));
    }

    /** Звіт 2: скільки студентів у кожній групі. */
    public Map<String, Long> reportCountByGroup() {
        return students.stream()
                .collect(Collectors.groupingBy(Student::getGroup, TreeMap::new, Collectors.counting()));
    }

    // ---------------------- Validation helpers ----------------------

    public static boolean isBlank(String s) {
        return s == null || s.isBlank();
    }

    public static boolean isValidCourse(int course) {
        return course >= 1 && course <= 6;
    }

    public static boolean isValidGroup(String group) {
        if (isBlank(group)) return false;
        // Мінімальна перевірка: 2..12 символів (типу IPZ-1, SE-22, etc.)
        String g = group.trim();
        return g.length() >= 2 && g.length() <= 12;
    }

    public static boolean isValidYear(int year) {
        int now = LocalDate.now().getYear();
        return year >= 1991 && year <= now + 1;
    }

    private static String safeLower(String s) {
        return s == null ? "" : s.toLowerCase();
    }
}
