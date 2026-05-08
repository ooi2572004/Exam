package bean;

import java.util.List;

/**
 * 学生1人の全成績情報を表す Bean クラス
 */
public class TestListStudent {

	private String studentNo;
	private String studentName;
	private String classNum;
	private int entYear;

	/** 科目ごとの成績リスト */
	private List<TestRecord> records;

	public String getStudentNo() { return studentNo; }
	public void setStudentNo(String studentNo) { this.studentNo = studentNo; }
	public String getStudentName() { return studentName; }
	public void setStudentName(String studentName) { this.studentName = studentName; }
	public String getClassNum() { return classNum; }
	public void setClassNum(String classNum) { this.classNum = classNum; }
	public int getEntYear() { return entYear; }
	public void setEntYear(int entYear) { this.entYear = entYear; }
	public List<TestRecord> getRecords() { return records; }
	public void setRecords(List<TestRecord> records) { this.records = records; }

	/**
	 * 1行分の成績レコード（科目名・科目コード・回数・点数）
	 */
	public static class TestRecord {
		private String subjectName;
		private String subjectCd;
		private int no;
		private Integer point;

		public String getSubjectName() { return subjectName; }
		public void setSubjectName(String subjectName) { this.subjectName = subjectName; }
		public String getSubjectCd() { return subjectCd; }
		public void setSubjectCd(String subjectCd) { this.subjectCd = subjectCd; }
		public int getNo() { return no; }
		public void setNo(int no) { this.no = no; }
		public Integer getPoint() { return point; }
		public void setPoint(Integer point) { this.point = point; }
	}
}