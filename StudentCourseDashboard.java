
package corejava;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class StudentCourseDashboard extends JFrame {

    private List<Student> students = new ArrayList<>();
    private List<Course> courses = new ArrayList<>();
    private List<Registration> registrations = new ArrayList<>();

    private JLabel summaryLabel;

    public StudentCourseDashboard() {
        setTitle("Student Course Registration System");
        setSize(800, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        loadData();

        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel heading = new JLabel(
                "STUDENT COURSE REGISTRATION SYSTEM",
                SwingConstants.CENTER);
        heading.setFont(new Font("Arial", Font.BOLD, 22));

        summaryLabel = new JLabel("", SwingConstants.CENTER);
        summaryLabel.setFont(new Font("Arial", Font.PLAIN, 15));

        JPanel topPanel = new JPanel(new GridLayout(2, 1, 5, 10));
        topPanel.add(heading);
        topPanel.add(summaryLabel);

        JPanel buttonPanel = new JPanel(new GridLayout(3, 2, 15, 15));

        JButton studentButton = new JButton("Student Management");
        JButton courseButton = new JButton("Course Management");
        JButton registrationButton = new JButton("Course Registration");
        JButton viewButton = new JButton("View Registrations");
        JButton summaryButton = new JButton("Dashboard Summary");
        JButton logoutButton = new JButton("Logout");

        buttonPanel.add(studentButton);
        buttonPanel.add(courseButton);
        buttonPanel.add(registrationButton);
        buttonPanel.add(viewButton);
        buttonPanel.add(summaryButton);
        buttonPanel.add(logoutButton);

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(buttonPanel, BorderLayout.CENTER);
        add(mainPanel);

        studentButton.addActionListener(e -> studentManagement());
        courseButton.addActionListener(e -> courseManagement());
        registrationButton.addActionListener(e -> courseRegistration());
        viewButton.addActionListener(e -> viewRegistrations());
        summaryButton.addActionListener(e -> dashboardSummary());

        logoutButton.addActionListener(e -> {
            int answer = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to logout?",
                "Logout",
                JOptionPane.YES_NO_OPTION
            );

            if (answer == JOptionPane.YES_OPTION) {
                JOptionPane.showMessageDialog(
                    this,
                    "Logged out successfully!"
                );

                System.exit(0);
            }
        });
            updateSummary();
    }

    // 1. STUDENT MANAGEMENT


private void studentManagement() {

    String[] options = {
        "Add Student",
        "View Students",
        "Remove Student",
        "Back"
    };

    int choice = JOptionPane.showOptionDialog(
        this,
        "Select Student Operation",
        "Student Management",
        JOptionPane.DEFAULT_OPTION,
        JOptionPane.PLAIN_MESSAGE,
        null,
        options,
        options[0]
    );

    // ADD STUDENT
    if (choice == 0) {

        String studentId = JOptionPane.showInputDialog(
            this, "Enter Student ID:");

        if (studentId == null || studentId.trim().isEmpty()) {
            return;
        }

        String name = JOptionPane.showInputDialog(
            this, "Enter Student Name:");

        if (name == null || name.trim().isEmpty()) {
            return;
        }

        String email = JOptionPane.showInputDialog(
            this, "Enter Student Email:");

        if (email == null || email.trim().isEmpty()) {
            return;
        }

        if (findStudent(studentId.trim()) != null) {
            JOptionPane.showMessageDialog(
                this, "Student ID already exists!");
            return;
        }

        Student student = new Student(
            studentId.trim(),
            name.trim(),
            email.trim()
        );

        students.add(student);
        saveStudents();
        updateSummary();

        JOptionPane.showMessageDialog(
            this, "Student added successfully!");

    }

    // VIEW STUDENTS
    else if (choice == 1) {

        if (students.isEmpty()) {
            JOptionPane.showMessageDialog(
                this, "No students available.");
        } else {

            StringBuilder studentList = new StringBuilder();

            for (Student student : students) {
                studentList.append("Student ID: ")
                           .append(student.getStudentId())
                           .append("\nName: ")
                           .append(student.getName())
                           .append("\nEmail: ")
                           .append(student.getEmail())
                           .append("\n\n");
            }

            showText("Student List", studentList.toString());
        }

    }

    // REMOVE STUDENT
    else if (choice == 2) {

        if (students.isEmpty()) {
            JOptionPane.showMessageDialog(
                this, "No students available to remove.");
            return;
        }

        String studentId = JOptionPane.showInputDialog(
            this, "Enter Student ID to remove:");

        if (studentId == null || studentId.trim().isEmpty()) {
            return;
        }

        String id = studentId.trim();
        Student student = findStudent(id);

        if (student == null) {
            JOptionPane.showMessageDialog(
                this, "Student ID not found!");
            return;
        }

        // Check whether the student has registrations
        for (Registration r : registrations) {
            if (r.getStudentId().equalsIgnoreCase(id)) {
                JOptionPane.showMessageDialog(
                    this,
                    "Drop this student's course registrations "
                    + "before removing the student.");
                return;
            }
        }

        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to remove this student?\n\n"
                + "Student ID: " + student.getStudentId()
                + "\nName: " + student.getName(),
            "Confirm Remove",
            JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            students.remove(student);
            saveStudents();
            updateSummary();

            JOptionPane.showMessageDialog(
                this, "Student removed successfully!");
        }
    }

    // Back returns to the dashboard
}


    // 2. COURSE MANAGEMENT



