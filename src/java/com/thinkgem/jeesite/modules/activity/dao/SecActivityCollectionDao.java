/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.activity.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.activity.entity.SecActivityCollection;

/**
 * 活动收藏DAO接口
 * @author 张高旗
 * @version 2020-04-03
 */
@MyBatisDao
public interface SecActivityCollectionDao extends CrudDao<SecActivityCollection> {
	
}