package com.fantechs.provider.eam.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysSpecItem;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.entity.security.search.SearchSysSpecItem;
import com.fantechs.common.base.entity.security.search.SearchSysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseWarehouseAreaDto;
import com.fantechs.common.base.general.dto.basic.BaseWorkingAreaDto;
import com.fantechs.common.base.general.dto.eam.EamJigDto;
import com.fantechs.common.base.general.dto.eam.EamSparePartReJigDto;
import com.fantechs.common.base.general.dto.eam.imports.EamJigImport;
import com.fantechs.common.base.general.entity.basic.BaseStorage;
import com.fantechs.common.base.general.entity.basic.BaseWarehouse;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseStorage;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseWarehouse;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseWarehouseArea;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseWorkingArea;
import com.fantechs.common.base.general.entity.eam.*;
import com.fantechs.common.base.general.entity.eam.history.EamHtJig;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.api.auth.service.AuthFeignApi;
import com.fantechs.provider.eam.mapper.*;
import com.fantechs.provider.eam.service.EamJigService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 *
 * Created by leifengzhi on 2021/07/28.
 */
@Service
public class EamJigServiceImpl extends BaseService<EamJig> implements EamJigService {

    @Resource
    private EamJigMapper eamJigMapper;
    @Resource
    private EamHtJigMapper eamHtJigMapper;
    @Resource
    private EamJigBarcodeMapper eamJigBarcodeMapper;
    @Resource
    private EamJigAttachmentMapper eamJigAttachmentMapper;
    @Resource
    private EamSparePartReJigMapper eamSparePartReJigMapper;
    @Resource
    private EamJigCategoryMapper eamJigCategoryMapper;
    @Resource
    private AuthFeignApi securityFeignApi;
    @Resource
    private BaseFeignApi baseFeignApi;
    @Resource
    private EamSparePartMapper eamSparePartMapper;

    @Override
    public List<EamJigDto> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId", user.getOrganizationId());

