package br.edu.ifsp.dsw1.model.dao;

public class ContactDaoFactory {
	private ContactDaoType type;
	
	public ContactDaoFactory() { 
		type = ContactDaoType.DATABASE;
	}
	
	public ContactDaoFactory(ContactDaoType type) {
		this.type = type;
	}
	
	public ContactDao factory() {
		switch (type) {
			case MONOSTATE:
				return new MonostateContactDao();
			case JSON:
				return new JsonContactDao();
			case DATABASE:
				return new DatabaseContactDao();
			default:
				throw new IllegalArgumentException("Tipo de contato desconhecido: " + type);
		}
	}

	public enum ContactDaoType {
		MONOSTATE, JSON, DATABASE
	}
}