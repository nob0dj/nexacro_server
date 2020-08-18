package com.kh.nxcr.movie.model.vo;

import java.io.Serializable;

public class Movie implements Serializable {

	private String id;
	private String title;
	private String poster;
	private String outline;
	public Movie() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Movie(String id, String title, String poster, String outline) {
		super();
		this.id = id;
		this.title = title;
		this.poster = poster;
		this.outline = outline;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getPoster() {
		return poster;
	}
	public void setPoster(String poster) {
		this.poster = poster;
	}
	public String getOutline() {
		return outline;
	}
	public void setOutline(String outline) {
		this.outline = outline;
	}
	@Override
	public String toString() {
		return "Movie [id=" + id + ", title=" + title + ", poster=" + poster + ", outline=" + outline + "]";
	}
	
	
}
