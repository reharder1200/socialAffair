/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.activity.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.activity.entity.SecPlace;
import com.thinkgem.jeesite.modules.activity.dao.SecPlaceDao;

/**
 * 地铁线路Service
 * @author zgq
 * @version 2020-05-12
 */
@Service
@Transactional(readOnly = true)
public class SecPlaceService extends CrudService<SecPlaceDao, SecPlace> {

	public SecPlace get(String id) {
		return super.get(id);
	}
	
	public List<SecPlace> findList(SecPlace secPlace) {
		return super.findList(secPlace);
	}
	
	public Page<SecPlace> findPage(Page<SecPlace> page, SecPlace secPlace) {
		return super.findPage(page, secPlace);
	}
	
	@Transactional(readOnly = false)
	public void save(SecPlace secPlace) {
		super.save(secPlace);
	}
	
	@Transactional(readOnly = false)
	public void delete(SecPlace secPlace) {
		super.delete(secPlace);
	}

	public void placeList(Map<String, Object> map, HttpServletRequest request) {
		// TODO Auto-generated method stub
		String type = request.getParameter("type");
		SecPlace secPlace = new SecPlace();
		secPlace.setType(type);
		List<SecPlace> secPlaceList = findList(secPlace);
		List<Map<String,String>> mapList = new ArrayList<Map<String,String>>();
		for (SecPlace secPlace2 : secPlaceList) {
			Map<String,String> map1 = new HashMap<String, String>();
			map1.put("id", secPlace2.getId());
			map1.put("name", secPlace2.getName());
			mapList.add(map1);
		}
		map.put("list", mapList);
	}

	public void placeList2(Map<String, Object> map, HttpServletRequest request) {
		// TODO Auto-generated method stub
		String parentId = request.getParameter("parentId");
		SecPlace secPlace = new SecPlace();
		SecPlace parent = new SecPlace();
		parent.setId(parentId);
		secPlace.setParent(parent);
		List<SecPlace> secPlaceList = findList(secPlace);
		List<Map<String,String>> mapList = new ArrayList<Map<String,String>>();
		for (SecPlace secPlace2 : secPlaceList) {
			Map<String,String> map1 = new HashMap<String, String>();
			map1.put("id", secPlace2.getId());
			map1.put("name", secPlace2.getName());
			map1.put("latitude", secPlace2.getLatitude());
			map1.put("longitude", secPlace2.getLongitude());
			mapList.add(map1);
		}
		map.put("list", mapList);
	}
	
}