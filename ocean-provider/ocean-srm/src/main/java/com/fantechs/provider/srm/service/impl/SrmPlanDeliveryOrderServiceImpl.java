package com.fantechs.provider.srm.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.om.OmPurchaseOrderDetDto;
import com.fantechs.common.base.general.dto.srm.SrmPlanDeliveryOrderDetDto;
import com.fantechs.common.base.general.dto.srm.SrmPlanDeliveryOrderDto;
import com.fantechs.common.base.general.dto.srm.imports.SrmPlanDeliveryOrderImport;
import com.fantechs.common.base.general.entity.basic.BaseSupplier;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseSupplier;
import com.fantechs.common.base.general.entity.om.search.SearchOmPurchaseOrderDet;
import com.fantechs.common.base.general.entity.srm.SrmPlanDeliveryOrder;
import com.fantechs.common.base.general.entity.srm.SrmPlanDeliveryOrderDet;
import com.fantechs.common.base.general.entity.srm.history.SrmHtPlanDeliveryOrder;
import com.fantechs.common.base.general.entity.srm.history.SrmHtPlanDeliveryOrderDet;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CodeUtils;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.api.qms.OMFeignApi;
import com.fantechs.provider.srm.mapper.SrmHtPlanDeliveryOrderDetMapper;
import com.fantechs.provider.srm.mapper.SrmHtPlanDeliveryOrderMapper;
import com.fantechs.provider.srm.mapper.SrmPlanDeliveryOrderDetMapper;
import com.fantechs.provider.srm.mapper.SrmPlanDeliveryOrderMapper;
import com.fantechs.provider.srm.service.SrmPlanDeliveryOrderService;
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
 * Created by leifengzhi on 2021/11/29.
 */
@Service
public class SrmPlanDeliveryOrderServiceImpl extends BaseService<SrmPlanDeliveryOrder> implements SrmPlanDeliveryOrderService {

    @Resource
    private SrmPlanDeliveryOrderMapper srmPlanDeliveryOrderMapper;
    @Resource
    private SrmPlanDeliveryOrderDetMapper srmPlanDeliveryOrderDetMapper;
    @Resource
    private SrmHtPlanDeliveryOrderMapper srmHtPlanDeliveryOrderMapper;
    @Resource
    private SrmHtPlanDeliveryOrderDetMapper srmHtPlanDeliveryOrderDetMapper;
    @Resource
    private OMFeignApi omFeignApi;
    @Resource
    private BaseFeignApi baseFeignApi;

