package org.example;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.Set;


public class UniversityService {
    private final University university =
            new University(
                    "Національний університет \"Києво-Могилянська академія\"",
                    "НаУКМА",
                    "Київ",
                    "вул. Григорія Сковороди, 2, Київ, 04070"
            );

    private final List<Student> students = new ArrayList<>();
    private final List<Teacher> teachers = new ArrayList<>();

    public UniversityService() {
        // -------------------- ФАКУЛЬТЕТ ГУМАНІТАРНИХ НАУК --------------------
        Teacher deanHumanities = new Teacher(
                UUID.randomUUID().toString(),
                "Мельник",
                "Оксана",
                "Ігорівна",
                LocalDate.of(1976, 3, 12),
                "dean.humanities@ukma.edu.ua",
                "+380500000001",
                "декан",
                "д.філол.н.",
                "професор",
                LocalDate.of(2011, 9, 1),
                1.0,
                null
        );

        Faculty humanitiesFaculty = new Faculty(
                "HUM",
                "Факультет гуманітарних наук",
                "ФГН",
                deanHumanities,
                "humanities@ukma.edu.ua"
        );

        Teacher headHistory = new Teacher(
                UUID.randomUUID().toString(),
                "Гнатюк",
                "Андрій",
                "Петрович",
                LocalDate.of(1980, 2, 10),
                "head.history@ukma.edu.ua",
                "+380500000011",
                "завідувач кафедри",
                "д.і.н.",
                "професор",
                LocalDate.of(2010, 9, 1),
                1.0,
                null
        );

        Department historyDept = new Department(
                "HIS",
                "Кафедра історії",
                humanitiesFaculty,
                headHistory,
                "Корпус 1, каб. 101"
        );

        deanHumanities.setDepartment(historyDept);
        headHistory.setDepartment(historyDept);

        humanitiesFaculty.addDepartment(historyDept);
        humanitiesFaculty.addSpecialty(new Specialty("B9", "Історія", humanitiesFaculty));
        humanitiesFaculty.addSpecialty(new Specialty("B10", "Філософія", humanitiesFaculty));
        humanitiesFaculty.addSpecialty(new Specialty("B11", "Філологія", humanitiesFaculty));
        humanitiesFaculty.addSpecialty(new Specialty("B12", "Культурологія", humanitiesFaculty));

        // -------------------- ФАКУЛЬТЕТ ЕКОНОМІЧНИХ НАУК --------------------
        Teacher deanEconomics = new Teacher(
                UUID.randomUUID().toString(),
                "Коваленко",
                "Олена",
                "Сергіївна",
                LocalDate.of(1978, 7, 3),
                "dean.econ@ukma.edu.ua",
                "+380500000002",
                "декан",
                "д.е.н.",
                "професор",
                LocalDate.of(2012, 9, 1),
                1.0,
                null
        );

        Faculty economicsFaculty = new Faculty(
                "ECON",
                "Факультет економічних наук",
                "ФЕН",
                deanEconomics,
                "econ@ukma.edu.ua"
        );

        Teacher headMarketing = new Teacher(
                UUID.randomUUID().toString(),
                "Сидоренко",
                "Марія",
                "Олександрівна",
                LocalDate.of(1982, 4, 14),
                "head.marketing@ukma.edu.ua",
                "+380500000012",
                "завідувач кафедри",
                "к.е.н.",
                "доцент",
                LocalDate.of(2011, 3, 15),
                1.0,
                null
        );

        Department marketingDept = new Department(
                "MKT",
                "Кафедра маркетингу та менеджменту",
                economicsFaculty,
                headMarketing,
                "Корпус 2, каб. 210"
        );

        deanEconomics.setDepartment(marketingDept);
        headMarketing.setDepartment(marketingDept);

        economicsFaculty.addDepartment(marketingDept);
        economicsFaculty.addSpecialty(new Specialty("C1", "Економіка", economicsFaculty));
        economicsFaculty.addSpecialty(new Specialty("D2", "Фінанси, банківська справа, страхування та фондовий ринок", economicsFaculty));
        economicsFaculty.addSpecialty(new Specialty("D3", "Менеджмент", economicsFaculty));
        economicsFaculty.addSpecialty(new Specialty("D5", "Маркетинг", economicsFaculty));

        // -------------------- ФАКУЛЬТЕТ СОЦІАЛЬНИХ НАУК --------------------
        Teacher deanSocial = new Teacher(
                UUID.randomUUID().toString(),
                "Бондар",
                "Ірина",
                "Миколаївна",
                LocalDate.of(1977, 11, 8),
                "dean.social@ukma.edu.ua",
                "+380500000003",
                "декан",
                "д.соц.н.",
                "професор",
                LocalDate.of(2013, 9, 1),
                1.0,
                null
        );

        Faculty socialFaculty = new Faculty(
                "SOC",
                "Факультет соціальних наук та соціальних технологій",
                "ФСНСТ",
                deanSocial,
                "social@ukma.edu.ua"
        );

        Teacher headPolitics = new Teacher(
                UUID.randomUUID().toString(),
                "Руденко",
                "Тарас",
                "Іванович",
                LocalDate.of(1981, 6, 18),
                "head.politics@ukma.edu.ua",
                "+380500000013",
                "завідувач кафедри",
                "д.політ.н.",
                "професор",
                LocalDate.of(2012, 9, 1),
                1.0,
                null
        );

        Department politicsDept = new Department(
                "POL",
                "Кафедра політичних наук",
                socialFaculty,
                headPolitics,
                "Корпус 4, каб. 305"
        );

        deanSocial.setDepartment(politicsDept);
        headPolitics.setDepartment(politicsDept);

        socialFaculty.addDepartment(politicsDept);
        socialFaculty.addSpecialty(new Specialty("C2", "Політологія", socialFaculty));
        socialFaculty.addSpecialty(new Specialty("C3", "Міжнародні відносини", socialFaculty));
        socialFaculty.addSpecialty(new Specialty("C5", "Соціологія", socialFaculty));
        socialFaculty.addSpecialty(new Specialty("C7", "Журналістика", socialFaculty));

        // -------------------- ФАКУЛЬТЕТ ІНФОРМАТИКИ --------------------
        Teacher deanIT = new Teacher(
                UUID.randomUUID().toString(),
                "Іваненко",
                "Іван",
                "Іванович",
                LocalDate.of(1975, 5, 12),
                "dean.it@ukma.edu.ua",
                "+380500000004",
                "декан",
                "к.т.н.",
                "доцент",
                LocalDate.of(2010, 9, 1),
                1.0,
                null
        );

        Faculty itFaculty = new Faculty(
                "IT",
                "Факультет інформатики",
                "ФІ",
                deanIT,
                "it@ukma.edu.ua"
        );

        Teacher headSe = new Teacher(
                UUID.randomUUID().toString(),
                "Петренко",
                "Петро",
                "Петрович",
                LocalDate.of(1980, 1, 20),
                "head.se@ukma.edu.ua",
                "+380500000014",
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
                itFaculty,
                headSe,
                "Корпус 3, каб. 312"
        );

        deanIT.setDepartment(seDept);
        headSe.setDepartment(seDept);

        itFaculty.addDepartment(seDept);
        itFaculty.addSpecialty(new Specialty("F1", "Прикладна математика", itFaculty));
        itFaculty.addSpecialty(new Specialty("F2", "Інженерія програмного забезпечення", itFaculty));
        itFaculty.addSpecialty(new Specialty("F3", "Комп’ютерні науки", itFaculty));
        itFaculty.addSpecialty(new Specialty("F5", "Кібербезпека та захист інформації", itFaculty));
        itFaculty.addSpecialty(new Specialty("G7", "Автоматизація, комп’ютерно-інтегровані технології та робототехніка", itFaculty));

        // -------------------- ФАКУЛЬТЕТ ПРАВНИЧИХ НАУК --------------------
        Teacher deanLaw = new Teacher(
                UUID.randomUUID().toString(),
                "Шевченко",
                "Леся",
                "Петрівна",
                LocalDate.of(1979, 2, 19),
                "dean.law@ukma.edu.ua",
                "+380500000005",
                "декан",
                "д.ю.н.",
                "професор",
                LocalDate.of(2014, 9, 1),
                1.0,
                null
        );

        Faculty lawFaculty = new Faculty(
                "LAW",
                "Факультет правничих наук",
                "ФПН",
                deanLaw,
                "law@ukma.edu.ua"
        );

        Teacher headLaw = new Teacher(
                UUID.randomUUID().toString(),
                "Демченко",
                "Олег",
                "Вікторович",
                LocalDate.of(1983, 8, 9),
                "head.law@ukma.edu.ua",
                "+380500000015",
                "завідувач кафедри",
                "к.ю.н.",
                "доцент",
                LocalDate.of(2013, 9, 1),
                1.0,
                null
        );

        Department lawDept = new Department(
                "LAW-D",
                "Кафедра права та публічного управління",
                lawFaculty,
                headLaw,
                "Корпус 5, каб. 115"
        );

        deanLaw.setDepartment(lawDept);
        headLaw.setDepartment(lawDept);

        lawFaculty.addDepartment(lawDept);
        lawFaculty.addSpecialty(new Specialty("D4", "Публічне управління та адміністрування", lawFaculty));
        lawFaculty.addSpecialty(new Specialty("D8", "Право", lawFaculty));

        // -------------------- ФАКУЛЬТЕТ ПРИРОДНИЧИХ НАУК --------------------
        Teacher deanNatural = new Teacher(
                UUID.randomUUID().toString(),
                "Романюк",
                "Світлана",
                "Василівна",
                LocalDate.of(1981, 6, 21),
                "dean.natural@ukma.edu.ua",
                "+380500000006",
                "декан",
                "д.б.н.",
                "професор",
                LocalDate.of(2015, 9, 1),
                1.0,
                null
        );

        Faculty naturalFaculty = new Faculty(
                "NAT",
                "Факультет природничих наук",
                "ФПрН",
                deanNatural,
                "natural@ukma.edu.ua"
        );

        Teacher headBiology = new Teacher(
                UUID.randomUUID().toString(),
                "Кириленко",
                "Наталія",
                "Степанівна",
                LocalDate.of(1984, 12, 2),
                "head.bio@ukma.edu.ua",
                "+380500000016",
                "завідувач кафедри",
                "д.б.н.",
                "професор",
                LocalDate.of(2014, 9, 1),
                1.0,
                null
        );

        Department biologyDept = new Department(
                "BIO",
                "Кафедра біології та екології",
                naturalFaculty,
                headBiology,
                "Корпус 6, каб. 221"
        );

        deanNatural.setDepartment(biologyDept);
        headBiology.setDepartment(biologyDept);

        naturalFaculty.addDepartment(biologyDept);
        naturalFaculty.addSpecialty(new Specialty("E1", "Біологія та біохімія", naturalFaculty));
        naturalFaculty.addSpecialty(new Specialty("E2", "Екологія", naturalFaculty));
        naturalFaculty.addSpecialty(new Specialty("E3", "Хімія", naturalFaculty));

        // -------------------- ФАКУЛЬТЕТ ОХОРОНИ ЗДОРОВ'Я --------------------
        Teacher deanHealth = new Teacher(
                UUID.randomUUID().toString(),
                "Ткаченко",
                "Марина",
                "Андріївна",
                LocalDate.of(1980, 9, 14),
                "dean.health@ukma.edu.ua",
                "+380500000007",
                "декан",
                "д.психол.н.",
                "професор",
                LocalDate.of(2016, 9, 1),
                1.0,
                null
        );

        Faculty healthFaculty = new Faculty(
                "HEALTH",
                "Факультет охорони здоров’я, соціальної роботи та психології",
                "ФОЗСРП",
                deanHealth,
                "health@ukma.edu.ua"
        );

        Teacher headPsychology = new Teacher(
                UUID.randomUUID().toString(),
                "Семенюк",
                "Юлія",
                "Володимирівна",
                LocalDate.of(1985, 5, 27),
                "head.psychology@ukma.edu.ua",
                "+380500000017",
                "завідувач кафедри",
                "к.психол.н.",
                "доцент",
                LocalDate.of(2015, 9, 1),
                1.0,
                null
        );

        Department psychologyDept = new Department(
                "PSY",
                "Кафедра психології та соціальної роботи",
                healthFaculty,
                headPsychology,
                "Корпус 7, каб. 120"
        );

        deanHealth.setDepartment(psychologyDept);
        headPsychology.setDepartment(psychologyDept);

        healthFaculty.addDepartment(psychologyDept);
        healthFaculty.addSpecialty(new Specialty("C4", "Психологія", healthFaculty));
        healthFaculty.addSpecialty(new Specialty("I10", "Соціальна робота", healthFaculty));
        healthFaculty.addSpecialty(new Specialty("I9", "Громадське здоров’я", healthFaculty));

        // -------------------- ДОДАЄМО ДО УНІВЕРСИТЕТУ --------------------
        university.getFaculties().add(humanitiesFaculty);
        university.getFaculties().add(economicsFaculty);
        university.getFaculties().add(socialFaculty);
        university.getFaculties().add(itFaculty);
        university.getFaculties().add(lawFaculty);
        university.getFaculties().add(naturalFaculty);
        university.getFaculties().add(healthFaculty);

        // -------------------- ВИКЛАДАЧІ --------------------
        teachers.add(deanHumanities);
        teachers.add(headHistory);

        teachers.add(deanEconomics);
        teachers.add(headMarketing);

        teachers.add(deanSocial);
        teachers.add(headPolitics);

        teachers.add(deanIT);
        teachers.add(headSe);

        teachers.add(deanLaw);
        teachers.add(headLaw);

        teachers.add(deanNatural);
        teachers.add(headBiology);

        teachers.add(deanHealth);
        teachers.add(headPsychology);

        // -------------------- ЛЮДИ В КАФЕДРАХ --------------------
        historyDept.getPeople().add(deanHumanities);
        historyDept.getPeople().add(headHistory);

        marketingDept.getPeople().add(deanEconomics);
        marketingDept.getPeople().add(headMarketing);

        politicsDept.getPeople().add(deanSocial);
        politicsDept.getPeople().add(headPolitics);

        seDept.getPeople().add(deanIT);
        seDept.getPeople().add(headSe);

        lawDept.getPeople().add(deanLaw);
        lawDept.getPeople().add(headLaw);

        biologyDept.getPeople().add(deanNatural);
        biologyDept.getPeople().add(headBiology);

        psychologyDept.getPeople().add(deanHealth);
        psychologyDept.getPeople().add(headPsychology);
    }

