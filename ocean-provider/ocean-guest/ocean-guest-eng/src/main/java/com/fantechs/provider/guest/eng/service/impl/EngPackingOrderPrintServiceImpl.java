package com.fantechs.provider.guest.eng.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.eng.EngPackingOrderPrintDto;
import com.fantechs.common.base.general.dto.eng.EngPackingOrderPrintParam;
import com.fantechs.common.base.general.dto.mes.sfc.PrintDto;
import com.fantechs.common.base.general.dto.mes.sfc.PrintModel;
import com.fantechs.common.base.general.entity.basic.BaseSupplierReUser;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseSupplierReUser;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.api.mes.sfc.SFCFeignApi;
import com.fantechs.provider.guest.eng.mapper.EngPackingOrderPrintMapper;
import com.fantechs.provider.guest.eng.service.EngPackingOrderPrintService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author mr.lei
 * @Date 2021/9/4
 */
@Service
public class EngPackingOrderPrintServiceImpl implements EngPackingOrderPrintService {
    @Resource
    private EngPackingOrderPrintMapper engPackingOrderPrintMapper;
    @Resource
    private SFCFeignApi sfcFeignApi;
    @Resource
    private BaseFeignApi baseFeignApi;

    @Override
    public List<EngPackingOrderPrintDto> findList(Map<String, Object> map) {
        SysUser sysUser = currentUser();
        map.put("orgId", sysUser.getOrganizationId());
        SearchBaseSupplierReUser searchBaseSupplierReUser = new SearchBaseSupplierReUser();
        searchBaseSupplierReUser.setUserId(sysUser.getUserId());
        ResponseEntity<List<BaseSupplierReUser>> list = baseFeignApi.findList(searchBaseSupplierReUser);
        if (StringUtils.isNotEmpty(list.getData())){
            map.put("supplierIdList", list.getData());
        }
        return engPackingOrderPrintMapper.findList(map);
    }

    @Override
    public int print(EngPackingOrderPrintParam engPackingOrderPrintParam) {
        SysUser sysUser = currentUser();
        SearchBaseSupplierReUser searchBaseSupplierReUser = new SearchBaseSupplierReUser();
        searchBaseSupplierReUser.setUserId(sysUser.getUserId());
        ResponseEntity<List<BaseSupplierReUser>> lists = baseFeignApi.findList(searchBaseSupplierReUser);
        if(StringUtils.isEmpty(engPackingOrderPrintParam.getPrintName())){
            throw new BizErrorException("请输入打印机名称");
        }
        PrintDto printDto = new PrintDto();
        printDto.setPrintName(engPackingOrderPrintParam.getPrintName());
        printDto.setLabelName(engPackingOrderPrintParam.getPrintMode());
        printDto.setLabelVersion("0.0.1");
        List<PrintModel> list = new ArrayList<>();
        for (Long id : engPackingOrderPrintParam.getIds()) {
            //获取标签信息
            PrintModel printModel = engPackingOrderPrintMapper.ViewPrint(ControllerUtil.dynamicCondition("type",engPackingOrderPrintParam.getType(),"id",id));
            if(StringUtils.isEmpty(printModel)){
                throw new BizErrorException("数据获取失败");
            }
            printModel.setSize(engPackingOrderPrintParam.getSize());
            list.add(printModel);
        }
        printDto.setPrintModelList(list);
        ResponseEntity responseEntity;
        if (StringUtils.isNotEmpty(lists.getData())){
             responseEntity = sfcFeignApi.QUEUEprint(printDto,lists.getData().get(0).getSupplierId().toString());
        }else {
            responseEntity = sfcFeignApi.print(printDto);
        }
        if(responseEntity.getCode()!=0){
            throw new BizErrorException(responseEntity.getCode(),responseEntity.getMessage());
        }
        return 1;
    }

    private SysUser currentUser(){
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(sysUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        return sysUser;
    }
}
