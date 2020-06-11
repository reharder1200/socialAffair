package com.thinkgem.jeesite.modules.activity.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.activity.dao.SecPayRefundDao;
import com.thinkgem.jeesite.modules.activity.entity.SecPayRefund;

/**
 * 活动退款支付Service
 * @author 黄亮亮
 * @version 2020-06-06
 */
@Service
@Transactional(readOnly = true)
public class SecPayRefundService extends CrudService<SecPayRefundDao, SecPayRefund> {
	public SecPayRefund get(String id) {
		return super.get(id);
	}
	
	public List<SecPayRefund> findList(SecPayRefund secPayRefund) {
		return super.findList(secPayRefund);
	}
	
	public Page<SecPayRefund> findPage(Page<SecPayRefund> page, SecPayRefund secPayRefund) {
		return super.findPage(page, secPayRefund);
	}
	
	@Transactional(readOnly = false)
	public void save(SecPayRefund secPayRefund) {
		super.save(secPayRefund);
	}
	
	@Transactional(readOnly = false)
	public void delete(SecPayRefund secPayRefund) {
		super.delete(secPayRefund);
	}

	public SecPayRefund getByOutRefundNo(String out_refund_no) {
		// TODO Auto-generated method stub
		return dao.getByOutRefundNo(out_refund_no);
	}
}
