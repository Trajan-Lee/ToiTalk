package com.algonquin.ToiTalk.model;

public class Language {
	
	private String langname;
	private int langID;
	


	public Language(int ID,String name) {
		this.langID = ID;
		this.langname = name;
	}
	
	//get and sett
	public String getLangName() {
		return langname;
	}
	
	public void setLangName(String name) {
		this.langname = name;
	}
	public int getLangID() {
		return langID;
	}

	public void setLangID(int landID) {
		this.langID = landID;
	}
	
}
