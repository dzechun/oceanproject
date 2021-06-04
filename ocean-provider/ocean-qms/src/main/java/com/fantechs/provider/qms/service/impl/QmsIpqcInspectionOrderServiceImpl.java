package com.fantechs.provider.qms.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.entity.basic.BaseSampleProcess;
import com.fantechs.common.base.general.entity.qms.QmsInspectionOrderDet;
import com.fantechs.common.base.general.entity.qms.QmsIpqcInspectionOrder;
import com.fantechs.common.base.general.entity.qms.QmsIpqcInspectionOrderDet;
import com.fantechs.common.base.general.entity.qms.history.QmsHtIpqcInspectionOrder;
import com.fantechs.common.base.general.entity.qms.search.SearchQmsInspectionOrderDet;
import com.fantechs.common.base.general.entity.qms.search.SearchQmsIpqcInspectionOrderDet;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CodeUtils;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.qms.mapper.QmsHtIpqcInspectionOrderMapper;
import com.fantechs.provider.qms.mapper.QmsIpqcInspectionOrderDetMapper;
import com.fantechs.provider.qms.mapper.QmsIpqcInspectionOrderMapper;
import com.fantechs.provider.qms.service.QmsIpqcInspectionOrderService;
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
 * Created by leifengzhi on 2021/06/02.
 */
@Service
public class QmsIpqcInspectionOrderServiceImpl extends BaseService<QmsIpqcInspectionOrder> implements QmsIpqcInspectionOrderService {

    @Resource
    private QmsIpqcInspectionOrderMapper qmsIpqcInspectionOrderMapper;
    @Resource
    private QmsIpqcInspectionOrderDetMapper qmsIpqcInspectionOrderDetMapper;
    @Resource
    private QmsHtIpqcInspectionOrderMapper qmsHtIpqcInspectionOrderMapper;
    @Resource
    private BaseFeignApi baseFeignApi;