    public University getUniversity() {
        return university;
    }

    public void addStudent(Student student) {
        students.add(student);
        if (student.getDepartment() != null) {
            student.getDepartment().getPeople().add(student);
        }
    }

    public boolean deleteStudentById(String id) {
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
        return students.stream()
                .filter(s -> s.getId().equals(id))
                .findFirst();
    }

    public boolean updateStudent(
            String id,
            String newLastName,
            String newFirstName,
            String newMiddleName,
            Integer newCourse,
            String newGroup,
            Integer newAdmissionYear,
            StudyForm newStudyForm,
            StudentStatus newStatus
    ) {
        Optional<Student> opt = findStudentById(id);
        if (opt.isEmpty()) {
            return false;
        }

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

    public void addTeacher(Teacher teacher) {
        teachers.add(teacher);
    }

    public List<Teacher> getAllTeachers() {
        return new ArrayList<>(teachers);
    }

    public List<Student> findStudentsByName(String namePart) {
        String q = safeLower(namePart);
        return students.stream()
                .filter(s -> safeLower(s.getPib()).contains(q))
                .sorted(Comparator.comparing(Person::getPib))
                .collect(Collectors.toList());
    }

    public List<Student> getStudentsByCourse(int course) {
        return students.stream()
                .filter(s -> s.getCourse() == course)
                .sorted(Comparator.comparing(Person::getPib))
                .collect(Collectors.toList());
    }

    public List<Student> getStudentsByGroup(String group) {
        String g = safeLower(group);
        return students.stream()
                .filter(s -> safeLower(s.getGroup()).equals(g))
                .sorted(Comparator.comparing(Person::getPib))
                .collect(Collectors.toList());
    }

    public Map<Integer, Long> reportCountByCourse() {
        return students.stream()
                .collect(Collectors.groupingBy(Student::getCourse, TreeMap::new, Collectors.counting()));
    }

    public Map<String, Long> reportCountByGroup() {
        return students.stream()
                .collect(Collectors.groupingBy(Student::getGroup, TreeMap::new, Collectors.counting()));
    }

    public static boolean isBlank(String s) {
        return s == null || s.isBlank();
    }

    public static boolean isValidCourse(int course) {
        return course >= 1 && course <= 6;
    }

    public static boolean isValidGroup(String group) {
        if (isBlank(group)) {
            return false;
        }
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
    public Set<String> getAllGroupsUnique() {
        return students.stream()
                .map(Student::getGroup)
                .collect(Collectors.toSet());
    }
}