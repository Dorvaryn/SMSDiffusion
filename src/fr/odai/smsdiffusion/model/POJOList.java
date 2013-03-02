package fr.odai.smsdiffusion.model;

import android.content.Context;
import fr.odai.smsdiffusion.db.DBHelper;

public class POJOList {
	
	private long id;
	public String name;
	public boolean enable;
	public long totalMessageSent;
	public long lastSentDate;
	public String lastMessage;
	
	public POJOList(long id, String name, boolean enable, long totalMessageSent) {
		super();
		this.id = id;
		this.name = name;
		this.enable = enable;
		this.totalMessageSent = totalMessageSent;
	}
	
	public long getId() {
		return id;
	}
	
	public boolean isInit(Context context){
		boolean hasKW = !DBHelper.getKeywords(context, id).isEmpty();
		boolean hasContacts = !DBHelper.getContactsPhoneOnly(context, id).isEmpty();
		if(hasKW && hasContacts){
			return true;
		}
		return false;
	}
	
	public long getNbContacts(Context context){
		return DBHelper.getContactsPhoneOnly(context, id).size();
	}
}
