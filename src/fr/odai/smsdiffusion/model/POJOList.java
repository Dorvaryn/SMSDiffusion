package fr.odai.smsdiffusion.model;

public class POJOList {
	
	private long id;
	public String name;
	public boolean enable;
	
	public POJOList(long id, String name, boolean enable) {
		super();
		this.id = id;
		this.name = name;
		this.enable = enable;
	}
	

	public long getId() {
		return id;
	}

}
