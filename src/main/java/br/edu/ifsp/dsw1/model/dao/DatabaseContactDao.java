package br.edu.ifsp.dsw1.model.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import br.edu.ifsp.dsw1.model.dao.connection.ContactsDatabaseConnection;
import br.edu.ifsp.dsw1.model.entity.Contact;

public class DatabaseContactDao implements ContactDao {

	@Override
	public boolean create(Contact contact) {
		if (contact != null) {
			var sql = "INSERT INTO tb_contacts (name, fone, email) VALUES ('" 
					+ contact.getName() + "', '" 
					+ contact.getFone() + "', '"
					+ contact.getEmail() + "')";
			
			int rows = -1;
			try {
				var connection = ContactsDatabaseConnection.getConnection();
				var statement = connection.createStatement();
				
				rows = statement.executeUpdate(sql);
				
				statement.close();
				connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
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
			var sql = "SELECT * FROM tb_contacts WHERE email = '" + email + "'";
			
			try {
				var connection = ContactsDatabaseConnection.getConnection();
				var statement = connection.createStatement();
				
				ResultSet result = statement.executeQuery(sql);
				
				if (result.next()) {
					contact = new Contact();
					contact.setEmail(result.getString("email"));
					contact.setFone(result.getString("fone"));
					contact.setName(result.getString("name"));
				}
				
				statement.close();
				connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return contact;
	}

	@Override
	public List<Contact> retrieve() {
		List<Contact> contacts = new LinkedList<Contact>();
		var sql = "SELECT * FROM tb_contacts ORDER BY name";
		
		try {
			var connection = ContactsDatabaseConnection.getConnection();
			var statement = connection.createStatement();
			
			var result = statement.executeQuery(sql);
			
			while(result.next()) {
				var contact = new Contact();
				contact.setEmail(result.getString("email"));
				contact.setFone(result.getString("fone"));
				contact.setName(result.getString("name"));
				contacts.add(contact);
			}
			
			statement.close();
			connection.close();
		} catch (SQLException e) {
			// TODO: handle exception
		}
		
		return contacts;
	}

	@Override
	public boolean update(Contact updatedContact, String oldEmail) {
		if (updatedContact != null && !oldEmail.isEmpty()) {
			var sql = "UPDATE tb_contacts SET " +
					"name = '" + updatedContact.getName() + "', " +
					"fone = '" + updatedContact.getFone() + "', " +
					"email = '" + updatedContact.getEmail() + "' " +
					"WHERE email = '" + oldEmail + "'";
			
			int rows = -1;
			try {
				var connection = ContactsDatabaseConnection.getConnection();
				var statement = connection.createStatement();
				
				rows = statement.executeUpdate(sql);
				
				statement.close();
				connection.close();
			} catch (SQLException e) {
				// TODO: handle exception
			}
			return rows > 0;
		}
		return false;
	}

	@Override
	public boolean delete(Contact contact) {
		if (contact != null) {
			var sql = "DELETE FROM tb_contacts WHERE email = '" + contact.getEmail() + "'";
			int rows = -1;
			try {
				var connection = ContactsDatabaseConnection.getConnection();
				var statement = connection.createStatement();
				
				rows = statement.executeUpdate(sql);
				
				statement.close();
				connection.close();
			} catch (SQLException e) {
				// TODO: handle exception
			}
			return rows > 0;
		}
		return false;
	}

	@Override
	public List<Contact> findByName(String name) {
		var contacts = new LinkedList<Contact>();
		if (name != null && !name.isEmpty()) {
			var sql = "SELECT * FROM tb_contacts WHERE name LIKE '%" + name + "%' ORDER BY name";
			
			try(var conn = ContactsDatabaseConnection.getConnection();
				var stm = conn.createStatement()) {
				
				var result = stm.executeQuery(sql);
				
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
}
