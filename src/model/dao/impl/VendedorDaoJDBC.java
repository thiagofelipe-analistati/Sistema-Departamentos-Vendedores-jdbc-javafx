package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DB;
import db.DbException;
import model.dao.VendedorDao;
import model.entidades.Departamento;
import model.entidades.Vendedor;

public class VendedorDaoJDBC implements VendedorDao{
	// atribto de conex�o
	private Connection conn;
	
	// inje��o de deped�ncias 
	public VendedorDaoJDBC (Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(Vendedor obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
					"INSERT INTO vendedor "
					+"(Name, Email, BirthDate, BaseSalary, DepartmentId)" 
					+ "VALUES "
					+ "(?, ?, ?, ?, ?)",
					Statement.RETURN_GENERATED_KEYS);
			st.setString(1, obj.getNome());
			st.setString(2, obj.getEmail());
			st.setDate(3, new java.sql.Date(obj.getDataNasc().getTime()));
			st.setDouble(4, obj.getSalarioBase());
			st.setInt(5, obj.getDepartamento().getId());
			
			int rowsAffected = st.executeUpdate();
			
			if(rowsAffected> 0) {
				ResultSet rs = st.getGeneratedKeys();
				if(rs.next()){
					int id = rs.getInt(1);
					obj.setId(id);
				}
				DB.closeResultSet(rs);
			}else {
				throw new DbException("Erro inesperado n�o foi alterado nada!");
			} }catch (SQLException e) {
				throw new DbException(e.getMessage());
			}finally {
				// corriginindo fechamento do statement
				//DB.closeConnection();
				DB.closeStatement(st);
			}
			
		
		
	}

	@Override
	public void update(Vendedor obj) { 

		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
					"UPDATE vendedor " 
					+ "SET Name = ?, Email = ?, BirthDate = ?, BaseSalary = ?, DepartmentId = ? "
					+ "WHERE Id = ?"); 
					
			st.setString(1, obj.getNome());
			st.setString(2, obj.getEmail());
			st.setDate(3, new java.sql.Date(obj.getDataNasc().getTime()));
			st.setDouble(4, obj.getSalarioBase());
			st.setInt(5, obj.getDepartamento().getId());
			st.setInt(6, obj.getId());
			
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
			// corre��o erro na tabela.
			st = conn.prepareStatement("DELETE FROM vendedor WHERE Id = ?");
			st.setInt(1, id);
			st.executeUpdate();
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		}finally {
			DB.closeStatement(st);
		}
		
	}

	@Override
	public Vendedor findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT vendedor.*,departamento.Name as DepName "
					+ "FROM vendedor INNER JOIN departamento "
					+ "ON vendedor.DepartmentId = departamento.Id "
					+ "WHERE vendedor.Id = ?");
			
			st.setInt(1, id);
			rs = st.executeQuery();;
			if (rs.next()) {
				Departamento dep = instanciacacaoDepartamento(rs);
				Vendedor obj = instanciacacaoVendedor(rs, dep);
				return obj;
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
	private Vendedor instanciacacaoVendedor(ResultSet rs, Departamento dep) throws SQLException {
		Vendedor obj = new Vendedor();
		obj.setId(rs.getInt("Id"));
		obj.setNome(rs.getString("Name"));
		obj.setEmail(rs.getString("Email"));
		obj.setSalarioBase(rs.getDouble("BaseSalary"));
		obj.setDataNasc(rs.getDate("BirthDate"));  
		obj.setDepartamento(dep);
		return obj;
	}

	private Departamento instanciacacaoDepartamento(ResultSet rs) throws SQLException {
		Departamento dep =new Departamento();
		dep.setId(rs.getInt("DepartmentId"));
		dep.setNome(rs.getString("DepName"));
		// corre��o erro no retorno no objeto.
		return dep;
	}

	@Override
	public List<Vendedor> FindAll() {

		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT vendedor.*,departamento.Name as DepName "
							+ "FROM vendedor INNER JOIN departamento "
							+ "ON vendedor.DepartmentId = departamento.Id "
							+ "ORDER BY Name");

			rs = st.executeQuery();
			List<Vendedor> list = new ArrayList<>();
			Map<Integer, Departamento> map = new HashMap<>();
			while (rs.next()) {
				Departamento dep = map.get(rs.getInt("DepartmentId"));
				if (dep == null) {
				dep = instanciacacaoDepartamento(rs);
				map.put(rs.getInt("DepartmentId"), dep);
				}
				
				Vendedor obj = instanciacacaoVendedor(rs, dep);
				list.add(obj);
			}
			return list;
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
	public List<Vendedor> FindByDepartamento(Departamento departamento) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT vendedor.*,departamento.Name as DepName "
							+ "FROM vendedor INNER JOIN departamento "
							+ "ON vendedor.DepartmentId = departamento.Id "
							+ "WHERE DepartmentId = ? "
							+ "ORDER BY Name");

			st.setInt(1, departamento.getId());
			rs = st.executeQuery();
			List<Vendedor> list = new ArrayList<>();
			Map<Integer, Departamento> map = new HashMap<>();
			while (rs.next()) {
				Departamento dep = map.get(rs.getInt("DepartmentId"));
				if (dep == null) {
				dep = instanciacacaoDepartamento(rs);
				map.put(rs.getInt("DepartmentId"), dep);
				}
				
				Vendedor obj = instanciacacaoVendedor(rs, dep);
				list.add(obj);
			}
			return list;
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

}