    @Override
    public List<SrmPlanDeliveryOrderDto> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(map.get("orgId"))) {
            map.put("orgId",user.getOrganizationId());
        }


        if (StringUtils.isNotEmpty(user.getSupplierId())) {
            map.put("supplierId", user.getSupplierId());
        }

        return srmPlanDeliveryOrderMapper.findList(map);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importExcel(List<SrmPlanDeliveryOrderImport> list) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        Map<String, Object> resultMap = new HashMap<>();  //封装操作结果
        int success = 0;  //记录操作成功数
        List<String> fail = new ArrayList<>();  //记录操作失败行数
        List<SrmPlanDeliveryOrderDet> detList = new ArrayList<>();


        SearchOmPurchaseOrderDet searchOmPurchaseOrderDet = new SearchOmPurchaseOrderDet();
        searchOmPurchaseOrderDet.setCodeQueryMark(1);

        Map<String, List<SrmPlanDeliveryOrderImport>> collect = list.stream().collect(Collectors.groupingBy(SrmPlanDeliveryOrderImport::getId));
        for (String s : collect.keySet()) {
            List<SrmPlanDeliveryOrderImport> srmPlanDeliveryOrderImportList = collect.get(s);

            String supplierCode = srmPlanDeliveryOrderImportList.get(0).getSupplierCode();
            if (StringUtils.isEmpty(supplierCode)) {
                fail.add(s);
            }
            SearchBaseSupplier searchBaseSupplier = new SearchBaseSupplier();
            searchBaseSupplier.setSupplierCode(supplierCode);
            searchBaseSupplier.setCodeQueryMark((byte) 1);
            List<BaseSupplier> baseSupplierList = baseFeignApi.findSupplierList(searchBaseSupplier).getData();
            if (StringUtils.isEmpty(baseSupplierList) || baseSupplierList.size() != 1) {
                fail.add(s);
            }

            SrmPlanDeliveryOrder srmPlanDeliveryOrder = new SrmPlanDeliveryOrder();
            srmPlanDeliveryOrder.setCreateUserId(user.getUserId());
            srmPlanDeliveryOrder.setCreateTime(new Date());
            srmPlanDeliveryOrder.setModifiedUserId(user.getUserId());
            srmPlanDeliveryOrder.setModifiedTime(new Date());
            srmPlanDeliveryOrder.setStatus((byte) 1);
            srmPlanDeliveryOrder.setOrgId(user.getOrganizationId());
            srmPlanDeliveryOrder.setPlanDeliveryOrderCode(CodeUtils.getId("SHJH"));
            srmPlanDeliveryOrder.setSupplierId(baseSupplierList.get(0).getSupplierId());
            srmPlanDeliveryOrder.setOrderStatus((byte) 1);
            srmPlanDeliveryOrderMapper.insertUseGeneratedKeys(srmPlanDeliveryOrder);

            for (int i = 0; i < srmPlanDeliveryOrderImportList.size(); i++) {
                SrmPlanDeliveryOrderImport srmPlanDeliveryOrderImport = srmPlanDeliveryOrderImportList.get(i);

                searchOmPurchaseOrderDet.setMaterialCode(srmPlanDeliveryOrderImport.getMaterialCode());
                searchOmPurchaseOrderDet.setPurchaseOrderCode(srmPlanDeliveryOrderImport.getPurchaseOrderCode());
                List<OmPurchaseOrderDetDto> omPurchaseOrderDetList = omFeignApi.findList(searchOmPurchaseOrderDet).getData();
                if (StringUtils.isEmpty(omPurchaseOrderDetList)) {
//                    fail.add(i + 4);
                    continue;
                }
                OmPurchaseOrderDetDto omPurchaseOrderDetDto = omPurchaseOrderDetList.get(0);

                if (omPurchaseOrderDetDto.getOrderQty().compareTo(omPurchaseOrderDetDto.getTotalPlanDeliveryQty().add(srmPlanDeliveryOrderImport.getPlanDeliveryQty())) == -1) {
//                    fail.add(i + 4);
                    continue;
                }

                SrmPlanDeliveryOrderDet srmPlanDeliveryOrderDet = new SrmPlanDeliveryOrderDet();
                srmPlanDeliveryOrderDet.setPlanDeliveryOrderId(srmPlanDeliveryOrder.getPlanDeliveryOrderId());
                srmPlanDeliveryOrderDet.setPurchaseOrderDetId(omPurchaseOrderDetDto.getPurchaseOrderDetId());
                srmPlanDeliveryOrderDet.setPlanDeliveryDate(srmPlanDeliveryOrderImport.getPlanDeliveryDate());
                srmPlanDeliveryOrderDet.setPlanDeliveryQty(srmPlanDeliveryOrderImport.getPlanDeliveryQty());
                srmPlanDeliveryOrderDet.setIfCreateAsn((byte) 0);
                srmPlanDeliveryOrderDet.setCreateUserId(user.getUserId());
                srmPlanDeliveryOrderDet.setCreateTime(new Date());
                srmPlanDeliveryOrderDet.setModifiedUserId(user.getUserId());
                srmPlanDeliveryOrderDet.setModifiedTime(new Date());
                srmPlanDeliveryOrderDet.setStatus((byte) 1);
                srmPlanDeliveryOrderDet.setOrgId(user.getOrganizationId());

                detList.add(srmPlanDeliveryOrderDet);
                success++;
            }
            if (StringUtils.isEmpty(detList)) {
                srmPlanDeliveryOrderMapper.deleteByIds(srmPlanDeliveryOrder.getPlanDeliveryOrderId().toString());
                fail.add(s);
            }

        }

        if (StringUtils.isNotEmpty(detList)) {
            srmPlanDeliveryOrderDetMapper.insertList(detList);
        }


        resultMap.put("操作成功总数",success);
        resultMap.put("操作失败的送货计划标识",fail);
        return resultMap;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(SrmPlanDeliveryOrder record) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        record.setCreateUserId(user.getUserId());
        record.setCreateTime(new Date());
        record.setModifiedUserId(user.getUserId());
        record.setModifiedTime(new Date());
        record.setStatus(StringUtils.isEmpty(record.getStatus())?1: record.getStatus());
        record.setOrgId(user.getOrganizationId());
        record.setPlanDeliveryOrderCode(CodeUtils.getId("SHJH"));
        record.setOrderStatus((byte) 1);
        int i = srmPlanDeliveryOrderMapper.insertUseGeneratedKeys(record);

        SrmHtPlanDeliveryOrder srmHtPlanDeliveryOrder = new SrmHtPlanDeliveryOrder();
        BeanUtils.copyProperties(record, srmHtPlanDeliveryOrder);
        srmHtPlanDeliveryOrderMapper.insertUseGeneratedKeys(srmHtPlanDeliveryOrder);

        ht(record,srmHtPlanDeliveryOrder);

        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(SrmPlanDeliveryOrder entity) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        entity.setModifiedUserId(user.getUserId());
        entity.setModifiedTime(new Date());
        int i = srmPlanDeliveryOrderMapper.updateByPrimaryKeySelective(entity);

        SrmHtPlanDeliveryOrder srmHtPlanDeliveryOrder = new SrmHtPlanDeliveryOrder();
        BeanUtils.copyProperties(entity, srmHtPlanDeliveryOrder);
        srmHtPlanDeliveryOrderMapper.insertUseGeneratedKeys(srmHtPlanDeliveryOrder);

        Example example = new Example(SrmPlanDeliveryOrderDet.class);
        example.createCriteria().andEqualTo("planDeliveryOrderId",entity.getPlanDeliveryOrderId());


        //提交的时候反写采购订单的累计计划送货数量
        if (StringUtils.isNotEmpty(entity.getOrderStatus()) && entity.getOrderStatus() == 2) {

            Map<String, Object> map = new HashMap<>();
            map.put("planDeliveryOrderId",entity.getPlanDeliveryOrderId());
            List<SrmPlanDeliveryOrderDetDto> list = srmPlanDeliveryOrderDetMapper.findList(map);
            entity.setList(list);

            Map<Long, List<SrmPlanDeliveryOrderDetDto>> collect = entity.getList().stream().collect(Collectors.groupingBy(SrmPlanDeliveryOrderDetDto::getPurchaseOrderDetId));
            SearchOmPurchaseOrderDet searchOmPurchaseOrderDet = new SearchOmPurchaseOrderDet();
            for (Long aLong : collect.keySet()) {
                List<SrmPlanDeliveryOrderDetDto> srmPlanDeliveryOrderDetDtos = collect.get(aLong);
                BigDecimal bigDecimal = StringUtils.isEmpty(srmPlanDeliveryOrderDetDtos.get(0).getTotalPlanDeliveryQty())?new BigDecimal(0):srmPlanDeliveryOrderDetDtos.get(0).getTotalPlanDeliveryQty();


                for (SrmPlanDeliveryOrderDetDto srmPlanDeliveryOrderDetDto : srmPlanDeliveryOrderDetDtos) {
                    bigDecimal = bigDecimal.add(srmPlanDeliveryOrderDetDto.getPlanDeliveryQty());
                }

                if (srmPlanDeliveryOrderDetDtos.get(0).getOrderQty().compareTo(bigDecimal) == -1) {
                    throw new BizErrorException(ErrorCodeEnum.OPT20012002.getCode(),"累计数量大于采购数量");
                }

                searchOmPurchaseOrderDet.setPurchaseOrderDetId(aLong);
                List<OmPurchaseOrderDetDto> omPurchaseOrderDetDtoList = omFeignApi.findList(searchOmPurchaseOrderDet).getData();

                if (StringUtils.isNotEmpty(omPurchaseOrderDetDtoList)) {
                    OmPurchaseOrderDetDto omPurchaseOrderDetDto = omPurchaseOrderDetDtoList.get(0);
                    omPurchaseOrderDetDto.setTotalPlanDeliveryQty(bigDecimal);
                    omFeignApi.update(omPurchaseOrderDetDto);
                }
            }
        } else {
            Map<Long, List<SrmPlanDeliveryOrderDetDto>> collect = entity.getList().stream().collect(Collectors.groupingBy(SrmPlanDeliveryOrderDetDto::getPurchaseOrderDetId));
            for (Long aLong : collect.keySet()) {
                List<SrmPlanDeliveryOrderDetDto> srmPlanDeliveryOrderDetDtos = collect.get(aLong);
                BigDecimal bigDecimal = srmPlanDeliveryOrderDetDtos.get(0).getTotalPlanDeliveryQty();
                for (SrmPlanDeliveryOrderDetDto srmPlanDeliveryOrderDetDto : srmPlanDeliveryOrderDetDtos) {
                    bigDecimal = bigDecimal.add(srmPlanDeliveryOrderDetDto.getPlanDeliveryQty());
                }
                if (srmPlanDeliveryOrderDetDtos.get(0).getOrderQty().compareTo(bigDecimal) == -1) {
                    throw new BizErrorException(ErrorCodeEnum.OPT20012002.getCode(),"累计数量大于采购数量");
                }
            }
        }

        srmPlanDeliveryOrderDetMapper.deleteByExample(example);

        ht(entity,srmHtPlanDeliveryOrder);

        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDelete(String ids) {
        String[] idArry = ids.split(",");

        Example example = new Example(SrmPlanDeliveryOrderDet.class);
        List<SrmHtPlanDeliveryOrderDet> htDetList = new ArrayList<>();
        for (String id : idArry) {
            SrmPlanDeliveryOrder srmPlanDeliveryOrder = srmPlanDeliveryOrderMapper.selectByPrimaryKey(id);
            if(StringUtils.isEmpty(srmPlanDeliveryOrder)){
                continue;
            }
            if (srmPlanDeliveryOrder.getOrderStatus() == 2) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012004.getCode(),"已提交的数据不能删除");
            }

            SrmHtPlanDeliveryOrder srmHtPlanDeliveryOrder = new SrmHtPlanDeliveryOrder();
            BeanUtils.copyProperties(srmPlanDeliveryOrder, srmHtPlanDeliveryOrder);
            srmHtPlanDeliveryOrderMapper.insertUseGeneratedKeys(srmHtPlanDeliveryOrder);

            example.createCriteria().andEqualTo("planDeliveryOrderId",srmPlanDeliveryOrder.getPlanDeliveryOrderId());
            List<SrmPlanDeliveryOrderDet> srmPlanDeliveryOrderDetList = srmPlanDeliveryOrderDetMapper.selectByExample(example);
            srmPlanDeliveryOrderDetMapper.deleteByExample(example);
            example.clear();

            for (SrmPlanDeliveryOrderDet srmPlanDeliveryOrderDet : srmPlanDeliveryOrderDetList) {
                SrmHtPlanDeliveryOrderDet srmHtPlanDeliveryOrderDet = new SrmHtPlanDeliveryOrderDet();
                BeanUtils.copyProperties(srmPlanDeliveryOrderDet, srmHtPlanDeliveryOrderDet);

                srmHtPlanDeliveryOrderDet.setPlanDeliveryOrderDetId(null);
                srmHtPlanDeliveryOrderDet.setPlanDeliveryOrderId(srmHtPlanDeliveryOrder.getPlanDeliveryOrderId());
                htDetList.add(srmHtPlanDeliveryOrderDet);
            }

        }
        if (StringUtils.isNotEmpty(htDetList))  {
            srmHtPlanDeliveryOrderDetMapper.insertList(htDetList);
        }
        return srmPlanDeliveryOrderMapper.deleteByIds(ids);
    }

    //履历添加
    private void ht(SrmPlanDeliveryOrder record, SrmHtPlanDeliveryOrder srmHtPlanDeliveryOrder){
        if (StringUtils.isNotEmpty(record.getList())) {
            List<SrmHtPlanDeliveryOrderDet> htDetList = new ArrayList<>();
            for (SrmPlanDeliveryOrderDetDto srmPlanDeliveryOrderDetDto : record.getList()) {
                srmPlanDeliveryOrderDetDto.setPlanDeliveryOrderId(record.getPlanDeliveryOrderId());
                srmPlanDeliveryOrderDetDto.setIfCreateAsn((byte) 0);

                SrmHtPlanDeliveryOrderDet srmHtPlanDeliveryOrderDet = new SrmHtPlanDeliveryOrderDet();
                BeanUtils.copyProperties(srmPlanDeliveryOrderDetDto, srmHtPlanDeliveryOrderDet);
                srmHtPlanDeliveryOrderDet.setPlanDeliveryOrderId(srmHtPlanDeliveryOrder.getPlanDeliveryOrderId());
                htDetList.add(srmHtPlanDeliveryOrderDet);
            }
            srmHtPlanDeliveryOrderDetMapper.insertList(htDetList);
            srmPlanDeliveryOrderDetMapper.insertList(record.getList());
        }
    }
}
