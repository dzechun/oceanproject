package com.fantechs.provider.qms.service.impl;

import com.fantechs.common.base.entity.security.SysImportAndExportLog;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.qms.QmsIncomingInspectionOrderDto;
import com.fantechs.common.base.general.dto.qms.imports.QmsIncomingInspectionOrderImport;
import com.fantechs.common.base.general.entity.basic.*;
import com.fantechs.common.base.general.entity.basic.search.*;
import com.fantechs.common.base.general.entity.qms.QmsIncomingInspectionOrder;
import com.fantechs.common.base.general.entity.qms.QmsIncomingInspectionOrderDet;
import com.fantechs.common.base.general.entity.qms.QmsIncomingInspectionOrderDetSample;
import com.fantechs.common.base.general.entity.qms.history.QmsHtIncomingInspectionOrder;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CodeUtils;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.api.security.service.SecurityFeignApi;
import com.fantechs.provider.qms.mapper.QmsHtIncomingInspectionOrderMapper;
import com.fantechs.provider.qms.mapper.QmsIncomingInspectionOrderDetMapper;
import com.fantechs.provider.qms.mapper.QmsIncomingInspectionOrderDetSampleMapper;
import com.fantechs.provider.qms.mapper.QmsIncomingInspectionOrderMapper;
import com.fantechs.provider.qms.service.QmsIncomingInspectionOrderService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 *
 * Created by leifengzhi on 2021/12/06.
 */
@Service
public class QmsIncomingInspectionOrderServiceImpl extends BaseService<QmsIncomingInspectionOrder> implements QmsIncomingInspectionOrderService {

    @Resource
    private QmsIncomingInspectionOrderMapper qmsIncomingInspectionOrderMapper;

    @Resource
    private QmsIncomingInspectionOrderDetMapper qmsIncomingInspectionOrderDetMapper;

    @Resource
    private QmsIncomingInspectionOrderDetSampleMapper qmsIncomingInspectionOrderDetSampleMapper;

    @Resource
    private QmsHtIncomingInspectionOrderMapper qmsHtIncomingInspectionOrderMapper;

    @Resource
    private BaseFeignApi baseFeignApi;

    @Resource
    private SecurityFeignApi securityFeignApi;

