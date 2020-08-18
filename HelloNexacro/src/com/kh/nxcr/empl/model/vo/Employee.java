package com.kh.nxcr.empl.model.vo;

import java.io.Serializable;
import java.sql.Date;

public class Employee implements Serializable {

	private String emplId;
	private String fullName;
	private String deptCode;
	private String posCode;
	private Date hireDate;
	private int salary;
	private String gender;
	private boolean married;
	private String memo;
	
	public Employee() {
		super();
	}

	public Employee(String emplId, String fullName, String deptCode, String posCode, Date hireDate, int salary,
			String gender, boolean married, String memo) {
		super();
		this.emplId = emplId;
		this.fullName = fullName;
		this.deptCode = deptCode;
		this.posCode = posCode;
		this.hireDate = hireDate;
		this.salary = salary;
		this.gender = gender;
		this.married = married;
		this.memo = memo;
	}

	public String getEmplId() {
		return emplId;
	}

	public void setEmplId(String emplId) {
		this.emplId = emplId;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getDeptCode() {
		return deptCode;
	}

	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}

	public String getPosCode() {
		return posCode;
	}

	public void setPosCode(String posCode) {
		this.posCode = posCode;
	}

	public Date getHireDate() {
		return hireDate;
	}

	public void setHireDate(Date hireDate) {
		this.hireDate = hireDate;
	}

	public int getSalary() {
		return salary;
	}

	public void setSalary(int salary) {
		this.salary = salary;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public boolean isMarried() {
		return married;
	}

	public void setMarried(boolean married) {
		this.married = married;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	@Override
	public String toString() {
		return "Employee [emplId=" + emplId + ", fullName=" + fullName + ", deptCode=" + deptCode + ", posCode="
				+ posCode + ", hireDate=" + hireDate + ", salary=" + salary + ", gender=" + gender + ", married="
				+ married + ", memo=" + memo + "]";
	}
	
	
	
}
