package com.fantechs.provider.base.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseBatchRulesDto;
import com.fantechs.common.base.general.dto.basic.imports.BaseBatchRulesImport;
import com.fantechs.common.base.general.entity.basic.BaseBatchRules;
import com.fantechs.common.base.general.entity.basic.BaseMaterialOwner;
import com.fantechs.common.base.general.entity.basic.BaseWarehouse;
import com.fantechs.common.base.general.entity.basic.history.BaseHtBatchRules;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.mapper.BaseBatchRulesMapper;
import com.fantechs.provider.base.mapper.BaseHtBatchRulesMapper;
import com.fantechs.provider.base.mapper.BaseMaterialOwnerMapper;
import com.fantechs.provider.base.mapper.BaseWarehouseMapper;
import com.fantechs.provider.base.service.BaseBatchRulesService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.*;

/**
 *
 * Created by leifengzhi on 2021/04/25.
 */
@Service
public class BaseBatchRulesServiceImpl extends BaseService<BaseBatchRules> implements BaseBatchRulesService {

    @Resource
    private BaseBatchRulesMapper baseBatchRulesMapper;
    @Resource
    private BaseHtBatchRulesMapper baseHtBatchRulesMapper;
    @Resource
    private BaseWarehouseMapper baseWarehouseMapper;
    @Resource
    private BaseMaterialOwnerMapper baseMaterialOwnerMapper;