    @Override
    public List<QmsHtIncomingInspectionOrder> findHtList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId",user.getOrganizationId());
        return qmsHtIncomingInspectionOrderMapper.findHtList(map);
    }

    @Override
    public List<QmsIncomingInspectionOrderDto> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId",user.getOrganizationId());
        return qmsIncomingInspectionOrderMapper.findList(map);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int pushDown(String ids) {
        //根据单据流生成入库计划单或上架作业单
        int i = 0;
        List<QmsIncomingInspectionOrder> qmsIncomingInspectionOrders = qmsIncomingInspectionOrderMapper.selectByIds(ids);
        //查当前单据的下游单据
        String sysOrderTypeCode = qmsIncomingInspectionOrders.get(0).getSysOrderTypeCode();

        if("".equals("")){
            //生成入库计划单

        }else if("".equals("")){
            //生成上架作业单

        }

        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int MRBReview(Long incomingInspectionOrderId, Byte mrbResult) {
        QmsIncomingInspectionOrder qmsIncomingInspectionOrder = qmsIncomingInspectionOrderMapper.selectByPrimaryKey(incomingInspectionOrderId);
        if(qmsIncomingInspectionOrder.getInspectionResult()==1){
            throw new BizErrorException("该检验单检验结果为合格，无法进行MRB评审");
        }
        qmsIncomingInspectionOrder.setMrbResult(mrbResult);
        return qmsIncomingInspectionOrderMapper.updateByPrimaryKeySelective(qmsIncomingInspectionOrder);
    }

    @Override
    public QmsIncomingInspectionOrder selectByKey(Long incomingInspectionOrderId) {
        Map<String, Object> map = new HashMap<>();
        map.put("incomingInspectionOrderId",incomingInspectionOrderId);
        List<QmsIncomingInspectionOrderDto> list = findList(map);
        return list.get(0);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(QmsIncomingInspectionOrderDto record) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        if(StringUtils.isNotEmpty(record.getAccessUrl())){
            BaseFile baseFile = new BaseFile();
            baseFile.setAccessUrl(record.getAccessUrl());
            ResponseEntity<BaseFile> responseEntity = baseFeignApi.add(baseFile);
            if (responseEntity.getCode() == 0) {
                BaseFile data = responseEntity.getData();
                record.setFileId(data.getFileId());
            }
        }

        //新增来料检验单
        record.setIncomingInspectionOrderCode(CodeUtils.getId("LLJY-"));
        record.setInspectionStatus(StringUtils.isEmpty(record.getInspectionStatus())?1:record.getInspectionStatus());
        record.setCreateUserId(user.getUserId());
        record.setCreateTime(new Date());
        record.setModifiedUserId(user.getUserId());
        record.setModifiedTime(new Date());
        record.setStatus(StringUtils.isEmpty(record.getStatus())?1:record.getStatus());
        record.setOrgId(user.getOrganizationId());
        int i = qmsIncomingInspectionOrderMapper.insertUseGeneratedKeys(record);

        //新增来料检验单明细
        List<QmsIncomingInspectionOrderDet> list = record.getList();
        if(StringUtils.isNotEmpty(list)){
            for (QmsIncomingInspectionOrderDet qmsIncomingInspectionOrderDet:list){
                qmsIncomingInspectionOrderDet.setIncomingInspectionOrderId(record.getIncomingInspectionOrderId());
                qmsIncomingInspectionOrderDet.setCreateUserId(user.getUserId());
                qmsIncomingInspectionOrderDet.setCreateTime(new Date());
                qmsIncomingInspectionOrderDet.setModifiedUserId(user.getUserId());
                qmsIncomingInspectionOrderDet.setModifiedTime(new Date());
                qmsIncomingInspectionOrderDet.setStatus(StringUtils.isEmpty(qmsIncomingInspectionOrderDet.getStatus())?1:qmsIncomingInspectionOrderDet.getStatus());
                qmsIncomingInspectionOrderDet.setOrgId(user.getOrganizationId());
            }
            qmsIncomingInspectionOrderDetMapper.insertList(list);
        }

        //履历
        QmsHtIncomingInspectionOrder qmsHtIncomingInspectionOrder = new QmsHtIncomingInspectionOrder();
        BeanUtils.copyProperties(record, qmsHtIncomingInspectionOrder);
        qmsHtIncomingInspectionOrderMapper.insertSelective(qmsHtIncomingInspectionOrder);

        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(QmsIncomingInspectionOrderDto entity) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        if(StringUtils.isNotEmpty(entity.getAccessUrl())){
            BaseFile baseFile = new BaseFile();
            baseFile.setAccessUrl(entity.getAccessUrl());
            ResponseEntity<BaseFile> responseEntity = baseFeignApi.add(baseFile);
            if (responseEntity.getCode() == 0) {
                BaseFile data = responseEntity.getData();
                entity.setFileId(data.getFileId());
            }
        }

        entity.setModifiedUserId(user.getUserId());
        entity.setModifiedTime(new Date());
        int i = qmsIncomingInspectionOrderMapper.updateByPrimaryKeySelective(entity);

        //删除原明细
        Example example = new Example(QmsIncomingInspectionOrderDet.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("incomingInspectionOrderId",entity.getIncomingInspectionOrderId());
        qmsIncomingInspectionOrderDetMapper.deleteByExample(example);

        //新增来料检验单明细
        List<QmsIncomingInspectionOrderDet> list = entity.getList();
        if(StringUtils.isNotEmpty(list)){
            for (QmsIncomingInspectionOrderDet qmsIncomingInspectionOrderDet:list){
                qmsIncomingInspectionOrderDet.setIncomingInspectionOrderId(entity.getIncomingInspectionOrderId());
                qmsIncomingInspectionOrderDet.setCreateUserId(user.getUserId());
                qmsIncomingInspectionOrderDet.setCreateTime(new Date());
                qmsIncomingInspectionOrderDet.setModifiedUserId(user.getUserId());
                qmsIncomingInspectionOrderDet.setModifiedTime(new Date());
                qmsIncomingInspectionOrderDet.setStatus(StringUtils.isEmpty(qmsIncomingInspectionOrderDet.getStatus())?1:qmsIncomingInspectionOrderDet.getStatus());
                qmsIncomingInspectionOrderDet.setOrgId(user.getOrganizationId());
            }
            qmsIncomingInspectionOrderDetMapper.insertList(list);
        }

        //返写单据信息
        checkInspectionResult(list);

        //履历
        QmsHtIncomingInspectionOrder qmsHtIncomingInspectionOrder = new QmsHtIncomingInspectionOrder();
        BeanUtils.copyProperties(entity, qmsHtIncomingInspectionOrder);
        qmsHtIncomingInspectionOrderMapper.insertSelective(qmsHtIncomingInspectionOrder);

        return i;
    }

    public void checkInspectionResult(List<QmsIncomingInspectionOrderDet> list){
        boolean tag = true;
        Byte inspectionResult = 1;
        for (QmsIncomingInspectionOrderDet qmsIncomingInspectionOrderDet : list){
            if(qmsIncomingInspectionOrderDet.getIfMustInspection()==1&&
                    StringUtils.isEmpty(qmsIncomingInspectionOrderDet.getInspectionResult())){
                tag = false;
                break;
            }
            if(qmsIncomingInspectionOrderDet.getInspectionResult()!=null&&
                    qmsIncomingInspectionOrderDet.getInspectionResult() == (byte)0){
                inspectionResult = 0;
            }
        }

        if(tag){
            //返写检验单结果
            QmsIncomingInspectionOrder qmsIncomingInspectionOrder = qmsIncomingInspectionOrderMapper.selectByPrimaryKey(list.get(0).getIncomingInspectionOrderId());
            qmsIncomingInspectionOrder.setInspectionResult(inspectionResult);
            qmsIncomingInspectionOrder.setInspectionStatus((byte)3);
            qmsIncomingInspectionOrderMapper.updateByPrimaryKeySelective(qmsIncomingInspectionOrder);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDelete(String ids) {
        List<QmsIncomingInspectionOrder> qmsIncomingInspectionOrders = qmsIncomingInspectionOrderMapper.selectByIds(ids);
        List<QmsIncomingInspectionOrderDet> detList = new LinkedList<>();
        for (QmsIncomingInspectionOrder qmsIncomingInspectionOrder : qmsIncomingInspectionOrders){
            Example example = new Example(QmsIncomingInspectionOrderDet.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("incomingInspectionOrderId",qmsIncomingInspectionOrder.getIncomingInspectionOrderId());
            List<QmsIncomingInspectionOrderDet> inspectionOrderDets = qmsIncomingInspectionOrderDetMapper.selectByExample(example);
            detList.addAll(inspectionOrderDets);
            qmsIncomingInspectionOrderDetMapper.deleteByExample(example);
        }

        for (QmsIncomingInspectionOrderDet qmsIncomingInspectionOrderDet : detList){
            Example example1 = new Example(QmsIncomingInspectionOrderDetSample.class);
            Example.Criteria criteria1 = example1.createCriteria();
            criteria1.andEqualTo("incomingInspectionOrderDetId",qmsIncomingInspectionOrderDet.getIncomingInspectionOrderDetId());
            qmsIncomingInspectionOrderDetSampleMapper.deleteByExample(example1);
        }

        return qmsIncomingInspectionOrderMapper.deleteByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importExcel(List<QmsIncomingInspectionOrderImport> qmsIncomingInspectionOrderImports) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        Map<String, Object> resultMap = new HashMap<>();  //封装操作结果
        int success = 0;  //记录操作成功数
        List<Integer> fail = new ArrayList<>();  //记录操作失败行数
        LinkedList<QmsIncomingInspectionOrder> list = new LinkedList<>();
        LinkedList<QmsHtIncomingInspectionOrder> htList = new LinkedList<>();
        LinkedList<QmsIncomingInspectionOrderImport> incomingInspectionOrderImports = new LinkedList<>();
        //日志记录
        StringBuilder succeedInfo = new StringBuilder();
        StringBuilder failInfo = new StringBuilder();
        Integer succeedCount = 0;
        Integer failCount = 0;
        SysImportAndExportLog sysImportAndExportLog = new SysImportAndExportLog();
        sysImportAndExportLog.setModuleNames("QMS");
        sysImportAndExportLog.setFileName("来料检验单导入信息表");
        sysImportAndExportLog.setType((byte)1);
        sysImportAndExportLog.setOperatorUserId(user.getUserId());
        sysImportAndExportLog.setResult((byte)1);
        sysImportAndExportLog.setTotalCount(qmsIncomingInspectionOrderImports.size());

        for (int i = 0; i < qmsIncomingInspectionOrderImports.size(); i++) {
            QmsIncomingInspectionOrderImport qmsIncomingInspectionOrderImport = qmsIncomingInspectionOrderImports.get(i);
            String incomingInspectionOrderCode = qmsIncomingInspectionOrderImport.getIncomingInspectionOrderCode();
            String materialCode = qmsIncomingInspectionOrderImport.getMaterialCode();

            if (StringUtils.isEmpty(
                    incomingInspectionOrderCode,materialCode
            )){
                failCount++;
                failInfo.append("必填项为空").append(",");
                fail.add(i+4);
                continue;
            }

            //判断单号是否重复
            Example example = new Example(QmsIncomingInspectionOrder.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("orgId", user.getOrganizationId())
                    .andEqualTo("incomingInspectionOrderCode", incomingInspectionOrderCode);
            if (StringUtils.isNotEmpty(qmsIncomingInspectionOrderMapper.selectOneByExample(example))){
                failCount++;
                failInfo.append("单号已存在").append(",");
                fail.add(i+4);
                continue;
            }

            //物料编码
            SearchBaseMaterial searchBaseMaterial = new SearchBaseMaterial();
            searchBaseMaterial.setMaterialCode(materialCode);
            searchBaseMaterial.setCodeQueryMark(1);
            List<BaseMaterial> baseMaterials = baseFeignApi.findList(searchBaseMaterial).getData();
            if (StringUtils.isEmpty(baseMaterials)) {
                failCount++;
                failInfo.append("物料不存在").append(",");
                fail.add(i + 4);
                continue;
            }
            qmsIncomingInspectionOrderImport.setMaterialId(baseMaterials.get(0).getMaterialId());


            //供应商编码
            String supplierCode = qmsIncomingInspectionOrderImport.getSupplierCode();
            if(StringUtils.isNotEmpty(supplierCode)){
                //供应商是否存在
                SearchBaseSupplier searchBaseSupplier = new SearchBaseSupplier();
                searchBaseSupplier.setCodeQueryMark((byte)1);
                searchBaseSupplier.setSupplierCode(supplierCode);
                List<BaseSupplier> baseSuppliers = baseFeignApi.findSupplierList(searchBaseSupplier).getData();
                if (StringUtils.isEmpty(baseSuppliers)) {
                    failCount++;
                    failInfo.append("供应商不存在").append(",");
                    fail.add(i + 4);
                    continue;
                }

                //供应商的物料是否存在于免检清单
                SearchBaseInspectionExemptedList searchBaseInspectionExemptedList = new SearchBaseInspectionExemptedList();
                searchBaseInspectionExemptedList.setObjType((byte)1);
                searchBaseInspectionExemptedList.setMaterialId(qmsIncomingInspectionOrderImport.getMaterialId());
                searchBaseInspectionExemptedList.setSupplierId(baseSuppliers.get(0).getSupplierId());
                List<BaseInspectionExemptedList> inspectionExemptedLists = baseFeignApi.findList(searchBaseInspectionExemptedList).getData();
                if(StringUtils.isNotEmpty(inspectionExemptedLists)){
                    failCount++;
                    failInfo.append("该供应商该物料属于免检清单").append(",");
                    fail.add(i+4);
                    continue;
                }
                qmsIncomingInspectionOrderImport.setSupplierId(baseSuppliers.get(0).getSupplierId());
            }

            //仓库编码
            String warehouseCode = qmsIncomingInspectionOrderImport.getWarehouseCode();
            if(StringUtils.isNotEmpty(warehouseCode)){
                //仓库是否存在
                SearchBaseWarehouse searchBaseWarehouse = new SearchBaseWarehouse();
                searchBaseWarehouse.setCodeQueryMark(1);
                searchBaseWarehouse.setWarehouseCode(supplierCode);
                List<BaseWarehouse> baseWarehouses = baseFeignApi.findList(searchBaseWarehouse).getData();
                if (StringUtils.isEmpty(baseWarehouses)) {
                    failCount++;
                    failInfo.append("仓库不存在").append(",");
                    fail.add(i + 4);
                    continue;
                }
                qmsIncomingInspectionOrderImport.setWarehouseId(baseWarehouses.get(0).getWarehouseId());
            }

            //检验方式
            String inspectionWayCode = qmsIncomingInspectionOrderImport.getInspectionWayCode();
            if(StringUtils.isNotEmpty(inspectionWayCode)){
                SearchBaseInspectionWay searchBaseInspectionWay = new SearchBaseInspectionWay();
                searchBaseInspectionWay.setInspectionWayCode(inspectionWayCode);
                searchBaseInspectionWay.setQueryMark(1);
                searchBaseInspectionWay.setInspectionType((byte)1);
                List<BaseInspectionWay> inspectionWays = baseFeignApi.findList(searchBaseInspectionWay).getData();
                if (StringUtils.isEmpty(inspectionWays)){
                    failCount++;
                    failInfo.append("检验方式不存在").append(",");
                    fail.add(i+4);
                    continue;
                }
                qmsIncomingInspectionOrderImport.setInspectionWayId(inspectionWays.get(0).getInspectionWayId());
            }

            //检验标准
            String inspectionStandardCode = qmsIncomingInspectionOrderImport.getInspectionStandardCode();
            if(StringUtils.isNotEmpty(inspectionStandardCode)){
                SearchBaseInspectionStandard searchBaseInspectionStandard = new SearchBaseInspectionStandard();
                searchBaseInspectionStandard.setInspectionStandardCode(inspectionStandardCode);
                searchBaseInspectionStandard.setMaterialId(qmsIncomingInspectionOrderImport.getMaterialId());
                searchBaseInspectionStandard.setSupplierId(qmsIncomingInspectionOrderImport.getSupplierId());
                searchBaseInspectionStandard.setInspectionWayId(qmsIncomingInspectionOrderImport.getInspectionWayId());
                List<BaseInspectionStandard> inspectionStandards = baseFeignApi.findList(searchBaseInspectionStandard).getData();
                if (StringUtils.isEmpty(inspectionStandards)){
                    failCount++;
                    failInfo.append("检验标准不存在").append(",");
                    fail.add(i+4);
                    continue;
                }
                qmsIncomingInspectionOrderImport.setInspectionStandardId(inspectionStandards.get(0).getInspectionStandardId());
            }

            succeedCount++;
            succeedInfo.append(i+4+"").append(",");
            incomingInspectionOrderImports.add(qmsIncomingInspectionOrderImport);
        }
        sysImportAndExportLog.setFailCount(failCount);
        sysImportAndExportLog.setSucceedCount(succeedCount);
        sysImportAndExportLog.setFailInfo(failInfo.toString());
        sysImportAndExportLog.setSucceedInfo(succeedInfo.toString());
        securityFeignApi.add(sysImportAndExportLog);

        if(StringUtils.isNotEmpty(incomingInspectionOrderImports)){
            //对合格数据进行分组
            HashMap<String, List<QmsIncomingInspectionOrderImport>> map = incomingInspectionOrderImports.stream().collect(Collectors.groupingBy(QmsIncomingInspectionOrderImport::getIncomingInspectionOrderCode, HashMap::new, Collectors.toList()));
            Set<String> codeList = map.keySet();
            for (String code : codeList) {
                List<QmsIncomingInspectionOrderImport> qmsIncomingInspectionOrderImports1 = map.get(code);
                QmsIncomingInspectionOrder qmsIncomingInspectionOrder = new QmsIncomingInspectionOrder();
                //新增父级数据
                BeanUtils.copyProperties(qmsIncomingInspectionOrderImports1.get(0), qmsIncomingInspectionOrder);
                qmsIncomingInspectionOrder.setInspectionStatus((byte)1);
                qmsIncomingInspectionOrder.setCreateTime(new Date());
                qmsIncomingInspectionOrder.setCreateUserId(user.getUserId());
                qmsIncomingInspectionOrder.setModifiedUserId(user.getUserId());
                qmsIncomingInspectionOrder.setModifiedTime(new Date());
                qmsIncomingInspectionOrder.setOrgId(user.getOrganizationId());
                qmsIncomingInspectionOrder.setStatus((byte)1);
                success += qmsIncomingInspectionOrderMapper.insertUseGeneratedKeys(qmsIncomingInspectionOrder);

                //履历
                QmsHtIncomingInspectionOrder qmsHtIncomingInspectionOrder = new QmsHtIncomingInspectionOrder();
                BeanUtils.copyProperties(qmsIncomingInspectionOrder, qmsHtIncomingInspectionOrder);
                htList.add(qmsHtIncomingInspectionOrder);

                //新增明细数据
                LinkedList<QmsIncomingInspectionOrderDet> detList = new LinkedList<>();
                    for (QmsIncomingInspectionOrderImport qmsIncomingInspectionOrderImport : qmsIncomingInspectionOrderImports1) {
                        QmsIncomingInspectionOrderDet qmsIncomingInspectionOrderDet = new QmsIncomingInspectionOrderDet();
                        BeanUtils.copyProperties(qmsIncomingInspectionOrderImport, qmsIncomingInspectionOrderDet);
                        qmsIncomingInspectionOrderDet.setIncomingInspectionOrderId(qmsIncomingInspectionOrder.getIncomingInspectionOrderId());
                        qmsIncomingInspectionOrderDet.setStatus((byte) 1);
                        qmsIncomingInspectionOrderDet.setIfMustInspection(StringUtils.isEmpty(qmsIncomingInspectionOrderImport.getIfMustInspection()) ? null : qmsIncomingInspectionOrderImport.getIfMustInspection().byteValue());
                        qmsIncomingInspectionOrderDet.setInspectionTag(StringUtils.isEmpty(qmsIncomingInspectionOrderImport.getInspectionTag()) ? null : qmsIncomingInspectionOrderImport.getInspectionTag().byteValue());
                        detList.add(qmsIncomingInspectionOrderDet);
                }
                qmsIncomingInspectionOrderDetMapper.insertList(detList);
            }
            qmsHtIncomingInspectionOrderMapper.insertList(htList);
        }

        resultMap.put("操作成功总数",success);
        resultMap.put("操作失败行数",fail);
        return resultMap;
    }
}
