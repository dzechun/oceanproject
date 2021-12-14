package com.fantechs.provider.qms.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.qms.PdaIncomingSelectToUseBarcodeDto;
import com.fantechs.common.base.general.dto.qms.PdaIncomingSelectToUseSubmitDto;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerMaterialBarcodeDto;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerMaterialBarcodeReOrderDto;
import com.fantechs.common.base.general.entity.qms.QmsBadnessManage;
import com.fantechs.common.base.general.entity.qms.QmsBadnessManageBarcode;
import com.fantechs.common.base.general.entity.qms.QmsIncomingInspectionOrder;
import com.fantechs.common.base.general.entity.wms.inner.search.SearchWmsInnerMaterialBarcode;
import com.fantechs.common.base.general.entity.wms.inner.search.SearchWmsInnerMaterialBarcodeReOrder;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.wms.inner.InnerFeignApi;
import com.fantechs.provider.qms.mapper.QmsBadnessManageBarcodeMapper;
import com.fantechs.provider.qms.mapper.QmsBadnessManageMapper;
import com.fantechs.provider.qms.mapper.QmsIncomingInspectionOrderMapper;
import com.fantechs.provider.qms.service.QmsBadnessManageService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * Created by leifengzhi on 2021/12/08.
 */
@Service
public class QmsBadnessManageServiceImpl extends BaseService<QmsBadnessManage> implements QmsBadnessManageService {

    @Resource
    private QmsBadnessManageMapper qmsBadnessManageMapper;
    @Resource
    private QmsBadnessManageBarcodeMapper qmsBadnessManageBarcodeMapper;
    @Resource
    private QmsIncomingInspectionOrderMapper qmsIncomingInspectionOrderMapper;
    @Resource
    private InnerFeignApi innerFeignApi;


    @Override
    public List<QmsBadnessManage> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId",user.getOrganizationId());
        return qmsBadnessManageMapper.findList(map);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PdaIncomingSelectToUseBarcodeDto checkBarcode(String barcode,Long incomingInspectionOrderId) {
        PdaIncomingSelectToUseBarcodeDto pdaIncomingSelectToUseBarcodeDto = new PdaIncomingSelectToUseBarcodeDto();
        QmsIncomingInspectionOrder qmsIncomingInspectionOrder = qmsIncomingInspectionOrderMapper.selectByPrimaryKey(incomingInspectionOrderId);

        //校验条码
        Long materialBarcodeId = 0L;
        SearchWmsInnerMaterialBarcodeReOrder searchWmsInnerMaterialBarcodeReOrder = new SearchWmsInnerMaterialBarcodeReOrder();
        searchWmsInnerMaterialBarcodeReOrder.setOrderTypeCode(qmsIncomingInspectionOrder.getSysOrderTypeCode());
        searchWmsInnerMaterialBarcodeReOrder.setOrderId(qmsIncomingInspectionOrder.getIncomingInspectionOrderId());
        List<WmsInnerMaterialBarcodeReOrderDto> materialBarcodeReOrderDtos = innerFeignApi.findList(searchWmsInnerMaterialBarcodeReOrder).getData();
        if(StringUtils.isEmpty(materialBarcodeReOrderDtos)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(),"未找到当前单据对应的条码");
        }
        boolean tag = false;
        for (WmsInnerMaterialBarcodeReOrderDto materialBarcodeReOrderDto : materialBarcodeReOrderDtos){
            if(barcode.equals(materialBarcodeReOrderDto.getBarcode())){
                tag = true;
                materialBarcodeId = materialBarcodeReOrderDto.getMaterialBarcodeId();
                break;
            }
        }
        if(!tag){
            throw new BizErrorException("该条码不属于当前单据");
        }
        pdaIncomingSelectToUseBarcodeDto.setBarcode(barcode);
        pdaIncomingSelectToUseBarcodeDto.setMaterialBarcodeId(materialBarcodeId);

        return pdaIncomingSelectToUseBarcodeDto;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int submit(PdaIncomingSelectToUseSubmitDto pdaIncomingSelectToUseSubmitDto) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        int i = 0;
        Byte pickResult = pdaIncomingSelectToUseSubmitDto.getPickResult();
        BigDecimal orderQty = pdaIncomingSelectToUseSubmitDto.getOrderQty();
        Long incomingInspectionOrderId = pdaIncomingSelectToUseSubmitDto.getIncomingInspectionOrderId();
        List<PdaIncomingSelectToUseBarcodeDto> barcodeDtoList = pdaIncomingSelectToUseSubmitDto.getBarcodeDtoList();
        QmsBadnessManage qmsBadnessManage = new QmsBadnessManage();
        List<QmsBadnessManageBarcode> barcodeList = new LinkedList<>();

        Example example = new Example(QmsBadnessManage.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("incomingInspectionOrderId",incomingInspectionOrderId);
        QmsBadnessManage badnessManage = qmsBadnessManageMapper.selectOneByExample(example);
        if(StringUtils.isNotEmpty(badnessManage)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001.getCode(),"不可重复挑选使用");
        }

