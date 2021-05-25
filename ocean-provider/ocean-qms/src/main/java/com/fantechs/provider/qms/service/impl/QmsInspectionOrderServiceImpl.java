package com.fantechs.provider.qms.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.entity.basic.BaseInAndOutRule;
import com.fantechs.common.base.general.entity.basic.BaseInAndOutRuleDet;
import com.fantechs.common.base.general.entity.basic.history.BaseHtInAndOutRule;
import com.fantechs.common.base.general.entity.qms.QmsInspectionOrder;
import com.fantechs.common.base.general.entity.qms.QmsInspectionOrderDet;
import com.fantechs.common.base.general.entity.qms.QmsInspectionOrderDetSample;
import com.fantechs.common.base.general.entity.qms.history.QmsHtInspectionOrder;
import com.fantechs.common.base.general.entity.qms.search.SearchQmsInspectionOrderDet;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CodeUtils;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.qms.mapper.QmsHtInspectionOrderMapper;
import com.fantechs.provider.qms.mapper.QmsInspectionOrderDetMapper;
import com.fantechs.provider.qms.mapper.QmsInspectionOrderDetSampleMapper;
import com.fantechs.provider.qms.mapper.QmsInspectionOrderMapper;
import com.fantechs.provider.qms.service.QmsInspectionOrderService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/05/25.
 */
@Service
public class QmsInspectionOrderServiceImpl extends BaseService<QmsInspectionOrder> implements QmsInspectionOrderService {

    @Resource
    private QmsInspectionOrderMapper qmsInspectionOrderMapper;
    @Resource
    private QmsInspectionOrderDetMapper qmsInspectionOrderDetMapper;
    @Resource
    private QmsHtInspectionOrderMapper qmsHtInspectionOrderMapper;
    @Resource
    private QmsInspectionOrderDetSampleMapper qmsInspectionOrderDetSampleMapper;

