package com.fantechs.security.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseOrganizationDto;
import com.fantechs.common.base.general.dto.security.SysCustomFormDetDto;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseOrganization;
import com.fantechs.common.base.general.entity.security.SysCustomForm;
import com.fantechs.common.base.general.entity.security.SysCustomFormDet;
import com.fantechs.common.base.general.entity.security.SysDefaultCustomForm;
import com.fantechs.common.base.general.entity.security.SysDefaultCustomFormDet;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.security.mapper.SysCustomFormDetMapper;
import com.fantechs.security.mapper.SysCustomFormMapper;
import com.fantechs.security.mapper.SysDefaultCustomFormDetMapper;
import com.fantechs.security.mapper.SysDefaultCustomFormMapper;
import com.fantechs.security.service.SysCustomFormDetService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/01/08.
 */
@Service
public class SysCustomFormDetServiceImpl  extends BaseService<SysCustomFormDet> implements SysCustomFormDetService {

     @Resource
     private SysCustomFormDetMapper sysCustomFormDetMapper;

     @Resource
     private BaseFeignApi baseFeignApi;

    @Resource
    private SysCustomFormMapper sysCustomFormMapper;

    @Resource
    private SysDefaultCustomFormMapper sysDefaultCustomFormMapper;

    @Resource
    private SysDefaultCustomFormDetMapper sysDefaultCustomFormDetMapper;

    @Override
    public List<SysCustomFormDetDto> findList(Map<String, Object> map) {
        // 获取登录用户
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        map.put("orgId", user.getOrganizationId());
        return sysCustomFormDetMapper.findList(map);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(SysCustomFormDet sysCustomFormDet){
        SysCustomForm sysCustomForm = sysCustomFormMapper.selectByPrimaryKey(sysCustomFormDet.getCustomFormId());

        List<SysCustomFormDet> dets = new ArrayList<>();
        dets.add(sysCustomFormDet);
        List<BaseOrganizationDto> organizationDtos = baseFeignApi.findOrganizationList(new SearchBaseOrganization()).getData();
        if(!organizationDtos.isEmpty()){
            for (BaseOrganizationDto org : organizationDtos){
                if(!org.getOrganizationId().equals(sysCustomFormDet.getOrgId())){
                    //查对应组织下该表单的id
                    Example example = new Example(SysCustomForm.class);
                    Example.Criteria criteria = example.createCriteria();
                    criteria.andEqualTo("customFormCode",sysCustomForm.getCustomFormCode())
                            .andEqualTo("orgId",org.getOrganizationId());
                    SysCustomForm customForm = sysCustomFormMapper.selectOneByExample(example);

                    SysCustomFormDet det = new SysCustomFormDet();
                    BeanUtil.copyProperties(sysCustomFormDet, det);
                    det.setCustomFormId(customForm.getCustomFormId());
                    det.setOrgId(org.getOrganizationId());
                    dets.add(det);
                }
            }
        }

        //同步到默认自定义表单明细
        SysDefaultCustomForm sysDefaultCustomForm = sysDefaultCustomFormMapper.selectByPrimaryKey(sysCustomFormDet.getCustomFormId());
        if(StringUtils.isNotEmpty(sysDefaultCustomForm)){
            SysDefaultCustomFormDet defaultCustomFormDet = new SysDefaultCustomFormDet();
            BeanUtil.copyProperties(sysCustomFormDet, defaultCustomFormDet);
            defaultCustomFormDet.setOrgId(null);
            sysDefaultCustomFormDetMapper.insertSelective(defaultCustomFormDet);
        }

        return sysCustomFormDetMapper.insertList(dets);
    }
}
