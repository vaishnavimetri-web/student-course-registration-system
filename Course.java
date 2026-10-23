
package corejava;

public class Course {
    private String courseId;
    private String courseName;
    private int credits;
    private int capacity;
    private String prerequisite;

    public Course(String courseId, String courseName, int credits,
                  int capacity, String prerequisite) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.credits = credits;
        this.capacity = capacity;
        this.prerequisite = prerequisite;
    }

    public String getCourseId() {
        return courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public int getCredits() {
        return credits;
    }

    public int getCapacity() {
        return capacity;
    }

    public String getPrerequisite() {
        return prerequisite;
    }

    @Override
    public String toString() {
        return courseId + " - " + courseName
                + " - Credits: " + credits
                + " - Capacity: " + capacity
                + " - Prerequisite: " + prerequisite;
    }
}
