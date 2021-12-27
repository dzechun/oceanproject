package com.fantechs.provider.wms.in.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysImportAndExportLog;
import com.fantechs.common.base.entity.security.SysSpecItem;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.entity.security.search.SearchSysSpecItem;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.wms.in.WmsInInPlanOrderDetDto;
import com.fantechs.common.base.general.dto.wms.in.WmsInInPlanOrderDto;
import com.fantechs.common.base.general.dto.wms.in.imports.WmsInInPlanOrderImport;
import com.fantechs.common.base.general.entity.basic.*;
import com.fantechs.common.base.general.entity.basic.search.*;
import com.fantechs.common.base.general.entity.om.search.SearchOmPurchaseOrderDet;
import com.fantechs.common.base.general.entity.wms.in.WmsInHtInPlanOrder;
import com.fantechs.common.base.general.entity.wms.in.WmsInHtInPlanOrderDet;
import com.fantechs.common.base.general.entity.wms.in.WmsInInPlanOrder;
import com.fantechs.common.base.general.entity.wms.in.WmsInInPlanOrderDet;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerJobOrder;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerJobOrderDet;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CodeUtils;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.api.security.service.SecurityFeignApi;
import com.fantechs.provider.api.wms.inner.InnerFeignApi;
import com.fantechs.provider.wms.in.mapper.WmsInHtInPlanOrderDetMapper;
import com.fantechs.provider.wms.in.mapper.WmsInHtInPlanOrderMapper;
import com.fantechs.provider.wms.in.mapper.WmsInInPlanOrderDetMapper;
import com.fantechs.provider.wms.in.mapper.WmsInInPlanOrderMapper;
import com.fantechs.provider.wms.in.service.WmsInInPlanOrderDetService;
import com.fantechs.provider.wms.in.service.WmsInInPlanOrderService;
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
 * Created by leifengzhi on 2021/12/08.
 */
@Service
public class WmsInInPlanOrderServiceImpl extends BaseService<WmsInInPlanOrder> implements WmsInInPlanOrderService {

    @Resource
    private WmsInInPlanOrderMapper wmsInInPlanOrderMapper;
    @Resource
    private WmsInHtInPlanOrderMapper wmsInHtInPlanOrderMapper;
    @Resource
    private WmsInInPlanOrderDetMapper wmsInInPlanOrderDetMapper;
    @Resource
    private WmsInHtInPlanOrderDetMapper wmsInHtInPlanOrderDetMapper;
    @Resource
    private WmsInInPlanOrderDetService wmsInInPlanOrderDetService;
    @Resource
    private BaseFeignApi baseFeignApi;
    @Resource
    private SecurityFeignApi securityFeignApi;
    @Resource
    private InnerFeignApi innerFeignApi;



