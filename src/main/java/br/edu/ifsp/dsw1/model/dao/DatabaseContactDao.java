package br.edu.ifsp.dsw1.model.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import br.edu.ifsp.dsw1.model.dao.connection.ContactsDatabaseConnection;
import br.edu.ifsp.dsw1.model.entity.Contact;

public class DatabaseContactDao implements ContactDao {
	
	public boolean create(Contact contact) {
		var sql = "INSERT INTO tb_contacts (name, fone, email) VALUES (?, ?, ?)";
		if (contact != null) {
			int rows = -1;
			try (var conn = ContactsDatabaseConnection.getConnection();
				 var insertPreparedStatement = conn.prepareStatement(sql)){
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
		var sql = "SELECT * FROM tb_contacts WHERE email = ?";
		Contact contact = null;
		if (email !=  null && !email.isEmpty() ) {
			try(var conn = ContactsDatabaseConnection.getConnection();
				var selectByEmailPreparedStatement = conn.prepareStatement(sql)) {
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
		var sql = "SELECT * FROM tb_contacts ORDER BY name";
		
		try(var conn = ContactsDatabaseConnection.getConnection();
			var selectAllPreparedStatement = conn.prepareStatement(sql)) {
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
		var sql = "SELECT * FROM tb_contacts WHERE name LIKE ? ORDER BY name";
		if (name != null && !name.isEmpty()) {
			try(var conn = ContactsDatabaseConnection.getConnection();
					var selectByNamePreparedStatement = conn.prepareStatement(sql)) {
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
		var sql = "UPDATE tb_contacts SET name = ?, fone = ?, email = ? WHERE email = ?";
		if (updatedContact != null && !oldEmail.isEmpty()) {
			int rows = -1;
			try(var conn = ContactsDatabaseConnection.getConnection();
					var updatePreparedStatement = conn.prepareStatement(sql)) {
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
		var sql = "DELETE FROM tb_contacts WHERE email = ?";
		if (contact != null) {
			int rows = -1;
			try(var conn = ContactsDatabaseConnection.getConnection();
					var deletePreparedStatement = conn.prepareStatement(sql)) {
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
