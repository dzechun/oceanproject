package com.fantechs.auth.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseOrganizationDto;
import com.fantechs.common.base.general.dto.security.SysCustomExportDTO;
import com.fantechs.common.base.general.dto.security.SysCustomFormDetDto;
import com.fantechs.common.base.general.dto.security.SysDefaultCustomFormDetDto;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseOrganization;
import com.fantechs.common.base.general.entity.security.SysCustomForm;
import com.fantechs.common.base.general.entity.security.SysCustomFormDet;
import com.fantechs.common.base.general.entity.security.SysDefaultCustomForm;
import com.fantechs.common.base.general.entity.security.SysDefaultCustomFormDet;
import com.fantechs.common.base.general.entity.security.search.SearchSysCustomFormDet;
import com.fantechs.common.base.general.entity.security.search.SearchSysDefaultCustomFormDet;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.auth.mapper.SysCustomFormDetMapper;
import com.fantechs.auth.mapper.SysCustomFormMapper;
import com.fantechs.auth.mapper.SysDefaultCustomFormDetMapper;
import com.fantechs.auth.mapper.SysDefaultCustomFormMapper;
import com.fantechs.auth.service.SysCustomFormDetService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * Created by leifengzhi on 2021/01/08.
 */
@Service
public class SysCustomFormDetServiceImpl  extends BaseService<SysCustomFormDet> implements SysCustomFormDetService {