        return eamJigMapper.findList(map);
    }


    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int save(EamJig record) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        //编码是否重复
        this.check(record,user);

        record.setCreateUserId(user.getUserId());
        record.setCreateTime(new Date());
        record.setModifiedUserId(user.getUserId());
        record.setModifiedTime(new Date());
        record.setStatus(StringUtils.isEmpty(record.getStatus())?1: record.getStatus());
        record.setOrgId(user.getOrganizationId());
        eamJigMapper.insertUseGeneratedKeys(record);

        //条码列表
        List<EamJigBarcode> eamJigBarcodeList = record.getEamJigBarcodeList();
        if(StringUtils.isNotEmpty(eamJigBarcodeList)){
            this.barcodeIfRepeat(eamJigBarcodeList,user);

            for (EamJigBarcode eamJigBarcode : eamJigBarcodeList){
                eamJigBarcode.setJigId(record.getJigId());
                eamJigBarcode.setCreateUserId(user.getUserId());
                eamJigBarcode.setCreateTime(new Date());
                eamJigBarcode.setModifiedUserId(user.getUserId());
                eamJigBarcode.setModifiedTime(new Date());
                eamJigBarcode.setStatus(record.getStatus());
                eamJigBarcode.setOrgId(user.getOrganizationId());
                eamJigBarcode.setUsageStatus((byte)1);
            }
            eamJigBarcodeMapper.insertList(eamJigBarcodeList);
        }

        //附件列表
        List<EamJigAttachment> eamJigAttachmentList = record.getEamJigAttachmentList();
        if(StringUtils.isNotEmpty(eamJigAttachmentList)){
            for (EamJigAttachment eamJigAttachment : eamJigAttachmentList){
                eamJigAttachment.setJigId(record.getJigId());
                eamJigAttachment.setCreateUserId(user.getUserId());
                eamJigAttachment.setCreateTime(new Date());
                eamJigAttachment.setModifiedUserId(user.getUserId());
                eamJigAttachment.setModifiedTime(new Date());
                eamJigAttachment.setStatus(StringUtils.isEmpty(eamJigAttachment.getStatus())?1: eamJigAttachment.getStatus());
                eamJigAttachment.setOrgId(user.getOrganizationId());
            }
            eamJigAttachmentMapper.insertList(eamJigAttachmentList);
        }

        //备用件列表
        List<EamSparePartReJigDto> eamJigBackupDtoList = record.getEamJigBackupDtoList();
        if(StringUtils.isNotEmpty(eamJigBackupDtoList)){
            for (EamSparePartReJigDto eamSparePartReJigDto : eamJigBackupDtoList){
                eamSparePartReJigDto.setJigId(record.getJigId());
                eamSparePartReJigDto.setCreateUserId(user.getUserId());
                eamSparePartReJigDto.setCreateTime(new Date());
                eamSparePartReJigDto.setModifiedUserId(user.getUserId());
                eamSparePartReJigDto.setModifiedTime(new Date());
                eamSparePartReJigDto.setStatus(StringUtils.isEmpty(eamSparePartReJigDto.getStatus())?1: eamSparePartReJigDto.getStatus());
                eamSparePartReJigDto.setOrgId(user.getOrganizationId());
            }
            eamSparePartReJigMapper.insertList(eamJigBackupDtoList);
        }

        //履历
        EamHtJig eamHtJig = new EamHtJig();
        BeanUtils.copyProperties(record, eamHtJig);
        int i = eamHtJigMapper.insertSelective(eamHtJig);

        return i;
    }

    /**
     * 判断条码是否重复
     * @param eamJigBarcodeList
     */
    public void barcodeIfRepeat(List<EamJigBarcode> eamJigBarcodeList,SysUser user){
        List<String> jigBarcodes = new ArrayList<>();
        List<String> assetCodes = new ArrayList<>();

        for (EamJigBarcode eamJigBarcode : eamJigBarcodeList) {
            if(StringUtils.isEmpty(eamJigBarcode.getJigBarcode())){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(), "治具条码不能为空");
            }

            if(jigBarcodes.contains(eamJigBarcode.getJigBarcode())){
                throw new BizErrorException(ErrorCodeEnum.OPT20012001.getCode(), "治具条码重复");
            }

            if(assetCodes.contains(eamJigBarcode.getAssetCode())){
                throw new BizErrorException(ErrorCodeEnum.OPT20012001.getCode(), "资产条码重复");
            }

            Example example = new Example(EamJigBarcode.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("jigBarcode", eamJigBarcode.getJigBarcode())
                    .andEqualTo("orgId",user.getOrganizationId());
            if(StringUtils.isNotEmpty(eamJigBarcode.getJigBarcodeId())){
                criteria.andNotEqualTo("jigBarcodeId",eamJigBarcode.getJigBarcodeId());
            }
            EamJigBarcode jigBarcode = eamJigBarcodeMapper.selectOneByExample(example);
            if (StringUtils.isNotEmpty(jigBarcode)) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012001.getCode(), "治具条码重复");
            }
            jigBarcodes.add(eamJigBarcode.getJigBarcode());

            if(StringUtils.isNotEmpty(eamJigBarcode.getAssetCode())) {
                example.clear();
                Example.Criteria criteria1 = example.createCriteria();
                criteria1.andEqualTo("assetCode", eamJigBarcode.getAssetCode())
                        .andEqualTo("orgId", user.getOrganizationId());
                if (StringUtils.isNotEmpty(eamJigBarcode.getJigBarcodeId())) {
                    criteria1.andNotEqualTo("jigBarcodeId", eamJigBarcode.getJigBarcodeId());
                }
                EamJigBarcode jigBarcode1 = eamJigBarcodeMapper.selectOneByExample(example);
                if (StringUtils.isNotEmpty(jigBarcode1)) {
                    throw new BizErrorException(ErrorCodeEnum.OPT20012001.getCode(), "资产条码重复");
                }
                assetCodes.add(eamJigBarcode.getAssetCode());
            }

        }
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int update(EamJig entity) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        //编码是否重复
        this.check(entity,user);

        entity.setModifiedTime(new Date());
        entity.setModifiedUserId(user.getUserId());
        int i = eamJigMapper.updateByPrimaryKeySelective(entity);

        //原来有的条码信息只更新
        List<Long> jigBarcodeIdList = new ArrayList<>();
        List<EamJigBarcode> eamJigBarcodeList = entity.getEamJigBarcodeList();
        if(StringUtils.isNotEmpty(eamJigBarcodeList)) {
            for (EamJigBarcode eamJigBarcode : eamJigBarcodeList) {
                if (StringUtils.isNotEmpty(eamJigBarcode.getJigBarcodeId())) {
                    eamJigBarcode.setCurrentUsageDays(null);
                    eamJigBarcode.setStatus(entity.getStatus());
                    eamJigBarcodeMapper.updateByPrimaryKeySelective(eamJigBarcode);
                    jigBarcodeIdList.add(eamJigBarcode.getJigBarcodeId());
                }
            }
        }

        //删除原条码
        Example example1 = new Example(EamJigBarcode.class);
        Example.Criteria criteria1 = example1.createCriteria();
        criteria1.andEqualTo("jigId", entity.getJigId());
        if (jigBarcodeIdList.size() > 0) {
            criteria1.andNotIn("jigBarcodeId", jigBarcodeIdList);
        }
        eamJigBarcodeMapper.deleteByExample(example1);

        //条码列表
        if(StringUtils.isNotEmpty(eamJigBarcodeList)){
            this.barcodeIfRepeat(eamJigBarcodeList,user);

            for (EamJigBarcode eamJigBarcode : eamJigBarcodeList){
                if(jigBarcodeIdList.contains(eamJigBarcode.getJigBarcodeId())){
                    continue;
                }
                eamJigBarcode.setJigId(entity.getJigId());
                eamJigBarcode.setCreateUserId(user.getUserId());
                eamJigBarcode.setCreateTime(new Date());
                eamJigBarcode.setModifiedUserId(user.getUserId());
                eamJigBarcode.setModifiedTime(new Date());
                eamJigBarcode.setStatus(entity.getStatus());
                eamJigBarcode.setOrgId(user.getOrganizationId());
                eamJigBarcode.setUsageStatus((byte)1);
                eamJigBarcodeMapper.insert(eamJigBarcode);
            }
        }

        //删除原附件
        Example example2 = new Example(EamJigAttachment.class);
        Example.Criteria criteria2 = example2.createCriteria();
        criteria2.andEqualTo("jigId", entity.getJigId());
        eamJigAttachmentMapper.deleteByExample(example2);

        //附件列表
        List<EamJigAttachment> eamJigAttachmentList = entity.getEamJigAttachmentList();
        if(StringUtils.isNotEmpty(eamJigAttachmentList)){
            for (EamJigAttachment eamJigAttachment : eamJigAttachmentList){
                eamJigAttachment.setJigId(entity.getJigId());
                eamJigAttachment.setCreateUserId(user.getUserId());
                eamJigAttachment.setCreateTime(new Date());
                eamJigAttachment.setModifiedUserId(user.getUserId());
                eamJigAttachment.setModifiedTime(new Date());
                eamJigAttachment.setStatus(StringUtils.isEmpty(eamJigAttachment.getStatus())?1: eamJigAttachment.getStatus());
                eamJigAttachment.setOrgId(user.getOrganizationId());
            }
            eamJigAttachmentMapper.insertList(eamJigAttachmentList);
        }

        //删除原备用件
        Example example3 = new Example(EamSparePartReJig.class);
        Example.Criteria criteria3 = example3.createCriteria();
        criteria3.andEqualTo("jigId", entity.getJigId());
        eamSparePartReJigMapper.deleteByExample(example3);

        //备用件列表
        List<EamSparePartReJigDto> eamJigBackupDtoList = entity.getEamJigBackupDtoList();
        if(StringUtils.isNotEmpty(eamJigBackupDtoList)){
            for (EamSparePartReJigDto eamSparePartReJigDto : eamJigBackupDtoList){
                eamSparePartReJigDto.setJigId(entity.getJigId());
                eamSparePartReJigDto.setCreateUserId(user.getUserId());
                eamSparePartReJigDto.setCreateTime(new Date());
                eamSparePartReJigDto.setModifiedUserId(user.getUserId());
                eamSparePartReJigDto.setModifiedTime(new Date());
                eamSparePartReJigDto.setStatus(StringUtils.isEmpty(eamSparePartReJigDto.getStatus())?1: eamSparePartReJigDto.getStatus());
                eamSparePartReJigDto.setOrgId(user.getOrganizationId());
            }
            eamSparePartReJigMapper.insertList(eamJigBackupDtoList);
        }

        EamHtJig eamHtJig = new EamHtJig();
        BeanUtils.copyProperties(entity, eamHtJig);
        eamHtJigMapper.insertSelective(eamHtJig);

        return i;
    }

    public void check(EamJig eamJig,SysUser user){

        Example example = new Example(EamJig.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("jigCode", eamJig.getJigCode())
                .andEqualTo("orgId",user.getOrganizationId());
        if(StringUtils.isNotEmpty(eamJig.getJigId())){
            criteria.andNotEqualTo("jigId",eamJig.getJigId());
        }
        EamJig jig = eamJigMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(jig)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        example.clear();
        Example.Criteria criteria1 = example.createCriteria();
        criteria1.andEqualTo("jigName",eamJig.getJigName())
                .andEqualTo("orgId",user.getOrganizationId());
        if(StringUtils.isNotEmpty(eamJig.getJigId())){
            criteria1.andNotEqualTo("jigId",eamJig.getJigId());
        }
        EamJig jig1 = eamJigMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(jig1)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001.getCode(),"名称重复");
        }
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int batchDelete(String ids) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        List<EamHtJig> list = new ArrayList<>();
        String[] idArry = ids.split(",");
        for (String id : idArry) {
            EamJig eamJig = eamJigMapper.selectByPrimaryKey(id);
            if(StringUtils.isEmpty(eamJig)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }

            EamHtJig eamHtJig = new EamHtJig();
            BeanUtils.copyProperties(eamJig, eamHtJig);
            list.add(eamHtJig);

            //删除条码
            Example example1 = new Example(EamJigBarcode.class);
            Example.Criteria criteria1 = example1.createCriteria();
            criteria1.andEqualTo("jigId", id);
            eamJigBarcodeMapper.deleteByExample(example1);

            //删除附件信息
            Example example2 = new Example(EamJigAttachment.class);
            Example.Criteria criteria2 = example2.createCriteria();
            criteria2.andEqualTo("jigId", id);
            eamJigAttachmentMapper.deleteByExample(example2);

            //删除备用件
            Example example3 = new Example(EamSparePartReJig.class);
            Example.Criteria criteria3 = example3.createCriteria();
            criteria3.andEqualTo("jigId", id);
            eamSparePartReJigMapper.deleteByExample(example3);
        }

        eamHtJigMapper.insertList(list);

        return eamJigMapper.deleteByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importExcel(List<EamJigImport> eamJigImports) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();

        Map<String, Object> resultMap = new HashMap<>();  //封装操作结果
        int success = 0;  //记录操作成功数
        List<Integer> fail = new ArrayList<>();  //记录操作失败行数
        LinkedList<EamJig> list = new LinkedList<>();
        LinkedList<EamHtJig> htList = new LinkedList<>();
        LinkedList<EamJigImport> eamJigImportList = new LinkedList<>();

        for (int i = 0; i < eamJigImports.size(); i++) {
            EamJigImport eamJigImport = eamJigImports.get(i);
            String jigCode = eamJigImport.getJigCode();
            String jigName = eamJigImport.getJigName();
            String jigCategoryCode = eamJigImport.getJigCategoryCode();

            if (StringUtils.isEmpty(
                    jigCode,jigName,jigCategoryCode
            )){
                fail.add(i+4);
                continue;
            }

            //判断编码是否重复
            Example example = new Example(EamJig.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("orgId", currentUser.getOrganizationId());
            criteria.andEqualTo("jigCode",jigCode);
            if (StringUtils.isNotEmpty(eamJigMapper.selectOneByExample(example))){
                fail.add(i+4);
                continue;
            }

            //判断集合中是否存在该条数据
           /* boolean tag = false;
            if (StringUtils.isNotEmpty(eamJigImportList)){
                for (EamJigImport jigImport: eamJigImportList) {
                    if (jigCode.equals(jigImport.getJigCode())){
                        tag = true;
                        break;
                    }
                }
            }
            if (tag){
                fail.add(i+4);
                continue;
            }*/

            //治具类别信息
            Example example1 = new Example(EamJigCategory.class);
            Example.Criteria criteria1 = example1.createCriteria();
            criteria1.andEqualTo("orgId", currentUser.getOrganizationId())
                     .andEqualTo("jigCategoryCode",jigCategoryCode);
            EamJigCategory eamJigCategory = eamJigCategoryMapper.selectOneByExample(example1);
            if (StringUtils.isEmpty(eamJigCategory)){
                fail.add(i+4);
                continue;
            }
            eamJigImport.setJigCategoryId(eamJigCategory.getJigCategoryId());

            //治具管理员
            String userCode = eamJigImport.getUserCode();
            if(StringUtils.isNotEmpty(userCode)){
                SearchSysUser searchSysUser = new SearchSysUser();
                searchSysUser.setUserCode(userCode);
                List<SysUser> sysUsers = securityFeignApi.selectUsers(searchSysUser).getData();
                if (StringUtils.isEmpty(sysUsers)){
                    fail.add(i+4);
                    continue;
                }
                eamJigImport.setUserId(sysUsers.get(0).getUserId());
            }

            //仓库
            String warehouseCode = eamJigImport.getWarehouseCode();
            if(StringUtils.isNotEmpty(warehouseCode)){
                SearchBaseWarehouse searchBaseWarehouse = new SearchBaseWarehouse();
                searchBaseWarehouse.setWarehouseCode(warehouseCode);
                searchBaseWarehouse.setOrgId(currentUser.getOrganizationId());
                List<BaseWarehouse> baseWarehouses = baseFeignApi.findList(searchBaseWarehouse).getData();
                if (StringUtils.isEmpty(baseWarehouses)){
                    fail.add(i+4);
                    continue;
                }
                eamJigImport.setWarehouseId(baseWarehouses.get(0).getWarehouseId());
            }

            //库区
            String warehouseAreaCode = eamJigImport.getWarehouseAreaCode();
            if(StringUtils.isNotEmpty(warehouseAreaCode)){
                SearchBaseWarehouseArea searchBaseWarehouseArea = new SearchBaseWarehouseArea();
                searchBaseWarehouseArea.setWarehouseAreaCode(warehouseAreaCode);
                searchBaseWarehouseArea.setOrgId(currentUser.getOrganizationId());
                List<BaseWarehouseAreaDto> baseWarehouseAreaDtos = baseFeignApi.findWarehouseAreaList(searchBaseWarehouseArea).getData();
                if (StringUtils.isEmpty(baseWarehouseAreaDtos)){
                    fail.add(i+4);
                    continue;
                }
                eamJigImport.setWarehouseAreaId(baseWarehouseAreaDtos.get(0).getWarehouseAreaId());
            }

            //工作区
            String workingAreaCode = eamJigImport.getWorkingAreaCode();
            if(StringUtils.isNotEmpty(workingAreaCode)){
                SearchBaseWorkingArea searchBaseWorkingArea = new SearchBaseWorkingArea();
                searchBaseWorkingArea.setWorkingAreaCode(workingAreaCode);
                searchBaseWorkingArea.setOrgId(currentUser.getOrganizationId());
                List<BaseWorkingAreaDto> baseWorkingAreaDtos = baseFeignApi.findWorkingAreaList(searchBaseWorkingArea).getData();
                if (StringUtils.isEmpty(baseWorkingAreaDtos)){
                    fail.add(i+4);
                    continue;
                }
                eamJigImport.setWorkingAreaId(baseWorkingAreaDtos.get(0).getWorkingAreaId());
            }

            //库区
            String storageCode = eamJigImport.getStorageCode();
            if(StringUtils.isNotEmpty(storageCode)){
                SearchBaseStorage searchBaseStorage = new SearchBaseStorage();
                searchBaseStorage.setStorageCode(storageCode);
                searchBaseStorage.setOrgId(currentUser.getOrganizationId());
                List<BaseStorage> baseStorages = baseFeignApi.findList(searchBaseStorage).getData();
                if (StringUtils.isEmpty(baseStorages)){
                    fail.add(i+4);
                    continue;
                }
                eamJigImport.setStorageId(baseStorages.get(0).getStorageId());
            }

            //配置项：最大使用次数和最大使用天数是否都可填
            SearchSysSpecItem searchSysSpecItem = new SearchSysSpecItem();
            searchSysSpecItem.setSpecCode("IfBoth");
            List<SysSpecItem> sysSpecItemList = securityFeignApi.findSpecItemList(searchSysSpecItem).getData();
            if(Integer.parseInt(sysSpecItemList.get(0).getParaValue()) == 0){
                if(StringUtils.isNotEmpty(eamJigImport.getMaxUsageTime(),eamJigImport.getWarningTime())){
                    eamJigImport.setMaxUsageDays(0);
                    eamJigImport.setWarningDays(0);
                }
            }

            //根据长宽高计算体积
            if(StringUtils.isNotEmpty(eamJigImport.getLength(),eamJigImport.getWidth(),eamJigImport.getHeight())){
                BigDecimal volume = eamJigImport.getLength().multiply(eamJigImport.getWidth()).multiply(eamJigImport.getHeight());
                eamJigImport.setVolume(volume);
            }

            //备用件
            String sparePartCode = eamJigImport.getSparePartCode();
            if(StringUtils.isNotEmpty(sparePartCode)){
                Example example2 = new Example(EamSparePart.class);
                Example.Criteria criteria2 = example2.createCriteria();
                criteria2.andEqualTo("orgId",currentUser.getOrganizationId())
                        .andEqualTo("sparePartCode",sparePartCode);
                EamSparePart eamSparePart = eamSparePartMapper.selectOneByExample(example2);
                if (StringUtils.isEmpty(eamSparePart)){
                    fail.add(i+4);
                    continue;
                }
                eamJigImport.setSparePartId(eamSparePart.getSparePartId());
            }

            eamJigImportList.add(eamJigImport);
        }

        if (StringUtils.isNotEmpty(eamJigImportList)){
            HashMap<String, List<EamJigImport>> map = eamJigImportList.stream().collect(Collectors.groupingBy(EamJigImport::getJigCode, HashMap::new, Collectors.toList()));
            Set<String> codeList = map.keySet();
            for (String code : codeList) {
                //主表
                List<EamJigImport> eamJigImports1 = map.get(code);
                EamJig eamJig = new EamJig();
                BeanUtils.copyProperties(eamJigImports1.get(0),eamJig);
                eamJig.setCreateTime(new Date());
                eamJig.setCreateUserId(currentUser.getUserId());
                eamJig.setModifiedTime(new Date());
                eamJig.setModifiedUserId(currentUser.getUserId());
                eamJig.setOrgId(currentUser.getOrganizationId());
                eamJig.setStatus((byte)1);
                list.add(eamJig);
                success += eamJigMapper.insertUseGeneratedKeys(eamJig);

                //明细
                LinkedList<EamJigBarcode> barcodeList = new LinkedList<>();
                for (EamJigImport eamJigImport : eamJigImports1) {
                    if (StringUtils.isEmpty(eamJigImport.getAssetCode(),eamJigImport.getJigBarcode())) {
                        continue;
                    }
                    EamJigBarcode eamJigBarcode = new EamJigBarcode();
                    BeanUtils.copyProperties(eamJigImport, eamJigBarcode);
                    eamJigBarcode.setCurrentUsageDays(StringUtils.isEmpty(eamJigBarcode.getCurrentUsageDays())?0:eamJigBarcode.getCurrentUsageDays());
                    eamJigBarcode.setCurrentUsageTime(StringUtils.isEmpty(eamJigBarcode.getCurrentUsageTime())?0:eamJigBarcode.getCurrentUsageTime());
                    eamJigBarcode.setJigId(eamJig.getJigId());
                    eamJigBarcode.setStatus((byte) 1);
                    eamJigBarcode.setUsageStatus((byte)1);
                    eamJigBarcode.setCreateUserId(currentUser.getUserId());
                    eamJigBarcode.setCreateTime(new Date());
                    eamJigBarcode.setModifiedUserId(currentUser.getUserId());
                    eamJigBarcode.setModifiedTime(new Date());
                    barcodeList.add(eamJigBarcode);
                }
                if(StringUtils.isNotEmpty(barcodeList)) {
                    eamJigBarcodeMapper.insertList(barcodeList);
                }

                LinkedList<EamSparePartReJig> sparePartReJigs = new LinkedList<>();
                for (EamJigImport eamJigImport : eamJigImports1) {
                    if (StringUtils.isEmpty(eamJigImport.getSparePartCode())) {
                        continue;
                    }
                    EamSparePartReJig eamSparePartReJig = new EamSparePartReJig();
                    BeanUtils.copyProperties(eamJigImport, eamSparePartReJig);
                    eamSparePartReJig.setJigId(eamJig.getJigId());
                    eamSparePartReJig.setStatus((byte) 1);
                    eamSparePartReJig.setOrgId(currentUser.getOrganizationId());
                    sparePartReJigs.add(eamSparePartReJig);
                }
                if(StringUtils.isNotEmpty(sparePartReJigs)) {
                    eamSparePartReJigMapper.insertList(sparePartReJigs);
                }
            }

            /*for (EamJigImport eamJigImport : eamJigImportList) {
                EamJig eamJig = new EamJig();
                BeanUtils.copyProperties(eamJigImport,eamJig);
                eamJig.setCreateTime(new Date());
                eamJig.setCreateUserId(currentUser.getUserId());
                eamJig.setModifiedTime(new Date());
                eamJig.setModifiedUserId(currentUser.getUserId());
                eamJig.setOrgId(currentUser.getOrganizationId());
                eamJig.setStatus((byte)1);
                list.add(eamJig);
            }
            success = eamJigMapper.insertList(list);*/

            if(StringUtils.isNotEmpty(list)){
                for (EamJig eamJig : list) {
                    EamHtJig eamHtJig = new EamHtJig();
                    BeanUtils.copyProperties(eamJig, eamHtJig);
                    htList.add(eamHtJig);
                }
                eamHtJigMapper.insertList(htList);
            }
        }

        resultMap.put("操作成功总数",success);
        resultMap.put("操作失败行数",fail);
        return resultMap;
    }
}
