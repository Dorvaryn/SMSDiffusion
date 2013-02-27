package fr.odai.smsdiffusion.adapter;

public class POJOList {
	
	private int id;
	public String name;
	public boolean enable;
	
	public POJOList(int id, String name, boolean enable) {
		super();
		this.id = id;
		this.name = name;
		this.enable = enable;
	}
	

	public int getId() {
		return id;
	}

}
