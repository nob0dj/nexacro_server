package com.kh.nxcr.celeb.model.vo;

import java.io.Serializable;

public class Celeb implements Serializable {

	private int no;
	private String name;
	private String kind;
	private String profile;
	
	public Celeb() {
		super();
	}

	public Celeb(int no, String name, String kind, String profile) {
		super();
		this.no = no;
		this.name = name;
		this.kind = kind;
		this.profile = profile;
	}

	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getKind() {
		return kind;
	}

	public void setKind(String kind) {
		this.kind = kind;
	}

	public String getProfile() {
		return profile;
	}

	public void setProfile(String profile) {
		this.profile = profile;
	}

	@Override
	public String toString() {
		return "Celeb [no=" + no + ", name=" + name + ", kind=" + kind + ", profile=" + profile + "]";
	}
	
	
	
}
