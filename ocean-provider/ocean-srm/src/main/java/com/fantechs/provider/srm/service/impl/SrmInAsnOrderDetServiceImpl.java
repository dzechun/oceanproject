package com.fantechs.provider.srm.service.impl;

import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.general.dto.srm.SrmInAsnOrderDetDto;
import com.fantechs.common.base.general.dto.srm.imports.SrmInAsnOrderDetImport;
import com.fantechs.common.base.general.entity.basic.BaseMaterial;
import com.fantechs.common.base.general.entity.basic.BaseWarehouse;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseMaterial;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseWarehouse;
import com.fantechs.common.base.general.entity.srm.SrmInAsnOrderDet;
import com.fantechs.common.base.general.entity.srm.history.SrmInHtAsnOrderDet;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.srm.mapper.SrmInAsnOrderDetMapper;
import com.fantechs.provider.srm.mapper.SrmInHtAsnOrderDetMapper;
import com.fantechs.provider.srm.service.SrmInAsnOrderDetService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 *
 * Created by leifengzhi on 2021/11/25.
 */
@Service
public class SrmInAsnOrderDetServiceImpl extends BaseService<SrmInAsnOrderDet> implements SrmInAsnOrderDetService {

    @Resource
    private SrmInAsnOrderDetMapper srmInAsnOrderDetMapper;
    @Resource
    private SrmInHtAsnOrderDetMapper srmInHtAsnOrderDetMapper;
    @Resource
    private BaseFeignApi baseFeignApi;

    @Override
    public List<SrmInAsnOrderDetDto> findList(Map<String, Object> map) {
        return srmInAsnOrderDetMapper.findList(map);
    }

    @Override
    public Map<String, Object> importExcel(List<SrmInAsnOrderDetImport> srmInAsnOrderDetImports, Long asnOrderId) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        Map<String, Object> resutlMap = new HashMap<>();  //封装操作结果
        int success = 0;  //记录操作成功数
        List<Integer> fail = new ArrayList<>();  //记录操作失败行数
        LinkedList<SrmInAsnOrderDet> list = new LinkedList<>();
        LinkedList<SrmInHtAsnOrderDet> htList = new LinkedList<>();
        for (int i = 0; i < srmInAsnOrderDetImports.size(); i++) {
            SrmInAsnOrderDetImport srmInAsnOrderDetImport = srmInAsnOrderDetImports.get(i);

            //判断非空
            String purchaseOrderCode = srmInAsnOrderDetImport.getPurchaseOrderCode();
            String materialCode = srmInAsnOrderDetImport.getMaterialCode();
            String warehouseName = srmInAsnOrderDetImport.getWarehouseName();
            if (StringUtils.isEmpty(
                    purchaseOrderCode,materialCode,warehouseName
            )){
                //  throw new BizErrorException("添加失败，采购单号、物料号、SN码和生产日期不能为空,"+"错误行数为:"+(i+2));
                fail.add(i+2);
                continue;
            }

            //判断集合中是否已经存在同样的数据
          /*  boolean tag = false;
            if (StringUtils.isNotEmpty(list)){
                for (SrmInAsnOrderDetBarcode srmInAsnOrderDetBarcode : list) {
                    if (srmInAsnOrderDetBarcode.getAsnOrderDetId().equals(purchaseOrderCode)){
                        tag = true;
                    }
                }
            }
            if (tag){
                fail.add(i+2);
                continue;
            }*/

            SrmInAsnOrderDet srmInAsnOrderDet = new SrmInAsnOrderDet();
            BeanUtils.copyProperties(srmInAsnOrderDetImport, srmInAsnOrderDet);

            SearchBaseMaterial searchBaseMaterial = new SearchBaseMaterial();
            searchBaseMaterial.setMaterialCode(srmInAsnOrderDetImport.getMaterialCode());
            searchBaseMaterial.setOrgId(user.getOrganizationId());
            List<BaseMaterial> baseMaterials = baseFeignApi.findList(searchBaseMaterial).getData();
            if (StringUtils.isEmpty(baseMaterials)){
                //   if(StringUtils.isEmpty(baseMaterials)) throw new BizErrorException("未查询到对应的物料信息");
                fail.add(i+2);
                continue;
            }
            SearchBaseWarehouse searchBaseWarehouse = new SearchBaseWarehouse();
            searchBaseWarehouse.setWarehouseName(srmInAsnOrderDetImport.getWarehouseName());
            searchBaseWarehouse.setOrgId(user.getOrganizationId());
            List<BaseWarehouse> baseWarehouses = baseFeignApi.findList(searchBaseWarehouse).getData();
            if (StringUtils.isEmpty(baseWarehouses)){
                //   if(StringUtils.isEmpty(baseMaterials)) throw new BizErrorException("未查询到对应的仓库信息");
                fail.add(i+2);
                continue;
            }
            if(StringUtils.isEmpty(srmInAsnOrderDetImport.getDeliveryQty()))
                srmInAsnOrderDet.setDeliveryQty(BigDecimal.ZERO);
            srmInAsnOrderDet.setAsnOrderId(asnOrderId);
            srmInAsnOrderDet.setMaterialId(baseMaterials.get(0).getMaterialId());
            srmInAsnOrderDet.setWarehouseId(baseWarehouses.get(0).getWarehouseId());
            srmInAsnOrderDet.setCreateTime(new Date());
            srmInAsnOrderDet.setCreateUserId(user.getUserId());
            srmInAsnOrderDet.setModifiedTime(new Date());
            srmInAsnOrderDet.setModifiedUserId(user.getUserId());
            srmInAsnOrderDet.setStatus((byte)1);
            srmInAsnOrderDet.setOrgId(user.getOrganizationId());
            list.add(srmInAsnOrderDet);
        }

        if (StringUtils.isNotEmpty(list)){
            success = srmInAsnOrderDetMapper.insertList(list);
        }

        for (SrmInAsnOrderDet srmInAsnOrderDet : list) {
            SrmInHtAsnOrderDet srmInHtAsnOrderDet = new SrmInHtAsnOrderDet();
            BeanUtils.copyProperties(srmInAsnOrderDet, srmInHtAsnOrderDet);
            htList.add(srmInHtAsnOrderDet);
        }
        if (StringUtils.isNotEmpty(htList)){
            srmInHtAsnOrderDetMapper.insertList(htList);
        }
        resutlMap.put("操作成功总数",success);
        resutlMap.put("操作失败行数",fail);
        return resutlMap;
    }

}
