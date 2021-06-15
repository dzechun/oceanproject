package com.fantechs.provider.base.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysSpecItem;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.entity.security.search.SearchSysSpecItem;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseInspectionTypeDto;
import com.fantechs.common.base.general.entity.basic.BaseInspectionType;
import com.fantechs.common.base.general.entity.basic.history.BaseHtInspectionType;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.security.service.SecurityFeignApi;
import com.fantechs.provider.base.mapper.BaseHtInspectionTypeMapper;
import com.fantechs.provider.base.mapper.BaseInspectionTypeMapper;
import com.fantechs.provider.base.service.BaseInspectionTypeService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by leifengzhi on 2020/12/23.
 */
@Service
public class BaseInspectionTypeServiceImpl extends BaseService<BaseInspectionType> implements BaseInspectionTypeService {

    @Resource
    private BaseInspectionTypeMapper baseInspectionTypeMapper;
    @Resource
    private BaseHtInspectionTypeMapper baseHtInspectionTypeMapper;
    @Resource
    private SecurityFeignApi securityFeignApi;

    @Override
    public List<BaseInspectionTypeDto> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        map.put("orgId", user.getOrganizationId());
        return baseInspectionTypeMapper.findList(map);
    }

    @Override
    public List<BaseInspectionTypeDto> exportExcel(Map<String, Object> map) {
        List<BaseInspectionTypeDto> list = this.findList(map);

        SearchSysSpecItem searchSysSpecItem = new SearchSysSpecItem();
        searchSysSpecItem.setSpecCode("inspectionItem");
        List<SysSpecItem> inspectionItems = securityFeignApi.findSpecItemList(searchSysSpecItem).getData();
        searchSysSpecItem.setSpecCode("inspectionLevel");
        List<SysSpecItem> inspectionLevels = securityFeignApi.findSpecItemList(searchSysSpecItem).getData();
        searchSysSpecItem.setSpecCode("inspectionTool");
        List<SysSpecItem> inspectionTools = securityFeignApi.findSpecItemList(searchSysSpecItem).getData();
        searchSysSpecItem.setSpecCode("testMethod");
        List<SysSpecItem> testMethods = securityFeignApi.findSpecItemList(searchSysSpecItem).getData();


        for (BaseInspectionTypeDto baseInspectionTypeDto : list) {
            baseInspectionTypeDto.setInspectionNapeName(JSONObject.parseObject(String.valueOf(JSONObject.parseArray(inspectionItems.get(0).getParaValue()).get(Integer.parseInt(baseInspectionTypeDto.getInspectionNape() + "")))).get("name") + "");
            baseInspectionTypeDto.setInspectionTypeLevelName(JSONObject.parseObject(String.valueOf(JSONObject.parseArray(inspectionLevels.get(0).getParaValue()).get(Integer.parseInt(baseInspectionTypeDto.getInspectionTypeLevel() + "")))).get("name") + "");
            baseInspectionTypeDto.setInspectionToolName(JSONObject.parseObject(String.valueOf(JSONObject.parseArray(inspectionTools.get(0).getParaValue()).get(Integer.parseInt(baseInspectionTypeDto.getInspectionTool() + "")))).get("name") + "");
            baseInspectionTypeDto.setTestMethodName(JSONObject.parseObject(String.valueOf(JSONObject.parseArray(testMethods.get(0).getParaValue()).get(Integer.parseInt(baseInspectionTypeDto.getTestMethod() + "")))).get("name") + "");
        }
        return list;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(BaseInspectionType baseInspectionType) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        baseInspectionType.setCreateTime(new Date());
        baseInspectionType.setCreateUserId(user.getUserId());
        baseInspectionType.setModifiedTime(new Date());
        baseInspectionType.setModifiedUserId(user.getUserId());
        baseInspectionType.setStatus(StringUtils.isEmpty(baseInspectionType.getStatus()) ? 1 : baseInspectionType.getStatus());
        baseInspectionType.setInspectionTypeCode(getOdd());
        baseInspectionType.setOrganizationId(user.getOrganizationId());

        int i = baseInspectionTypeMapper.insertUseGeneratedKeys(baseInspectionType);

        BaseHtInspectionType baseHtProductFamily = new BaseHtInspectionType();
        BeanUtils.copyProperties(baseInspectionType, baseHtProductFamily);
        baseHtInspectionTypeMapper.insert(baseHtProductFamily);

        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(BaseInspectionType baseInspectionType) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        baseInspectionType.setModifiedTime(new Date());
        baseInspectionType.setModifiedUserId(user.getUserId());
        baseInspectionType.setOrganizationId(user.getOrganizationId());

        BaseHtInspectionType baseHtProductFamily = new BaseHtInspectionType();
        BeanUtils.copyProperties(baseInspectionType, baseHtProductFamily);
        baseHtInspectionTypeMapper.insert(baseHtProductFamily);

        return baseInspectionTypeMapper.updateByPrimaryKeySelective(baseInspectionType);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDelete(String ids) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        List<BaseHtInspectionType> qmsHtQualityInspections = new ArrayList<>();
        String[] idsArr = ids.split(",");
        for (String id : idsArr) {
            BaseInspectionType qmsQualityInspection = baseInspectionTypeMapper.selectByPrimaryKey(id);
            if (StringUtils.isEmpty(qmsQualityInspection)) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }

            BaseHtInspectionType baseHtInspectionType = new BaseHtInspectionType();
            BeanUtils.copyProperties(qmsQualityInspection, baseHtInspectionType);
            qmsHtQualityInspections.add(baseHtInspectionType);
        }

        baseHtInspectionTypeMapper.insertList(qmsHtQualityInspections);

        return baseInspectionTypeMapper.deleteByIds(ids);
    }

    /**
     * 生成检验类型单号
     *
     * @return
     */
    public String getOdd() {
        String before = "JL";
        String amongst = new SimpleDateFormat("yyMMdd").format(new Date());
        BaseInspectionType baseInspectionType = baseInspectionTypeMapper.getMax();
        String qmsInspectionTypeCode = before + amongst + "0000";
        if (StringUtils.isNotEmpty(baseInspectionType)) {
            qmsInspectionTypeCode = baseInspectionType.getInspectionTypeCode();
        }
        Integer maxCode = Integer.parseInt(qmsInspectionTypeCode.substring(8, qmsInspectionTypeCode.length()));
        String after = String.format("%04d", ++maxCode);
        String code = before + amongst + after;
        return code;
    }

}
