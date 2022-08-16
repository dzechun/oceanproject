package com.fantechs.provider.base.service.impl;


import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseMaterialOwnerReWhDto;
import com.fantechs.common.base.general.dto.basic.imports.BaseWarehouseImport;
import com.fantechs.common.base.general.entity.basic.BaseMaterialOwner;
import com.fantechs.common.base.general.entity.basic.BaseMaterialOwnerReWh;
import com.fantechs.common.base.general.entity.basic.BaseWarehouse;
import com.fantechs.common.base.general.entity.basic.BaseWarehouseArea;
import com.fantechs.common.base.general.entity.basic.history.BaseHtWarehouse;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseMaterialOwnerReWh;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.mapper.*;
import com.fantechs.provider.base.service.BaseWarehouseService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 *
 * Created by wcz on 2020/09/23.
 */
@Service
public class BaseWarehouseServiceImpl extends BaseService<BaseWarehouse> implements BaseWarehouseService {

    @Resource
    private BaseWarehouseMapper baseWarehouseMapper;

    @Resource
    private BaseHtWarehouseMapper baseHtWarehouseMapper;

    @Resource
    private BaseWarehouseAreaMapper baseWarehouseAreaMapper;

    @Resource
    private BaseMaterialOwnerReWhMapper baseMaterialOwnerReWhMapper;

    @Resource
    private BaseMaterialOwnerMapper baseMaterialOwnerMapper;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(BaseWarehouse baseWarehouse) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();

