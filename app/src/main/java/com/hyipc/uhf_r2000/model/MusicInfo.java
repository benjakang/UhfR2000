package com.hyipc.uhf_r2000.model;

public class MusicInfo {
	private String strMusicName;
	private int iMusicResureId;
	
	public MusicInfo(String strMusicName,int iMusicResureId){
		this.strMusicName = strMusicName;
		this.iMusicResureId = iMusicResureId;
	}
	
	public String getStrMusicName() {
		return strMusicName;
	}
	public void setStrMusicName(String strMusicName) {
		this.strMusicName = strMusicName;
	}
	public int getiMusicResureId() {
		return iMusicResureId;
	}
	public void setiMusicResureId(int iMusicResureId) {
		this.iMusicResureId = iMusicResureId;
	}
}
