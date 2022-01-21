package com.fantechs.provider.wms.inner.service.impl;

import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerInventoryDetDto;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerMaterialBarcodeDto;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerInventoryDet;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerMaterialBarcode;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.wms.inner.mapper.WmsInnerInventoryDetMapper;
import com.fantechs.provider.wms.inner.mapper.WmsInnerMaterialBarcodeMapper;
import com.fantechs.provider.wms.inner.service.WmsInnerInventoryDetService;
import com.fantechs.provider.wms.inner.service.WmsInnerMaterialBarcodeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 *
 * Created by Mr.Lei on 2021/06/02.
 */
@Service
public class WmsInnerInventoryDetServiceImpl extends BaseService<WmsInnerInventoryDet> implements WmsInnerInventoryDetService {

    @Resource
    private WmsInnerInventoryDetMapper wmsInnerInventoryDetMapper;
    @Resource
    private WmsInnerMaterialBarcodeService wmsInnerMaterialBarcodeService;
    @Resource
    private WmsInnerMaterialBarcodeMapper wmsInnerMaterialBarcodeMapper;

    @Override
    public List<WmsInnerInventoryDetDto> findList(Map<String, Object> map) {
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId",sysUser.getOrganizationId());
        return wmsInnerInventoryDetMapper.findList(map);
    }

