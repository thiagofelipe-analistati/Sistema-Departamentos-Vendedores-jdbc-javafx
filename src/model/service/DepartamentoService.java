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
	public void saveorUpdate (Departamento obj) {
		if(obj.getId()== null) {
			dao.insert(obj);
		}else {
			dao.update(obj);
		}
	}
	public void remove (Departamento obj) {
		dao.deleteById(obj.getId());
	}
}
