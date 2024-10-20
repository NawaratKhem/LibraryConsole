package Controller;

import java.util.ArrayList;
import Models.Student;
import Provider.Instance;

public class StudentController implements Instance {
    
    private ArrayList<Student> students = new ArrayList<>();

    public StudentController(){
        Student student1 = new Student(1, 1001, 103,"John Doe", "University A");
        student1.numberOfBorrow = 5;
        student1.totalSpent = 200.0;
        students.add(student1);

        Student student2 = new Student(2, 1002, 102,"Jane Smith", "University B");
        student2.numberOfBorrow = 2;
        student2.totalSpent = 50.0;
        students.add(student2);

        Student student3 = new Student(3, 1003, 103,"Alex Johnson", "University C");
        student3.numberOfBorrow = 3;
        student3.totalSpent = 100.0;
        students.add(student3);

        Student student4 = new Student(4, 1004, 101,"Emma Davis", "University D");
        student4.numberOfBorrow = 1;
        student4.totalSpent = 20.0;
        students.add(student4);

        Student student5 = new Student(5, 1005, 102,"Michael Brown", "University E");
        student5.numberOfBorrow = 4;
        student5.totalSpent = 150.0;
        students.add(student5);
    }

    public ArrayList<Student> getStudents(){
        return students;
    }
    
    public ArrayList<Student> getStudents(int teacherId){
        ArrayList<Student> teachers_students = new ArrayList<>();
        for (Student student : students) {
            if(student.teacherId == teacherId){
                teachers_students.add(student);
            }
        }
        return teachers_students;
    }
}