    @Override
    public List<WmsInInPlanOrderDto> findList(Map<String, Object> map) {
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(map.get("orgId"))){
            map.put("orgId",sysUser.getOrganizationId());
        }
        return wmsInInPlanOrderMapper.findList(map);
    }


    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int save(WmsInInPlanOrderDto wmsInInPlanOrderDto){
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();


        //预约数量校验
        if(StringUtils.isEmpty(wmsInInPlanOrderDto.getWarehouseId())) {
            throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(), "仓库编码不能为空");
        }else{
            SearchBaseStorage searchBaseStorage = new SearchBaseStorage();
            searchBaseStorage.setWarehouseId(wmsInInPlanOrderDto.getWarehouseId());
            searchBaseStorage.setStorageType((byte)2);
            List<BaseStorage> baseStorages = baseFeignApi.findList(searchBaseStorage).getData();
            if(StringUtils.isEmpty(baseStorages))
                throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(), "未查询到该仓库的收货库位");
            else
                wmsInInPlanOrderDto.setStorageId(baseStorages.get(0).getStorageId());
        }

        wmsInInPlanOrderDto.setInPlanOrderCode(CodeUtils.getId("IN-IPO"));
        if(StringUtils.isEmpty(wmsInInPlanOrderDto.getMakeOrderUserId()))
            wmsInInPlanOrderDto.setMakeOrderUserId(user.getUserId());
        wmsInInPlanOrderDto.setCreateUserId(user.getUserId());
        wmsInInPlanOrderDto.setCreateTime(new Date());
        wmsInInPlanOrderDto.setModifiedUserId(user.getUserId());
        wmsInInPlanOrderDto.setModifiedTime(new Date());
        wmsInInPlanOrderDto.setStatus(StringUtils.isEmpty(wmsInInPlanOrderDto.getStatus())?1: wmsInInPlanOrderDto.getStatus());
        wmsInInPlanOrderDto.setOrgId(user.getOrganizationId());
        wmsInInPlanOrderDto.setIsDelete((byte)1);
        wmsInInPlanOrderDto.setOrderStatus((byte)1);
        int i =wmsInInPlanOrderMapper.insertUseGeneratedKeys(wmsInInPlanOrderDto);

        //保存履历表
        WmsInHtInPlanOrder wmsInHtInPlanOrder = new WmsInHtInPlanOrder();
        BeanUtils.copyProperties(wmsInInPlanOrderDto, wmsInHtInPlanOrder);
        wmsInHtInPlanOrderMapper.insertSelective(wmsInHtInPlanOrder);

        if(StringUtils.isNotEmpty(wmsInInPlanOrderDto.getWmsInInPlanOrderDetDtos())) {
            List<WmsInInPlanOrderDetDto> list = new ArrayList<>();
            List<WmsInHtInPlanOrderDet> htList = new ArrayList<>();
            for (WmsInInPlanOrderDetDto wmsInInPlanOrderDetDto : wmsInInPlanOrderDto.getWmsInInPlanOrderDetDtos()) {

                if(StringUtils.isEmpty(wmsInInPlanOrderDetDto.getPlanQty()) || wmsInInPlanOrderDetDto.getPlanQty().compareTo(BigDecimal.ZERO) == -1)
                    throw new BizErrorException(ErrorCodeEnum.OPT20012001.getCode(),"计划数量需大于0");

                wmsInInPlanOrderDetDto.setCreateUserId(user.getUserId());
                wmsInInPlanOrderDetDto.setCreateTime(new Date());
                wmsInInPlanOrderDetDto.setModifiedUserId(user.getUserId());
                wmsInInPlanOrderDetDto.setModifiedTime(new Date());
                wmsInInPlanOrderDetDto.setStatus(StringUtils.isEmpty(wmsInInPlanOrderDetDto.getStatus()) ? 1 : wmsInInPlanOrderDetDto.getStatus());
                wmsInInPlanOrderDetDto.setOrgId(user.getOrganizationId());
                wmsInInPlanOrderDetDto.setInPlanOrderId(wmsInInPlanOrderDto.getInPlanOrderId());
                wmsInInPlanOrderDetDto.setPutawayQty(BigDecimal.ZERO);
                list.add(wmsInInPlanOrderDetDto);
                WmsInHtInPlanOrderDet wmsInHtInPlanOrderDet = new WmsInHtInPlanOrderDet();
                BeanUtils.copyProperties(wmsInInPlanOrderDetDto, wmsInHtInPlanOrderDet);
                htList.add(wmsInHtInPlanOrderDet);
            }
            if (StringUtils.isNotEmpty(list)) wmsInInPlanOrderDetMapper.insertList(list);
            if (StringUtils.isNotEmpty(htList)) wmsInHtInPlanOrderDetMapper.insertList(htList);
        }

        return i;
    }


    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int update(WmsInInPlanOrderDto wmsInInPlanOrderDto) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        Example example = new Example(WmsInInPlanOrder.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("inPlanOrderCode",wmsInInPlanOrderDto.getInPlanOrderCode())
                .andNotEqualTo("inPlanOrderId",wmsInInPlanOrderDto.getInPlanOrderId());
        List<WmsInInPlanOrder> srmInAsnOrders = wmsInInPlanOrderMapper.selectByExample(example);
        if(StringUtils.isNotEmpty(srmInAsnOrders)) throw new BizErrorException(ErrorCodeEnum.OPT20012001.getCode(),"单号已存在，请勿重复添加");

        wmsInInPlanOrderDto.setModifiedTime(new Date());
        wmsInInPlanOrderDto.setModifiedUserId(user.getUserId());
        int i = wmsInInPlanOrderMapper.updateByPrimaryKeySelective(wmsInInPlanOrderDto);

        //保存履历表
        WmsInHtInPlanOrder wmsInHtInPlanOrder = new WmsInHtInPlanOrder();
        BeanUtils.copyProperties(wmsInInPlanOrderDto, wmsInHtInPlanOrder);
        wmsInHtInPlanOrderMapper.insertSelective(wmsInHtInPlanOrder);


        //保存详情表
        //更新原有明细
        ArrayList<Long> idList = new ArrayList<>();
        List<WmsInInPlanOrderDetDto> list = wmsInInPlanOrderDto.getWmsInInPlanOrderDetDtos();
        if(StringUtils.isNotEmpty(list)) {
            for (WmsInInPlanOrderDetDto wmsInInPlanOrderDetDto : list) {
                if(StringUtils.isEmpty(wmsInInPlanOrderDetDto.getPlanQty()) || wmsInInPlanOrderDetDto.getPlanQty().compareTo(BigDecimal.ZERO) == -1)
                    throw new BizErrorException(ErrorCodeEnum.OPT20012001.getCode(),"计划数量需大于0");
                if(StringUtils.isEmpty(wmsInInPlanOrderDetDto.getPutawayQty()) || wmsInInPlanOrderDetDto.getPutawayQty().compareTo(BigDecimal.ZERO) == -1)
                    throw new BizErrorException(ErrorCodeEnum.OPT20012001.getCode(),"上架数量需大于0");
                if(wmsInInPlanOrderDetDto.getPlanQty().compareTo(wmsInInPlanOrderDetDto.getPutawayQty()) == -1)
                    throw new BizErrorException(ErrorCodeEnum.OPT20012001.getCode(),"上架数量不能大于计划数量");

                if (StringUtils.isNotEmpty(wmsInInPlanOrderDetDto.getInPlanOrderDetId())) {
                    wmsInInPlanOrderDetMapper.updateByPrimaryKey(wmsInInPlanOrderDetDto);
                    idList.add(wmsInInPlanOrderDetDto.getInPlanOrderDetId());
                }
            }
        }

        //删除更新之外的明细
        Example example1 = new Example(WmsInInPlanOrderDet.class);
        Example.Criteria criteria1 = example1.createCriteria();
        criteria1.andEqualTo("inPlanOrderId", wmsInInPlanOrderDto.getInPlanOrderId());
        if (idList.size() > 0) {
            criteria1.andNotIn("inPlanOrderDetId", idList);
        }
        wmsInInPlanOrderDetMapper.deleteByExample(example1);

        //新增剩余的明细
        if(StringUtils.isNotEmpty(list)){
            List<WmsInInPlanOrderDetDto> addlist = new ArrayList<>();
            for (WmsInInPlanOrderDetDto wmsInInPlanOrderDetDto  : list){
                if (idList.contains(wmsInInPlanOrderDetDto.getInPlanOrderDetId())) {
                    continue;
                }
                wmsInInPlanOrderDetDto.setInPlanOrderId(wmsInInPlanOrderDto.getInPlanOrderId());
                wmsInInPlanOrderDetDto.setCreateUserId(user.getUserId());
                wmsInInPlanOrderDetDto.setCreateTime(new Date());
                wmsInInPlanOrderDetDto.setModifiedUserId(user.getUserId());
                wmsInInPlanOrderDetDto.setModifiedTime(new Date());
                wmsInInPlanOrderDetDto.setStatus(StringUtils.isEmpty(wmsInInPlanOrderDetDto.getStatus())?1: wmsInInPlanOrderDetDto.getStatus());
                wmsInInPlanOrderDetDto.setOrgId(user.getOrganizationId());
                addlist.add(wmsInInPlanOrderDetDto);
            }
            if (StringUtils.isNotEmpty(addlist))
                wmsInInPlanOrderDetMapper.insertList(addlist);
        }
        return i;
    }

    @Override
    public int close(String ids) {
        List<WmsInInPlanOrderDetDto> wmsInInPlanOrderDetDtos = wmsInInPlanOrderDetService.findListByIds(ids);
        int i = 1;
        if(StringUtils.isNotEmpty()){
            List<WmsInHtInPlanOrderDet> htList = new ArrayList<>();
            for(WmsInInPlanOrderDetDto wmsInInPlanOrderDetDto : wmsInInPlanOrderDetDtos ){
                wmsInInPlanOrderDetDto.setLineStatus((byte)3);
                wmsInInPlanOrderDetMapper.updateByPrimaryKey(wmsInInPlanOrderDetDto);

                //保存履历表
                WmsInHtInPlanOrderDet wmsInHtInPlanOrderDet = new WmsInHtInPlanOrderDet();
                BeanUtils.copyProperties(wmsInInPlanOrderDetDto, wmsInHtInPlanOrderDet);
                htList.add(wmsInHtInPlanOrderDet);
            }
            if (StringUtils.isNotEmpty(htList))
                i = wmsInHtInPlanOrderDetMapper.insertList(htList);
        }
        return i;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importExcel(List<WmsInInPlanOrderImport> list) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        Map<String, Object> resultMap = new HashMap<>();  //封装操作结果
        int success = 0;  //记录操作成功数
        List<String> fail = new ArrayList<>();  //记录操作失败行数
        List<Map<Integer,String>> failMap = new ArrayList<>();  //记录操作失败行数
        List<WmsInInPlanOrderDet> detList = new ArrayList<>();
        Map map = new HashMap();
        SearchOmPurchaseOrderDet searchOmPurchaseOrderDet = new SearchOmPurchaseOrderDet();
        searchOmPurchaseOrderDet.setCodeQueryMark(1);

        Map<String, List<WmsInInPlanOrderImport>> collect = list.stream().collect(Collectors.groupingBy(WmsInInPlanOrderImport::getId));
        for (String s : collect.keySet()) {
            List<WmsInInPlanOrderImport> wmsInInPlanOrderImportList = collect.get(s);

            //判断非空
            String warehouseName = wmsInInPlanOrderImportList.get(0).getWarehouseName();
            if (StringUtils.isEmpty(
                    warehouseName
            )){
                map.put(s,"仓库名称、计划入库单号不鞥呢为空");
                failMap.add(map);
                fail.add(s);
                continue;
            }

            SearchBaseWarehouse searchBaseWarehouse = new SearchBaseWarehouse();
            searchBaseWarehouse.setWarehouseName(warehouseName);
            searchBaseWarehouse.setCodeQueryMark(1);
            List<BaseWarehouse> baseWarehouses = baseFeignApi.findList(searchBaseWarehouse).getData();
            if (StringUtils.isEmpty(baseWarehouses) || baseWarehouses.size() != 1) {
                map.put(s,"未查询到仓库或查询出的仓库不唯一");
                failMap.add(map);
                fail.add(s);
                continue;
            }


            WmsInInPlanOrder wmsInInPlanOrder = new WmsInInPlanOrder();
            wmsInInPlanOrder.setCreateUserId(user.getUserId());
            wmsInInPlanOrder.setCreateTime(new Date());
            wmsInInPlanOrder.setModifiedUserId(user.getUserId());
            wmsInInPlanOrder.setModifiedTime(new Date());
            wmsInInPlanOrder.setStatus((byte) 1);
            wmsInInPlanOrder.setOrgId(user.getOrganizationId());
            wmsInInPlanOrder.setInPlanOrderCode(CodeUtils.getId("IN-IPO"));
            wmsInInPlanOrder.setWarehouseId(baseWarehouses.get(0).getWarehouseId());
            wmsInInPlanOrder.setMakeOrderUserId(user.getUserId());
            wmsInInPlanOrder.setOrderStatus((byte) 1);
            wmsInInPlanOrderMapper.insertUseGeneratedKeys(wmsInInPlanOrder);

            for (int i = 0; i < wmsInInPlanOrderImportList.size(); i++) {
                WmsInInPlanOrderImport wmsInInPlanOrderImport = wmsInInPlanOrderImportList.get(i);

                SearchBaseMaterial searchBaseMaterial = new SearchBaseMaterial();
                searchBaseMaterial.setOrgId(user.getOrganizationId());
                searchBaseMaterial.setMaterialCode(wmsInInPlanOrderImport.getMaterialCode());
                searchBaseMaterial.setCodeQueryMark(1);
                List<BaseMaterial> baseMaterials = baseFeignApi.findList(searchBaseMaterial).getData();

                if (StringUtils.isEmpty(baseMaterials)) {
                    map.put(s,"未查询到对应的物料数据，编码为："+wmsInInPlanOrderImport.getMaterialCode());
                    failMap.add(map);
                    fail.add(s);
                    continue;
                }
                SearchBaseInventoryStatus searchBaseInventoryStatus = new SearchBaseInventoryStatus();
                searchBaseInventoryStatus.setInventoryStatusName("合格");
                List<BaseInventoryStatus> baseInventoryStatuss = baseFeignApi.findList(searchBaseInventoryStatus).getData();
                if (StringUtils.isEmpty(baseInventoryStatuss)) {
                    map.put(s,"未查询到库存状态数据");
                    failMap.add(map);
                    fail.add(s);
                    continue;
                }
                if(StringUtils.isEmpty(wmsInInPlanOrderImport.getPlanQty()) || wmsInInPlanOrderImport.getPlanQty().compareTo(BigDecimal.ZERO) == -1) {
                    map.put(s,"计划数量需大于0");
                    failMap.add(map);
                    fail.add(s);
                    continue;
                }

                WmsInInPlanOrderDet wmsInInPlanOrderDet = new WmsInInPlanOrderDet();
                BeanUtils.copyProperties(wmsInInPlanOrderImport, wmsInInPlanOrderDet);
                wmsInInPlanOrderDet.setInPlanOrderId(wmsInInPlanOrder.getInPlanOrderId());
                wmsInInPlanOrderDet.setInventoryStatusId(baseInventoryStatuss.get(0).getInventoryStatusId());
                wmsInInPlanOrderDet.setMaterialId(baseMaterials.get(0).getMaterialId());
                wmsInInPlanOrderDet.setCreateUserId(user.getUserId());
                wmsInInPlanOrderDet.setCreateTime(new Date());
                wmsInInPlanOrderDet.setModifiedUserId(user.getUserId());
                wmsInInPlanOrderDet.setModifiedTime(new Date());
                wmsInInPlanOrderDet.setStatus((byte) 1);
                wmsInInPlanOrderDet.setPutawayQty(BigDecimal.ZERO);
                wmsInInPlanOrderDet.setOrgId(user.getOrganizationId());

                detList.add(wmsInInPlanOrderDet);
                success++;
            }
            if (StringUtils.isEmpty(detList)) {
                wmsInInPlanOrderMapper.deleteByPrimaryKey(wmsInInPlanOrder.getInPlanOrderId());
                map.put(s,"无详情数据");
                failMap.add(map);
                fail.add(s);
                continue;
            }

        }

        if (StringUtils.isNotEmpty(detList)) {
            wmsInInPlanOrderDetMapper.insertList(detList);
        }
        //添加日志
        SysImportAndExportLog log = new SysImportAndExportLog();
        log.setSucceedCount(list.size() - fail.size());
        log.setFailCount(fail.size());
        log.setFailInfo(failMap.toString());
        log.setOperatorUserId(user.getUserId());
        log.setTotalCount(list.size());
        log.setType((byte)1);
        securityFeignApi.add(log);

        resultMap.put("操作成功总数",success);
        resultMap.put("操作失败的送货计划标识",fail);
        return resultMap;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int pushDown(String ids) {
        //根据单据流生成入库计划单或上架作业单
        //等待确认是否限制相同的单据来源
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        SearchSysSpecItem searchSysSpecItem = new SearchSysSpecItem();
        searchSysSpecItem.setSpecCode("InPlanOrderIsWork");
        List<SysSpecItem> specItems = securityFeignApi.findSpecItemList(searchSysSpecItem).getData();
        if(StringUtils.isEmpty(specItems))
            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(),"需先配置作业循序先后");
        if("0".equals(StringUtils.isEmpty(specItems.get(0).getParaValue())))
            throw new BizErrorException(ErrorCodeEnum.OPT20012002.getCode(),"先作业后单据无法进行下推操作");

        int i = 0;
        List<WmsInInPlanOrderDet> wmsInInPlanOrderDets = wmsInInPlanOrderDetMapper.selectByIds(ids);
        //查当前单据的下游单据
        SearchBaseOrderFlow searchBaseOrderFlow = new SearchBaseOrderFlow();
        searchBaseOrderFlow.setOrderTypeCode("IN-IPO");
        BaseOrderFlow baseOrderFlow = baseFeignApi.findOrderFlow(searchBaseOrderFlow).getData();
        if(StringUtils.isEmpty(baseOrderFlow)){
            throw new BizErrorException("未找到当前单据配置的下游单据");
        }

        Example example = new Example(WmsInInPlanOrder.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("inPlanOrderId",wmsInInPlanOrderDets.get(0).getInPlanOrderId());
        List<WmsInInPlanOrder> wmsInInPlanOrders = wmsInInPlanOrderMapper.selectByExample(example);
        if(StringUtils.isEmpty(wmsInInPlanOrders))
            throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(),"未查询到对应的入库计划");

        if("IN-IWK".equals(baseOrderFlow.getNextOrderTypeCode())){
            //生成上架作业单
            List<WmsInnerJobOrderDet> detList = new LinkedList<>();
            for(WmsInInPlanOrderDet wmsInInPlanOrderDet : wmsInInPlanOrderDets){
                int lineNumber = 1;
                WmsInnerJobOrderDet wmsInnerJobOrderDet = new WmsInnerJobOrderDet();
                wmsInnerJobOrderDet.setCoreSourceOrderCode(wmsInInPlanOrderDet.getCoreSourceOrderCode());
                wmsInnerJobOrderDet.setSourceOrderCode(wmsInInPlanOrders.get(0).getInPlanOrderCode());
                wmsInnerJobOrderDet.setSourceId(wmsInInPlanOrderDet.getInPlanOrderDetId());
                wmsInnerJobOrderDet.setLineNumber(lineNumber+"");
                wmsInnerJobOrderDet.setMaterialId(wmsInInPlanOrderDet.getMaterialId());
                wmsInnerJobOrderDet.setPlanQty(wmsInInPlanOrderDet.getPlanQty());
                wmsInnerJobOrderDet.setLineStatus((byte)1);
                detList.add(wmsInnerJobOrderDet);
            }

            WmsInnerJobOrder wmsInnerJobOrder = new WmsInnerJobOrder();
            wmsInnerJobOrder.setSourceSysOrderTypeCode(wmsInInPlanOrders.get(0).getSourceSysOrderTypeCode());
            wmsInnerJobOrder.setCoreSourceSysOrderTypeCode(wmsInInPlanOrders.get(0).getCoreSourceSysOrderTypeCode());
            wmsInnerJobOrder.setJobOrderType((byte)1);
            wmsInnerJobOrder.setOrderStatus((byte)1);
            wmsInnerJobOrder.setCreateUserId(user.getUserId());
            wmsInnerJobOrder.setCreateTime(new Date());
            wmsInnerJobOrder.setModifiedUserId(user.getUserId());
            wmsInnerJobOrder.setModifiedTime(new Date());
            wmsInnerJobOrder.setStatus((byte)1);
            wmsInnerJobOrder.setOrgId(user.getOrganizationId());
            wmsInnerJobOrder.setWmsInPutawayOrderDets(detList);

            ResponseEntity responseEntity = innerFeignApi.add(wmsInnerJobOrder);
            if(responseEntity.getCode() != 0){
                throw new BizErrorException("下推生成上架作业单失败");
            }else {
                i++;
            }
        }else {
            throw new BizErrorException("单据流配置错误");
        }

        return i;
    }


}
