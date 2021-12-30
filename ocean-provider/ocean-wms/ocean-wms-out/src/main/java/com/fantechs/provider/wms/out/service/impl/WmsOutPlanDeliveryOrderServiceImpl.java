package com.fantechs.provider.wms.out.service.impl;

import cn.hutool.core.date.DateTime;
import com.fantechs.common.base.entity.security.SysImportAndExportLog;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.wms.out.WmsOutPlanDeliveryOrderDetDto;
import com.fantechs.common.base.general.dto.wms.out.WmsOutPlanDeliveryOrderDto;
import com.fantechs.common.base.general.dto.wms.out.imports.WmsOutPlanDeliveryOrderImport;
import com.fantechs.common.base.general.entity.basic.BaseMaterial;
import com.fantechs.common.base.general.entity.basic.BaseWarehouse;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseMaterial;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseWarehouse;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerJobOrder;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerJobOrderDet;
import com.fantechs.common.base.general.entity.wms.out.WmsOutPlanDeliveryOrder;
import com.fantechs.common.base.general.entity.wms.out.WmsOutPlanDeliveryOrderDet;
import com.fantechs.common.base.general.entity.wms.out.history.WmsOutHtPlanDeliveryOrder;
import com.fantechs.common.base.general.entity.wms.out.history.WmsOutHtPlanDeliveryOrderDet;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CodeUtils;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.api.security.service.SecurityFeignApi;
import com.fantechs.provider.api.wms.inner.InnerFeignApi;
import com.fantechs.provider.wms.out.mapper.WmsOutHtPlanDeliveryOrderDetMapper;
import com.fantechs.provider.wms.out.mapper.WmsOutHtPlanDeliveryOrderMapper;
import com.fantechs.provider.wms.out.mapper.WmsOutPlanDeliveryOrderDetMapper;
import com.fantechs.provider.wms.out.mapper.WmsOutPlanDeliveryOrderMapper;
import com.fantechs.provider.wms.out.service.WmsOutPlanDeliveryOrderService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 *
 * Created by leifengzhi on 2021/12/22.
 */
@Service
public class WmsOutPlanDeliveryOrderServiceImpl extends BaseService<WmsOutPlanDeliveryOrder> implements WmsOutPlanDeliveryOrderService {

    @Resource
    private WmsOutPlanDeliveryOrderMapper wmsOutPlanDeliveryOrderMapper;
    @Resource
    private WmsOutPlanDeliveryOrderDetMapper wmsOutPlanDeliveryOrderDetMapper;
    @Resource
    private WmsOutHtPlanDeliveryOrderMapper wmsOutHtPlanDeliveryOrderMapper;
    @Resource
    private WmsOutHtPlanDeliveryOrderDetMapper wmsOutHtPlanDeliveryOrderDetMapper;
    @Resource
    private BaseFeignApi baseFeignApi;
    @Resource
    private SecurityFeignApi securityFeignApi;
    @Resource
    private InnerFeignApi innerFeignApi;

