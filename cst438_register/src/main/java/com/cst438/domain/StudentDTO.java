package com.cst438.domain;

public class StudentDTO {
	public int id;
	public String studentName;
	public String studentEmail;
	public String studentStatus;
	public int studentStatusCode;
	
	@Override
	public String toString() {
		return "StudentDTO [id=" + id + ", studentName=" + studentName + ", studentEmail=" + studentEmail + ", studentStatus=" + studentStatus + ", studentStatusCode=" + studentStatusCode + "]";
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		StudentDTO other = (StudentDTO) obj;
		if (id != other.id)
			return false;
		if (studentName == null) {
			if (other.studentName != null)
				return false;
		} else if (!studentName.equals(other.studentName))
			return false;
		if (studentEmail == null) {
			if (other.studentEmail != null)
				return false;
		} else if (!studentEmail.equals(other.studentEmail))
			return false;
		if (studentStatus == null) {
			if (other.studentStatus != null)
				return false;
		} else if (!studentStatus.equals(other.studentStatus))
			return false;
		if (studentStatusCode != other.studentStatusCode)
			return false;
		return true;
	}
}
