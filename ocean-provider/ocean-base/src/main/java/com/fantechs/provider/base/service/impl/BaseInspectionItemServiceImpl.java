package com.fantechs.provider.base.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysSpecItem;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.entity.security.search.SearchSysSpecItem;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseInspectionItemDto;
import com.fantechs.common.base.general.entity.basic.BaseInspectionItem;
import com.fantechs.common.base.general.entity.basic.BaseInspectionItemDet;
import com.fantechs.common.base.general.entity.basic.history.BaseHtInspectionItem;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.security.service.SecurityFeignApi;
import com.fantechs.provider.base.mapper.BaseHtInspectionItemMapper;
import com.fantechs.provider.base.mapper.BaseInspectionItemDetMapper;
import com.fantechs.provider.base.mapper.BaseInspectionItemMapper;
import com.fantechs.provider.base.service.BaseInspectionItemService;
import lombok.SneakyThrows;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by leifengzhi on 2020/12/25.
 */
@Service
public class BaseInspectionItemServiceImpl extends BaseService<BaseInspectionItem> implements BaseInspectionItemService {

    @Resource
    private BaseInspectionItemMapper baseInspectionItemMapper;
    @Resource
    private BaseHtInspectionItemMapper baseHtInspectionItemMapper;
    @Resource
    private BaseInspectionItemDetMapper baseInspectionItemDetMapper;
    @Resource
    private SecurityFeignApi securityFeignApi;

    @Override
    public List<BaseInspectionItemDto> findList(Map<String, Object> map) {
        return baseInspectionItemMapper.findList(map);
    }

    @SneakyThrows
    @Override
    public List<BaseInspectionItemDto> exportExcel(Map<String, Object> map) {
        List<BaseInspectionItemDto> list = this.findList(map);

        SearchSysSpecItem searchSysSpecItem = new SearchSysSpecItem();
        searchSysSpecItem.setSpecCode("inspectionItem");
        List<SysSpecItem> inspectionItems = securityFeignApi.findSpecItemList(searchSysSpecItem).getData();
        searchSysSpecItem.setSpecCode("inspectionLevel");
        List<SysSpecItem> inspectionLevels = securityFeignApi.findSpecItemList(searchSysSpecItem).getData();
        searchSysSpecItem.setSpecCode("inspectionTool");
        List<SysSpecItem> inspectionTools = securityFeignApi.findSpecItemList(searchSysSpecItem).getData();
        searchSysSpecItem.setSpecCode("testMethod");
        List<SysSpecItem> testMethods = securityFeignApi.findSpecItemList(searchSysSpecItem).getData();

        for (BaseInspectionItemDto qmsInspectionItemDto : list) {
            qmsInspectionItemDto.setInspectionNapeName(JSONObject.parseObject(String.valueOf(JSONObject.parseArray(inspectionItems.get(0).getParaValue()).get(Integer.parseInt(qmsInspectionItemDto.getInspectionNape() + "")))).get("name") + "");
            qmsInspectionItemDto.setInspectionItemLevelName(JSONObject.parseObject(String.valueOf(JSONObject.parseArray(inspectionLevels.get(0).getParaValue()).get(Integer.parseInt(qmsInspectionItemDto.getInspectionItemLevel() + "")))).get("name") + "");
            qmsInspectionItemDto.setInspectionToolName(JSONObject.parseObject(String.valueOf(JSONObject.parseArray(inspectionTools.get(0).getParaValue()).get(Integer.parseInt(qmsInspectionItemDto.getInspectionTool() + "")))).get("name") + "");
            qmsInspectionItemDto.setTestMethodName(JSONObject.parseObject(String.valueOf(JSONObject.parseArray(testMethods.get(0).getParaValue()).get(Integer.parseInt(qmsInspectionItemDto.getTestMethod() + "")))).get("name") + "");
        }

        return list;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(BaseInspectionItem baseInspectionItem) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        baseInspectionItem.setCreateTime(new Date());
        baseInspectionItem.setCreateUserId(user.getUserId());
        baseInspectionItem.setModifiedTime(new Date());
        baseInspectionItem.setModifiedUserId(user.getUserId());
        baseInspectionItem.setStatus(StringUtils.isEmpty(baseInspectionItem.getStatus()) ? 1 : baseInspectionItem.getStatus());
        baseInspectionItem.setInspectionItemCode(getOdd());
        baseInspectionItem.setOrganizationId(user.getOrganizationId());

        int i = baseInspectionItemMapper.insertUseGeneratedKeys(baseInspectionItem);

        BaseHtInspectionItem baseHtProductFamily = new BaseHtInspectionItem();
        BeanUtils.copyProperties(baseInspectionItem, baseHtProductFamily);
        baseHtInspectionItemMapper.insert(baseHtProductFamily);

        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(BaseInspectionItem baseInspectionItem) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        baseInspectionItem.setModifiedTime(new Date());
        baseInspectionItem.setModifiedUserId(user.getUserId());
        baseInspectionItem.setOrganizationId(user.getOrganizationId());

        BaseHtInspectionItem baseHtProductFamily = new BaseHtInspectionItem();
        BeanUtils.copyProperties(baseInspectionItem, baseHtProductFamily);
        baseHtInspectionItemMapper.insert(baseHtProductFamily);

        return baseInspectionItemMapper.updateByPrimaryKeySelective(baseInspectionItem);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDelete(String ids) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        List<BaseHtInspectionItem> qmsHtQualityInspections = new ArrayList<>();
        String[] idsArr = ids.split(",");
        for (String id : idsArr) {
            BaseInspectionItem baseInspectionItem = baseInspectionItemMapper.selectByPrimaryKey(id);
            if (StringUtils.isEmpty(baseInspectionItem)) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }

            BaseHtInspectionItem baseHtInspectionItem = new BaseHtInspectionItem();
            BeanUtils.copyProperties(baseInspectionItem, baseHtInspectionItem);
            qmsHtQualityInspections.add(baseHtInspectionItem);
        }

        baseHtInspectionItemMapper.insertList(qmsHtQualityInspections);

        Example example = new Example(BaseInspectionItemDet.class);
        Example.Criteria criteria = example.createCriteria();
        String[] split = ids.split(",");
        criteria.andIn("inspectionItemId", Arrays.asList(split));
        baseInspectionItemDetMapper.deleteByExample(example);

        return baseInspectionItemMapper.deleteByIds(ids);
    }

    /**
     * 生成检验项目单号
     *
     * @return
     */
    public String getOdd() {
        String before = "JYXM";
        String amongst = new SimpleDateFormat("yyMMdd").format(new Date());
        BaseInspectionItem baseInspectionItem = baseInspectionItemMapper.getMax();
        String qmsInspectionItemCode = before + amongst + "0000";
        if (StringUtils.isNotEmpty(baseInspectionItem)) {
            qmsInspectionItemCode = baseInspectionItem.getInspectionItemCode();
        }
        Integer maxCode = Integer.parseInt(qmsInspectionItemCode.substring(10, qmsInspectionItemCode.length()));
        String after = String.format("%04d", ++maxCode);
        String code = before + amongst + after;
        return code;
    }

}