    @Resource
    private SysCustomFormMapper sysCustomFormMapper;
     @Resource
     private SysCustomFormDetMapper sysCustomFormDetMapper;
    @Resource
    private SysDefaultCustomFormMapper sysDefaultCustomFormMapper;
    @Resource
    private SysDefaultCustomFormDetMapper sysDefaultCustomFormDetMapper;
    @Resource
    private BaseFeignApi baseFeignApi;

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
    public List<SysCustomExportDTO> findCustomExportParamList(Map<String, Object> map) {
        // 获取登录用户
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        map.put("orgId", user.getOrganizationId());
        return sysCustomFormDetMapper.findCustomExportParamList(map);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public int saveInAllOrg(SysCustomFormDet sysCustomFormDet) {
        // 获取登录用户
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        sysCustomFormDet.setOrgId(user.getOrganizationId());

        SysCustomForm sysCustomForm = sysCustomFormMapper.selectByPrimaryKey(sysCustomFormDet.getCustomFormId());

        //默认表新增
        Example example = new Example(SysDefaultCustomForm.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("customFormCode",sysCustomForm.getCustomFormCode());
        SysDefaultCustomForm defaultCustomForm = sysDefaultCustomFormMapper.selectOneByExample(example);
        if(StringUtils.isNotEmpty(defaultCustomForm)) {
            SysDefaultCustomFormDet defaultCustomFormDet = new SysDefaultCustomFormDet();
            BeanUtil.copyProperties(sysCustomFormDet, defaultCustomFormDet);
            defaultCustomFormDet.setCustomFormId(defaultCustomForm.getCustomFormId());
            defaultCustomFormDet.setOrgId(null);
            sysDefaultCustomFormDetMapper.insertSelective(defaultCustomFormDet);
        }

        //全组织新增
        List<SysCustomFormDet> formDetList = new LinkedList<>();
        formDetList.add(sysCustomFormDet);
        SearchBaseOrganization baseOrganization = new SearchBaseOrganization();
        baseOrganization.setPageSize(99999);
        List<BaseOrganizationDto> organizationDtos = baseFeignApi.findOrganizationList(baseOrganization).getData();
        if(!organizationDtos.isEmpty()){
            for (BaseOrganizationDto org : organizationDtos){
                if(!org.getOrganizationId().equals(sysCustomFormDet.getOrgId())){
                    Example example1 = new Example(SysDefaultCustomForm.class);
                    Example.Criteria criteria1 = example1.createCriteria();
                    criteria1.andEqualTo("customFormCode",sysCustomForm.getCustomFormCode())
                            .andEqualTo("orgId",org.getOrganizationId());
                    SysCustomForm customForm = sysCustomFormMapper.selectOneByExample(example1);

                    if(StringUtils.isNotEmpty(customForm)) {
                        SysCustomFormDet formDet = new SysCustomFormDet();
                        BeanUtil.copyProperties(sysCustomFormDet, formDet);
                        formDet.setCustomFormId(customForm.getCustomFormId());
                        formDet.setOrgId(org.getOrganizationId());
                        formDetList.add(formDet);
                    }
                }
            }
        }

        return sysCustomFormDetMapper.insertList(formDetList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateInAllOrg(SysCustomFormDet sysCustomFormDet) {
        // 获取登录用户
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        sysCustomFormDet.setOrgId(user.getOrganizationId());

        //获取原数据
        SysCustomFormDet oldCustomFormDet = sysCustomFormDetMapper.selectByPrimaryKey(sysCustomFormDet.getCustomFormDetId());

        int i = 0;
        //默认表修改
        SysCustomForm sysCustomForm = sysCustomFormMapper.selectByPrimaryKey(oldCustomFormDet.getCustomFormId());
        SearchSysDefaultCustomFormDet searchSysDefaultCustomFormDet = new SearchSysDefaultCustomFormDet();
        searchSysDefaultCustomFormDet.setCustomFormCode(sysCustomForm.getCustomFormCode());
        searchSysDefaultCustomFormDet.setItemKey(oldCustomFormDet.getItemKey());
        List<SysDefaultCustomFormDetDto> defaultDetList = sysDefaultCustomFormDetMapper.findList(ControllerUtil.dynamicConditionByEntity(searchSysDefaultCustomFormDet));
        if(StringUtils.isNotEmpty(defaultDetList)) {
            SysDefaultCustomFormDetDto sysDefaultCustomFormDetDto = defaultDetList.get(0);
            Long defaultCustomFormId = sysDefaultCustomFormDetDto.getCustomFormId();
            Long defaultCustomFormDetId = sysDefaultCustomFormDetDto.getCustomFormDetId();
            BeanUtil.copyProperties(sysCustomFormDet, sysDefaultCustomFormDetDto);
            sysDefaultCustomFormDetDto.setOrgId(null);
            sysDefaultCustomFormDetDto.setCustomFormId(defaultCustomFormId);
            sysDefaultCustomFormDetDto.setCustomFormDetId(defaultCustomFormDetId);
            sysDefaultCustomFormDetMapper.updateByPrimaryKeySelective(sysDefaultCustomFormDetDto);
        }

        //全组织修改
        List<SysCustomFormDetDto> detList = new LinkedList<>();
        SysCustomFormDetDto formDetDto = new SysCustomFormDetDto();
        BeanUtil.copyProperties(sysCustomFormDet,formDetDto);
        detList.add(formDetDto);

        SearchBaseOrganization baseOrganization = new SearchBaseOrganization();
        baseOrganization.setPageSize(99999);
        List<BaseOrganizationDto> organizationDtos = baseFeignApi.findOrganizationList(baseOrganization).getData();
        if(!organizationDtos.isEmpty()) {
            for (BaseOrganizationDto org : organizationDtos) {
                if (!org.getOrganizationId().equals(sysCustomFormDet.getOrgId())) {
                    SearchSysCustomFormDet searchSysCustomFormDet = new SearchSysCustomFormDet();
                    searchSysCustomFormDet.setCustomFormCode(sysCustomForm.getCustomFormCode());
                    searchSysCustomFormDet.setItemKey(oldCustomFormDet.getItemKey());
                    searchSysCustomFormDet.setOrgId(org.getOrganizationId());
                    List<SysCustomFormDetDto> detListInOrg = sysCustomFormDetMapper.findList(ControllerUtil.dynamicConditionByEntity(searchSysCustomFormDet));
                    if(StringUtils.isNotEmpty(detListInOrg)){
                        SysCustomFormDetDto customFormDetDto = detListInOrg.get(0);
                        detList.add(customFormDetDto);
                    }
                }
            }
        }

        for(SysCustomFormDetDto sysCustomFormDetDto:detList){
            Long customFormId = sysCustomFormDetDto.getCustomFormId();
            Long customFormDetId = sysCustomFormDetDto.getCustomFormDetId();
            Long orgId = sysCustomFormDetDto.getOrgId();
            BeanUtil.copyProperties(sysCustomFormDet, sysCustomFormDetDto);
            sysCustomFormDetDto.setCustomFormId(customFormId);
            sysCustomFormDetDto.setCustomFormDetId(customFormDetId);
            sysCustomFormDetDto.setOrgId(orgId);
            i += sysCustomFormDetMapper.updateByPrimaryKeySelective(sysCustomFormDetDto);
        }

        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDeleteInAllOrg(String ids) {
        int i = 0;

        List<SysCustomFormDet> sysCustomFormDets = sysCustomFormDetMapper.selectByIds(ids);
        SysCustomForm sysCustomForm = sysCustomFormMapper.selectByPrimaryKey(sysCustomFormDets.get(0).getCustomFormId());
        for (SysCustomFormDet sysCustomFormDet : sysCustomFormDets){
            //删除默认表单
            SearchSysDefaultCustomFormDet searchSysDefaultCustomFormDet = new SearchSysDefaultCustomFormDet();
            searchSysDefaultCustomFormDet.setCustomFormCode(sysCustomForm.getCustomFormCode());
            searchSysDefaultCustomFormDet.setItemKey(sysCustomFormDet.getItemKey());
            List<SysDefaultCustomFormDetDto> defaultDetList = sysDefaultCustomFormDetMapper.findList(ControllerUtil.dynamicConditionByEntity(searchSysDefaultCustomFormDet));
            if(StringUtils.isNotEmpty(defaultDetList)) {
                sysDefaultCustomFormDetMapper.deleteByPrimaryKey(defaultDetList.get(0).getCustomFormDetId());
            }

            //全组织删除
            SearchSysCustomFormDet searchSysCustomFormDet = new SearchSysCustomFormDet();
            searchSysCustomFormDet.setCustomFormCode(sysCustomForm.getCustomFormCode());
            searchSysCustomFormDet.setItemKey(sysCustomFormDet.getItemKey());
            List<SysCustomFormDetDto> detList = sysCustomFormDetMapper.findList(ControllerUtil.dynamicConditionByEntity(searchSysCustomFormDet));
            List<Long> idList = detList.stream().map(SysCustomFormDetDto::getCustomFormDetId).collect(Collectors.toList());
            Example example = new Example(SysCustomFormDet.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andIn("customFormDetId",idList);
            i = sysCustomFormDetMapper.deleteByExample(example);
        }

        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchUpdate(List<SysCustomFormDet> list) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        Example example = new Example(SysCustomFormDet.class);
        if (StringUtils.isNotEmpty(list)) {
            List<Long> idList = new ArrayList<>();
            Long id = 0L;
            for (SysCustomFormDet sysCustomFormDet : list) {
                id = sysCustomFormDet.getCustomFormId();
                sysCustomFormDet.setModifiedUserId(user.getUserId());
                sysCustomFormDet.setOrgId(user.getOrganizationId());
                idList.add(sysCustomFormDet.getCustomFormDetId());
            }
            example.createCriteria().andIn("customFormDetId",idList);
            int delCount = sysCustomFormDetMapper.deleteByExample(example);
            logger.info("删除自定义表单条数：{}",delCount);
            int insertCount = sysCustomFormDetMapper.insertList(list);
            logger.info("新增自定义表单条数：{}",insertCount);
        }
        return 1;
    }
}
