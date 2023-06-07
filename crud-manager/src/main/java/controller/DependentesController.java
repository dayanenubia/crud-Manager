package controller;

import java.io.IOException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import model.Dependente;
import model.ModelException;
import model.User;
import model.dao.DAOFactory;
import model.dao.DependenteDAO;


@WebServlet(urlPatterns = {"/dependentes", "/dependente/form", "/dependente/insert", "/dependente/delete", "/dependente/update"})
public class DependentesController extends HttpServlet{
	
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String action = req.getRequestURI();
		
		switch (action) {
			case "/crud-manager/dependente/form": {
				CommonsController.listUsers(req);
				req.setAttribute("action", "insert");
				ControllerUtil.forward(req, resp, "/form-dependente.jsp");
				break;
			}
			
			case "/crud-manager/dependente/update": {
				
				String idStr = req.getParameter("dependenteId");
				int idDependente = Integer.parseInt(idStr);
				
				DependenteDAO dao = DAOFactory.createDAO(DependenteDAO.class);
				
				Dependente dependente = null;
				try {
					dependente = dao.findById(idDependente);
				} catch (ModelException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				CommonsController.listUsers(req);
				req.setAttribute("action", "update");
				req.setAttribute("dependente", dependente);
				ControllerUtil.forward(req, resp, "/form-dependente.jsp");
				break;
			}
			
			default:
				
				listDependentes(req);
				
				ControllerUtil.transferSessionMessagesToRequest(req);
				
				ControllerUtil.forward(req, resp, "/dependentes.jsp");

		}
	}
	
	private void listDependentes(HttpServletRequest req) {
		DependenteDAO dao = DAOFactory.createDAO(DependenteDAO.class);
		
		List<Dependente> dependentes = null;
		try {
			dependentes = dao.listAll();
		} catch (ModelException e) {
			// Log no servidor
			e.printStackTrace();
		}
		
		if (dependentes != null)
			req.setAttribute("dependentes", dependentes);
		
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String action = req.getRequestURI();
	
		switch (action) {
			case "/crud-manager/dependente/insert":
				insertDependente(req,resp);
				break;
			case "/crud-manager/dependente/delete":
				deleteDependente(req, resp);
				break;
			case "/crud-manager/dependente/update":
				updateDependente(req, resp);
				break;
			default:
				System.out.println("URL inválida" + action);
		}
		
		ControllerUtil.redirect(resp, req.getContextPath() + "/dependentes");

	}
	
	private void updateDependente(HttpServletRequest req, HttpServletResponse resp) {
		String dependenteId = req.getParameter("dependenteId");
		String nome = req.getParameter("nome");
		String parentesco = req.getParameter("parentesco");
		String dataNasc = req.getParameter("dataNasc");
		String infoSaude = req.getParameter("infoSaude");
		Integer userId = Integer.parseInt(req.getParameter("user"));
		
		Dependente dependente = new Dependente(Integer.parseInt(dependenteId));
		dependente.setNome(nome);
		dependente.setParentesco(parentesco);
		dependente.setDataNasc(ControllerUtil.formatDate(dataNasc));
		dependente.setInfoSaude(infoSaude);
		dependente.setUser(new User(userId));
		
		DependenteDAO dao = DAOFactory.createDAO(DependenteDAO.class);
		
		try {
			if (dao.update(dependente)) {
				ControllerUtil.sucessMessage(req, "Dependente '" + dependente.getNome() + "' atualizado com sucesso.");
			}
			else {
				ControllerUtil.errorMessage(req, "Dependente '" + dependente.getNome() + "' não pode ser atualizado.");
			}				
		} catch (ModelException e) {
			// log no servidor
			e.printStackTrace();
			ControllerUtil.errorMessage(req, e.getMessage());
		}
		
	}

	private void deleteDependente(HttpServletRequest req, HttpServletResponse resp) {
		String dependenteIdParameter = req.getParameter("id");
		
		int dependenteId = Integer.parseInt(dependenteIdParameter);
		
		DependenteDAO dao = DAOFactory.createDAO(DependenteDAO.class);
		
		try {
			Dependente dependente = dao.findById(dependenteId);
			
			if (dependente == null)
				throw new ModelException("Dependete não encontrado para deleção.");
			
			if (dao.delete(dependente)) {
				ControllerUtil.sucessMessage(req, "Dependete '" + dependente.getNome() + "' deletado com sucesso.");
			}
			else {
				ControllerUtil.errorMessage(req, "Dependete '" + dependente.getNome() + "' não pode ser deletado. Há dados relacionados ao dependente.");
			}
		} catch (ModelException e) {
			// log no servidor
			if (e.getCause() instanceof SQLIntegrityConstraintViolationException) {
				ControllerUtil.errorMessage(req, e.getMessage());
			}
			e.printStackTrace();
			ControllerUtil.errorMessage(req, e.getMessage());
		}
		
	}

	private void insertDependente(HttpServletRequest req, HttpServletResponse resp) {
		String nome = req.getParameter("nome");
		String parentesco = req.getParameter("parentesco");
		String dataNasc = req.getParameter("dataNasc");
		String infoSaude = req.getParameter("infoSaude");
		Integer userId = Integer.parseInt(req.getParameter("user"));
		
		Dependente dependente = new Dependente();
		dependente.setNome(nome);
		dependente.setParentesco(parentesco);
		dependente.setDataNasc(ControllerUtil.formatDate(dataNasc));
		dependente.setInfoSaude(infoSaude);
		dependente.setUser(new User(userId));
		
		DependenteDAO dao = DAOFactory.createDAO(DependenteDAO.class);
		
		try {
			if (dao.save(dependente)) {
				ControllerUtil.sucessMessage(req, "Dependente '" + dependente.getNome() + "' salvo com sucesso.");
			}
			else {
				ControllerUtil.errorMessage(req, "Dependente '" + dependente.getNome() + "' não pode ser salvo.");
			}
		} catch (ModelException e) {
			e.printStackTrace();
			ControllerUtil.errorMessage(req, e.getMessage());
		}
		
	}
	
}
