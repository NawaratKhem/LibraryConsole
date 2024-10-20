package Models;

public class Student extends User {

    public int studentId;
    public int teacherId;
    public String universityName;

    public Student(int id, int studentId, int teacherId, String name, String universityName){
        super(id, name);
        this.studentId = studentId;
        this.teacherId = teacherId;
        this.universityName = universityName;
    }
}