private void courseManagement() {

    String[] options = {
        "Add Course",
        "View Courses",
        "Delete Course",
        "Back"
    };

    int choice = JOptionPane.showOptionDialog(
        this,
        "Select Course Operation",
        "Course Management",
        JOptionPane.DEFAULT_OPTION,
        JOptionPane.PLAIN_MESSAGE,
        null,
        options,
        options[0]
    );

    // ADD COURSE
    if (choice == 0) {

        String courseId = JOptionPane.showInputDialog(
            this, "Enter Course ID:");

        if (courseId == null || courseId.trim().isEmpty()) {
            return;
        }

        String courseName = JOptionPane.showInputDialog(
            this, "Enter Course Name:");

        if (courseName == null || courseName.trim().isEmpty()) {
            return;
        }

        String creditsText = JOptionPane.showInputDialog(
            this, "Enter Credits:");

        if (creditsText == null || creditsText.trim().isEmpty()) {
            return;
        }

        try {
            int credits = Integer.parseInt(creditsText.trim());

            if (credits <= 0) {
                JOptionPane.showMessageDialog(
                    this, "Credits must be greater than zero.");
                return;
            }

            if (findCourse(courseId.trim()) != null) {
                JOptionPane.showMessageDialog(
                    this, "Course ID already exists!");
                return;
            }

            Course course = new Course(
                courseId.trim(),
                courseName.trim(),
                credits,
                0,
                "None"
            );

            courses.add(course);
            saveCourses();
            updateSummary();

            JOptionPane.showMessageDialog(
                this, "Course added successfully!");

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(
                this, "Please enter valid credits.");
        }

    }

    // VIEW COURSES
    else if (choice == 1) {

        if (courses.isEmpty()) {
            JOptionPane.showMessageDialog(
                this, "No courses available.");
        } else {

            StringBuilder courseList = new StringBuilder();

            for (Course course : courses) {
                courseList.append("Course ID: ")
                          .append(course.getCourseId())
                          .append("\nCourse Name: ")
                          .append(course.getCourseName())
                          .append("\nCredits: ")
                          .append(course.getCredits())
                          .append("\n\n");
            }

            showText("Course List", courseList.toString());
        }

    }

    // DELETE COURSE
    else if (choice == 2) {

        if (courses.isEmpty()) {
            JOptionPane.showMessageDialog(
                this, "No courses available to delete.");
            return;
        }

        String courseId = JOptionPane.showInputDialog(
            this, "Enter Course ID to delete:");

        if (courseId == null || courseId.trim().isEmpty()) {
            return;
        }

        String id = courseId.trim();
        Course course = findCourse(id);

        if (course == null) {
            JOptionPane.showMessageDialog(
                this, "Course ID not found!");
            return;
        }

        boolean registered = false;

        for (Registration r : registrations) {
            if (r.getCourseId().equalsIgnoreCase(id)) {
                registered = true;
                break;
            }
        }

        if (registered) {
            JOptionPane.showMessageDialog(
                this,
                "Cannot delete this course because students are "
                + "registered for it. Drop those registrations first.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to delete this course?\n\n"
                + "Course ID: " + course.getCourseId()
                + "\nCourse Name: " + course.getCourseName(),
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            courses.remove(course);
            saveCourses();
            updateSummary();

            JOptionPane.showMessageDialog(
                this, "Course deleted successfully!");
        }

    }

    // BACK
    // Selecting Back returns to the dashboard.
}


    // 3. COURSE REGISTRATION


private void courseRegistration() {

    // Enter Student ID
    String studentId = JOptionPane.showInputDialog(
        this, "Enter Student ID:");

    if (studentId == null || studentId.trim().isEmpty()) {
        return;
    }

    studentId = studentId.trim();

    // Check whether student exists
    if (findStudent(studentId) == null) {
        JOptionPane.showMessageDialog(
            this, "Student ID not found!");
        return;
    }

    // Enter Course ID
    String courseId = JOptionPane.showInputDialog(
        this, "Enter Course ID:");

    if (courseId == null || courseId.trim().isEmpty()) {
        return;
    }

    courseId = courseId.trim();

    // Check whether course exists
    if (findCourse(courseId) == null) {
        JOptionPane.showMessageDialog(
            this, "Course ID not found!");
        return;
    }

    // Check duplicate registration
    for (Registration r : registrations) {
        if (r.getStudentId().equalsIgnoreCase(studentId)
                && r.getCourseId().equalsIgnoreCase(courseId)) {

            JOptionPane.showMessageDialog(
                this,
                "This student is already registered for this course!");
            return;
        }
    }

    // Register student without checking seat capacity
    registrations.add(new Registration(studentId, courseId));

    saveRegistrations();
    updateSummary();

    JOptionPane.showMessageDialog(
        this, "Course registration successful!");
}

    // 4. VIEW AND DROP REGISTRATIONS

    private void viewRegistrations() {
        if (registrations.isEmpty()) {
            message("No registrations available.");
            return;
        }

        String[] options = {"View All", "Drop Registration", "Back"};

        int choice = JOptionPane.showOptionDialog(
                this, "Select Registration Operation", "Registrations",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, options, options[0]);

        if (choice == 0) {
            StringBuilder result = new StringBuilder("REGISTRATION DETAILS\n\n");

            for (Registration r : registrations) {
                Student s = findStudent(r.getStudentId());
                Course c = findCourse(r.getCourseId());

                result.append("Student: ")
                        .append(s == null ? r.getStudentId() : s.getName())
                        .append(" (").append(r.getStudentId()).append(")")
                        .append(" | Course: ")
                        .append(c == null ? r.getCourseId() : c.getCourseName())
                        .append(" (").append(r.getCourseId()).append(")\n");
            }

            showText("View Registrations", result.toString());

        } else if (choice == 1) {
            String studentId = JOptionPane.showInputDialog(this, "Enter Student ID:");
            if (studentId == null || studentId.trim().isEmpty()) return;

            String courseId = JOptionPane.showInputDialog(this, "Enter Course ID:");
            if (courseId == null || courseId.trim().isEmpty()) return;

            boolean removed = registrations.removeIf(r ->
                    r.getStudentId().equalsIgnoreCase(studentId.trim())
                    && r.getCourseId().equalsIgnoreCase(courseId.trim()));

            if (removed) {
                saveRegistrations();
                updateSummary();
                message("Registration dropped successfully.");
            } else {
                message("Registration not found.");
            }
        }
    }

    // 5. DASHBOARD SUMMARY

    private void dashboardSummary() {
        updateSummary();

        JOptionPane.showMessageDialog(this,
                "DASHBOARD SUMMARY\n\n"
                + "Total Students: " + students.size() + "\n"
                + "Total Courses: " + courses.size() + "\n"
                + "Total Registrations: " + registrations.size(),
                "Dashboard Summary",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void updateSummary() {
        summaryLabel.setText(
                "Students: " + students.size()
                + "     Courses: " + courses.size()
                + "     Registrations: " + registrations.size());
    }

    // FIND RECORDS

    private Student findStudent(String id) {
        for (Student s : students) {
            if (s.getStudentId().equalsIgnoreCase(id)) return s;
        }
        return null;
    }

    private Course findCourse(String id) {
        for (Course c : courses) {
            if (c.getCourseId().equalsIgnoreCase(id)) return c;
        }
        return null;
    }

    // FILE STORAGE

    private void loadData() {
        for (String line : FileManager.loadData("students.txt")) {
            String[] p = line.split("\\|", -1);
            if (p.length == 3) {
                students.add(new Student(p[0], p[1], p[2]));
            }
        }

        for (String line : FileManager.loadData("courses.txt")) {
            String[] p = line.split("\\|", -1);
            if (p.length == 5) {
                try {
                    courses.add(new Course(p[0], p[1],
                            Integer.parseInt(p[2]),
                            Integer.parseInt(p[3]), p[4]));
                } catch (NumberFormatException ex) {
                    System.out.println("Invalid course record: " + line);
                }
            }
        }

        for (String line : FileManager.loadData("registrations.txt")) {
            String[] p = line.split("\\|", -1);
            if (p.length == 2) {
                registrations.add(new Registration(p[0], p[1]));
            }
        }
    }

    private void saveStudents() {
        List<String> data = new ArrayList<>();
        for (Student s : students) {
            data.add(s.getStudentId() + "|" + s.getName()
                    + "|" + s.getEmail());
        }
        FileManager.saveData("students.txt", data);
    }

    private void saveCourses() {
        List<String> data = new ArrayList<>();
        for (Course c : courses) {
            data.add(c.getCourseId() + "|" + c.getCourseName()
                    + "|" + c.getCredits() + "|" + c.getCapacity()
                    + "|" + c.getPrerequisite());
        }
        FileManager.saveData("courses.txt", data);
    }

    private void saveRegistrations() {
        List<String> data = new ArrayList<>();
        for (Registration r : registrations) {
            data.add(r.getStudentId() + "|" + r.getCourseId());
        }
        FileManager.saveData("registrations.txt", data);
    }

    // HELPER METHODS

    private void message(String text) {
        JOptionPane.showMessageDialog(this, text);
    }

    private void showText(String title, String content) {
        JTextArea area = new JTextArea(content, 15, 45);
        area.setEditable(false);
        area.setCaretPosition(0);

        JOptionPane.showMessageDialog(
                this, new JScrollPane(area), title,
                JOptionPane.INFORMATION_MESSAGE);
    }
}
