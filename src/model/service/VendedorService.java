package model.service;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.VendedorDao;
import model.entidades.Vendedor;

public class VendedorService {
	
	private VendedorDao dao = DaoFactory.createVendedordao();
	
	public List<Vendedor> findAll(){;
		
		return dao.FindAll();
		
	}
	public void saveorUpdate (Vendedor obj) {
		if(obj.getId()== null) {
			dao.insert(obj);
		}else {
			dao.update(obj);
		}
	}
	public void remove (Vendedor obj) {
		dao.deleteById(obj.getId());
	}
}
