package model.dao;

import java.util.List;

import model.ModelException;
import model.Dependente;

public interface DependenteDAO {
	boolean save(Dependente dependente) throws ModelException;
	boolean update(Dependente dependente) throws ModelException;
	boolean delete(Dependente dependente) throws ModelException;
	List<Dependente> listAll() throws ModelException;
	Dependente findById(int id) throws ModelException;
}

