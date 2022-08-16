package com.fantechs.provider.base.service.impl;


import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseWarehouseAreaDto;
import com.fantechs.common.base.general.dto.basic.imports.BaseWarehouseAreaImport;
import com.fantechs.common.base.general.entity.basic.BaseStorage;
import com.fantechs.common.base.general.entity.basic.BaseWarehouse;
import com.fantechs.common.base.general.entity.basic.BaseWarehouseArea;
import com.fantechs.common.base.general.entity.basic.history.BaseHtWarehouseArea;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.mapper.BaseHtWarehouseAreaMapper;
import com.fantechs.provider.base.mapper.BaseStorageMapper;
import com.fantechs.provider.base.mapper.BaseWarehouseAreaMapper;
import com.fantechs.provider.base.mapper.BaseWarehouseMapper;
import com.fantechs.provider.base.service.BaseWarehouseAreaService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.*;

/**
 *
 * Created by leifengzhi on 2020/09/23.
 */
@Service
public class BaseWarehouseAreaServiceImpl extends BaseService<BaseWarehouseArea> implements BaseWarehouseAreaService {

    @Resource
    private BaseWarehouseAreaMapper baseWarehouseAreaMapper;
    @Resource
    private BaseHtWarehouseAreaMapper baseHtWarehouseAreaMapper;
    @Resource
    private BaseStorageMapper baseStorageMapper;
    @Resource
    private BaseWarehouseMapper baseWarehouseMapper;


