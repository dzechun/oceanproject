package com.fantechs.provider.imes.apply.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.apply.SmtWorkOrder;
import com.fantechs.common.base.entity.apply.history.SmtHtWorkOrder;
import com.fantechs.common.base.entity.apply.search.SearchSmtWorkOrder;
import com.fantechs.common.base.entity.basic.SmtProductBom;
import com.fantechs.common.base.entity.basic.SmtProductBomDet;
import com.fantechs.common.base.entity.basic.history.SmtHtProductBom;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.imes.apply.mapper.SmtHtWorkOrderMapper;
import com.fantechs.provider.imes.apply.mapper.SmtWorkOrderMapper;
import com.fantechs.provider.imes.apply.service.SmtWorkOrderService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * Created by wcz on 2020/10/13.
 */
@Service
public class SmtWorkOrderServiceImpl extends BaseService<SmtWorkOrder> implements SmtWorkOrderService {

        @Resource
        private SmtWorkOrderMapper smtWorkOrderMapper;
        @Resource
        private SmtHtWorkOrderMapper smtHtWorkOrderMapper;

        @Override
        @Transactional(rollbackFor = Exception.class)
        public int save(SmtWorkOrder smtWorkOrder) {
            SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
            if(StringUtils.isEmpty(currentUser)){
                throw new BizErrorException(ErrorCodeEnum.UAC10011039);
            }

            Example example = new Example(SmtWorkOrder.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("workOrderCode",smtWorkOrder.getWorkOrderCode());

            List<SmtWorkOrder> smtWorkOrders = smtWorkOrderMapper.selectByExample(example);
            if(StringUtils.isNotEmpty(smtWorkOrders)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012001);
            }

            smtWorkOrder.setCreateUserId(currentUser.getUserId());
            smtWorkOrder.setCreateTime(new Date());
            smtWorkOrderMapper.insertUseGeneratedKeys(smtWorkOrder);

            //新增工单历史信息
            SmtHtWorkOrder smtHtWorkOrder=new SmtHtWorkOrder();
            BeanUtils.copyProperties(smtWorkOrder,smtHtWorkOrder);
            int i = smtHtWorkOrderMapper.insertSelective(smtHtWorkOrder);
            return i;
        }

        @Override
        @Transactional(rollbackFor = Exception.class)
        public int update(SmtWorkOrder smtWorkOrder) {
            SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
            if(StringUtils.isEmpty(currentUser)){
                throw new BizErrorException(ErrorCodeEnum.UAC10011039);
            }

            Example example = new Example(SmtWorkOrder.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("workOrderCode",smtWorkOrder.getWorkOrderCode());

            SmtWorkOrder workOrder = smtWorkOrderMapper.selectOneByExample(example);

            if(StringUtils.isNotEmpty(workOrder)&&!workOrder.getWorkOrderId().equals(smtWorkOrder.getWorkOrderId())){
                throw new BizErrorException(ErrorCodeEnum.OPT20012001);
            }

            smtWorkOrder.setModifiedUserId(currentUser.getUserId());
            smtWorkOrder.setModifiedTime(new Date());
            int i= smtWorkOrderMapper.updateByPrimaryKeySelective(smtWorkOrder);

            //新增工单历史信息
            SmtHtWorkOrder smtHtWorkOrder=new SmtHtWorkOrder();
            BeanUtils.copyProperties(smtWorkOrder,smtHtWorkOrder);
            smtHtWorkOrder.setModifiedUserId(currentUser.getUserId());
            smtHtWorkOrder.setModifiedTime(new Date());
            smtHtWorkOrderMapper.insertSelective(smtHtWorkOrder);
            return i;
        }

        @Override
        @Transactional(rollbackFor = Exception.class)
        public int batchDelete(String ids) {
            List<SmtHtWorkOrder> list=new ArrayList<>();

            SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
            if(StringUtils.isEmpty(currentUser)){
                throw new BizErrorException(ErrorCodeEnum.UAC10011039);
            }

            String[] workOrderIds = ids.split(",");
            for (String workOrderId : workOrderIds) {
                SmtWorkOrder smtWorkOrder = smtWorkOrderMapper.selectByPrimaryKey(workOrderId);
                if(StringUtils.isEmpty(smtWorkOrder)){
                    throw new BizErrorException(ErrorCodeEnum.OPT20012003);
                }
                //新增工单历史信息
                SmtHtWorkOrder smtHtWorkOrder=new SmtHtWorkOrder();
                BeanUtils.copyProperties(smtWorkOrder,smtHtWorkOrder);
                smtHtWorkOrder.setModifiedUserId(currentUser.getUserId());
                smtHtWorkOrder.setModifiedTime(new Date());
                list.add(smtHtWorkOrder);

            }
            smtHtWorkOrderMapper.insertList(list);

            return smtWorkOrderMapper.deleteByIds(ids);
        }


        @Override
        public List<SmtWorkOrder> findList(SearchSmtWorkOrder searchSmtWorkOrder) {
            return smtWorkOrderMapper.findList(searchSmtWorkOrder);
        }
}