    @Override
    public List<BaseBatchRulesDto> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId", user.getOrganizationId());
        return baseBatchRulesMapper.findList(map);
    }

    @Override
    public int save(BaseBatchRules record) {
        SysUser sysUser = currentUser();
        if(StringUtils.isEmpty(record.getBatchRulesName())){
            throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"批次名称不能为空");
        }
        record.setCreateTime(new Date());
        record.setCreateUserId(sysUser.getUserId());
        record.setModifiedTime(new Date());
        record.setModifiedUserId(sysUser.getUserId());
        record.setOrgId(sysUser.getOrganizationId());
        int num = baseBatchRulesMapper.insertUseGeneratedKeys(record);

        //添加履历
        BaseHtBatchRules baseHtBatchRules = new BaseHtBatchRules();
        BeanUtil.copyProperties(record,baseHtBatchRules);
        baseHtBatchRulesMapper.insertSelective(baseHtBatchRules);
        return num;
    }

    @Override
    public int update(BaseBatchRules entity) {
        SysUser sysUser = currentUser();
        entity.setModifiedTime(new Date());
        entity.setModifiedUserId(sysUser.getUserId());
        entity.setOrgId(sysUser.getOrganizationId());
        int num = baseBatchRulesMapper.updateByPrimaryKeySelective(entity);

        //添加履历
        BaseHtBatchRules baseHtBatchRules = new BaseHtBatchRules();
        BeanUtil.copyProperties(entity,baseHtBatchRules);
        baseHtBatchRulesMapper.insertSelective(baseHtBatchRules);

        return num;
    }

    @Override
    public int batchDelete(String ids) {
        SysUser sysUser = currentUser();
        String[] arrId = ids.split(",");
        List<BaseHtBatchRules> list = new ArrayList<>();
        for (String s : arrId) {
            BaseBatchRules baseBatchRules = baseBatchRulesMapper.selectByPrimaryKey(s);
            if(StringUtils.isEmpty(baseBatchRules)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
            BaseHtBatchRules baseHtBatchRules = new BaseHtBatchRules();
            BeanUtil.copyProperties(baseBatchRules,baseHtBatchRules);
            list.add(baseHtBatchRules);
        }
        baseHtBatchRulesMapper.insertList(list);
        return super.batchDelete(ids);
    }

    /**
     * 获取当前登录用户
     * @return
     */
    private SysUser currentUser(){
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        return user;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importExcel(List<BaseBatchRulesImport> baseBatchRulesImports) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();

        Map<String, Object> resutlMap = new HashMap<>();  //封装操作结果
        int success = 0;  //记录操作成功数
        List<Integer> fail = new ArrayList<>();  //记录操作失败行数
        LinkedList<BaseBatchRules> list = new LinkedList<>();
        LinkedList<BaseHtBatchRules> htList = new LinkedList<>();
        for (int i = 0; i < baseBatchRulesImports.size(); i++) {
            BaseBatchRulesImport baseBatchRulesImport = baseBatchRulesImports.get(i);
            String warehouseCode = baseBatchRulesImport.getWarehouseCode();
            String materialOwnerCode = baseBatchRulesImport.getMaterialOwnerCode();
            String batchRulesName = baseBatchRulesImport.getBatchRulesName();

            if (StringUtils.isEmpty(
                    warehouseCode,materialOwnerCode,batchRulesName
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
            baseBatchRulesImport.setWarehouseId(baseWarehouse.getWarehouseId());

            //货主信息
            Example example2 = new Example(BaseMaterialOwner.class);
            Example.Criteria criteria2 = example2.createCriteria();
            criteria2.andEqualTo("orgId", currentUser.getOrganizationId());
            criteria2.andEqualTo("materialOwnerCode", materialOwnerCode);
            BaseMaterialOwner baseMaterialOwner = baseMaterialOwnerMapper.selectOneByExample(example2);
            if (StringUtils.isEmpty(baseMaterialOwner)) {
                fail.add(i + 4);
                continue;
            }
            baseBatchRulesImport.setMaterialOwnerId(baseMaterialOwner.getMaterialOwnerId());


            BaseBatchRules baseBatchRules = new BaseBatchRules();
            BeanUtils.copyProperties(baseBatchRulesImport, baseBatchRules);
            baseBatchRules.setCreateTime(new Date());
            baseBatchRules.setCreateUserId(currentUser.getUserId());
            baseBatchRules.setModifiedTime(new Date());
            baseBatchRules.setModifiedUserId(currentUser.getUserId());
            baseBatchRules.setStatus((byte)1);
            baseBatchRules.setOrgId(currentUser.getOrganizationId());
            baseBatchRules.setNotMixedWith(StringUtils.isEmpty(baseBatchRulesImport.getNotMixedWith())?1:baseBatchRulesImport.getNotMixedWith().byteValue());
            baseBatchRules.setTailAfterBatch(StringUtils.isEmpty(baseBatchRulesImport.getTailAfterBatch())?1:baseBatchRulesImport.getTailAfterBatch().byteValue());
            baseBatchRules.setTailAfterDateInProduced(StringUtils.isEmpty(baseBatchRulesImport.getTailAfterDateInProduced())?1:baseBatchRulesImport.getTailAfterDateInProduced().byteValue());
            baseBatchRules.setTailAfterReceivingCode(StringUtils.isEmpty(baseBatchRulesImport.getTailAfterReceivingCode())?1:baseBatchRulesImport.getTailAfterReceivingCode().byteValue());
            baseBatchRules.setTailAfterReceiveDate(StringUtils.isEmpty(baseBatchRulesImport.getTailAfterReceiveDate())?1:baseBatchRulesImport.getTailAfterReceiveDate().byteValue());
            baseBatchRules.setTailAfterQualityDate(StringUtils.isEmpty(baseBatchRulesImport.getTailAfterQualityDate())?1:baseBatchRulesImport.getTailAfterQualityDate().byteValue());
            baseBatchRules.setTailAfterSaleCode(StringUtils.isEmpty(baseBatchRulesImport.getTailAfterSaleCode())?1:baseBatchRulesImport.getTailAfterSaleCode().byteValue());
            baseBatchRules.setTailAfterSupplier(StringUtils.isEmpty(baseBatchRulesImport.getTailAfterSupplier())?1:baseBatchRulesImport.getTailAfterSupplier().byteValue());
            list.add(baseBatchRules);
        }

        if (StringUtils.isNotEmpty(list)){
            success = baseBatchRulesMapper.insertList(list);
        }

        for (BaseBatchRules baseBatchRules : list) {
            BaseHtBatchRules baseHtBatchRules = new BaseHtBatchRules();
            BeanUtils.copyProperties(baseBatchRules, baseHtBatchRules);
            htList.add(baseHtBatchRules);
        }

        if (StringUtils.isNotEmpty(htList)){
            baseHtBatchRulesMapper.insertList(htList);
        }

        resutlMap.put("操作成功总数",success);
        resutlMap.put("操作失败行数",fail);
        return resutlMap;
    }
}
