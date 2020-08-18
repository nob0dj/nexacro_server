package com.kh.nxcr.movie.model.service;

import static com.kh.common.JDBCTemplate.close;
import static com.kh.common.JDBCTemplate.commit;
import static com.kh.common.JDBCTemplate.getConnection;
import static com.kh.common.JDBCTemplate.rollback;

import java.sql.Connection;
import java.sql.Date;
import java.util.List;

import com.kh.nxcr.empl.model.exception.EmployeeSaveException;
import com.kh.nxcr.empl.model.vo.Employee;
import com.kh.nxcr.movie.model.dao.MovieDAO;
import com.kh.nxcr.movie.model.exception.MovieSaveException;
import com.kh.nxcr.movie.model.vo.Movie;
import com.nexacro17.xapi.data.DataSet;

public class MovieService {

	MovieDAO movieDAO = new MovieDAO();

	public void saveMovies(DataSet ds) {
		Connection conn = getConnection();

		try{
			//1.insert | update
			for(int i = 0; i < ds.getRowCount(); i++) {
				int result = 0;

				//rowType 구분
				//0. ROW_TYPE_NORMAL
				//1. ROW_TYPE_INSERTED
				//2. ROW_TYPE_UPDATED
				int rowType = ds.getRowType(i);
				
				//변경되지 않은 내역은 건너뛴다.
				if(rowType == DataSet.ROW_TYPE_NORMAL)
					continue;
				
				//dataset.row -> Employee객체
				Movie movie = new Movie();
				movie.setId(ds.getString(i, "id"));
				movie.setTitle(ds.getString(i, "title"));
				movie.setPoster(ds.getString(i, "poster"));
				movie.setOutline(ds.getString(i, "outline"));
				
				
				//rowType에 따른 분기처리
				//행추가
				if(rowType == DataSet.ROW_TYPE_INSERTED)
					result = movieDAO.insertMovie(conn, movie);
				//행수정
				else if(rowType == DataSet.ROW_TYPE_UPDATED) 
					result = movieDAO.updateMovie(conn, movie);

				//정상처리 되지 않은 경우
				if(result == 0)
					throw new MovieSaveException("영화정보 저장 오류 : " + rowType);

			}
			
			//2. delete
			//empl_id
			for(int i = 0; i < ds.getRemovedRowCount(); i++) {
				String id = (String)ds.getRemovedData(i, "ID");
				int result = movieDAO.deleteMovie(conn, id);
				
				if(result == 0) 
					throw new MovieSaveException("사원 정보 삭제 오류 ");
			}
			//트랜잭션 처리
			commit(conn);
			
		} catch(Exception e) {
			//하나라도 오류가 발생하면 전체 롤백처리
			rollback(conn);
			
			//다시 예외를 던져서 controller에서 분기처리하도록함.
			throw e;
		}
	}
	

	public List<Movie> selectAll() {
		Connection conn = getConnection();
		List<Movie> list = movieDAO.selectAll(conn);
		close(conn);
		return list;
	}
	
}
