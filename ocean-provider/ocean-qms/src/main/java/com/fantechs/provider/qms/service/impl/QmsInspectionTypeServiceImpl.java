package com.fantechs.provider.qms.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysSpecItem;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.entity.security.search.SearchSysSpecItem;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.qms.QmsInspectionTypeDto;
import com.fantechs.common.base.general.entity.qms.QmsInspectionType;
import com.fantechs.common.base.general.entity.qms.QmsQualityInspection;
import com.fantechs.common.base.general.entity.qms.history.QmsHtInspectionType;
import com.fantechs.common.base.general.entity.qms.history.QmsHtQualityInspection;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.security.service.SecurityFeignApi;
import com.fantechs.provider.qms.mapper.QmsHtInspectionTypeMapper;
import com.fantechs.provider.qms.mapper.QmsInspectionTypeMapper;
import com.fantechs.provider.qms.service.QmsInspectionTypeService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 *
 * Created by leifengzhi on 2020/12/23.
 */
@Service
public class QmsInspectionTypeServiceImpl extends BaseService<QmsInspectionType> implements QmsInspectionTypeService {

    @Resource
    private QmsInspectionTypeMapper qmsInspectionTypeMapper;
    @Resource
    private QmsHtInspectionTypeMapper qmsHtInspectionTypeMapper;
    @Resource
    private SecurityFeignApi securityFeignApi;

    @Override
    public List<QmsInspectionTypeDto> findList(Map<String, Object> map) {
        return qmsInspectionTypeMapper.findList(map);
    }

    @Override
    public List<QmsInspectionTypeDto> exportExcel(Map<String, Object> map) {
        List<QmsInspectionTypeDto> list = this.findList(map);

        SearchSysSpecItem searchSysSpecItem = new SearchSysSpecItem();
        searchSysSpecItem.setSpecCode("inspectionItem");
        List<SysSpecItem> inspectionItems = securityFeignApi.findSpecItemList(searchSysSpecItem).getData();
        searchSysSpecItem.setSpecCode("inspectionLevel");
        List<SysSpecItem> inspectionLevels = securityFeignApi.findSpecItemList(searchSysSpecItem).getData();
        searchSysSpecItem.setSpecCode("inspectionTool");
        List<SysSpecItem> inspectionTools = securityFeignApi.findSpecItemList(searchSysSpecItem).getData();
        searchSysSpecItem.setSpecCode("testMethod");
        List<SysSpecItem> testMethods = securityFeignApi.findSpecItemList(searchSysSpecItem).getData();


        for (QmsInspectionTypeDto qmsInspectionTypeDto : list) {
            qmsInspectionTypeDto.setInspectionNapeName(JSONObject.parseObject(String.valueOf(JSONObject.parseArray(inspectionItems.get(0).getParaValue()).get(Integer.parseInt(qmsInspectionTypeDto.getInspectionNape()+"")))).get("name")+"");
            qmsInspectionTypeDto.setInspectionTypeLevelName(JSONObject.parseObject(String.valueOf(JSONObject.parseArray(inspectionLevels.get(0).getParaValue()).get(Integer.parseInt(qmsInspectionTypeDto.getInspectionTypeLevel()+"")))).get("name")+"");
            qmsInspectionTypeDto.setInspectionToolName(JSONObject.parseObject(String.valueOf(JSONObject.parseArray(inspectionTools.get(0).getParaValue()).get(Integer.parseInt(qmsInspectionTypeDto.getInspectionTool()+"")))).get("name")+"");
            qmsInspectionTypeDto.setInspectionToolName(JSONObject.parseObject(String.valueOf(JSONObject.parseArray(testMethods.get(0).getParaValue()).get(Integer.parseInt(qmsInspectionTypeDto.getTestMethod()+"")))).get("name")+"");
        }
        return list;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(QmsInspectionType qmsInspectionType) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        qmsInspectionType.setCreateTime(new Date());
        qmsInspectionType.setCreateUserId(user.getUserId());
        qmsInspectionType.setModifiedTime(new Date());
        qmsInspectionType.setModifiedUserId(user.getUserId());
        qmsInspectionType.setStatus(StringUtils.isEmpty(qmsInspectionType.getStatus())?1:qmsInspectionType.getStatus());
        qmsInspectionType.setInspectionTypeCode(getOdd());

        int i = qmsInspectionTypeMapper.insertUseGeneratedKeys(qmsInspectionType);

        QmsHtInspectionType baseHtProductFamily = new QmsHtInspectionType();
        BeanUtils.copyProperties(qmsInspectionType,baseHtProductFamily);
        qmsHtInspectionTypeMapper.insert(baseHtProductFamily);

        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(QmsInspectionType qmsInspectionType) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        qmsInspectionType.setModifiedTime(new Date());
        qmsInspectionType.setModifiedUserId(user.getUserId());

        QmsHtInspectionType baseHtProductFamily = new QmsHtInspectionType();
        BeanUtils.copyProperties(qmsInspectionType,baseHtProductFamily);
        qmsHtInspectionTypeMapper.insert(baseHtProductFamily);

        return qmsInspectionTypeMapper.updateByPrimaryKeySelective(qmsInspectionType);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDelete(String ids) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        List<QmsHtInspectionType> qmsHtQualityInspections = new ArrayList<>();
        String[] idsArr  = ids.split(",");
        for (String id : idsArr) {
            QmsInspectionType qmsQualityInspection = qmsInspectionTypeMapper.selectByPrimaryKey(id);
            if (StringUtils.isEmpty(qmsQualityInspection)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }

            QmsHtInspectionType qmsHtInspectionType = new QmsHtInspectionType();
            BeanUtils.copyProperties(qmsQualityInspection,qmsHtInspectionType);
            qmsHtQualityInspections.add(qmsHtInspectionType);
        }

        qmsHtInspectionTypeMapper.insertList(qmsHtQualityInspections);

        return qmsInspectionTypeMapper.deleteByIds(ids);
    }

    /**
     * 生成检验类型单号
     * @return
     */
    public String getOdd(){
        String before = "JL";
        String amongst = new SimpleDateFormat("yyMMdd").format(new Date());
        QmsInspectionType qmsInspectionType = qmsInspectionTypeMapper.getMax();
        String qmsInspectionTypeCode = before+amongst+"0000";
        if (StringUtils.isNotEmpty(qmsInspectionType)){
            qmsInspectionTypeCode = qmsInspectionType.getInspectionTypeCode();
        }
        Integer maxCode = Integer.parseInt(qmsInspectionTypeCode.substring(8, qmsInspectionTypeCode.length()));
        String after = String.format("%04d", ++maxCode);
        String code = before + amongst + after;
        return code;
    }

}
