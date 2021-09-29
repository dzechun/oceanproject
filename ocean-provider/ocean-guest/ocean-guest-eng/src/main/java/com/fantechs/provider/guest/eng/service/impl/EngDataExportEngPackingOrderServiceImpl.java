package com.fantechs.provider.guest.eng.service.impl;

import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.general.dto.eng.EngContractQtyOrderAndPurOrderDto;
import com.fantechs.common.base.general.dto.restapi.EngDataExportEngPackingOrderDto;
import com.fantechs.common.base.general.entity.eng.EngContractQtyOrder;
import com.fantechs.common.base.general.entity.eng.EngPackingOrder;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.JsonUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.guest.fivering.FiveringFeignApi;
import com.fantechs.provider.guest.eng.mapper.EngContractQtyOrderAndPurOrderMapper;
import com.fantechs.provider.guest.eng.mapper.EngContractQtyOrderMapper;
import com.fantechs.provider.guest.eng.mapper.EngDataExportEngPackingOrderMapper;
import com.fantechs.provider.guest.eng.service.EngDataExportEngPackingOrderService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/09/01.
 */
@Service
public class EngDataExportEngPackingOrderServiceImpl extends BaseService<EngDataExportEngPackingOrderDto> implements EngDataExportEngPackingOrderService {

    @Resource
    private EngDataExportEngPackingOrderMapper engDataExportEngPackingOrderMapper;
    @Resource
    private EngContractQtyOrderMapper engContractQtyOrderMapper;
    @Resource
    FiveringFeignApi fiveringFeignApi;
    @Resource
    private EngContractQtyOrderAndPurOrderMapper engContractQtyOrderAndPurOrderMapper;

    @Override
    public List<EngDataExportEngPackingOrderDto> findExportData(Map<String, Object> map) {
        return engDataExportEngPackingOrderMapper.findExportData(map);
    }

    @Override
    public String writePackingLists(EngPackingOrder engPackingOrder) {
        //获取当前操作用户
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        String jsonVoiceArray="";
        String projectID="3919";
        Map<String, Object> map=new HashMap<>();
        map.put("packingOrderId",engPackingOrder.getPackingOrderId());
        List<EngDataExportEngPackingOrderDto> listDto=findExportData(map);
        for (EngDataExportEngPackingOrderDto item : listDto) {
            //设置合同号表头ID
            Example example = new Example(EngContractQtyOrder.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("contractCode",item.getContractCode());
            List<EngContractQtyOrder> listOrder=engContractQtyOrderMapper.selectByExample(example);
            if(listOrder.size()>0){
                item.setOption2(listOrder.get(0).getOption2());
            }

            //设置材料用途
            Map<String, Object> mapPur=new HashMap<>();
            mapPur.put("contractCode",item.getContractCode());
            mapPur.put("purchaseReqOrderCode",item.getPurchaseReqOrderCode());
            mapPur.put("deviceCode",item.getDeviceCode());
            mapPur.put("dominantTermCode",item.getDominantTermCode());
            List<EngContractQtyOrderAndPurOrderDto> listEngOrder=engContractQtyOrderAndPurOrderMapper.findList(mapPur);
            if(listEngOrder.size()>0){
                item.setMaterialPurpose(listEngOrder.get(0).getMaterialPurpose());
            }

            //设置登记人
            if(StringUtils.isNotEmpty(user)) {
                item.setRecordUser(user.getUserName());
            }
        }

        jsonVoiceArray= JsonUtils.objectToJson(listDto);
        String s0=jsonVoiceArray.replaceAll("option2","PSGUID");
        String s1=s0.replaceAll("materialCode","材料编码");
        String s2=s1.replaceAll("locationNum","位号");
        String s3=s2.replaceAll("designQty","设计量");
        String s4=s3.replaceAll("surplusQty","余量");
        String s5=s4.replaceAll("purchaseReqQty","请购量");
        String s6=s5.replaceAll("chQty","采购量");
        String s7=s6.replaceAll("dominantTermCode","主项号");
        String s8=s7.replaceAll("deviceCode","装置号");
        String s9=s8.replaceAll("materialPurpose","材料用途");
        String s10=s9.replaceAll("remark","备注");
        String s11=s10.replaceAll("recordTime","登记时间");
        String s12=s11.replaceAll("recordUser","登记人");
        String s13=s12.replaceAll("materialName","货物名称");
        String s14=s13.replaceAll("spec","规格");
        String s15=s14.replaceAll("unitName","单位");

        ResponseEntity<String>  responseEntityResult=fiveringFeignApi.writePackingLists(s15,projectID);

        return responseEntityResult.getData();
    }
}
