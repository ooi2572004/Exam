package bean;

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
}