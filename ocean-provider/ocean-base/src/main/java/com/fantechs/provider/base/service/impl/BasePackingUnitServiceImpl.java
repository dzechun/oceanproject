package com.fantechs.provider.base.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BasePackingUnitDto;
import com.fantechs.common.base.general.dto.basic.imports.BasePackingUnitImport;
import com.fantechs.common.base.general.entity.basic.BasePackingUnit;
import com.fantechs.common.base.general.entity.basic.history.BaseHtPackingUnit;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.mapper.BaseHtPackingUnitMapper;
import com.fantechs.provider.base.mapper.BasePackingUnitMapper;
import com.fantechs.provider.base.service.BasePackingUnitService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by leifengzhi on 2020/11/03.
 */
@Service
public class BasePackingUnitServiceImpl extends BaseService<BasePackingUnit> implements BasePackingUnitService {

    @Resource
    private BasePackingUnitMapper basePackingUnitMapper;
    @Resource
    private BaseHtPackingUnitMapper baseHtPackingUnitMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(BasePackingUnit basePackingUnit) {
        SysUser currentUserInfo = CurrentUserInfoUtils.getCurrentUserInfo();

        Example example = new Example(BasePackingUnit.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("organizationId", currentUserInfo.getOrganizationId());
        criteria.andEqualTo("packingUnitName", basePackingUnit.getPackingUnitName());
        List<BasePackingUnit> basePackingUnits = basePackingUnitMapper.selectByExample(example);
        if (StringUtils.isNotEmpty(basePackingUnits)) {
            throw new BizErrorException("包装单位名称重复");
        }
        basePackingUnit.setCreateTime(new Date());
        basePackingUnit.setCreateUserId(currentUserInfo.getUserId());
        basePackingUnit.setModifiedTime(new Date());
        basePackingUnit.setModifiedUserId(currentUserInfo.getUserId());
        basePackingUnit.setOrganizationId(currentUserInfo.getOrganizationId());
        int i = basePackingUnitMapper.insertUseGeneratedKeys(basePackingUnit);

        BaseHtPackingUnit baseHtPackingUnit = new BaseHtPackingUnit();
        BeanUtils.copyProperties(basePackingUnit, baseHtPackingUnit);
        baseHtPackingUnitMapper.insertSelective(baseHtPackingUnit);

        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(BasePackingUnit basePackingUnit) {
        SysUser currentUserInfo = CurrentUserInfoUtils.getCurrentUserInfo();

        Example example = new Example(BasePackingUnit.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("organizationId", currentUserInfo.getOrganizationId());
        criteria.andEqualTo("packingUnitName", basePackingUnit.getPackingUnitName())
                .andNotEqualTo("packingUnitId", basePackingUnit.getPackingUnitId());
        List<BasePackingUnit> basePackingUnits = basePackingUnitMapper.selectByExample(example);
        if (StringUtils.isNotEmpty(basePackingUnits)) {
            throw new BizErrorException("包装单位名称重复");
        }

        basePackingUnit.setModifiedTime(new Date());
        basePackingUnit.setModifiedUserId(currentUserInfo.getUserId());
        basePackingUnit.setOrganizationId(currentUserInfo.getOrganizationId());
        int i = basePackingUnitMapper.updateByPrimaryKeySelective(basePackingUnit);

        BaseHtPackingUnit baseHtPackingUnit = new BaseHtPackingUnit();
        BeanUtils.copyProperties(basePackingUnit, baseHtPackingUnit);
        baseHtPackingUnitMapper.insertSelective(baseHtPackingUnit);

        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDelete(String ids) {
        SysUser currentUserInfo = CurrentUserInfoUtils.getCurrentUserInfo();

        LinkedList<BaseHtPackingUnit> baseHtPackingUnits = new LinkedList<>();
        String[] idsArr = ids.split(",");
        for (String id : idsArr) {
            BasePackingUnit basePackingUnit = basePackingUnitMapper.selectByPrimaryKey(id);
            if (StringUtils.isEmpty(basePackingUnit)) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }

            BaseHtPackingUnit baseHtPackingUnit = new BaseHtPackingUnit();
            BeanUtils.copyProperties(basePackingUnit, baseHtPackingUnit);
            baseHtPackingUnit.setModifiedUserId(currentUserInfo.getUserId());
            baseHtPackingUnit.setModifiedTime(new Date());
            baseHtPackingUnits.add(baseHtPackingUnit);
        }
        baseHtPackingUnitMapper.insertList(baseHtPackingUnits);
        return basePackingUnitMapper.deleteByIds(ids);
    }

    @Override
    public List<BasePackingUnitDto> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId", user.getOrganizationId());
        return basePackingUnitMapper.findList(map);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importExcel(List<BasePackingUnitImport> basePackingUnitImports) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();

        Map<String, Object> resultMap = new HashMap<>();  //封装操作结果
        int success = 0;  //记录操作成功数
        List<Integer> fail = new ArrayList<>();  //记录操作失败行数
        LinkedList<BasePackingUnit> list = new LinkedList<>();
        LinkedList<BaseHtPackingUnit> htList = new LinkedList<>();
        LinkedList<BasePackingUnitImport> packingUnitImports = new LinkedList<>();
        for (int i = 0; i < basePackingUnitImports.size(); i++) {
            BasePackingUnitImport basePackingUnitImport = basePackingUnitImports.get(i);
            String packingUnitName = basePackingUnitImport.getPackingUnitName();
            if (StringUtils.isEmpty(
                    packingUnitName
            )){
                fail.add(i+4);
                continue;
            }

            //判断包装单位是否存在
            Example example = new Example(BasePackingUnit.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("organizationId", currentUser.getOrganizationId());
            criteria.andEqualTo("packingUnitName",packingUnitName);
            BasePackingUnit basePackingUnit1 = basePackingUnitMapper.selectOneByExample(example);
            if (StringUtils.isNotEmpty(basePackingUnit1)){
                fail.add(i+4);
                continue;
            }

            packingUnitImports.add(basePackingUnitImport);
        }

        if (StringUtils.isNotEmpty(packingUnitImports)){

            for (BasePackingUnitImport packingUnitImport : packingUnitImports) {
                BasePackingUnit basePackingUnit = new BasePackingUnit();
                BeanUtils.copyProperties(packingUnitImport,basePackingUnit);
                basePackingUnit.setCreateTime(new Date());
                basePackingUnit.setCreateUserId(currentUser.getUserId());
                basePackingUnit.setModifiedTime(new Date());
                basePackingUnit.setModifiedUserId(currentUser.getUserId());
                if (StringUtils.isEmpty(basePackingUnit.getStatus())){
                    basePackingUnit.setStatus((byte) 1);
                }
                basePackingUnit.setOrganizationId(currentUser.getOrganizationId());
                list.add(basePackingUnit);
            }
            success += basePackingUnitMapper.insertList(list);

            for (BasePackingUnit basePackingUnit : list) {
                BaseHtPackingUnit baseHtPackingUnit = new BaseHtPackingUnit();
                BeanUtils.copyProperties(basePackingUnit,baseHtPackingUnit);
                htList.add(baseHtPackingUnit);
            }
            baseHtPackingUnitMapper.insertList(htList);
        }

        resultMap.put("操作成功总数",success);
        resultMap.put("操作失败行数",fail);
        return resultMap;
    }
}
