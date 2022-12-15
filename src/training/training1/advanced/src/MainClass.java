package training.training1.advanced.src;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class MainClass {
    public static void main(String[] args) throws Exception {
        Set<CourseSelectionRecord> recordSet = new HashSet<>();
        try (Scanner scanner = new Scanner(System.in)) {
            while (scanner.hasNext()) {
                String[] command = scanner.nextLine().split(" ");
                if (command.length != 5) {
                    throw new Exception("command format wrong!");
                }
                String cmd = command[0];
                String studentId = command[1];
                String studentName = command[2];
                String courseId = command[3];
                String courseName = command[4];
                if ("select".equals(cmd)) {
                    recordSet.add(new CourseSelectionRecord(new Student(studentId, studentName), new Course(courseId, courseName)));
                } else if ("unselect".equals(cmd)) {
                    recordSet.remove(new CourseSelectionRecord(new Student(studentId, studentName), new Course(courseId, courseName)));
                } else {
                    throw new Exception("command format wrong!");
                }
            }
        }
        System.out.println(recordSet);
    }
}
