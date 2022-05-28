package com.fantechs.provider.srm.service.impl;

import com.fantechs.common.base.entity.security.SysImportAndExportLog;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.general.dto.srm.SrmInAsnOrderDetBarcodeDto;
import com.fantechs.common.base.general.dto.srm.imports.SrmInAsnOrderDetBarcodeImport;
import com.fantechs.common.base.general.entity.basic.BaseMaterial;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseMaterial;
import com.fantechs.common.base.general.entity.srm.SrmInAsnOrderDet;
import com.fantechs.common.base.general.entity.srm.SrmInAsnOrderDetBarcode;
import com.fantechs.common.base.general.entity.srm.history.SrmInHtAsnOrderDetBarcode;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.api.auth.service.AuthFeignApi;
import com.fantechs.provider.srm.mapper.SrmInAsnOrderDetBarcodeMapper;
import com.fantechs.provider.srm.mapper.SrmInAsnOrderDetMapper;
import com.fantechs.provider.srm.mapper.SrmInHtAsnOrderDetBarcodeMapper;
import com.fantechs.provider.srm.service.SrmInAsnOrderDetBarcodeService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 *
 * Created by leifengzhi on 2021/11/25.
 */
@Service
public class SrmInAsnOrderDetBarcodeServiceImpl extends BaseService<SrmInAsnOrderDetBarcode> implements SrmInAsnOrderDetBarcodeService {

    @Resource
    private SrmInAsnOrderDetBarcodeMapper srmInAsnOrderDetBarcodeMapper;
    @Resource
    private SrmInHtAsnOrderDetBarcodeMapper srmInHtAsnOrderDetBarcodeMapper;
    @Resource
    private SrmInAsnOrderDetMapper smInAsnOrderDetMapper;
    @Resource
    private BaseFeignApi baseFeignApi;
    @Resource
    private AuthFeignApi securityFeignApi;

    @Override
    public List<SrmInAsnOrderDetBarcodeDto> findList(Map<String, Object> map) {
        return srmInAsnOrderDetBarcodeMapper.findList(map);
    }

    @Override
    public Map<String, Object> importExcel(List<SrmInAsnOrderDetBarcodeImport> srmInAsnOrderDetBarcodeImports,Long asnOrderId) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        Map<String, Object> resutlMap = new HashMap<>();  //封装操作结果
        int success = 0;  //记录操作成功数
        List<Integer> fail = new ArrayList<>();  //记录操作失败行数
        List<Map<Integer,String>> failMap = new ArrayList<>();  //记录操作失败行数
        LinkedList<SrmInAsnOrderDetBarcode> list = new LinkedList<>();
        LinkedList<SrmInHtAsnOrderDetBarcode> htList = new LinkedList<>();
        for (int i = 0; i < srmInAsnOrderDetBarcodeImports.size(); i++) {
            SrmInAsnOrderDetBarcodeImport srmInAsnOrderDetBarcodeImport = srmInAsnOrderDetBarcodeImports.get(i);

            //判断非空
            String purchaseOrderCode = srmInAsnOrderDetBarcodeImport.getPurchaseOrderCode();
            String materialCode = srmInAsnOrderDetBarcodeImport.getMaterialCode();
            String barcode = srmInAsnOrderDetBarcodeImport.getBarcode();
            Date productionDate = srmInAsnOrderDetBarcodeImport.getProductionDate();
            if (StringUtils.isEmpty(
                    purchaseOrderCode,materialCode,barcode,productionDate
            )){
              //  throw new BizErrorException("添加失败，采购单号、物料号、SN码和生产日期不能为空,"+"错误行数为:"+(i+2));
                Map map = new HashMap();
                map.put(i+2,"采购单号、物料号、SN码和生产日期不能为空");
                failMap.add(map);
                fail.add(i+2);
                continue;
            }

            SrmInAsnOrderDetBarcode srmInAsnOrderDetBarcode = new SrmInAsnOrderDetBarcode();
            BeanUtils.copyProperties(srmInAsnOrderDetBarcodeImport, srmInAsnOrderDetBarcode);

            SearchBaseMaterial searchBaseMaterial = new SearchBaseMaterial();
            searchBaseMaterial.setMaterialCode(srmInAsnOrderDetBarcodeImport.getMaterialCode());
            searchBaseMaterial.setOrgId(user.getOrganizationId());
            List<BaseMaterial> baseMaterials = baseFeignApi.findList(searchBaseMaterial).getData();
            if (StringUtils.isEmpty(baseMaterials)){
                //   if(StringUtils.isEmpty(baseMaterials)) throw new BizErrorException("未查询到对应的物料信息");
                Map map = new HashMap();
                map.put(i+2,"未查询到对应的物料信息");
                failMap.add(map);
                fail.add(i+2);
                continue;
            }

            Example example = new Example(SrmInAsnOrderDet.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("asnOrderId",asnOrderId);
            criteria.andEqualTo("materialId",baseMaterials.get(0).getMaterialId());
            List<SrmInAsnOrderDet> srmInAsnOrderDets = smInAsnOrderDetMapper.selectByExample(example);

            if(StringUtils.isNotEmpty(srmInAsnOrderDets))
                srmInAsnOrderDetBarcode.setAsnOrderDetId(srmInAsnOrderDets.get(0).getAsnOrderDetId());
            if(StringUtils.isEmpty(srmInAsnOrderDetBarcodeImport.getQty()))
                srmInAsnOrderDetBarcode.setQty(BigDecimal.ZERO);
            srmInAsnOrderDetBarcode.setAsnOrderId(asnOrderId);
            srmInAsnOrderDetBarcode.setCreateTime(new Date());
            srmInAsnOrderDetBarcode.setCreateUserId(user.getUserId());
            srmInAsnOrderDetBarcode.setModifiedTime(new Date());
            srmInAsnOrderDetBarcode.setModifiedUserId(user.getUserId());
            srmInAsnOrderDetBarcode.setStatus((byte)1);
            srmInAsnOrderDetBarcode.setOrgId(user.getOrganizationId());
            list.add(srmInAsnOrderDetBarcode);
        }

        if (StringUtils.isNotEmpty(list)){
            success = srmInAsnOrderDetBarcodeMapper.insertList(list);
        }

        for (SrmInAsnOrderDetBarcode srmInAsnOrderDetBarcode : list) {
            SrmInHtAsnOrderDetBarcode srmInHtAsnOrderDetBarcode = new SrmInHtAsnOrderDetBarcode();
            BeanUtils.copyProperties(srmInAsnOrderDetBarcode, srmInHtAsnOrderDetBarcode);
            htList.add(srmInHtAsnOrderDetBarcode);
        }
        if (StringUtils.isNotEmpty(htList)){
            srmInHtAsnOrderDetBarcodeMapper.insertList(htList);
        }
        SysImportAndExportLog log = new SysImportAndExportLog();
        log.setSucceedCount(srmInAsnOrderDetBarcodeImports.size() - fail.size());
        log.setFailCount(fail.size());
        log.setFailInfo(failMap.toString());
        log.setOperatorUserId(user.getUserId());
        log.setTotalCount(srmInAsnOrderDetBarcodeImports.size());
        log.setType((byte)1);
        securityFeignApi.add(log);
        resutlMap.put("操作成功总数",success);
        resutlMap.put("操作失败行数",fail);
        return resutlMap;
    }


}
