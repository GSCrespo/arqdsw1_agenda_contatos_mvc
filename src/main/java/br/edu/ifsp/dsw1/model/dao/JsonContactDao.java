package br.edu.ifsp.dsw1.model.dao;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.edu.ifsp.dsw1.model.entity.Contact;

public class JsonContactDao implements ContactDao {
	
	private File file;
	private final ObjectMapper mapper;
	private List<Contact> contacts;
	
	public JsonContactDao() {
		mapper = new ObjectMapper();
		
		String path = System.getProperty("catalina.base") + 
				"/webapps/dsw1_agenda_contatos/mvc/data/data_contacts.json";
		file  = new File(path);
		file.getParentFile().mkdirs();
		
	}
	
	
	@Override
	public boolean create(Contact contact) {
		if (contact != null) {
			readJsonFile();
			Contact inDatasource = contacts.stream()
					.filter(c -> c.getEmail().equalsIgnoreCase(contact.getEmail()))
					.findFirst()
					.orElse(null);
			
			if (inDatasource == null) {
				contacts.add(new Contact(contact.getName(), contact.getFone(), contact.getEmail()));
				writeJsonFile();
				return true;
			}
		}
		return false;
		
	}

	@Override
	public Contact retrieve(String email) {
		readJsonFile();
		Contact contact = contacts.stream()
				.filter(c -> c.getEmail().equalsIgnoreCase(email))
				.findAny()
				.orElse(null);
		
		if (contact == null) {
			return null;
		} else {
			return new Contact(contact.getName(), contact.getFone(), contact.getEmail());
		}
	}

	@Override
	public List<Contact> retrieve() {
		readJsonFile();
		return new ArrayList<Contact>(contacts);
	}

	@Override
	public boolean update(Contact updatedContact, String oldEmail) {
		readJsonFile();
		var inDatasource = contacts.stream()
				.filter(c -> c.getEmail().equalsIgnoreCase(oldEmail))
				.findAny()
				.orElse(null);
		
		if (inDatasource != null) {
			inDatasource.setEmail(updatedContact.getEmail());
			inDatasource.setFone(updatedContact.getFone());
			inDatasource.setName(updatedContact.getName());
			writeJsonFile();
			return true;
		}
		
		return false;
	}

	@Override
	public boolean delete(Contact contact) {
		readJsonFile();
		var inDatasouce = contacts.stream()
				.filter(c -> c.getEmail().equalsIgnoreCase(contact.getEmail()))
				.findAny()
				.orElse(null);
		
		if (contacts.remove(inDatasouce) ) {
			writeJsonFile();
			return true;
		}
		
		return false;
		
	}

	private void readJsonFile() {
		try {
			contacts = mapper.readValue(file, 
					mapper.getTypeFactory().constructCollectionType(List.class, Contact.class));
		}catch (Exception e) {
			e.printStackTrace();
			contacts = new LinkedList<Contact>();
		}
	}
	
	private void writeJsonFile() {
		try {
			mapper.writeValue(file, contacts);
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
}