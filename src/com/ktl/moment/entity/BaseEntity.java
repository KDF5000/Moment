/**
 * 数据实体的基类
 * @author KDF5000
 * @date 2015-3-30
 */
package com.ktl.moment.entity;

import java.io.Serializable;

public class BaseEntity implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected long id;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
}
