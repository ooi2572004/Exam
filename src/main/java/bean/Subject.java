package bean;

<<<<<<< HEAD
import java.io.Serializable;

public class Subject implements Serializable {

    private String schoolCd;
    private String subjectCd;
    private String subjectName;

    public Subject() {
    }

    public String getSchoolCd() {
        return schoolCd;
    }

    public void setSchoolCd(String schoolCd) {
        this.schoolCd = schoolCd;
    }

    public String getSubjectCd() {
        return subjectCd;
    }

    public void setSubjectCd(String subjectCd) {
        this.subjectCd = subjectCd;
    }

    public String getSubjectName() {aaa
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }
=======
public class Subject {

	private String subjectCd;
	private String subjectName;
	private School school;

	public String getSubjectCd() { return subjectCd; }
	public void setSubjectCd(String subjectId) { this.subjectCd = subjectId; }
	public String getSubjectName() { return subjectName; }
	public void setSubjectName(String subjectName) { this.subjectName = subjectName; }
	public School getSchool() { return school; }
	public void setSchool(School school) { this.school = school; }
>>>>>>> branch 'master' of https://github.com/ooi2572004/Exam.git
}