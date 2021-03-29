package model.service;

import java.util.ArrayList;
import java.util.List;

import model.entidades.Departamento;

public class DepartamentoService {
	public List<Departamento> findAll(){;
		
		List<Departamento> list = new ArrayList<Departamento>();
		list.add(new Departamento(1, "Livros"));
		list.add(new Departamento(2, "Computadores "));
		list.add(new Departamento(3, "Eletronicos"));
		list.add(new Departamento(4, "Roupas"));
		
		return list;
		
	}
}