    @Override
    public List<BaseWarehouseAreaDto> findList(Map<String, Object> map) {
        if(StringUtils.isEmpty(map.get("orgId"))){
            SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
            map.put("orgId", user.getOrganizationId());
        }

        return baseWarehouseAreaMapper.findList(map);
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(BaseWarehouseArea baseWarehouseArea) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        int i=0;
        Example example = new Example(BaseWarehouseArea.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("orgId", currentUser.getOrganizationId());
        criteria.andEqualTo("warehouseAreaCode", baseWarehouseArea.getWarehouseAreaCode());
        List<BaseWarehouseArea> baseWarehouseAreaList = baseWarehouseAreaMapper.selectByExample(example);
        if(StringUtils.isNotEmpty(baseWarehouseAreaList)){
             throw new BizErrorException (ErrorCodeEnum.OPT20012001);
        }
        baseWarehouseArea.setCreateUserId(currentUser.getUserId());
        baseWarehouseArea.setCreateTime(new Date());
        baseWarehouseArea.setModifiedUserId(currentUser.getUserId());
        baseWarehouseArea.setModifiedTime(new Date());
        baseWarehouseArea.setOrgId(currentUser.getOrganizationId());
        baseWarehouseAreaMapper.insertUseGeneratedKeys(baseWarehouseArea);

        //新增历史记录
        BaseHtWarehouseArea baseHtWarehouseArea =new BaseHtWarehouseArea();
        BeanUtils.copyProperties(baseWarehouseArea, baseHtWarehouseArea);
        i= baseHtWarehouseAreaMapper.insertSelective(baseHtWarehouseArea);
        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(BaseWarehouseArea baseWarehouseArea) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();

        Example example = new Example(BaseWarehouseArea.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("orgId", currentUser.getOrganizationId());
        criteria.andEqualTo("warehouseAreaCode", baseWarehouseArea.getWarehouseAreaCode())
                .andNotEqualTo("warehouseAreaId",baseWarehouseArea.getWarehouseAreaId());
        List<BaseWarehouseArea> baseWarehouseAreaList = baseWarehouseAreaMapper.selectByExample(example);
        if(StringUtils.isNotEmpty(baseWarehouseAreaList)){
            throw new BizErrorException (ErrorCodeEnum.OPT20012001);
        }

        int i=0;
        baseWarehouseArea.setModifiedUserId(currentUser.getUserId());
        baseWarehouseArea.setModifiedTime(new Date());
        baseWarehouseArea.setOrgId(currentUser.getOrganizationId());
        i= baseWarehouseAreaMapper.updateByPrimaryKeySelective(baseWarehouseArea);

        //新增历史记录
        BaseHtWarehouseArea baseHtWarehouseArea =new BaseHtWarehouseArea();
        BeanUtils.copyProperties(baseWarehouseArea, baseHtWarehouseArea);
        baseHtWarehouseAreaMapper.insertSelective(baseHtWarehouseArea);
        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDelete(String ids) {
        int i=0;
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        String[] idsArr = ids.split(",");
        List<BaseHtWarehouseArea> baseHtWarehouseAreaList =  new LinkedList<>();
        for(String id :idsArr){
            BaseWarehouseArea baseWarehouseArea = baseWarehouseAreaMapper.selectByPrimaryKey(id);
            if(StringUtils.isEmpty(baseWarehouseArea)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012000,id);
            }

            //被储位引用
            Example example = new Example(BaseStorage.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("warehouseAreaId", baseWarehouseArea.getWarehouseAreaId());
            List<BaseStorage> baseStorages = baseStorageMapper.selectByExample(example);
            if(StringUtils.isNotEmpty(baseStorages)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012004);
            }

            BaseHtWarehouseArea baseHtWarehouseArea =  new BaseHtWarehouseArea();
            BeanUtils.copyProperties(baseWarehouseArea, baseHtWarehouseArea);
            baseHtWarehouseArea.setModifiedTime(new Date());
            baseHtWarehouseArea.setCreateUserId(currentUser.getUserId());
            baseHtWarehouseAreaList.add(baseHtWarehouseArea);
        }
        i= baseWarehouseAreaMapper.deleteByIds(ids);
        baseHtWarehouseAreaMapper.insertList(baseHtWarehouseAreaList);
        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importExcel(List<BaseWarehouseAreaImport> baseWarehouseAreaImports) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();

        Map<String, Object> resultMap = new HashMap<>();  //封装操作结果
        int success = 0;  //记录操作成功数
        List<Integer> fail = new ArrayList<>();  //记录操作失败行数
        LinkedList<BaseWarehouseArea> list = new LinkedList<>();
        LinkedList<BaseHtWarehouseArea> htList = new LinkedList<>();
        LinkedList<BaseWarehouseAreaImport> warehouseAreaImports = new LinkedList<>();

        for (int i = 0; i < baseWarehouseAreaImports.size(); i++) {
            BaseWarehouseAreaImport baseWarehouseAreaImport = baseWarehouseAreaImports.get(i);
            String warehouseAreaCode = baseWarehouseAreaImport.getWarehouseAreaCode();
            String warehouseAreaName = baseWarehouseAreaImport.getWarehouseAreaName();
            String warehouseCode = baseWarehouseAreaImport.getWarehouseCode();

            if (StringUtils.isEmpty(
                    warehouseCode,warehouseAreaCode,warehouseAreaName
            )){
                fail.add(i+4);
                continue;
            }

            //判断编码是否重复
            Example example = new Example(BaseWarehouseArea.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("orgId", currentUser.getOrganizationId());
            criteria.andEqualTo("warehouseAreaCode",warehouseAreaCode);
            if (StringUtils.isNotEmpty(baseWarehouseAreaMapper.selectOneByExample(example))){
                fail.add(i+4);
                continue;
            }

            //判断仓库信息是否存在
            Example example1 = new Example(BaseWarehouse.class);
            Example.Criteria criteria1 = example1.createCriteria();
            criteria1.andEqualTo("orgId", currentUser.getOrganizationId());
            criteria1.andEqualTo("warehouseCode", warehouseCode);
            BaseWarehouse baseWarehouse = baseWarehouseMapper.selectOneByExample(example1);
            if (StringUtils.isEmpty(baseWarehouse)) {
                fail.add(i + 4);
                continue;
            }
            baseWarehouseAreaImport.setWarehouseId(baseWarehouse.getWarehouseId());

            //判断集合中是否存在该条数据
            boolean tag = false;
            if (StringUtils.isNotEmpty(warehouseAreaImports)){
                for (BaseWarehouseAreaImport warehouseAreaImport : warehouseAreaImports) {
                    if (warehouseAreaCode.equals(warehouseAreaImport.getWarehouseAreaCode())){
                        tag = true;
                        break;
                    }
                }
            }
            if (tag){
                fail.add(i+4);
                continue;
            }

            warehouseAreaImports.add(baseWarehouseAreaImport);
        }

        if (StringUtils.isNotEmpty(warehouseAreaImports)){

            for (BaseWarehouseAreaImport baseWarehouseAreaImport : warehouseAreaImports) {
                BaseWarehouseArea baseWarehouseArea = new BaseWarehouseArea();
                BeanUtils.copyProperties(baseWarehouseAreaImport,baseWarehouseArea);
                baseWarehouseArea.setCreateTime(new Date());
                baseWarehouseArea.setCreateUserId(currentUser.getUserId());
                baseWarehouseArea.setModifiedTime(new Date());
                baseWarehouseArea.setModifiedUserId(currentUser.getUserId());
                baseWarehouseArea.setOrgId(currentUser.getOrganizationId());
                baseWarehouseArea.setStatus((byte)1);
                list.add(baseWarehouseArea);
            }
            success = baseWarehouseAreaMapper.insertList(list);

            if(StringUtils.isNotEmpty(list)){
                for (BaseWarehouseArea baseWarehouseArea : list) {
                    BaseHtWarehouseArea baseHtWarehouseArea = new BaseHtWarehouseArea();
                    BeanUtils.copyProperties(baseWarehouseArea, baseHtWarehouseArea);
                    htList.add(baseHtWarehouseArea);
                }
                baseHtWarehouseAreaMapper.insertList(htList);
            }
        }

        resultMap.put("操作成功总数",success);
        resultMap.put("操作失败行数",fail);
        return resultMap;
    }
}
