package model.service;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.DepartamentoDao;
import model.entidades.Departamento;

public class DepartamentoService {
	
	private DepartamentoDao dao = DaoFactory.createDepartamentoDao();
	
	public List<Departamento> findAll(){;
		
		return dao.FindAll();
		
	}
}
