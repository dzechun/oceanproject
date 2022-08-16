package com.fantechs.provider.base.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseSafeStockDto;
import com.fantechs.common.base.general.dto.basic.imports.BaseSafeStockImport;
import com.fantechs.common.base.general.entity.basic.BaseMaterial;
import com.fantechs.common.base.general.entity.basic.BaseMaterialOwner;
import com.fantechs.common.base.general.entity.basic.BaseSafeStock;
import com.fantechs.common.base.general.entity.basic.BaseWarehouse;
import com.fantechs.common.base.general.entity.basic.history.BaseHtSafeStock;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.mapper.*;
import com.fantechs.provider.base.service.BaseSafeStockService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 *
 * Created by mr.lei on 2021/03/04.
 */
@Service
public class BaseSafeStockServiceImpl extends BaseService<BaseSafeStock> implements BaseSafeStockService {

    @Resource
    private BaseSafeStockMapper baseSafeStockMapper;
    @Resource
    private BaseHtSafeStockMapper baseHtSafeStockMapper;
    @Resource
    private BaseWarehouseMapper baseWarehouseMapper;
    @Resource
    private BaseMaterialMapper baseMaterialMapper;
    @Resource
    private BaseMaterialOwnerMapper baseMaterialOwnerMapper;


    @Override
    public List<BaseSafeStockDto> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId", user.getOrganizationId());
        return baseSafeStockMapper.findList(map);
    }

    @Override
    public List<BaseSafeStockDto> findHtList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId", user.getOrganizationId());
        return baseHtSafeStockMapper.findHtList(map);
    }

    /**
     * 安全库存预警
     * @return
     */
    @Override
    public int inventeryWarning() {
        List<BaseSafeStockDto> list = findList(new HashMap<>());
        List<BaseSafeStockDto> oltList = new ArrayList<>();
        for (BaseSafeStockDto oltSafeStockDto : list) {
            BigDecimal qty = baseSafeStockMapper.selectCountByWare(oltSafeStockDto.getWarehouseId(),oltSafeStockDto.getMaterialId());
            if(qty.compareTo(oltSafeStockDto.getMaxQty())==1){
                oltSafeStockDto.setIsMax(true);
                oltList.add(oltSafeStockDto);
            }else if(qty.compareTo(oltSafeStockDto.getMinQty())==-1){
                oltSafeStockDto.setIsMax(false);
                oltList.add(oltSafeStockDto);

            }
        }
        if(oltList.size()>0){
            StringBuffer sb = new StringBuffer();
            sb.append("安全库存预警：\n");
            for (BaseSafeStockDto oltSafeStockDto : oltList) {
                if(oltSafeStockDto.getIsMax()){
                    sb.append("仓库："+oltSafeStockDto.getWarehouseName()+"超出设定安全库存");
                }else{
                    sb.append("仓库："+oltSafeStockDto.getWarehouseName()+"低于设定安全库存");
                }
            }
//            SearchBaseWarning searchBaseWarning = new SearchBaseWarning();
//            searchBaseWarning
//            List<BaseWarningDto> baseWarningDtos = baseFeignApi.findBaseWarningList(searchBaseWarning).getData();
        }
        return 1;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int save(BaseSafeStock record) {
        SysUser sysUser = currentUser();
        if(StringUtils.isEmpty(record.getWarehouseId())){
            throw new BizErrorException("仓库不能为空");
        }
        record.setCreateTime(new Date());
        record.setCreateUserId(sysUser.getUserId());
        record.setModifiedTime(new Date());
        record.setModifiedUserId(sysUser.getUserId());
        record.setOrganizationId(sysUser.getOrganizationId());
        int num = baseSafeStockMapper.insertUseGeneratedKeys(record);
        recordHistory(record);
        return num;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int update(BaseSafeStock entity) {
        SysUser sysUser = currentUser();
        entity.setModifiedUserId(sysUser.getUserId());
        entity.setModifiedTime(new Date());
        entity.setOrganizationId(sysUser.getOrganizationId());
        int num = baseSafeStockMapper.updateByPrimaryKeySelective(entity);
        recordHistory(entity);
        return num;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int batchDelete(String ids) {
        SysUser sysUser = currentUser();
        String[] idArray = ids.split(",");
        for (String id : idArray) {
            BaseSafeStock baseSafeStock = baseSafeStockMapper.selectByPrimaryKey(id);
            if(StringUtils.isEmpty(baseSafeStock)){
                throw new BizErrorException("删除失败");
            }
            recordHistory(baseSafeStock);
        }
        return baseSafeStockMapper.deleteByIds(ids);
    }

    /**
     * 获取当前登录用户
     * @return
     */
    private SysUser currentUser(){
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        return user;
    }

    /**
     * 历史记录
     * @param baseSafeStock
     */
    private void recordHistory(BaseSafeStock baseSafeStock){
        BaseHtSafeStock baseHtSafeStock = new BaseHtSafeStock();
        if (StringUtils.isEmpty(baseHtSafeStock)){
            return;
        }
        BeanUtils.copyProperties(baseSafeStock, baseHtSafeStock);
        baseHtSafeStockMapper.insertSelective(baseHtSafeStock);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importExcel(List<BaseSafeStockImport> baseSafeStockImports) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        Map<String, Object> resutlMap = new HashMap<>();  //封装操作结果
        int success = 0;  //记录操作成功数
        List<Integer> fail = new ArrayList<>();  //记录操作失败行数
        LinkedList<BaseSafeStock> list = new LinkedList<>();
        LinkedList<BaseHtSafeStock> htList = new LinkedList<>();
        for (int i = 0; i < baseSafeStockImports.size(); i++) {
            BaseSafeStockImport baseSafeStockImport = baseSafeStockImports.get(i);
            String warehouseCode = baseSafeStockImport.getWarehouseCode();

            if (StringUtils.isEmpty(
                    warehouseCode
            )){
                fail.add(i+4);
                continue;
            }

            //仓库信息
            Example example1 = new Example(BaseWarehouse.class);
            Example.Criteria criteria1 = example1.createCriteria();
            criteria1.andEqualTo("orgId", currentUser.getOrganizationId());
            criteria1.andEqualTo("warehouseCode", warehouseCode);
            BaseWarehouse baseWarehouse = baseWarehouseMapper.selectOneByExample(example1);
            if (StringUtils.isEmpty(baseWarehouse)) {
                fail.add(i + 4);
                continue;
            }
            baseSafeStockImport.setWarehouseId(baseWarehouse.getWarehouseId());

            //物料信息
            String materialCode = baseSafeStockImport.getMaterialCode();
            if(StringUtils.isNotEmpty(materialCode)){
                Example example2 = new Example(BaseMaterial.class);
                Example.Criteria criteria2 = example2.createCriteria();
                criteria2.andEqualTo("organizationId", currentUser.getOrganizationId());
                criteria2.andEqualTo("materialCode",materialCode);
                BaseMaterial baseMaterial = baseMaterialMapper.selectOneByExample(example2);
                if (StringUtils.isEmpty(baseMaterial)){
                    fail.add(i+4);
                    continue;
                }
                baseSafeStockImport.setMaterialId(baseMaterial.getMaterialId());
            }

            //货主信息
            String materialOwnerCode = baseSafeStockImport.getMaterialOwnerCode();
            if(StringUtils.isNotEmpty(materialOwnerCode)){
                Example example3 = new Example(BaseMaterialOwner.class);
                Example.Criteria criteria3 = example3.createCriteria();
                criteria3.andEqualTo("orgId", currentUser.getOrganizationId());
                criteria3.andEqualTo("materialOwnerCode",materialOwnerCode);
                BaseMaterialOwner baseMaterialOwner = baseMaterialOwnerMapper.selectOneByExample(example3);
                if (StringUtils.isEmpty(baseMaterialOwner)){
                    fail.add(i+4);
                    continue;
                }
                baseSafeStockImport.setMaterialOwnerId(baseMaterialOwner.getMaterialOwnerId());
            }

            BaseSafeStock baseSafeStock = new BaseSafeStock();
            BeanUtils.copyProperties(baseSafeStockImport, baseSafeStock);
            baseSafeStock.setCreateTime(new Date());
            baseSafeStock.setCreateUserId(currentUser.getUserId());
            baseSafeStock.setModifiedTime(new Date());
            baseSafeStock.setModifiedUserId(currentUser.getUserId());
            baseSafeStock.setStatus(StringUtils.isEmpty(baseSafeStock.getStatus())?1:baseSafeStock.getStatus().byteValue());
            baseSafeStock.setOrganizationId(currentUser.getOrganizationId());
            list.add(baseSafeStock);
        }

        if (StringUtils.isNotEmpty(list)){
            success = baseSafeStockMapper.insertList(list);
        }

        for (BaseSafeStock baseSafeStock : list) {
            BaseHtSafeStock baseHtSafeStock = new BaseHtSafeStock();
            BeanUtils.copyProperties(baseSafeStock, baseHtSafeStock);
            htList.add(baseHtSafeStock);
        }

        if (StringUtils.isNotEmpty(htList)){
            baseHtSafeStockMapper.insertList(htList);
        }
        resutlMap.put("操作成功总数",success);
        resutlMap.put("操作失败行数",fail);
        return resutlMap;
    }
}
