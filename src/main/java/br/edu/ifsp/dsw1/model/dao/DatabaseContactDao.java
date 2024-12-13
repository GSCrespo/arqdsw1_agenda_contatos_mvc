package br.edu.ifsp.dsw1.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import br.edu.ifsp.dsw1.model.dao.connection.ContactsDatabaseConnection;
import br.edu.ifsp.dsw1.model.entity.Contact;

public class DatabaseContactDao implements ContactDao {
	private static Connection conn;
	
	private static PreparedStatement insertPreparedStatement;
	private static PreparedStatement selectByEmailPreparedStatement;
	private static PreparedStatement selectByNamePreparedStatement;
	private static PreparedStatement selectAllPreparedStatement;
	private static PreparedStatement updatePreparedStatement;
	private static PreparedStatement deletePreparedStatement;
	
	
	public DatabaseContactDao() {
		
		try {
			if (conn == null) {
				conn = ContactsDatabaseConnection.getConnection();
			
				var sql = "INSERT INTO tb_contacts (name, fone, email) VALUES (?, ?, ?)";
				insertPreparedStatement = conn.prepareStatement(sql);
				
				sql = "SELECT * FROM tb_contacts WHERE email = ?";
				selectByEmailPreparedStatement = conn.prepareStatement(sql);
				
				sql = "SELECT * FROM tb_contacts WHERE name LIKE ? ORDER BY name";
				selectByNamePreparedStatement = conn.prepareStatement(sql);
				
				sql = "SELECT * FROM tb_contacts ORDER BY name";
				selectAllPreparedStatement = conn.prepareStatement(sql);
				
				sql = "UPDATE tb_contacts SET name = ?, fone = ?, email = ? WHERE email = ?";
				updatePreparedStatement = conn.prepareStatement(sql);
				
				sql = "DELETE FROM tb_contacts WHERE email = ?";
				deletePreparedStatement = conn.prepareStatement(sql);
			}
			
		} catch (SQLException sqlException) {
			sqlException.printStackTrace();
		}
	}
	

	@Override
	public boolean create(Contact contact) {
		if (contact != null) {
			
			
			int rows = -1;
			try {
				insertPreparedStatement.setString(1, contact.getName());
				insertPreparedStatement.setString(2, contact.getFone());
				insertPreparedStatement.setString(3, contact.getEmail());
				rows = insertPreparedStatement.executeUpdate();
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			return rows > 0;
		}
		return false;
	}

	@Override
	public Contact retrieve(String email) {
		Contact contact = null;
		if (email !=  null && !email.isEmpty() ) {
			try {
				selectByEmailPreparedStatement.setString(1, email);
					
				ResultSet result = selectByEmailPreparedStatement.executeQuery();
				if (result.next()) {
					contact = new Contact();
					contact.setEmail(result.getString("email"));
					contact.setFone(result.getString("fone"));
					contact.setName(result.getString("name"));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return contact;
	}

	@Override
	public List<Contact> retrieve() {
		List<Contact> contacts = new LinkedList<Contact>();
		
		try {
			var result = selectAllPreparedStatement.executeQuery();
			
			while(result.next()) {
				var contact = new Contact();
				contact.setEmail(result.getString("email"));
				contact.setFone(result.getString("fone"));
				contact.setName(result.getString("name"));
				contacts.add(contact);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return contacts;
	}

	@Override
	public List<Contact> findByName(String name) {
		var contacts = new LinkedList<Contact>();
		if (name != null && !name.isEmpty()) {
			try {
				/**
				 * Como o objetivo é usar o LIKE e usar coringas antes de depois do 
				 * parâmetro, insere-se os coringas no parâmetro para ser definido 
				 * no PreparedStatement.
				 */
				name = "%" + name + "%";
				selectByNamePreparedStatement.setString(1, name);
				var result = selectByNamePreparedStatement.executeQuery();
				
				while(result.next()) {
					var contact = new Contact();
					contact.setEmail(result.getString("email"));
					contact.setFone(result.getString("fone"));
					contact.setName(result.getString("name"));
					contacts.add(contact);
				}	
			} catch (SQLException sqlEx) {
				sqlEx.printStackTrace();
				contacts = new LinkedList<Contact>();
			}
		}
		return contacts;
	}

	@Override
	public boolean update(Contact updatedContact, String oldEmail) {
		if (updatedContact != null && !oldEmail.isEmpty()) {
			int rows = -1;
			try {
				updatePreparedStatement.setString(1, updatedContact.getName());
				updatePreparedStatement.setString(2, updatedContact.getFone());
				updatePreparedStatement.setString(3, updatedContact.getEmail());
				updatePreparedStatement.setString(4, oldEmail);
				
				rows = updatePreparedStatement.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return rows > 0;
		}
		return false;
	}

	@Override
	public boolean delete(Contact contact) {
		if (contact != null) {
			int rows = -1;
			try {
				deletePreparedStatement.setString(1, contact.getEmail());
				
				rows = deletePreparedStatement.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return rows > 0;
		}
		return false;
	}

}
