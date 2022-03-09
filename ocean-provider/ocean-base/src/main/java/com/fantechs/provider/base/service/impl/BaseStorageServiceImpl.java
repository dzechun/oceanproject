package com.fantechs.provider.base.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysSpecItem;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.entity.security.search.SearchSysSpecItem;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.PrintBaseStorageCode;
import com.fantechs.common.base.general.dto.basic.StorageRuleDto;
import com.fantechs.common.base.general.dto.basic.imports.BaseStorageImport;
import com.fantechs.common.base.general.dto.mes.sfc.PrintDto;
import com.fantechs.common.base.general.dto.mes.sfc.PrintModel;
import com.fantechs.common.base.general.dto.restapi.EngReportStorageDto;
import com.fantechs.common.base.general.entity.basic.*;
import com.fantechs.common.base.general.entity.basic.history.BaseHtStorage;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.JsonUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.common.base.utils.UUIDUtils;
import com.fantechs.provider.api.guest.fivering.FiveringFeignApi;
import com.fantechs.provider.api.mes.sfc.SFCFeignApi;
import com.fantechs.provider.api.security.service.SecurityFeignApi;
import com.fantechs.provider.base.mapper.*;
import com.fantechs.provider.base.service.BaseStorageService;
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
    @Resource
    private SFCFeignApi sfcFeignApi;
    @Resource
    private SecurityFeignApi securityFeignApi;
    @Resource
    private FiveringFeignApi fiveringFeignApi;
    @Resource
    private BaseProLineMapper baseProLineMapper;
    @Resource
    private WanbaoErpLogicMapper wanbaoErpLogicMapper;

    @Override
    public int batchUpdate(List<BaseStorage> baseStorages) {
        return baseStorageMapper.batchUpdate(baseStorages);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(BaseStorage baseStorage) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();

        Example example = new Example(BaseStorage.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("orgId", currentUser.getOrganizationId());
        criteria.andEqualTo("storageCode", baseStorage.getStorageCode());
        List<BaseStorage> baseStorages = baseStorageMapper.selectByExample(example);
        if (StringUtils.isNotEmpty(baseStorages)) {
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        baseStorage.setCreateUserId(currentUser.getUserId());
        baseStorage.setCreateTime(new Date());
        baseStorage.setModifiedUserId(currentUser.getUserId());
        baseStorage.setModifiedTime(new Date());
        baseStorage.setOrgId(currentUser.getOrganizationId());
        baseStorageMapper.insertUseGeneratedKeys(baseStorage);

        //新增库位历史信息
        BaseHtStorage baseHtStorage = new BaseHtStorage();
        org.springframework.beans.BeanUtils.copyProperties(baseStorage, baseHtStorage);
        int i = baseHtStorageMapper.insertSelective(baseHtStorage);

        //五环库位回传开始
        SearchSysSpecItem searchSysSpecItemFiveRing = new SearchSysSpecItem();
        searchSysSpecItemFiveRing.setSpecCode("FiveRing");
        List<SysSpecItem> itemListFiveRing = securityFeignApi.findSpecItemList(searchSysSpecItemFiveRing).getData();
        if (itemListFiveRing.size() < 1) {
            throw new BizErrorException("配置项 FiveRing 获取失败");
        }
        SysSpecItem sysSpecItem = itemListFiveRing.get(0);
        if ("1".equals(sysSpecItem.getParaValue())) {
            String jsonVoiceArray="";
            String projectID="3919";
            List<EngReportStorageDto> listStorage=new ArrayList<>();

            String uuid= UUIDUtils.getRawUUID();
            EngReportStorageDto engReportStorageDto=new EngReportStorageDto();
            engReportStorageDto.setStorageId(uuid);
            engReportStorageDto.setStorageCode(baseStorage.getStorageCode());
            engReportStorageDto.setStorageDesc(baseStorage.getStorageCode());
            engReportStorageDto.setStorageType("项目现场主仓库");
            listStorage.add(engReportStorageDto);

            jsonVoiceArray= JsonUtils.objectToJson(listStorage);
            String s0=jsonVoiceArray.replaceAll("storageId","DHGUID");
            String s1=s0.replaceAll("storageCode","货架编号");
            String s2=s1.replaceAll("storageDesc","货架编号描述");
            String s3=s2.replaceAll("storageType","货架地点类别");

            //回传
            fiveringFeignApi.writeShelvesNo(s3,projectID);

            //更新DHGUID
            baseStorage.setOption1(uuid);
            baseStorageMapper.updateByPrimaryKeySelective(baseStorage);
        }
        //五环库位回传结束

        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDelete(String ids) {
        int i = 0;
        List<BaseHtStorage> list = new ArrayList<>();

        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();

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

        Example example = new Example(BaseStorage.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("orgId", currentUser.getOrganizationId());
        criteria.andEqualTo("storageCode", storage.getStorageCode());

        BaseStorage baseStorage = baseStorageMapper.selectOneByExample(example);

        if (StringUtils.isNotEmpty(baseStorage) && !baseStorage.getStorageId().equals(storage.getStorageId())) {
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        storage.setModifiedUserId(currentUser.getUserId());
        storage.setModifiedTime(new Date());
        storage.setOrgId(currentUser.getOrganizationId());
        int i = baseStorageMapper.updateByPrimaryKeySelective(storage);

        //新增库位历史信息
        BaseHtStorage baseHtStorage = new BaseHtStorage();
        org.springframework.beans.BeanUtils.copyProperties(storage, baseHtStorage);
        baseHtStorageMapper.insertSelective(baseHtStorage);
        return i;
    }

    @Override
    public List<BaseStorage> findList(Map<String, Object> map) {
        if(StringUtils.isEmpty(map.get("orgId"))){
            SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
            map.put("orgId", user.getOrganizationId());
        }

        return baseStorageMapper.findList(map);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importExcel(List<BaseStorageImport> baseStorageImports) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
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
            Integer materialStoreType = baseStorageImport.getMaterialStoreType();
            Integer isHeelpiece = baseStorageImport.getIsHeelpiece();
            String proCode = baseStorageImport.getProCode();
            String logicCode = baseStorageImport.getLogicCode();

            if (StringUtils.isEmpty(
                    storageCode,warehouseAreaCode,storageType,workingAreaCode,roadway,rowNo,
                    columnNo,levelNo,pickingMoveLineNo,putawayMoveLineNo,stockMoveLineNo,
                    materialStoreType,isHeelpiece,proCode,logicCode
            )){
                fail.add(i+4);
                continue;
            }

            //判断仓库区域是否存在
            Example example1 = new Example(BaseWarehouseArea.class);
            Example.Criteria criteria1 = example1.createCriteria();
            criteria1.andEqualTo("orgId", currentUser.getOrganizationId());
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

            //判断产线是否存在
            Example example3 = new Example(BaseProLine.class);
            Example.Criteria criteria3 = example3.createCriteria();
            criteria3.andEqualTo("organizationId", currentUser.getOrganizationId());
            criteria3.andEqualTo("proCode",proCode);
            BaseProLine baseProLine = baseProLineMapper.selectOneByExample(example3);
            if (StringUtils.isEmpty(baseProLine)){
                fail.add(i+4);
                continue;
            }
            baseStorageImport.setProLineId(baseProLine.getProLineId());

            //判断ERP逻辑仓是否存在
            Example example4 = new Example(WanbaoErpLogic.class);
            Example.Criteria criteria4 = example4.createCriteria();
            criteria4.andEqualTo("orgId", currentUser.getOrganizationId());
            criteria4.andEqualTo("logicCode",logicCode);
            WanbaoErpLogic wanbaoErpLogic = wanbaoErpLogicMapper.selectOneByExample(example4);
            if (StringUtils.isEmpty(wanbaoErpLogic)){
                fail.add(i+4);
                continue;
            }
            baseStorageImport.setLogicId(wanbaoErpLogic.getLogicId());

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
            baseStorage.setOrgId(currentUser.getOrganizationId());
            baseStorage.setStorageType(baseStorageImport.getStorageType().byteValue());
            baseStorage.setMaterialStoreType(baseStorageImport.getMaterialStoreType().byteValue());
            baseStorage.setIsHeelpiece(baseStorageImport.getIsHeelpiece().byteValue());
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
    public List<StorageRuleDto> findStorageMaterial(Map<String, Object> map) {
        return baseStorageMapper.findStorageMaterial(map);
    }

    @Override
    public List<StorageRuleDto> findPutawayRule(Map<String,Object> map) {
        return baseStorageMapper.findPutawayRule(map);
    }

    @Override
    public Integer findPutawayNo(Long warehouseId, Long materialId) {
        return baseStorageMapper.findPutawayNo(warehouseId,materialId);
    }

    @Override
    public List<StorageRuleDto> BatchEqualStorage(Map<String, Object> map) {
        return baseStorageMapper.BatchEqualStorage(map);
    }

    @Override
    public List<StorageRuleDto> EmptyStorage(Map<String, Object> map) {
        return baseStorageMapper.EmptyStorage(map);
    }

    @Override
    public List<StorageRuleDto> MixedWithStorage(Map<String, Object> map) {
        return baseStorageMapper.MixedWithStorage(map);
    }

    @Override
    public List<StorageRuleDto> LastStorage(Map<String, Object> map) {
        return baseStorageMapper.LastStorage(map);
    }

    @Override
    public int saveByApi(BaseStorage baseStorage) {
        Example example = new Example(BaseStorage.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("storageCode",baseStorage.getStorageCode());
        criteria.andEqualTo("orgId",baseStorage.getOrgId());
        BaseStorage baseStorageExist = baseStorageMapper.selectOneByExample(example);
        int i= 0;
        if(StringUtils.isEmpty(baseStorageExist)) {
            baseStorage.setCreateTime(new Date());
            baseStorage.setCreateUserId((long) 1);
            baseStorage.setModifiedUserId((long) 1);
            baseStorage.setModifiedTime(new Date());
            baseStorage.setIsDelete((byte) 1);
            i = baseStorageMapper.insertSelective(baseStorage);
        }else{
            baseStorage.setStorageId(baseStorageExist.getStorageId());
            baseStorage.setModifiedTime(new Date());
            baseStorageMapper.updateByPrimaryKeySelective(baseStorage);
        }
        return i;
    }

    @Override
    public int printStorageCode(List<PrintBaseStorageCode> printBaseStorageCodes) {
        for (PrintBaseStorageCode printBaseStorageCode : printBaseStorageCodes) {
            if(StringUtils.isEmpty(printBaseStorageCode.getPrintName())){
                throw new BizErrorException("请输入打印机名称");
            }
            PrintDto printDto = new PrintDto();
            printDto.setPrintName(printBaseStorageCode.getPrintName());
            printDto.setLabelName(printBaseStorageCode.getPrintMode());
            printDto.setLabelVersion("0.0.1");
            List<PrintModel> printModels = new ArrayList<>();
            PrintModel printModel = new PrintModel();
            printModel.setId(Long.parseLong("1"));
            printModel.setQrCode(printBaseStorageCode.getStorageCode());
            printModel.setSize(printBaseStorageCode.getSize());
            printModels.add(printModel);
            printDto.setPrintModelList(printModels);
            ResponseEntity responseEntity = sfcFeignApi.print(printDto);
            if(responseEntity.getCode()!=0){
                throw new BizErrorException(responseEntity.getCode(),responseEntity.getMessage());
            }
        }
        return 1;
    }
}