    @Override
    public List<QmsIpqcInspectionOrder> findList(Map<String, Object> map) {
        List<QmsIpqcInspectionOrder> qmsIpqcInspectionOrders = qmsIpqcInspectionOrderMapper.findList(map);
        SearchQmsIpqcInspectionOrderDet searchIpqcQmsInspectionOrderDet = new SearchQmsIpqcInspectionOrderDet();

        for (QmsIpqcInspectionOrder qmsIpqcInspectionOrder:qmsIpqcInspectionOrders){
            searchIpqcQmsInspectionOrderDet.setIpqcInspectionOrderId(qmsIpqcInspectionOrder.getIpqcInspectionOrderId());
            List<QmsIpqcInspectionOrderDet> qmsIpqcInspectionOrderDets = qmsIpqcInspectionOrderDetMapper.findList(ControllerUtil.dynamicConditionByEntity(searchIpqcQmsInspectionOrderDet));
            if(StringUtils.isNotEmpty(qmsIpqcInspectionOrderDets)){
                for (QmsIpqcInspectionOrderDet qmsIpqcInspectionOrderDet : qmsIpqcInspectionOrderDets){
                    //抽样类型为抽样方案时，去抽样方案取AC、RE、样本数
                    if(qmsIpqcInspectionOrderDet.getSampleProcessType()!=null&&qmsIpqcInspectionOrderDet.getSampleProcessType()==(byte)4){
                        BaseSampleProcess baseSampleProcess = baseFeignApi.getAcReQty(qmsIpqcInspectionOrderDet.getSampleProcessId(), qmsIpqcInspectionOrder.getQty()).getData();
                        qmsIpqcInspectionOrderDet.setSampleQty(baseSampleProcess.getSampleQty());
                        qmsIpqcInspectionOrderDet.setAcValue(baseSampleProcess.getAcValue());
                        qmsIpqcInspectionOrderDet.setReValue(baseSampleProcess.getReValue());
                    }
                }
                qmsIpqcInspectionOrder.setQmsIpqcInspectionOrderDets(qmsIpqcInspectionOrderDets);
            }
        }

        return qmsIpqcInspectionOrders;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int save(QmsIpqcInspectionOrder qmsIpqcInspectionOrder) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        //新增IPQC检验单
        qmsIpqcInspectionOrder.setIpqcInspectionOrderCode(CodeUtils.getId("IPQC-"));
        qmsIpqcInspectionOrder.setCreateUserId(user.getUserId());
        qmsIpqcInspectionOrder.setCreateTime(new Date());
        qmsIpqcInspectionOrder.setModifiedUserId(user.getUserId());
        qmsIpqcInspectionOrder.setModifiedTime(new Date());
        qmsIpqcInspectionOrder.setStatus(StringUtils.isEmpty(qmsIpqcInspectionOrder.getStatus())?1:qmsIpqcInspectionOrder.getStatus());
        qmsIpqcInspectionOrder.setOrgId(user.getOrganizationId());
        int i = qmsIpqcInspectionOrderMapper.insertUseGeneratedKeys(qmsIpqcInspectionOrder);

        //新增IPQC检验单明细
        List<QmsIpqcInspectionOrderDet> qmsIpqcInspectionOrderDets = qmsIpqcInspectionOrder.getQmsIpqcInspectionOrderDets();
        if(StringUtils.isNotEmpty(qmsIpqcInspectionOrderDets)){
            for (QmsIpqcInspectionOrderDet qmsIpqcInspectionOrderDet:qmsIpqcInspectionOrderDets){
                qmsIpqcInspectionOrderDet.setIpqcInspectionOrderId(qmsIpqcInspectionOrder.getIpqcInspectionOrderId());
                qmsIpqcInspectionOrderDet.setCreateUserId(user.getUserId());
                qmsIpqcInspectionOrderDet.setCreateTime(new Date());
                qmsIpqcInspectionOrderDet.setModifiedUserId(user.getUserId());
                qmsIpqcInspectionOrderDet.setModifiedTime(new Date());
                qmsIpqcInspectionOrderDet.setStatus(StringUtils.isEmpty(qmsIpqcInspectionOrderDet.getStatus())?1:qmsIpqcInspectionOrderDet.getStatus());
                qmsIpqcInspectionOrderDet.setOrgId(user.getOrganizationId());
            }
            qmsIpqcInspectionOrderDetMapper.insertList(qmsIpqcInspectionOrderDets);
        }

        //履历
        QmsHtIpqcInspectionOrder qmsHtIpqcInspectionOrder = new QmsHtIpqcInspectionOrder();
        BeanUtils.copyProperties(qmsIpqcInspectionOrder, qmsHtIpqcInspectionOrder);
        qmsHtIpqcInspectionOrderMapper.insert(qmsHtIpqcInspectionOrder);

        return i;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int update(QmsIpqcInspectionOrder qmsIpqcInspectionOrder) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        //修改IPQC检验单
        qmsIpqcInspectionOrder.setModifiedUserId(user.getUserId());
        qmsIpqcInspectionOrder.setModifiedTime(new Date());
        qmsIpqcInspectionOrder.setOrgId(user.getOrganizationId());
        int i=qmsIpqcInspectionOrderMapper.updateByPrimaryKeySelective(qmsIpqcInspectionOrder);

        //删除原有IPQC检验单明细
        Example example = new Example(QmsIpqcInspectionOrderDet.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("ipqcInspectionOrderId", qmsIpqcInspectionOrder.getIpqcInspectionOrderId());
        qmsIpqcInspectionOrderDetMapper.deleteByExample(example);

        //新增新的IPQC检验单明细
        List<QmsIpqcInspectionOrderDet> qmsIpqcInspectionOrderDets = qmsIpqcInspectionOrder.getQmsIpqcInspectionOrderDets();
        if(StringUtils.isNotEmpty(qmsIpqcInspectionOrderDets)){
            for (QmsIpqcInspectionOrderDet qmsIpqcInspectionOrderDet:qmsIpqcInspectionOrderDets){
                qmsIpqcInspectionOrderDet.setIpqcInspectionOrderId(qmsIpqcInspectionOrder.getIpqcInspectionOrderId());
                qmsIpqcInspectionOrderDet.setCreateUserId(user.getUserId());
                qmsIpqcInspectionOrderDet.setCreateTime(new Date());
                qmsIpqcInspectionOrderDet.setModifiedUserId(user.getUserId());
                qmsIpqcInspectionOrderDet.setModifiedTime(new Date());
                qmsIpqcInspectionOrderDet.setStatus(StringUtils.isEmpty(qmsIpqcInspectionOrderDet.getStatus())?1:qmsIpqcInspectionOrderDet.getStatus());
                qmsIpqcInspectionOrderDet.setOrgId(user.getOrganizationId());
            }
            qmsIpqcInspectionOrderDetMapper.insertList(qmsIpqcInspectionOrderDets);
        }

        //履历
        QmsHtIpqcInspectionOrder qmsHtIpqcInspectionOrder = new QmsHtIpqcInspectionOrder();
        BeanUtils.copyProperties(qmsIpqcInspectionOrder, qmsHtIpqcInspectionOrder);
        qmsHtIpqcInspectionOrderMapper.insert(qmsHtIpqcInspectionOrder);

        return i;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int batchDelete(String ids) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        List<QmsHtIpqcInspectionOrder> list = new ArrayList<>();
        String[] idArry = ids.split(",");
        for (String id : idArry) {
            QmsIpqcInspectionOrder qmsIpqcInspectionOrder = qmsIpqcInspectionOrderMapper.selectByPrimaryKey(id);
            if(StringUtils.isEmpty(qmsIpqcInspectionOrder)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
            QmsHtIpqcInspectionOrder qmsHtIpqcInspectionOrder = new QmsHtIpqcInspectionOrder();
            BeanUtils.copyProperties(qmsIpqcInspectionOrder, qmsHtIpqcInspectionOrder);
            list.add(qmsHtIpqcInspectionOrder);

            //删除IPQC检验单明细
            Example example = new Example(QmsIpqcInspectionOrderDet.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("ipqcInspectionOrderId", qmsIpqcInspectionOrder.getIpqcInspectionOrderId());
            qmsIpqcInspectionOrderDetMapper.deleteByExample(example);
        }

        //履历
        qmsHtIpqcInspectionOrderMapper.insertList(list);

        return qmsIpqcInspectionOrderMapper.deleteByIds(ids);
    }

}