    @Override
    public List<WmsOutPlanDeliveryOrderDto> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId",user.getOrganizationId());
        return wmsOutPlanDeliveryOrderMapper.findList(map);
    }

    @Override
    public List<WmsOutHtPlanDeliveryOrder> findHtList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId",user.getOrganizationId());
        return wmsOutHtPlanDeliveryOrderMapper.findHtList(map);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int pushDown(List<WmsOutPlanDeliveryOrderDetDto> wmsOutPlanDeliveryOrderDetDtos) {
        int i;
        Long warehouseId = wmsOutPlanDeliveryOrderDetDtos.get(0).getWarehouseId();
        for (WmsOutPlanDeliveryOrderDetDto wmsOutPlanDeliveryOrderDetDto : wmsOutPlanDeliveryOrderDetDtos){
            if(!warehouseId.equals(wmsOutPlanDeliveryOrderDetDto.getWarehouseId())){
                throw new BizErrorException("所选数据的仓库需一致");
            }
            if(wmsOutPlanDeliveryOrderDetDto.getIfAllIssued()!=null&&wmsOutPlanDeliveryOrderDetDto.getIfAllIssued()==(byte)1){
                throw new BizErrorException("物料"+wmsOutPlanDeliveryOrderDetDto.getMaterialCode()+"已下推，不能重复下推");
            }
            wmsOutPlanDeliveryOrderDetDto.setTotalIssueQty(wmsOutPlanDeliveryOrderDetDto.getOrderQty());
            wmsOutPlanDeliveryOrderDetDto.setIfAllIssued((byte)1);
        }
        i = wmsOutPlanDeliveryOrderDetMapper.batchUpdate(wmsOutPlanDeliveryOrderDetDtos);

        //拣货作业
        int lineNumber = 1;
        List<WmsInnerJobOrderDet> wmsInnerJobOrderDets = new LinkedList<>();
        for (WmsOutPlanDeliveryOrderDetDto wmsOutPlanDeliveryOrderDetDto : wmsOutPlanDeliveryOrderDetDtos) {
            WmsInnerJobOrderDet wmsInnerJobOrderDet = new WmsInnerJobOrderDet();
            wmsInnerJobOrderDet.setCoreSourceOrderCode(wmsOutPlanDeliveryOrderDetDto.getCoreSourceOrderCode());
            wmsInnerJobOrderDet.setSourceOrderCode(wmsOutPlanDeliveryOrderDetDto.getPlanDeliveryOrderCode());
            wmsInnerJobOrderDet.setCoreSourceId(wmsOutPlanDeliveryOrderDetDto.getCoreSourceId());
            wmsInnerJobOrderDet.setSourceId(wmsOutPlanDeliveryOrderDetDto.getPlanDeliveryOrderDetId());
            wmsInnerJobOrderDet.setLineNumber(lineNumber + "");
            lineNumber++;
            wmsInnerJobOrderDet.setMaterialId(wmsOutPlanDeliveryOrderDetDto.getMaterialId());
            wmsInnerJobOrderDet.setPlanQty(wmsOutPlanDeliveryOrderDetDto.getOrderQty());
            wmsInnerJobOrderDet.setLineStatus((byte) 1);
            wmsInnerJobOrderDets.add(wmsInnerJobOrderDet);
        }
        WmsInnerJobOrder wmsInnerJobOrder = new WmsInnerJobOrder();
        //wmsInnerJobOrder.setCoreSourceSysOrderTypeCode("OUT-PRO");
        wmsInnerJobOrder.setSourceSysOrderTypeCode("OUT-PDO");
        wmsInnerJobOrder.setWarehouseId(warehouseId);
        wmsInnerJobOrder.setJobOrderType((byte) 2);
        wmsInnerJobOrder.setWmsInPutawayOrderDets(wmsInnerJobOrderDets);
        ResponseEntity responseEntity = innerFeignApi.add(wmsInnerJobOrder);
        if (responseEntity.getCode() != 0) {
            throw new BizErrorException(responseEntity.getCode(), responseEntity.getMessage());
        } else {
            i++;
        }

        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(WmsOutPlanDeliveryOrderDto record) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        record.setPlanDeliveryOrderCode(CodeUtils.getId("OUT-PDO"));
        record.setOrgId(user.getOrganizationId());
        record.setCreateTime(new DateTime());
        record.setCreateUserId(user.getUserId());
        record.setModifiedUserId(user.getUserId());
        record.setModifiedTime(new DateTime());
        int i = wmsOutPlanDeliveryOrderMapper.insertUseGeneratedKeys(record);

        //明细
        List<WmsOutHtPlanDeliveryOrderDet> htList = new LinkedList<>();
        List<WmsOutPlanDeliveryOrderDetDto> wmsOutPlanDeliveryOrderDetDtos = record.getWmsOutPlanDeliveryOrderDetDtos();
        if(StringUtils.isNotEmpty(wmsOutPlanDeliveryOrderDetDtos)){
            for (WmsOutPlanDeliveryOrderDetDto wmsOutPlanDeliveryOrderDetDto:wmsOutPlanDeliveryOrderDetDtos){
                wmsOutPlanDeliveryOrderDetDto.setPlanDeliveryOrderId(record.getPlanDeliveryOrderId());
                wmsOutPlanDeliveryOrderDetDto.setCreateUserId(user.getUserId());
                wmsOutPlanDeliveryOrderDetDto.setCreateTime(new Date());
                wmsOutPlanDeliveryOrderDetDto.setModifiedUserId(user.getUserId());
                wmsOutPlanDeliveryOrderDetDto.setModifiedTime(new Date());
                wmsOutPlanDeliveryOrderDetDto.setOrgId(user.getOrganizationId());

                WmsOutHtPlanDeliveryOrderDet wmsOutHtPlanDeliveryOrderDet = new WmsOutHtPlanDeliveryOrderDet();
                org.springframework.beans.BeanUtils.copyProperties(wmsOutPlanDeliveryOrderDetDto, wmsOutHtPlanDeliveryOrderDet);
                htList.add(wmsOutHtPlanDeliveryOrderDet);
            }
            wmsOutPlanDeliveryOrderDetMapper.insertList(wmsOutPlanDeliveryOrderDetDtos);
            wmsOutHtPlanDeliveryOrderDetMapper.insertList(htList);
        }

        //履历
        WmsOutHtPlanDeliveryOrder wmsOutHtPlanDeliveryOrder = new WmsOutHtPlanDeliveryOrder();
        BeanUtils.copyProperties(record, wmsOutHtPlanDeliveryOrder);
        wmsOutHtPlanDeliveryOrderMapper.insertSelective(wmsOutHtPlanDeliveryOrder);

        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(WmsOutPlanDeliveryOrderDto entity) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        entity.setModifiedUserId(user.getUserId());
        entity.setModifiedTime(new Date());
        int i = wmsOutPlanDeliveryOrderMapper.updateByPrimaryKeySelective(entity);

        ArrayList<Long> idList = new ArrayList<>();
        List<WmsOutPlanDeliveryOrderDetDto> wmsOutPlanDeliveryOrderDetDtos = entity.getWmsOutPlanDeliveryOrderDetDtos();
        if(StringUtils.isNotEmpty(wmsOutPlanDeliveryOrderDetDtos)) {
            for (WmsOutPlanDeliveryOrderDetDto wmsOutPlanDeliveryOrderDetDto : wmsOutPlanDeliveryOrderDetDtos) {
                if (StringUtils.isNotEmpty(wmsOutPlanDeliveryOrderDetDto.getPlanDeliveryOrderDetId())) {
                    wmsOutPlanDeliveryOrderDetMapper.updateByPrimaryKeySelective(wmsOutPlanDeliveryOrderDetDto);
                    idList.add(wmsOutPlanDeliveryOrderDetDto.getPlanDeliveryOrderDetId());
                }
            }
        }

        //删除原明细
        Example example = new Example(WmsOutPlanDeliveryOrderDet.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("planDeliveryOrderId",entity.getPlanDeliveryOrderId());
        if (idList.size() > 0) {
            criteria.andNotIn("planDeliveryOrderDetId", idList);
        }
        wmsOutPlanDeliveryOrderDetMapper.deleteByExample(example);

        //明细
        List<WmsOutHtPlanDeliveryOrderDet> htList = new LinkedList<>();
        if(StringUtils.isNotEmpty(wmsOutPlanDeliveryOrderDetDtos)){
            List<WmsOutPlanDeliveryOrderDetDto> addDetList = new LinkedList<>();
            for (WmsOutPlanDeliveryOrderDetDto wmsOutPlanDeliveryOrderDetDto : wmsOutPlanDeliveryOrderDetDtos){
                WmsOutHtPlanDeliveryOrderDet wmsOutHtPlanDeliveryOrderDet = new WmsOutHtPlanDeliveryOrderDet();
                org.springframework.beans.BeanUtils.copyProperties(wmsOutPlanDeliveryOrderDetDto, wmsOutHtPlanDeliveryOrderDet);
                htList.add(wmsOutHtPlanDeliveryOrderDet);

                if (idList.contains(wmsOutPlanDeliveryOrderDetDto.getPlanDeliveryOrderDetId())) {
                    continue;
                }
                wmsOutPlanDeliveryOrderDetDto.setPlanDeliveryOrderId(entity.getPlanDeliveryOrderId());
                wmsOutPlanDeliveryOrderDetDto.setCreateUserId(user.getUserId());
                wmsOutPlanDeliveryOrderDetDto.setCreateTime(new Date());
                wmsOutPlanDeliveryOrderDetDto.setModifiedUserId(user.getUserId());
                wmsOutPlanDeliveryOrderDetDto.setModifiedTime(new Date());
                wmsOutPlanDeliveryOrderDetDto.setOrgId(user.getOrganizationId());
                addDetList.add(wmsOutPlanDeliveryOrderDetDto);
            }
            if(StringUtils.isNotEmpty(addDetList)) {
                wmsOutPlanDeliveryOrderDetMapper.insertList(addDetList);
            }
            if(StringUtils.isNotEmpty(htList)) {
                wmsOutHtPlanDeliveryOrderDetMapper.insertList(htList);
            }
        }

        //履历
        WmsOutHtPlanDeliveryOrder wmsOutHtPlanDeliveryOrder = new WmsOutHtPlanDeliveryOrder();
        BeanUtils.copyProperties(entity, wmsOutHtPlanDeliveryOrder);
        wmsOutHtPlanDeliveryOrderMapper.insertSelective(wmsOutHtPlanDeliveryOrder);

        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDelete(String ids) {
        //表头履历
        List<WmsOutPlanDeliveryOrder> wmsOutPlanDeliveryOrders = wmsOutPlanDeliveryOrderMapper.selectByIds(ids);
        List<WmsOutHtPlanDeliveryOrder> htList = new LinkedList<>();
        if(StringUtils.isNotEmpty(wmsOutPlanDeliveryOrders)) {
            for (WmsOutPlanDeliveryOrder wmsOutPlanDeliveryOrder : wmsOutPlanDeliveryOrders) {
                WmsOutHtPlanDeliveryOrder wmsOutHtPlanDeliveryOrder = new WmsOutHtPlanDeliveryOrder();
                org.springframework.beans.BeanUtils.copyProperties(wmsOutPlanDeliveryOrder, wmsOutHtPlanDeliveryOrder);
                htList.add(wmsOutHtPlanDeliveryOrder);
            }
            wmsOutHtPlanDeliveryOrderMapper.insertList(htList);
        }

        //表体履历
        List<String> idList = Arrays.asList(ids.split(","));
        Example example = new Example(WmsOutPlanDeliveryOrderDet.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("planDeliveryOrderId",idList);
        List<WmsOutPlanDeliveryOrderDet> wmsOutPlanDeliveryOrderDets = wmsOutPlanDeliveryOrderDetMapper.selectByExample(example);
        List<WmsOutHtPlanDeliveryOrderDet> htDetList = new LinkedList<>();
        if(StringUtils.isNotEmpty(wmsOutPlanDeliveryOrderDets)) {
            for (WmsOutPlanDeliveryOrderDet wmsOutPlanDeliveryOrderDet : wmsOutPlanDeliveryOrderDets) {
                WmsOutHtPlanDeliveryOrderDet wmsOutHtPlanDeliveryOrderDet = new WmsOutHtPlanDeliveryOrderDet();
                org.springframework.beans.BeanUtils.copyProperties(wmsOutPlanDeliveryOrderDet, wmsOutHtPlanDeliveryOrderDet);
                htDetList.add(wmsOutHtPlanDeliveryOrderDet);
            }
            wmsOutHtPlanDeliveryOrderDetMapper.insertList(htDetList);
        }

        wmsOutPlanDeliveryOrderDetMapper.deleteByExample(example);

        return wmsOutPlanDeliveryOrderMapper.deleteByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importExcel(List<WmsOutPlanDeliveryOrderImport> wmsOutPlanDeliveryOrderImports) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        Map<String, Object> resultMap = new HashMap<>();  //封装操作结果
        int success = 0;  //记录操作成功数
        List<Integer> fail = new ArrayList<>();  //记录操作失败行数
        LinkedList<WmsOutPlanDeliveryOrder> list = new LinkedList<>();
        LinkedList<WmsOutHtPlanDeliveryOrder> htList = new LinkedList<>();
        LinkedList<WmsOutPlanDeliveryOrderImport> planDeliveryOrderImports = new LinkedList<>();
        //日志记录
        StringBuilder succeedInfo = new StringBuilder();
        StringBuilder failInfo = new StringBuilder();
        Integer succeedCount = 0;
        Integer failCount = 0;

        for (int i = 0; i < wmsOutPlanDeliveryOrderImports.size(); i++) {
            WmsOutPlanDeliveryOrderImport wmsOutPlanDeliveryOrderImport = wmsOutPlanDeliveryOrderImports.get(i);
            String groupNum = wmsOutPlanDeliveryOrderImport.getGroupNum();

            if (StringUtils.isEmpty(
                    groupNum
            )) {
                failCount++;
                failInfo.append("必填项为空").append(",");
                fail.add(i + 4);
                continue;
            }

            //物料
            String materialCode = wmsOutPlanDeliveryOrderImport.getMaterialCode();
            if(StringUtils.isNotEmpty(materialCode)){
                SearchBaseMaterial searchBaseMaterial = new SearchBaseMaterial();
                searchBaseMaterial.setMaterialCode(materialCode);
                searchBaseMaterial.setCodeQueryMark(1);
                List<BaseMaterial> baseMaterials = baseFeignApi.findList(searchBaseMaterial).getData();
                if (StringUtils.isEmpty(baseMaterials)){
                    failCount++;
                    failInfo.append("物料编码不存在").append(",");
                    fail.add(i+4);
                    continue;
                }
                wmsOutPlanDeliveryOrderImport.setMaterialId(baseMaterials.get(0).getMaterialId());
            }

            //仓库
            String warehouseCode = wmsOutPlanDeliveryOrderImport.getWarehouseCode();
            if(StringUtils.isNotEmpty(warehouseCode)){
                SearchBaseWarehouse searchBaseWarehouse = new SearchBaseWarehouse();
                searchBaseWarehouse.setWarehouseCode(warehouseCode);
                searchBaseWarehouse.setCodeQueryMark(1);
                List<BaseWarehouse> baseWarehouses = baseFeignApi.findList(searchBaseWarehouse).getData();
                if (StringUtils.isEmpty(baseWarehouses)){
                    failCount++;
                    failInfo.append("仓库编码不存在").append(",");
                    fail.add(i+4);
                    continue;
                }
                wmsOutPlanDeliveryOrderImport.setWarehouseId(baseWarehouses.get(0).getWarehouseId());
            }

            succeedCount++;
            succeedInfo.append(i+4).append(",");
            planDeliveryOrderImports.add(wmsOutPlanDeliveryOrderImport);
        }

        SysImportAndExportLog sysImportAndExportLog = new SysImportAndExportLog();
        sysImportAndExportLog.setModuleNames("WMS-OUT");
        sysImportAndExportLog.setFileName("出库计划导入信息表");
        sysImportAndExportLog.setType((byte)1);
        sysImportAndExportLog.setOperatorUserId(user.getUserId());
        sysImportAndExportLog.setResult((byte)1);
        sysImportAndExportLog.setTotalCount(wmsOutPlanDeliveryOrderImports.size());
        sysImportAndExportLog.setFailCount(failCount);
        sysImportAndExportLog.setSucceedCount(succeedCount);
        sysImportAndExportLog.setFailInfo(failInfo.toString());
        sysImportAndExportLog.setSucceedInfo(succeedInfo.toString());
        securityFeignApi.add(sysImportAndExportLog);

        if(StringUtils.isNotEmpty(planDeliveryOrderImports)){
            //对合格数据进行分组
            HashMap<String, List<WmsOutPlanDeliveryOrderImport>> map = planDeliveryOrderImports.stream().collect(Collectors.groupingBy(WmsOutPlanDeliveryOrderImport::getGroupNum, HashMap::new, Collectors.toList()));
            Set<String> codeList = map.keySet();
            for (String code : codeList) {
                List<WmsOutPlanDeliveryOrderImport> wmsOutPlanDeliveryOrderImports1 = map.get(code);
                WmsOutPlanDeliveryOrder wmsOutPlanDeliveryOrder = new WmsOutPlanDeliveryOrder();
                //新增父级数据
                BeanUtils.copyProperties(wmsOutPlanDeliveryOrderImports1.get(0), wmsOutPlanDeliveryOrder);
                wmsOutPlanDeliveryOrder.setPlanDeliveryOrderCode(CodeUtils.getId("OUT-PDO"));
                wmsOutPlanDeliveryOrder.setOrderStatus((byte)1);
                wmsOutPlanDeliveryOrder.setCreateTime(new Date());
                wmsOutPlanDeliveryOrder.setCreateUserId(user.getUserId());
                wmsOutPlanDeliveryOrder.setModifiedUserId(user.getUserId());
                wmsOutPlanDeliveryOrder.setModifiedTime(new Date());
                wmsOutPlanDeliveryOrder.setOrgId(user.getOrganizationId());
                wmsOutPlanDeliveryOrder.setStatus((byte)1);
                success += wmsOutPlanDeliveryOrderMapper.insertUseGeneratedKeys(wmsOutPlanDeliveryOrder);

                //履历
                WmsOutHtPlanDeliveryOrder wmsOutHtPlanDeliveryOrder = new WmsOutHtPlanDeliveryOrder();
                BeanUtils.copyProperties(wmsOutPlanDeliveryOrder, wmsOutHtPlanDeliveryOrder);
                htList.add(wmsOutHtPlanDeliveryOrder);

                //新增明细数据
                LinkedList<WmsOutPlanDeliveryOrderDet> detList = new LinkedList<>();
                for (WmsOutPlanDeliveryOrderImport wmsOutPlanDeliveryOrderImport : wmsOutPlanDeliveryOrderImports1) {
                    WmsOutPlanDeliveryOrderDet wmsOutPlanDeliveryOrderDet = new WmsOutPlanDeliveryOrderDet();
                    BeanUtils.copyProperties(wmsOutPlanDeliveryOrderImport, wmsOutPlanDeliveryOrderDet);
                    wmsOutPlanDeliveryOrderDet.setPlanDeliveryOrderId(wmsOutPlanDeliveryOrder.getPlanDeliveryOrderId());
                    wmsOutPlanDeliveryOrderDet.setStatus((byte) 1);
                    detList.add(wmsOutPlanDeliveryOrderDet);
                }
                wmsOutPlanDeliveryOrderDetMapper.insertList(detList);
            }
            wmsOutHtPlanDeliveryOrderMapper.insertList(htList);
        }

        resultMap.put("操作成功总数",success);
        resultMap.put("操作失败行数",fail);
        return resultMap;
    }
}
