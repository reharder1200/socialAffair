/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.activity.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.activity.entity.SecPay;
import com.thinkgem.jeesite.modules.activity.dao.SecPayDao;

/**
 * 活动支付Service
 * @author 黄亮亮
 * @version 2020-04-03
 */
@Service
@Transactional(readOnly = true)
public class SecPayService extends CrudService<SecPayDao, SecPay> {

	public SecPay get(String id) {
		return super.get(id);
	}
	
	public List<SecPay> findList(SecPay secPay) {
		return super.findList(secPay);
	}
	
	public Page<SecPay> findPage(Page<SecPay> page, SecPay secPay) {
		return super.findPage(page, secPay);
	}
	
	@Transactional(readOnly = false)
	public void save(SecPay secPay) {
		super.save(secPay);
	}
	
	@Transactional(readOnly = false)
	public void delete(SecPay secPay) {
		super.delete(secPay);
	}

	public SecPay getByOutTradeNo(String out_trade_no) {
		// TODO Auto-generated method stub
		return dao.getByOutTradeNo(out_trade_no);
	}
	
}