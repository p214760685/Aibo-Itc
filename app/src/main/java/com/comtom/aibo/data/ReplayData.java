package com.comtom.aibo.data;

import java.io.Serializable;

public class ReplayData implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String name;
	public String addr;

	public ReplayData() {

	}

	public ReplayData(String name, String addr) {
		this.name = name;
		this.addr = addr;
	}
}
