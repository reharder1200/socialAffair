/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.activity.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.activity.entity.SecPlace;

/**
 * 地铁线路DAO接口
 * @author zgq
 * @version 2020-05-12
 */
@MyBatisDao
public interface SecPlaceDao extends CrudDao<SecPlace> {
	
}