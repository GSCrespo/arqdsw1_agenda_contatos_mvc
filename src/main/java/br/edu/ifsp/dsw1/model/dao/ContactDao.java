package br.edu.ifsp.dsw1.model.dao;

import java.util.List;

import br.edu.ifsp.dsw1.model.entity.Contact;

public interface ContactDao {
	
	boolean create(Contact contact);
	
	Contact retrieve(String email);
	
	List<Contact> retrieve();
	
	List<Contact> findByName(String name);
	
	boolean update(Contact updatedContact, String oldEmail);
	
	boolean delete(Contact contact);
}