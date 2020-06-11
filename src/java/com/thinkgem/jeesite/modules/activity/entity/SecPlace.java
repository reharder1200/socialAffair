/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.activity.entity;

import org.hibernate.validator.constraints.Length;
import com.fasterxml.jackson.annotation.JsonBackReference;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 地铁线路Entity
 * @author zgq
 * @version 2020-05-12
 */
public class SecPlace extends DataEntity<SecPlace> {
	
	private static final long serialVersionUID = 1L;
	private String name;		// 地点名称
	private SecPlace parent;		// parent_id
	private String longitude;		// 经度
	private String latitude;		// 纬度
	private String type;
	
	public SecPlace() {
		super();
	}

	public SecPlace(String id){
		super(id);
	}

	@Length(min=0, max=100, message="地点名称长度必须介于 0 和 100 之间")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@JsonBackReference
	public SecPlace getParent() {
		return parent;
	}

	public void setParent(SecPlace parent) {
		this.parent = parent;
	}
	
	@Length(min=0, max=100, message="经度长度必须介于 0 和 100 之间")
	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	
	@Length(min=0, max=100, message="纬度长度必须介于 0 和 100 之间")
	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
}