    @Override
    public List<QmsInspectionOrder> findList(Map<String, Object> map) {
        List<QmsInspectionOrder> qmsInspectionOrders = qmsInspectionOrderMapper.findList(map);
        SearchQmsInspectionOrderDet searchQmsInspectionOrderDet = new SearchQmsInspectionOrderDet();

        for (QmsInspectionOrder qmsInspectionOrder : qmsInspectionOrders){
            searchQmsInspectionOrderDet.setInspectionOrderId(qmsInspectionOrder.getInspectionOrderId());
            List<QmsInspectionOrderDet> qmsInspectionOrderDets = qmsInspectionOrderDetMapper.findList(ControllerUtil.dynamicConditionByEntity(searchQmsInspectionOrderDet));
            if(StringUtils.isNotEmpty(qmsInspectionOrderDets)){
                qmsInspectionOrder.setQmsInspectionOrderDets(qmsInspectionOrderDets);
            }
        }

        return qmsInspectionOrders;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(QmsInspectionOrder qmsInspectionOrder) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        qmsInspectionOrder.setInspectionOrderCode(CodeUtils.getId("CPJY-"));
        qmsInspectionOrder.setCreateUserId(user.getUserId());
        qmsInspectionOrder.setCreateTime(new Date());
        qmsInspectionOrder.setModifiedUserId(user.getUserId());
        qmsInspectionOrder.setModifiedTime(new Date());
        qmsInspectionOrder.setStatus(StringUtils.isEmpty(qmsInspectionOrder.getStatus())?1:qmsInspectionOrder.getStatus());
        qmsInspectionOrder.setOrgId(user.getOrganizationId());
        int i = qmsInspectionOrderMapper.insertUseGeneratedKeys(qmsInspectionOrder);

        //履历
        QmsHtInspectionOrder qmsHtInspectionOrder = new QmsHtInspectionOrder();
        BeanUtils.copyProperties(qmsInspectionOrder, qmsHtInspectionOrder);
        qmsHtInspectionOrderMapper.insert(qmsHtInspectionOrder);

        //明细
        List<QmsInspectionOrderDet> qmsInspectionOrderDets = qmsInspectionOrder.getQmsInspectionOrderDets();
        if(StringUtils.isNotEmpty(qmsInspectionOrderDets)){
            for (QmsInspectionOrderDet qmsInspectionOrderDet : qmsInspectionOrderDets) {
                qmsInspectionOrderDet.setInspectionOrderId(qmsInspectionOrder.getInspectionOrderId());
                qmsInspectionOrderDet.setCreateUserId(user.getUserId());
                qmsInspectionOrderDet.setCreateTime(new Date());
                qmsInspectionOrderDet.setModifiedUserId(user.getUserId());
                qmsInspectionOrderDet.setModifiedTime(new Date());
                qmsInspectionOrderDet.setStatus(StringUtils.isEmpty(qmsInspectionOrderDet.getStatus())?1:qmsInspectionOrderDet.getStatus());
                qmsInspectionOrderDet.setOrgId(user.getOrganizationId());
            }
            qmsInspectionOrderDetMapper.insertList(qmsInspectionOrderDets);
        }

        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(QmsInspectionOrder qmsInspectionOrder) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        qmsInspectionOrder.setModifiedUserId(user.getUserId());
        qmsInspectionOrder.setModifiedTime(new Date());
        qmsInspectionOrder.setOrgId(user.getOrganizationId());
        int i=qmsInspectionOrderMapper.updateByPrimaryKeySelective(qmsInspectionOrder);

        //履历
        QmsHtInspectionOrder qmsHtInspectionOrder = new QmsHtInspectionOrder();
        BeanUtils.copyProperties(qmsInspectionOrder, qmsHtInspectionOrder);
        qmsHtInspectionOrderMapper.insert(qmsHtInspectionOrder);

        //删除原明细
        Example example1 = new Example(QmsInspectionOrderDet.class);
        Example.Criteria criteria1 = example1.createCriteria();
        criteria1.andEqualTo("inspectionOrderId", qmsInspectionOrder.getInspectionOrderId());
        List<QmsInspectionOrderDet> qmsInspectionOrderDets = qmsInspectionOrderDetMapper.selectByExample(example1);
        for (QmsInspectionOrderDet qmsInspectionOrderDet : qmsInspectionOrderDets){
            //删除检验单明细样本
            Example example2 = new Example(QmsInspectionOrderDetSample.class);
            Example.Criteria criteria2 = example2.createCriteria();
            criteria2.andEqualTo("inspectionOrderDetId", qmsInspectionOrderDet.getInspectionOrderDetId());
            qmsInspectionOrderDetSampleMapper.deleteByExample(example2);
        }
        //删除原检验单明细
        qmsInspectionOrderDetMapper.deleteByExample(example1);

        //新增新的检验单明细
        List<QmsInspectionOrderDet> qmsInspectionOrderDetList = qmsInspectionOrder.getQmsInspectionOrderDets();
        if(StringUtils.isNotEmpty(qmsInspectionOrderDetList)){
            for (QmsInspectionOrderDet qmsInspectionOrderDet : qmsInspectionOrderDetList) {
                qmsInspectionOrderDet.setInspectionOrderId(qmsInspectionOrder.getInspectionOrderId());
                qmsInspectionOrderDet.setCreateUserId(user.getUserId());
                qmsInspectionOrderDet.setCreateTime(new Date());
                qmsInspectionOrderDet.setModifiedUserId(user.getUserId());
                qmsInspectionOrderDet.setModifiedTime(new Date());
                qmsInspectionOrderDet.setOrgId(user.getOrganizationId());
            }
            qmsInspectionOrderDetMapper.insertList(qmsInspectionOrderDetList);
        }

        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDelete(String ids) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        List<QmsHtInspectionOrder> list = new ArrayList<>();
        String[] idArry = ids.split(",");
        for (String id : idArry) {
            QmsInspectionOrder qmsInspectionOrder = qmsInspectionOrderMapper.selectByPrimaryKey(id);
            if(StringUtils.isEmpty(qmsInspectionOrder)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
            QmsHtInspectionOrder qmsHtInspectionOrder = new QmsHtInspectionOrder();
            BeanUtils.copyProperties(qmsInspectionOrder, qmsHtInspectionOrder);
            list.add(qmsHtInspectionOrder);

            Example example1 = new Example(QmsInspectionOrderDet.class);
            Example.Criteria criteria1 = example1.createCriteria();
            criteria1.andEqualTo("inspectionOrderId", qmsInspectionOrder.getInspectionOrderId());
            List<QmsInspectionOrderDet> qmsInspectionOrderDets = qmsInspectionOrderDetMapper.selectByExample(example1);
            for (QmsInspectionOrderDet qmsInspectionOrderDet : qmsInspectionOrderDets){
                //删除检验单明细样本
                Example example2 = new Example(QmsInspectionOrderDetSample.class);
                Example.Criteria criteria2 = example2.createCriteria();
                criteria2.andEqualTo("inspectionOrderDetId", qmsInspectionOrderDet.getInspectionOrderDetId());
                qmsInspectionOrderDetSampleMapper.deleteByExample(example2);
            }
            //删除检验单明细
            qmsInspectionOrderDetMapper.deleteByExample(example1);

        }
        //履历
        qmsHtInspectionOrderMapper.insertList(list);

        return qmsInspectionOrderMapper.deleteByIds(ids);
    }
}
