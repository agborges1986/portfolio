package com.kaymansoft.model.elements;

import java.io.Serializable;

public class Image implements Serializable {

	private static final long serialVersionUID = 1L;
	private long id = -1;
	private byte image[] = null;
	
	public Image(byte[] image) {
		super();
		this.image = image;
	}
	public Image(long id, byte[] image) {
		super();
		this.id = id;
		this.image = image;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public byte[] getImage() {
		return image;
	}
	public  void setImage(byte image[]) {
		this.image = image;
	}	

}
