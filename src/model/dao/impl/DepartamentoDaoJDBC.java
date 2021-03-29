package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import db.DB;
import db.DbException;
import model.dao.DepartamentoDao;
import model.entidades.Departamento;

public class DepartamentoDaoJDBC implements DepartamentoDao {
	
	// atribto de conexão
	private Connection conn;
	
	// injeção de depedências 
	public DepartamentoDaoJDBC (Connection conn) {
		this.conn = conn;
	}
	


	@Override
	public void insert(Departamento obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
					"INSERT INTO departamento "
					+"(Name)" 
					+ "VALUES "
					+ "(?)",
					Statement.RETURN_GENERATED_KEYS);
			st.setString(1, obj.getNome());
			
			int rowsAffected = st.executeUpdate();
			
			if(rowsAffected> 0) {
				ResultSet rs = st.getGeneratedKeys();
				if(rs.next()){
					int id = rs.getInt(1);
					obj.setId(id);
				}
				DB.closeResultSet(rs);
			}else {
				throw new DbException("Erro inesperado não foi alterado nada!");
			} }catch (SQLException e) {
				throw new DbException(e.getMessage());
			}finally {
				// corriginindo fechamento do statement
				//DB.closeConnection();
				DB.closeStatement(st);
			}
			
		
		
	}

	@Override
	public void update(Departamento obj) {

		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
					"UPDATE departamento " 
					+ "SET Name = ? "
					+ "WHERE Id = ?"); 
					
			st.setString(1, obj.getNome());
			st.setInt(2, obj.getId());
			
		 st.executeUpdate();
			
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		
		}

		
	}

	@Override
	public void deleteById(Integer id) {
		PreparedStatement st = null;
		try {
			// correção erro na tabela.
			st = conn.prepareStatement("DELETE FROM departamento WHERE Id = ?");
			st.setInt(1, id);
			st.executeUpdate();
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		}finally {
			DB.closeStatement(st);
		}
		
		
	}

	
	@Override
	public Departamento findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT * from departamento WHERE Id = ?");
			st.setInt(1, id);
			rs = st.executeQuery();;
			if (rs.next()) {
				Departamento dep =  new Departamento();
				dep.setId(rs.getInt("Id"));
				dep.setNome(rs.getString("Name"));
				return dep;
			}
			return null;
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	@Override
	public List<Departamento> FindAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		List<Departamento> list = new ArrayList<>();
		try {
			st = conn.prepareStatement(
					"SELECT * from departamento");

			rs = st.executeQuery();
			while (rs.next()) {
				
				Departamento dep =  new Departamento();
				dep.setId(rs.getInt("Id"));
				dep.setNome(rs.getString("Name"));
				list.add(dep);
			}
			return list;
		}catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
}	
}



