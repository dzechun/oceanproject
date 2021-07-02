package com.fantechs.provider.base.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.general.dto.basic.imports.BaseProLineImport;
import com.fantechs.common.base.general.dto.basic.imports.BaseStorageImport;
import com.fantechs.common.base.general.dto.basic.imports.BaseWorkShopImport;
import com.fantechs.common.base.general.entity.basic.*;
import com.fantechs.common.base.general.entity.basic.history.BaseHtStation;
import com.fantechs.common.base.general.entity.basic.history.BaseHtStorage;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseStorage;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.mapper.*;
import com.fantechs.provider.base.service.BaseStorageService;
import com.fantechs.provider.base.util.StorageRuleDto;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by wcz on 2020/09/23.
 */
@Service
public class BaseStorageServiceImpl extends BaseService<BaseStorage> implements BaseStorageService {

    @Resource
    private BaseStorageMapper baseStorageMapper;
    @Resource
    private BaseHtStorageMapper baseHtStorageMapper;
    @Resource
    private BaseStorageMaterialMapper baseStorageMaterialMapper;
    @Resource
    private BaseWarehouseAreaMapper baseWarehouseAreaMapper;
    @Resource
    private BaseWorkingAreaMapper baseWorkingAreaMapper;

    @Override
    public int batchUpdate(List<BaseStorage> baseStorages) {
        return baseStorageMapper.batchUpdate(baseStorages);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(BaseStorage baseStorage) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(currentUser)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        Example example = new Example(BaseStorage.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("organizationId", currentUser.getOrganizationId());
        criteria.andEqualTo("storageCode", baseStorage.getStorageCode());
        List<BaseStorage> baseStorages = baseStorageMapper.selectByExample(example);
        if (StringUtils.isNotEmpty(baseStorages)) {
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        baseStorage.setCreateUserId(currentUser.getUserId());
        baseStorage.setCreateTime(new Date());
        baseStorage.setModifiedUserId(currentUser.getUserId());
        baseStorage.setModifiedTime(new Date());
        baseStorage.setOrganizationId(currentUser.getOrganizationId());
        baseStorageMapper.insertUseGeneratedKeys(baseStorage);

        //新增库位历史信息
        BaseHtStorage baseHtStorage = new BaseHtStorage();
        org.springframework.beans.BeanUtils.copyProperties(baseStorage, baseHtStorage);
        int i = baseHtStorageMapper.insertSelective(baseHtStorage);
        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDelete(String ids) {
        int i = 0;
        List<BaseHtStorage> list = new ArrayList<>();

        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(currentUser)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        String[] storageIds = ids.split(",");
        for (String storageId : storageIds) {
            BaseStorage baseStorage = baseStorageMapper.selectByPrimaryKey(Long.parseLong(storageId));
            if (StringUtils.isEmpty(baseStorage)) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }

            //被库位物料引用
            Example example = new Example(BaseStorageMaterial.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("storageId", storageId);
            List<BaseStorageMaterial> baseStorageMaterials = baseStorageMaterialMapper.selectByExample(example);
            if (StringUtils.isNotEmpty(baseStorageMaterials)) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012004);
            }

            //该库位和电子标签控制器绑定
//            SearchSmtElectronicTagStorage searchSmtElectronicTagStorage = new SearchSmtElectronicTagStorage();
//            searchSmtElectronicTagStorage.setStorageId(storageId);
//            ResponseEntity<List<SmtElectronicTagStorageDto>> list1 = electronicTagFeignApi.findList(searchSmtElectronicTagStorage);
//            if (StringUtils.isNotEmpty(list1.getData().get(0))){
//                throw new BizErrorException(ErrorCodeEnum.OPT20012004);
//            }

            //新增库位历史信息
            BaseHtStorage baseHtStorage = new BaseHtStorage();
            org.springframework.beans.BeanUtils.copyProperties(baseStorage, baseHtStorage);
            baseHtStorage.setModifiedUserId(currentUser.getUserId());
            baseHtStorage.setModifiedTime(new Date());
            list.add(baseHtStorage);
        }
        baseHtStorageMapper.insertList(list);

        return baseStorageMapper.deleteByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(BaseStorage storage) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(currentUser)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        Example example = new Example(BaseStorage.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("organizationId", currentUser.getOrganizationId());
        criteria.andEqualTo("storageCode", storage.getStorageCode());

        BaseStorage baseStorage = baseStorageMapper.selectOneByExample(example);

        if (StringUtils.isNotEmpty(baseStorage) && !baseStorage.getStorageId().equals(storage.getStorageId())) {
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        storage.setModifiedUserId(currentUser.getUserId());
        storage.setModifiedTime(new Date());
        storage.setOrganizationId(currentUser.getOrganizationId());
        int i = baseStorageMapper.updateByPrimaryKeySelective(storage);

        //新增库位历史信息
        BaseHtStorage baseHtStorage = new BaseHtStorage();
        org.springframework.beans.BeanUtils.copyProperties(storage, baseHtStorage);
        baseHtStorageMapper.insertSelective(baseHtStorage);
        return i;
    }

    @Override
    public List<BaseStorage> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        map.put("orgId", user.getOrganizationId());
        return baseStorageMapper.findList(map);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importExcel(List<BaseStorageImport> baseStorageImports) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        Map<String, Object> resutlMap = new HashMap<>();  //封装操作结果
        int success = 0;  //记录操作成功数
        List<Integer> fail = new ArrayList<>();  //记录操作失败行数
        LinkedList<BaseStorage> list = new LinkedList<>();
        LinkedList<BaseHtStorage> htList = new LinkedList<>();
        ArrayList<BaseStorageImport> baseStorageImportArrayList = new ArrayList<>();
        for (int i = 0; i < baseStorageImports.size(); i++) {
            BaseStorageImport baseStorageImport = baseStorageImports.get(i);
            String storageCode = baseStorageImport.getStorageCode();
            String warehouseAreaCode = baseStorageImport.getWarehouseAreaCode();
            Integer storageType = baseStorageImport.getStorageType();
            String workingAreaCode = baseStorageImport.getWorkingAreaCode();
            Integer roadway = baseStorageImport.getRoadway();
            Integer rowNo = baseStorageImport.getRowNo();
            Integer columnNo= baseStorageImport.getColumnNo();
            Integer levelNo = baseStorageImport.getLevelNo();
            Integer pickingMoveLineNo = baseStorageImport.getPickingMoveLineNo();
            Integer putawayMoveLineNo = baseStorageImport.getPutawayMoveLineNo();
            Integer stockMoveLineNo = baseStorageImport.getStockMoveLineNo();

            if (StringUtils.isEmpty(
                    storageCode,warehouseAreaCode,storageType,workingAreaCode,roadway,rowNo,
                    columnNo,levelNo,pickingMoveLineNo,putawayMoveLineNo,stockMoveLineNo
            )){
                fail.add(i+4);
                continue;
            }

            //判断仓库区域是否存在
            Example example1 = new Example(BaseWarehouseArea.class);
            Example.Criteria criteria1 = example1.createCriteria();
            criteria1.andEqualTo("organizationId", currentUser.getOrganizationId());
            criteria1.andEqualTo("warehouseAreaCode",warehouseAreaCode);
            BaseWarehouseArea baseWarehouseArea = baseWarehouseAreaMapper.selectOneByExample(example1);
            if (StringUtils.isEmpty(baseWarehouseArea)){
                fail.add(i+4);
                continue;
            }
            baseStorageImport.setWarehouseId(baseWarehouseArea.getWarehouseId());
            baseStorageImport.setWarehouseAreaId(baseWarehouseArea.getWarehouseAreaId());

            //判断工作区是否存在
            Example example2 = new Example(BaseWorkingArea.class);
            Example.Criteria criteria2 = example2.createCriteria();
            criteria2.andEqualTo("orgId", currentUser.getOrganizationId());
            criteria2.andEqualTo("workingAreaCode",workingAreaCode);
            BaseWorkingArea baseWorkingArea = baseWorkingAreaMapper.selectOneByExample(example2);
            if (StringUtils.isEmpty(baseWorkingArea)){
                fail.add(i+4);
                continue;
            }
            baseStorageImport.setWorkingAreaId(baseWorkingArea.getWorkingAreaId());

            baseStorageImportArrayList.add(baseStorageImport);
        }

        for (BaseStorageImport baseStorageImport : baseStorageImportArrayList) {
            BaseStorage baseStorage = new BaseStorage();
            BeanUtils.copyProperties(baseStorageImport, baseStorage);
            baseStorage.setStatus(1);
            baseStorage.setCreateTime(new Date());
            baseStorage.setCreateUserId(currentUser.getUserId());
            baseStorage.setModifiedTime(new Date());
            baseStorage.setModifiedUserId(currentUser.getUserId());
            baseStorage.setOrganizationId(currentUser.getOrganizationId());
            baseStorage.setStorageType(baseStorageImport.getStorageType().byteValue());
            list.add(baseStorage);
        }

        if (StringUtils.isNotEmpty(list)){
            success = baseStorageMapper.insertList(list);
        }

        for (BaseStorage baseStorage : list) {
            BaseHtStorage baseHtStorage = new BaseHtStorage();
            BeanUtils.copyProperties(baseStorage, baseHtStorage);
            htList.add(baseHtStorage);
        }

        if (StringUtils.isNotEmpty(htList)){
            baseHtStorageMapper.insertList(htList);
        }

        resutlMap.put("操作成功总数",success);
        resutlMap.put("操作失败行数",fail);
        return resutlMap;
    }

    @Override
    public int minusSurplusCanPutSalver(Long storageId,Integer num) {
        BaseStorage baseStorage = baseStorageMapper.selectByPrimaryKey(storageId);
        if (StringUtils.isEmpty(baseStorage)) {
            throw new BizErrorException(ErrorCodeEnum.OPT20012003);
        }
        baseStorage.setSurplusCanPutSalver(baseStorage.getSurplusCanPutSalver()-num);
        return baseStorageMapper.updateByPrimaryKeySelective(baseStorage);
    }

    @Override
    public int plusSurplusCanPutSalver(Long storageId, Integer num) {
        BaseStorage baseStorage = baseStorageMapper.selectByPrimaryKey(storageId);
        if (StringUtils.isEmpty(baseStorage)) {
            throw new BizErrorException(ErrorCodeEnum.OPT20012003);
        }
        baseStorage.setSurplusCanPutSalver(baseStorage.getSurplusCanPutSalver()+num);
        return baseStorageMapper.updateByPrimaryKeySelective(baseStorage);
    }

    @Override
    public List<StorageRuleDto> findPutawayRule(Map<String,Object> map) {
        return baseStorageMapper.findPutawayRule(map);
    }
}