    /**
     * 加库存明细
     * @param wmsInnerInventoryDets
     * @return
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int add(List<WmsInnerInventoryDet> wmsInnerInventoryDets) {
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        for (WmsInnerInventoryDet wmsInnerInventoryDet : wmsInnerInventoryDets) {
            wmsInnerInventoryDet.setCreateTime(new Date());
            wmsInnerInventoryDet.setCreateUserId(sysUser.getUserId());
            wmsInnerInventoryDet.setModifiedTime(new Date());
            wmsInnerInventoryDet.setModifiedUserId(sysUser.getUserId());
            wmsInnerInventoryDet.setOrgId(sysUser.getOrganizationId());
        }
        return wmsInnerInventoryDetMapper.insertList(wmsInnerInventoryDets);
    }

    /**
     * 减库存明细
     * @param wmsInnerInventoryDetDto
     * @return
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    @LcnTransaction
    public int subtract(WmsInnerInventoryDetDto wmsInnerInventoryDetDto) {
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        Example example = new Example(WmsInnerInventoryDet.class);
        Example.Criteria criteria = example.createCriteria();
        if(StringUtils.isEmpty(wmsInnerInventoryDetDto.getMaterialQty()) || wmsInnerInventoryDetDto.getMaterialQty().compareTo(BigDecimal.ZERO)<1){
            throw new BizErrorException("出库数量错误");
        }
        if(StringUtils.isNotEmpty(wmsInnerInventoryDetDto.getMaterialBarcodeId())){
            criteria.andEqualTo("materialBarcodeId",wmsInnerInventoryDetDto.getMaterialBarcodeId());
        }
        if(StringUtils.isNotEmpty(wmsInnerInventoryDetDto.getStorageId())){
            criteria.andEqualTo("storageId",wmsInnerInventoryDetDto.getStorageId());
        }
        if(StringUtils.isNotEmpty(wmsInnerInventoryDetDto.getMaterialId())){
            criteria.andEqualTo("materialId",wmsInnerInventoryDetDto.getMaterialId());
        }
        if(StringUtils.isNotEmpty(wmsInnerInventoryDetDto.getAsnCode())){
            criteria.andEqualTo("asnCode",wmsInnerInventoryDetDto.getAsnCode());
        }
        if(StringUtils.isNotEmpty(wmsInnerInventoryDetDto.getDeliveryOrderCode())){
            criteria.andEqualTo("deliveryOrderCode",wmsInnerInventoryDetDto.getDeliveryOrderCode());
        }
        criteria.andEqualTo("orgId",sysUser.getOrganizationId());
        List<WmsInnerInventoryDet> wms = wmsInnerInventoryDetMapper.selectByExample(example);
        BigDecimal qty = wmsInnerInventoryDetDto.getMaterialQty();
        int num=0;
        for (WmsInnerInventoryDet wm : wms) {
            if(qty.compareTo(BigDecimal.ZERO)==0){
                break;
            }
                if(qty.compareTo(wmsInnerInventoryDetDto.getMaterialQty())==1){
                    if(qty.compareTo(wmsInnerInventoryDetDto.getMaterialQty())==1){
                        //删除记录
                        wmsInnerInventoryDetMapper.deleteByPrimaryKey(wmsInnerInventoryDetDto.getInventoryDetId());
                    }else{
                        wmsInnerInventoryDetDto.setMaterialQty(wmsInnerInventoryDetDto.getMaterialQty().subtract(qty));
                    }
                    qty.subtract(wmsInnerInventoryDetDto.getMaterialQty());
                }
                num+=wmsInnerInventoryDetMapper.updateByPrimaryKeySelective(wm);
        }
        return num;
    }

    @Override
    public WmsInnerInventoryDet findByOne(String barCode){
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        Example example = new Example(WmsInnerInventoryDet.class);
        example.createCriteria().andEqualTo("barcode",barCode).andEqualTo("orgId",sysUser.getOrganizationId());
        List<WmsInnerInventoryDet> wmsInnerInventoryDet = wmsInnerInventoryDetMapper.selectByExample(example);
        if(wmsInnerInventoryDet.size()>0){
            return wmsInnerInventoryDet.get(0);
        }
        return null;
    }

    /**
     * 查询对应条码的库存(箱码只查询到箱码，栈板码只查询到栈板码，不查询下一级)
     * @param codes
     * @return
     */
    @Override
    public List<WmsInnerInventoryDetDto> findListByBarCode(List<String> codes) {
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        List<WmsInnerMaterialBarcodeDto> list = wmsInnerMaterialBarcodeService.findListByCode(codes);
        List<WmsInnerInventoryDetDto> wmsInnerInventoryDetDtos = new ArrayList<>();
        List<WmsInnerMaterialBarcodeDto> wmsInnerMaterialBarcodeDtoList = new ArrayList<>();
        Map m = new HashMap();
        Map map = new HashMap();
        if(StringUtils.isNotEmpty(list)){
            for(String code : codes){
                for(WmsInnerMaterialBarcodeDto dto : list) {
                    if(StringUtils.isNotEmpty(dto.getIfScan()) && dto.getIfScan() == 1) throw new BizErrorException(ErrorCodeEnum.OPT20012001,"该条码或该条码下的其他条码已经被扫描");
                    m.clear();
                    m.put("materialBarcodeId",dto.getMaterialBarcodeId());
                    List<WmsInnerInventoryDetDto> det = wmsInnerInventoryDetMapper.findList(m);
                    if(StringUtils.isEmpty(det))  throw new BizErrorException("未在库存中查询到该条码");

                    if (code.equals(dto.getBarcode())) {
                        WmsInnerInventoryDetDto wmsInnerInventoryDetDto = det.get(0);
                        wmsInnerInventoryDetDto.setMaterialTotalQty(wmsInnerInventoryDetDto.getMaterialQty());
                        wmsInnerInventoryDetDtos.add(wmsInnerInventoryDetDto);
                    }else if(code.equals(dto.getColorBoxCode())){
                        //彩盒码,箱码为空
                        if(StringUtils.isEmpty(dto.getBarcode())) {
                            map.put("colorBoxCode",dto.getBarcode());
                            Integer i = wmsInnerInventoryDetMapper.materialQty(map);
                            WmsInnerInventoryDetDto wmsInnerInventoryDetDto = det.get(0);
                            wmsInnerInventoryDetDto.setMaterialTotalQty(new BigDecimal(i).subtract(wmsInnerInventoryDetDto.getMaterialQty()));
                            wmsInnerInventoryDetDtos.add(wmsInnerInventoryDetDto);
                        }

                    }else if(code.equals(dto.getCartonCode())){
                        //箱码,箱码为空
                        if(StringUtils.isEmpty(dto.getBarcode())  &&  StringUtils.isEmpty(dto.getColorBoxCode())) {
                            map.put("cartonCode",dto.getBarcode());
                            Integer i = wmsInnerInventoryDetMapper.materialQty(map);
                            WmsInnerInventoryDetDto wmsInnerInventoryDetDto = det.get(0);
                            wmsInnerInventoryDetDto.setMaterialTotalQty(new BigDecimal(i).subtract(wmsInnerInventoryDetDto.getMaterialQty()));
                            wmsInnerInventoryDetDtos.add(wmsInnerInventoryDetDto);
                        }
                    }else if(code.equals(dto.getPalletCode())){
                        //栈板码,箱码为空
                        if(StringUtils.isEmpty(dto.getBarcode()) &&  StringUtils.isEmpty(dto.getColorBoxCode())  && StringUtils.isEmpty(dto.getCartonCode())) {
                            map.put("palletCode",dto.getPalletCode());
                            Integer i = wmsInnerInventoryDetMapper.materialQty(map);
                            WmsInnerInventoryDetDto wmsInnerInventoryDetDto = det.get(0);
                            wmsInnerInventoryDetDto.setMaterialTotalQty(new BigDecimal(i).subtract(wmsInnerInventoryDetDto.getMaterialQty()));
                            wmsInnerInventoryDetDtos.add(wmsInnerInventoryDetDto);
                        }
                    }
                    dto.setIfScan((byte)1);
                    dto.setModifiedUserId(sysUser.getUserId());
                    dto.setModifiedTime(new Date());
                    wmsInnerMaterialBarcodeDtoList.add(dto);
                }
            }

        }
        if(StringUtils.isEmpty(wmsInnerInventoryDetDtos))
            throw new BizErrorException("未在库存明细中查询到相应的条码信息");
        //增加扫描条码标记
        if(StringUtils.isEmpty(wmsInnerMaterialBarcodeDtoList))
            wmsInnerMaterialBarcodeService.batchUpdate(wmsInnerMaterialBarcodeDtoList);
        return wmsInnerInventoryDetDtos;
    }