        QmsIncomingInspectionOrder qmsIncomingInspectionOrder = qmsIncomingInspectionOrderMapper.selectByPrimaryKey(incomingInspectionOrderId);
        if(qmsIncomingInspectionOrder.getOrderQty().compareTo(new BigDecimal(barcodeDtoList.size())) == -1){
            throw new BizErrorException("条码数量不可大于总数量");
        }

        //该单据的所有条码
        SearchWmsInnerMaterialBarcodeReOrder searchWmsInnerMaterialBarcodeReOrder = new SearchWmsInnerMaterialBarcodeReOrder();
        searchWmsInnerMaterialBarcodeReOrder.setOrderTypeCode(qmsIncomingInspectionOrder.getSysOrderTypeCode());
        searchWmsInnerMaterialBarcodeReOrder.setOrderId(qmsIncomingInspectionOrder.getIncomingInspectionOrderId());
        List<WmsInnerMaterialBarcodeReOrderDto> materialBarcodeReOrderDtos = innerFeignApi.findList(searchWmsInnerMaterialBarcodeReOrder).getData();
        if(StringUtils.isEmpty(materialBarcodeReOrderDtos)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(),"未找到当前单据对应的条码");
        }

        //提交
        qmsBadnessManage.setIncomingInspectionOrderId(incomingInspectionOrderId);
        List<Long> idList = new LinkedList<>();//挑选使用的条码id列表
        if(pickResult == 1){
            qmsBadnessManage.setSpecialReceiveQty(new BigDecimal(barcodeDtoList.size()));
            qmsBadnessManage.setReturnQty(orderQty.subtract(new BigDecimal(barcodeDtoList.size())));
            idList = barcodeDtoList.stream().map(PdaIncomingSelectToUseBarcodeDto::getMaterialBarcodeId).collect(Collectors.toList());
        }else if(pickResult == 0){
            qmsBadnessManage.setSpecialReceiveQty(orderQty.subtract(new BigDecimal(barcodeDtoList.size())));
            qmsBadnessManage.setReturnQty(new BigDecimal(barcodeDtoList.size()));
            for (WmsInnerMaterialBarcodeReOrderDto materialBarcodeReOrderDto : materialBarcodeReOrderDtos){
                boolean isReturn = false;
                for (PdaIncomingSelectToUseBarcodeDto barcodeDto : barcodeDtoList){
                    if(barcodeDto.getMaterialBarcodeId().equals(materialBarcodeReOrderDto.getMaterialBarcodeId())){
                        isReturn = true;
                        break;
                    }
                }
                if(!isReturn){
                    idList.add(materialBarcodeReOrderDto.getMaterialBarcodeId());
                }
            }
        }
        //修改挑选使用的条码的状态
        SearchWmsInnerMaterialBarcode searchWmsInnerMaterialBarcode = new SearchWmsInnerMaterialBarcode();
        searchWmsInnerMaterialBarcode.setMaterialBarcodeIdList(idList);
        List<WmsInnerMaterialBarcodeDto> barcodeDtos = innerFeignApi.findList(searchWmsInnerMaterialBarcode).getData();
        for (WmsInnerMaterialBarcodeDto barcodeDto : barcodeDtos){
            barcodeDto.setInspectionStatus((byte)2);
        }
        ResponseEntity responseEntity = innerFeignApi.batchUpdate(barcodeDtos);
        if(responseEntity.getCode() != 0){
            throw new BizErrorException("返写条码检验状态失败");
        }

        qmsBadnessManage.setOperatorUserId(user.getUserId());
        qmsBadnessManage.setCreateUserId(user.getUserId());
        qmsBadnessManage.setCreateTime(new Date());
        qmsBadnessManage.setModifiedUserId(user.getUserId());
        qmsBadnessManage.setModifiedTime(new Date());
        qmsBadnessManage.setStatus((byte)1);
        qmsBadnessManage.setOrgId(user.getOrganizationId());
        i = qmsBadnessManageMapper.insertUseGeneratedKeys(qmsBadnessManage);

        for (PdaIncomingSelectToUseBarcodeDto barcodeDto : barcodeDtoList){
            QmsBadnessManageBarcode qmsBadnessManageBarcode = new QmsBadnessManageBarcode();
            qmsBadnessManageBarcode.setBadnessManageId(qmsBadnessManage.getBadnessManageId());
            qmsBadnessManageBarcode.setBarcode(barcodeDto.getBarcode());
            qmsBadnessManageBarcode.setPickResult(pickResult);
            qmsBadnessManageBarcode.setCreateUserId(user.getUserId());
            qmsBadnessManageBarcode.setCreateTime(new Date());
            qmsBadnessManageBarcode.setModifiedUserId(user.getUserId());
            qmsBadnessManageBarcode.setModifiedTime(new Date());
            qmsBadnessManageBarcode.setStatus((byte)1);
            qmsBadnessManageBarcode.setOrgId(user.getOrganizationId());
            barcodeList.add(qmsBadnessManageBarcode);
        }
        qmsBadnessManageBarcodeMapper.insertList(barcodeList);

        return i;
    }

}
