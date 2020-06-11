package com.thinkgem.jeesite.modules.activity.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.activity.entity.SecPayRefund;

/**
 * 活动退款DAO接口
 * @author 黄亮亮
 * @version 2020-06-06
 */
@MyBatisDao
public interface SecPayRefundDao extends CrudDao<SecPayRefund> {

	SecPayRefund getByOutRefundNo(String out_refund_no);

}
