package model.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import model.Dependente;
import model.ModelException;
import model.User;

public class MySQLDependenteDAO implements DependenteDAO {

	@Override
	public boolean save(Dependente dependente) throws ModelException {
		DBHandler db = new DBHandler();
		
		String sqlInsert = "INSERT INTO dependentes VALUES (DEFAULT, ?, ?, ?, ?, ?);";
		
		db.prepareStatement(sqlInsert);
		
		db.setString(1, dependente.getNome());
		db.setString(2, dependente.getParentesco());
		db.setDate(3, dependente.getDataNasc() == null ? new Date() : dependente.getDataNasc());
		db.setString(4, dependente.getInfoSaude());
		db.setInt(5, dependente.getUser().getId());
		
		return db.executeUpdate() > 0;	
	}

	@Override
	public boolean update(Dependente dependente) throws ModelException {
		DBHandler db = new DBHandler();
		
		String sqlUpdate = "UPDATE dependentes "
				+ " SET nome = ?, "
				+ " parentesco = ?, "
				+ " dataNasc = ?, "
				+ " infoSaude = ?, "
				+ " user_id = ? "
				+ " WHERE id = ?; "; 
		
		db.prepareStatement(sqlUpdate);
		
		db.setString(1, dependente.getNome());
		db.setString(2, dependente.getParentesco());		
		db.setDate(3, dependente.getDataNasc() == null ? new Date() : dependente.getDataNasc());		
		db.setString(4, dependente.getInfoSaude());		
		db.setInt(5, dependente.getUser().getId());
		db.setInt(6, dependente.getId());
		
		return db.executeUpdate() > 0;
	}

	@Override
	public boolean delete(Dependente dependente) throws ModelException {
		DBHandler db = new DBHandler();
		
		String sqlDelete = " DELETE FROM dependentes "
		         + " WHERE id = ?;";

		db.prepareStatement(sqlDelete);		
		db.setInt(1, dependente.getId());
		
		return db.executeUpdate() > 0;
	}

	@Override
	public List<Dependente> listAll() throws ModelException {
		DBHandler db = new DBHandler();
		
		List<Dependente> dependentes = new ArrayList<Dependente>();
			
		// Declara uma instrução SQL
		String sqlQuery = " SELECT c.id as 'dependente_id', c.*, u.* \n"
				+ " FROM dependentes c \n"
				+ " INNER JOIN users u \n"
				+ " ON c.user_id = u.id;";
		
		db.createStatement();
	
		db.executeQuery(sqlQuery);

		while (db.next()) {
			User user = new User(db.getInt("user_id"));
			user.setName(db.getString("nome"));
			user.setGender(db.getString("sexo"));
			user.setEmail(db.getString("email"));
			
			Dependente dependente = new Dependente(db.getInt("dependente_id"));
			dependente.setNome(db.getString("nome"));
			dependente.setParentesco(db.getString("parentesco"));
			dependente.setDataNasc(db.getDate("dataNasc"));
			dependente.setInfoSaude(db.getString("infoSaude"));
			dependente.setUser(user);
			
			dependentes.add(dependente);
		}
		
		return dependentes;
	}

	@Override
	public Dependente findById(int id) throws ModelException {
		DBHandler db = new DBHandler();
		
		String sql = "SELECT * FROM dependentes WHERE id = ?;";
		
		db.prepareStatement(sql);
		db.setInt(1, id);
		db.executeQuery();
		
		Dependente c = null;
		while (db.next()) {
			c = new Dependente(id);
			c.setNome(db.getString("nome"));
			c.setParentesco(db.getString("parentesco"));
			c.setDataNasc(db.getDate("dataNasc"));
			c.setInfoSaude(db.getString("infoSaude"));
			
			UserDAO userDAO = DAOFactory.createDAO(UserDAO.class); 
			User user = userDAO.findById(db.getInt("user_id"));
			c.setUser(user);
			
			break;
		}
		
		return c;
	}
}
