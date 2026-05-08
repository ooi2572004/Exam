package bean;

import java.util.Map;

public class TestListStudent {

	private String studentNo;
	private String studentName;
	private String classNum;
	private Map<String, Integer> points; // 科目ID→得点

	public String getStudentNo() { return studentNo; }
	public void setStudentNo(String studentNo) { this.studentNo = studentNo; }
	public String getStudentName() { return studentName; }
	public void setStudentName(String studentName) { this.studentName = studentName; }
	public String getClassNum() { return classNum; }
	public void setClassNum(String classNum) { this.classNum = classNum; }
	public Map<String, Integer> getPoints() { return points; }
	public void setPoints(Map<String, Integer> points) { this.points = points; }

	/** 指定した科目IDの点数を返す（存在しない場合はnull） */
	public Integer getPoint(String subjectId) {
		if (points == null) return null;
		return points.get(subjectId);
	}
}