    @Override
    public int isAllOutInventory(List<WmsInnerInventoryDetDto> wmsInnerInventoryDetDtos) {
        int i= 1;
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        List<WmsInnerInventoryDetDto> palletCodeDetDtos = new ArrayList<>();
        List<WmsInnerInventoryDetDto> cartonCodeDetDtos = new ArrayList<>();
        List<WmsInnerInventoryDetDto> colorBoxCodeDetDtos = new ArrayList<>();
        for(WmsInnerInventoryDetDto dto : wmsInnerInventoryDetDtos){
            if(StringUtils.isNotEmpty(dto.getPalletCode()))
                palletCodeDetDtos.add(dto);
            if(StringUtils.isNotEmpty(dto.getCartonCode()) && StringUtils.isEmpty(dto.getPalletCode()))
                cartonCodeDetDtos.add(dto);
            if(StringUtils.isNotEmpty(dto.getColorBoxCode()) && StringUtils.isEmpty(dto.getCartonCode()) && StringUtils.isEmpty(dto.getPalletCode()))
                colorBoxCodeDetDtos.add(dto);
        }
        Map<String, List<WmsInnerInventoryDetDto>> palletCodes =
                palletCodeDetDtos.stream().collect(Collectors.groupingBy(WmsInnerInventoryDetDto::getPalletCode));
        Set<String> palletCodeList = palletCodes.keySet();
        Example example = new Example(WmsInnerMaterialBarcode.class);
        for (String palletCode : palletCodeList) {
            example.clear();
            example.createCriteria()
                    .andEqualTo("palletCode",palletCode)
                    .andEqualTo("orgId",sysUser.getOrganizationId());
           int palletNum = wmsInnerMaterialBarcodeMapper.selectCountByExample(example);
           if(palletCodes.get(palletCode).size() + 1 != palletNum)
               throw new BizErrorException("栈板码对应的条码数量错误，栈板码为："+palletCode);
        }

        Map<String, List<WmsInnerInventoryDetDto>> cartonCodes =
                cartonCodeDetDtos.stream().collect(Collectors.groupingBy(WmsInnerInventoryDetDto::getCartonCode));
        Set<String> cartonCodeList = cartonCodes.keySet();
        for (String cartonCode : cartonCodeList) {
            example.clear();
            example.createCriteria()
                    .andEqualTo("cartonCode",cartonCode)
                    .andEqualTo("orgId",sysUser.getOrganizationId());
            int palletNum = wmsInnerMaterialBarcodeMapper.selectCountByExample(example);
            if(cartonCodes.get(cartonCode).size() + 1 != palletNum)
                throw new BizErrorException("箱码对应的条码数量错误，箱码为："+cartonCode);
        }

        Map<String, List<WmsInnerInventoryDetDto>> colorBoxCodes =
                colorBoxCodeDetDtos.stream().collect(Collectors.groupingBy(WmsInnerInventoryDetDto::getColorBoxCode));
        Set<String> colorBoxCodeList = colorBoxCodes.keySet();
        for (String colorBoxCode : colorBoxCodeList) {
            example.clear();
            example.createCriteria()
                    .andEqualTo("colorBoxCode",colorBoxCode)
                    .andEqualTo("orgId",sysUser.getOrganizationId());
            int palletNum = wmsInnerMaterialBarcodeMapper.selectCountByExample(example);
            if(colorBoxCodes.get(colorBoxCode).size() + 1 != palletNum)
                throw new BizErrorException("彩盒码对应的条码数量错误，彩盒码为："+colorBoxCode);
        }
        return i;
    }

}