        Example example = new Example(BaseWarehouse.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("orgId", currentUser.getOrganizationId());
        criteria.andEqualTo("warehouseCode", baseWarehouse.getWarehouseCode());
        List<BaseWarehouse> baseWarehouses = baseWarehouseMapper.selectByExample(example);
        if(StringUtils.isNotEmpty(baseWarehouses)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        baseWarehouse.setCreateUserId(currentUser.getUserId());
        baseWarehouse.setCreateTime(new Date());
        baseWarehouse.setModifiedUserId(currentUser.getUserId());
        baseWarehouse.setModifiedTime(new Date());
        baseWarehouse.setOrgId(currentUser.getOrganizationId());
        baseWarehouseMapper.insertUseGeneratedKeys(baseWarehouse);

        //新增货主和仓库关系
        List<BaseMaterialOwnerReWhDto> baseMaterialOwnerReWhDtos = baseWarehouse.getBaseMaterialOwnerReWhDtos();
        if(StringUtils.isNotEmpty(baseMaterialOwnerReWhDtos)){
            for (BaseMaterialOwnerReWhDto baseMaterialOwnerReWhDto : baseMaterialOwnerReWhDtos) {
                baseMaterialOwnerReWhDto.setWarehouseId(baseWarehouse.getWarehouseId());
                baseMaterialOwnerReWhDto.setOrgId(currentUser.getOrganizationId());
            }
            baseMaterialOwnerReWhMapper.insertList(baseMaterialOwnerReWhDtos);
        }

        //新增仓库历史信息
        BaseHtWarehouse baseHtWarehouse = new BaseHtWarehouse();
        BeanUtils.copyProperties(baseWarehouse, baseHtWarehouse);
        int i = baseHtWarehouseMapper.insertSelective(baseHtWarehouse);

        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDelete(String ids) {
        List<BaseHtWarehouse> list=new ArrayList<>();

        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();

        String[] warehouseIds = ids.split(",");
        for (String warehouseId : warehouseIds) {
            BaseWarehouse baseWarehouse = baseWarehouseMapper.selectByPrimaryKey(Long.parseLong(warehouseId));
            if(StringUtils.isEmpty(baseWarehouse)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }

            //被库区引用
            Example example = new Example(BaseWarehouseArea.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("warehouseId",warehouseId);
            List<BaseWarehouseArea> baseWarehouseAreas = baseWarehouseAreaMapper.selectByExample(example);
            if(StringUtils.isNotEmpty(baseWarehouseAreas)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012004);
            }

            //新增仓库历史信息
            BaseHtWarehouse baseHtWarehouse =new BaseHtWarehouse();
            BeanUtils.copyProperties(baseWarehouse, baseHtWarehouse);
            baseHtWarehouse.setModifiedUserId(currentUser.getUserId());
            baseHtWarehouse.setModifiedTime(new Date());
            list.add(baseHtWarehouse);

            //删除原有绑定关系
            Example example1 = new Example(BaseMaterialOwnerReWh.class);
            Example.Criteria criteria1 = example1.createCriteria();
            criteria1.andEqualTo("warehouseId", baseWarehouse.getWarehouseId());
            baseMaterialOwnerReWhMapper.deleteByExample(example1);
        }
        baseHtWarehouseMapper.insertList(list);

        return baseWarehouseMapper.deleteByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(BaseWarehouse baseWarehouse) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();

        Example example = new Example(BaseWarehouse.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("orgId", currentUser.getOrganizationId());
        criteria.andEqualTo("warehouseCode", baseWarehouse.getWarehouseCode())
                .andNotEqualTo("warehouseId", baseWarehouse.getWarehouseId());
        BaseWarehouse warehouse = baseWarehouseMapper.selectOneByExample(example);
        if(StringUtils.isNotEmpty(warehouse)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        baseWarehouse.setModifiedUserId(currentUser.getUserId());
        baseWarehouse.setModifiedTime(new Date());
        baseWarehouse.setOrgId(currentUser.getOrganizationId());
        int i= baseWarehouseMapper.updateByPrimaryKeySelective(baseWarehouse);

        //删除原有绑定关系
        Example example1 = new Example(BaseMaterialOwnerReWh.class);
        Example.Criteria criteria1 = example1.createCriteria();
        criteria1.andEqualTo("warehouseId", baseWarehouse.getWarehouseId());
        baseMaterialOwnerReWhMapper.deleteByExample(example1);

        //新增货主和仓库关系
        List<BaseMaterialOwnerReWhDto> baseMaterialOwnerReWhDtos = baseWarehouse.getBaseMaterialOwnerReWhDtos();
        if(StringUtils.isNotEmpty(baseMaterialOwnerReWhDtos)){
            for (BaseMaterialOwnerReWhDto baseMaterialOwnerReWhDto : baseMaterialOwnerReWhDtos) {
                baseMaterialOwnerReWhDto.setWarehouseId(baseWarehouse.getWarehouseId());
                baseMaterialOwnerReWhDto.setOrgId(currentUser.getOrganizationId());
            }
            baseMaterialOwnerReWhMapper.insertList(baseMaterialOwnerReWhDtos);
        }

        //新增仓库历史信息
        BaseHtWarehouse baseHtWarehouse =new BaseHtWarehouse();
        BeanUtils.copyProperties(baseWarehouse, baseHtWarehouse);
        baseHtWarehouseMapper.insertSelective(baseHtWarehouse);
        return i;
    }


    @Override
    public List<BaseWarehouse> findList(Map<String, Object> map) {
        if(StringUtils.isEmpty(map.get("orgId"))) {
            SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
            map.put("orgId", user.getOrganizationId());
        }
        List<BaseWarehouse> baseWarehouses = baseWarehouseMapper.findList(map);
        if (StringUtils.isNotEmpty(baseWarehouses)){
            SearchBaseMaterialOwnerReWh searchBaseMaterialOwnerReWh = new SearchBaseMaterialOwnerReWh();

            for (BaseWarehouse baseWarehouse : baseWarehouses) {
                searchBaseMaterialOwnerReWh.setWarehouseId(baseWarehouse.getWarehouseId());
                List<BaseMaterialOwnerReWhDto> list = baseMaterialOwnerReWhMapper.findList(ControllerUtil.dynamicConditionByEntity(searchBaseMaterialOwnerReWh));
                if (StringUtils.isNotEmpty(list)){
                    baseWarehouse.setBaseMaterialOwnerReWhDtos(list);
                }
            }
        }
        return baseWarehouses;
    }

    @Override
    public int batchUpdateByCode(List<BaseWarehouse> baseWarehouses) {
        if (StringUtils.isNotEmpty(baseWarehouses)){
            return baseWarehouseMapper.batchUpdateByCode(baseWarehouses);
        }else {
            return 0;
        }
    }

    @Override
    public int insertList(List<BaseWarehouse> baseWarehouses) {
        return baseWarehouseMapper.insertList(baseWarehouses);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importExcel(List<BaseWarehouseImport> baseWarehouseImports) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();

        Map<String, Object> resultMap = new HashMap<>();  //封装操作结果
        int success = 0;  //记录操作成功数
        List<Integer> fail = new ArrayList<>();  //记录操作失败行数
        LinkedList<BaseWarehouse> list = new LinkedList<>();
        LinkedList<BaseHtWarehouse> htList = new LinkedList<>();
        LinkedList<BaseWarehouseImport> warehouseImports = new LinkedList<>();
        LinkedList<BaseMaterialOwnerReWh> baseMaterialOwnerReWhs = new LinkedList<>();
        for (int i = 0; i < baseWarehouseImports.size(); i++) {
            BaseWarehouseImport baseWarehouseImport = baseWarehouseImports.get(i);
            String warehouseCode = baseWarehouseImport.getWarehouseCode();
            String warehouseName = baseWarehouseImport.getWarehouseName();

            if (StringUtils.isEmpty(
                    warehouseCode,warehouseName
            )){
                fail.add(i+4);
                continue;
            }

            //判断编码是否重复
            Example example = new Example(BaseWarehouse.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("orgId", currentUser.getOrganizationId());
            criteria.andEqualTo("warehouseCode",warehouseCode);
            if (StringUtils.isNotEmpty(baseWarehouseMapper.selectOneByExample(example))){
                fail.add(i+4);
                continue;
            }

            //如果货主编码不为空，则判断货主信息是否存在
            if (StringUtils.isNotEmpty(baseWarehouseImport.getMaterialOwnerCode())){
                Example example1 = new Example(BaseMaterialOwner.class);
                Example.Criteria criteria1 = example1.createCriteria();
                criteria1.andEqualTo("orgId", currentUser.getOrganizationId());
                criteria1.andEqualTo("materialOwnerCode",baseWarehouseImport.getMaterialOwnerCode());
                BaseMaterialOwner baseMaterialOwner = baseMaterialOwnerMapper.selectOneByExample(example1);
                if (StringUtils.isEmpty(baseMaterialOwner)){
                    fail.add(i+4);
                    continue;
                }
                baseWarehouseImport.setMaterialOwnerId(baseMaterialOwner.getMaterialOwnerId());
            }

            warehouseImports.add(baseWarehouseImport);
        }

        if (StringUtils.isNotEmpty(warehouseImports)){
            //对合格数据进行分组
            HashMap<String, List<BaseWarehouseImport>> map = warehouseImports.stream().collect(Collectors.groupingBy(BaseWarehouseImport::getWarehouseCode, HashMap::new, Collectors.toList()));
            Set<String> codeList = map.keySet();
            for (String code : codeList) {
                List<BaseWarehouseImport> baseWarehouseImports1 = map.get(code);
                //新增仓库父级数据
                BaseWarehouse baseWarehouse = new BaseWarehouse();
                BeanUtils.copyProperties(baseWarehouseImports1.get(0), baseWarehouse);
                baseWarehouse.setCreateTime(new Date());
                baseWarehouse.setCreateUserId(currentUser.getUserId());
                baseWarehouse.setModifiedUserId(currentUser.getUserId());
                baseWarehouse.setModifiedTime(new Date());
                baseWarehouse.setOrgId(currentUser.getOrganizationId());
                baseWarehouse.setStatus(1);
                success += baseWarehouseMapper.insertUseGeneratedKeys(baseWarehouse);

                BaseHtWarehouse baseHtWarehouse = new BaseHtWarehouse();
                BeanUtils.copyProperties(baseWarehouse, baseHtWarehouse);
                baseHtWarehouse.setModifiedTime(new Date());
                baseHtWarehouse.setModifiedUserId(currentUser.getUserId());
                htList.add(baseHtWarehouse);

                //新增关联货主数据
                for (BaseWarehouseImport baseWarehouseImport : baseWarehouseImports1) {
                    BaseMaterialOwnerReWh baseMaterialOwnerReWh = new BaseMaterialOwnerReWh();
                    BeanUtils.copyProperties(baseWarehouseImport, baseMaterialOwnerReWh);
                    baseMaterialOwnerReWh.setWarehouseId(baseWarehouse.getWarehouseId());
                    baseMaterialOwnerReWh.setStatus((byte) 1);
                    baseMaterialOwnerReWhs.add(baseMaterialOwnerReWh);
                }
                baseMaterialOwnerReWhMapper.insertList(baseMaterialOwnerReWhs);
            }
            baseHtWarehouseMapper.insertList(htList);
        }

        resultMap.put("操作成功总数",success);
        resultMap.put("操作失败行数",fail);
        return resultMap;
    }
}
