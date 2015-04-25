package com.ktl.moment.android.component.wheel;

import java.util.ArrayList;
/**
 * @author fs
 * 
 */
public class Area {
	private int id;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	private String province;
	private ArrayList<String> citys;
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public ArrayList<String> getCitys() {
		return citys;
	}
	public void setCitys(ArrayList<String> citys) {
		this.citys = citys;
	}
	@Override
	public String toString() {
		return "Area [id=" + id + ", province=" + province + ", citys=" + citys
				+ "]";
	}
	
}
