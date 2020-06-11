/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.activity.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.activity.entity.SecPay;

/**
 * 活动支付DAO接口
 * @author 黄亮亮
 * @version 2020-04-03
 */
@MyBatisDao
public interface SecPayDao extends CrudDao<SecPay> {

	SecPay getByOutTradeNo(String out_trade_no);
	
}