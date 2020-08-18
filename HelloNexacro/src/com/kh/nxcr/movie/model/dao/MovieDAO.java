package com.kh.nxcr.movie.model.dao;

import static com.kh.common.JDBCTemplate.close;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.kh.nxcr.movie.model.exception.MovieSaveException;
import com.kh.nxcr.movie.model.vo.Movie;


public class MovieDAO {

	private Properties prop = new Properties();
	
	public MovieDAO() {
		try {
			//클래스객체 위치찾기 : 절대경로를 반환한다. 
			// "/" : 루트디렉토리를 절대경로로 URL객체로 반환한다.
			// "./" : 현재디렉토리를 절대경로로 URL객체로 반환한다.
			// "./query.properties : 현재경로의 query.properties파일의 경로를 URL객체로 반환함.
			String fileName = MovieDAO.class.getResource("/movie-query.properties").getPath();
			prop.load(new FileReader(fileName));
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}

	public int insertMovie(Connection conn, Movie movie) {
		int result = 0;
		PreparedStatement pstmt = null;
		String sql = prop.getProperty("insertMovie");
		
		try {
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, movie.getId());
			pstmt.setString(2, movie.getTitle());
			pstmt.setString(3, movie.getPoster());
			pstmt.setString(4, movie.getOutline());
			
			result = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			throw new MovieSaveException("insertMovie 서비스 오류!", e);
		} finally {
			close(pstmt);
		}
		
		return result;
	}

	public int updateMovie(Connection conn, Movie movie) {
		int result = 0;
		PreparedStatement pstmt = null;
		String sql = prop.getProperty("updateMovie");
		
		try {
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, movie.getTitle());
			pstmt.setString(2, movie.getPoster());
			pstmt.setString(3, movie.getOutline());
			pstmt.setString(4, movie.getId());
			
			result = pstmt.executeUpdate();
			
		} catch(SQLException e) {
			throw new MovieSaveException("updateMovie 서비스 오류!", e);
		} finally {
			close(pstmt);
		}
		
		return result;
	}

	public int deleteMovie(Connection conn, String emplId) {
		int result = 0;
		PreparedStatement pstmt = null;
		String sql = prop.getProperty("deleteMovie");
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, emplId);
			
			result = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			throw new MovieSaveException("deleteMovie 서비스 오류!", e);
		} finally {
			close(pstmt);
		}
		
		return result;
	}

	public List<Movie> selectAll(Connection conn) {
		List<Movie> list = null;
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		String sql = prop.getProperty("selectAll");
		
		try {
			pstmt = conn.prepareStatement(sql);
			
			rset = pstmt.executeQuery();
			list = new ArrayList<Movie>();
			
			while(rset.next()) {
				Movie movie = new Movie(
						rset.getString("ID"), 
						rset.getString("TITLE"),
						rset.getString("POSTER"),
						rset.getString("OUTLINE")
					);
				
				list.add(movie);
			}
			
		} catch(SQLException e) {
			throw new MovieSaveException("selectAll 서비스 오류!", e);
		} finally {
			close(rset);
			close(pstmt);
		}
		
		return list;
	}

}
