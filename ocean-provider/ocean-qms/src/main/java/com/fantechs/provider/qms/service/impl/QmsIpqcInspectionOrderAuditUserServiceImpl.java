package com.fantechs.provider.qms.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysSpecItem;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.entity.security.search.SearchSysSpecItem;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.entity.qms.QmsIpqcInspectionOrder;
import com.fantechs.common.base.general.entity.qms.QmsIpqcInspectionOrderAuditUser;
import com.fantechs.common.base.general.entity.qms.search.SearchQmsIpqcInspectionOrder;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.security.service.SecurityFeignApi;
import com.fantechs.provider.qms.mapper.QmsIpqcInspectionOrderAuditUserMapper;
import com.fantechs.provider.qms.mapper.QmsIpqcInspectionOrderMapper;
import com.fantechs.provider.qms.service.QmsIpqcInspectionOrderAuditUserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/06/08.
 */
@Service
public class QmsIpqcInspectionOrderAuditUserServiceImpl extends BaseService<QmsIpqcInspectionOrderAuditUser> implements QmsIpqcInspectionOrderAuditUserService {

    @Resource
    private QmsIpqcInspectionOrderAuditUserMapper qmsIpqcInspectionOrderAuditUserMapper;
    @Resource
    private QmsIpqcInspectionOrderMapper qmsIpqcInspectionOrderMapper;
    @Resource
    private SecurityFeignApi securityFeignApi;

    @Override
    public List<QmsIpqcInspectionOrderAuditUser> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId",user.getOrganizationId());
        return qmsIpqcInspectionOrderAuditUserMapper.findList(map);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(QmsIpqcInspectionOrderAuditUser record) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        Example example = new Example(QmsIpqcInspectionOrderAuditUser.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("ipqcInspectionOrderId",record.getIpqcInspectionOrderId())
                .andEqualTo("auditUserId",record.getAuditUserId());
        qmsIpqcInspectionOrderAuditUserMapper.deleteByExample(example);

        record.setCreateUserId(user.getUserId());
        record.setCreateTime(new Date());
        record.setModifiedUserId(user.getUserId());
        record.setModifiedTime(new Date());
        record.setStatus(StringUtils.isEmpty(record.getStatus())?1:record.getStatus());
        record.setOrgId(user.getOrganizationId());
        int i =  qmsIpqcInspectionOrderAuditUserMapper.insertSelective(record);

        //修改审批状态
        SearchQmsIpqcInspectionOrder searchQmsIpqcInspectionOrder = new SearchQmsIpqcInspectionOrder();
        searchQmsIpqcInspectionOrder.setIpqcInspectionOrderId(record.getIpqcInspectionOrderId());
        QmsIpqcInspectionOrder qmsIpqcInspectionOrder = qmsIpqcInspectionOrderMapper.findList(ControllerUtil.dynamicConditionByEntity(searchQmsIpqcInspectionOrder)).get(0);
        if("首检".equals(qmsIpqcInspectionOrder.getInspectionWayDesc())){
            example.clear();
            Example.Criteria criteria1 = example.createCriteria();
            criteria1.andEqualTo("ipqcInspectionOrderId",record.getIpqcInspectionOrderId());
            int count = qmsIpqcInspectionOrderAuditUserMapper.selectCountByExample(example);

            //审批部门
            SearchSysSpecItem searchSysSpecItem = new SearchSysSpecItem();
            searchSysSpecItem.setSpecCode("ipqcAuditDeptList");
            List<SysSpecItem> sysSpecItemList = securityFeignApi.findSpecItemList(searchSysSpecItem).getData();
            String paraValue = sysSpecItemList.get(0).getParaValue();
            String[] DeptList = paraValue.substring(1, paraValue.length() - 1).split(",");

            if(count == DeptList.length){
                qmsIpqcInspectionOrder.setAuditStatus((byte)2);
                qmsIpqcInspectionOrderMapper.updateByPrimaryKeySelective(qmsIpqcInspectionOrder);
            }
        }else {
            qmsIpqcInspectionOrder.setAuditStatus((byte)2);
            qmsIpqcInspectionOrderMapper.updateByPrimaryKeySelective(qmsIpqcInspectionOrder);
        }

        return i;
    }
}
