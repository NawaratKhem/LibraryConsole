package Controller;

import java.util.ArrayList;
import Models.Student;
import Provider.Instance;

public class StudentController implements Instance {
    
    private ArrayList<Student> students = new ArrayList<>();

    public StudentController(ArrayList<Student> students){
        this.students = students;
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
