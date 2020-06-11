/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.activity.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.activity.entity.SecImage;
import com.thinkgem.jeesite.modules.activity.dao.SecImageDao;

/**
 * 保存图片Service
 * @author 张高旗
 * @version 2020-04-05
 */
@Service
@Transactional(readOnly = true)
public class SecImageService extends CrudService<SecImageDao, SecImage> {

	public SecImage get(String id) {
		return super.get(id);
	}
	
	public List<SecImage> findList(SecImage secImage) {
		return super.findList(secImage);
	}
	
	public Page<SecImage> findPage(Page<SecImage> page, SecImage secImage) {
		return super.findPage(page, secImage);
	}
	
	@Transactional(readOnly = false)
	public void save(SecImage secImage) {
		super.save(secImage);
	}
	
	@Transactional(readOnly = false)
	public void delete(SecImage secImage) {
		super.delete(secImage);
	}
	
}