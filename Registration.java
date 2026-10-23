
package corejava;

public class Registration {
    private String studentId;
    private String courseId;

    public Registration(String studentId, String courseId) {
        this.studentId = studentId;
        this.courseId = courseId;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getCourseId() {
        return courseId;
    }

    @Override
    public String toString() {
        return "Student ID: " + studentId
                + " | Course ID: " + courseId;
    }
}
