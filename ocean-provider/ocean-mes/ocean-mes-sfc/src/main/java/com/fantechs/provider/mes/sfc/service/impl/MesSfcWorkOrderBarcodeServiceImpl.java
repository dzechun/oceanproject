package com.fantechs.provider.mes.sfc.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.mes.pm.MesPmWorkOrderDto;
import com.fantechs.common.base.general.dto.mes.pm.search.SearchMesPmWorkOrder;
import com.fantechs.common.base.general.dto.mes.sfc.PrintDto;
import com.fantechs.common.base.general.dto.mes.sfc.PrintModel;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseBarcodeRuleSpec;
import com.fantechs.common.base.general.dto.mes.sfc.LabelRuteDto;
import com.fantechs.common.base.general.dto.mes.sfc.MesSfcWorkOrderBarcodeDto;
import com.fantechs.common.base.general.entity.basic.BaseLabelCategory;
import com.fantechs.common.base.general.entity.basic.BaseBarcodeRuleSpec;
import com.fantechs.common.base.general.entity.mes.sfc.MesSfcBarcodeProcess;
import com.fantechs.common.base.general.entity.mes.sfc.MesSfcWorkOrderBarcode;
import com.fantechs.common.base.general.entity.mes.sfc.SearchMesSfcWorkOrderBarcode;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.mes.pm.PMFeignApi;
import com.fantechs.provider.mes.sfc.mapper.MesSfcBarcodeProcessMapper;
import com.fantechs.provider.mes.sfc.util.RabbitProducer;
import com.fantechs.provider.mes.sfc.mapper.MesSfcWorkOrderBarcodeMapper;
import com.fantechs.provider.mes.sfc.service.MesSfcWorkOrderBarcodeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * Created by Mr.Lei on 2021/04/07.
 */
@Service
public class MesSfcWorkOrderBarcodeServiceImpl extends BaseService<MesSfcWorkOrderBarcode> implements MesSfcWorkOrderBarcodeService {

    @Resource
    private MesSfcWorkOrderBarcodeMapper mesSfcWorkOrderBarcodeMapper;
    @Resource
    private PMFeignApi pmFeignApi;
    @Resource
    private RabbitProducer rabbitProducer;
    @Resource
    private MesSfcBarcodeProcessMapper mesSfcBarcodeProcessMapper;

    @Override
    public List<MesSfcWorkOrderBarcodeDto> findList(SearchMesSfcWorkOrderBarcode searchMesSfcWorkOrderBarcode) {
        return mesSfcWorkOrderBarcodeMapper.findList(searchMesSfcWorkOrderBarcode);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int print(String ids) {
        String[] arrId = ids.split(",");
        for (String s : arrId) {
            //查询模版信息
            MesSfcWorkOrderBarcode mesSfcWorkOrderBarcode = mesSfcWorkOrderBarcodeMapper.selectByPrimaryKey(s);
            LabelRuteDto labelRuteDto = null;
            PrintModel printModel = null;
            switch (mesSfcWorkOrderBarcode.getBarcodeType()){
                case 2:
                    //获取工单类别模版
                    labelRuteDto = mesSfcWorkOrderBarcodeMapper.findRule("01",mesSfcWorkOrderBarcode.getWorkOrderId());
                    if(StringUtils.isEmpty(labelRuteDto)&&StringUtils.isEmpty(labelRuteDto.getLabelName())){
                        //获取默认模版
                        labelRuteDto = mesSfcWorkOrderBarcodeMapper.DefaultLabel("01");
                    }
                    printModel = mesSfcWorkOrderBarcodeMapper.findPrintModel("view_workOrder");
                    break;
                case 4:
                    //获取销售类别模版
                    labelRuteDto = mesSfcWorkOrderBarcodeMapper.findRule("02",mesSfcWorkOrderBarcode.getWorkOrderId());
                    if(StringUtils.isEmpty(labelRuteDto)&&StringUtils.isEmpty(labelRuteDto.getLabelName())){
                        //获取默认模版
                        labelRuteDto = mesSfcWorkOrderBarcodeMapper.DefaultLabel("02");
                    }
                    printModel = mesSfcWorkOrderBarcodeMapper.findPrintModel("view_order");
                    break;
            }
            if(labelRuteDto.getBarcodeType()==(byte)0 &&mesSfcWorkOrderBarcode.getBarcodeType()==(byte)2){
                //生成条码过站记录
                MesSfcBarcodeProcess mesSfcBarcodeProcess = new MesSfcBarcodeProcess();
                mesSfcBarcodeProcess.setWorkOrderId(mesSfcWorkOrderBarcode.getWorkOrderId());
                mesSfcBarcodeProcess.setWorkOrderCode(mesSfcWorkOrderBarcode.getWorkOrderCode());
                mesSfcBarcodeProcess.setBarcodeType((byte)2);
                mesSfcBarcodeProcess.setBarcode(mesSfcWorkOrderBarcode.getBarcode());
                SearchMesPmWorkOrder searchMesPmWorkOrder = new SearchMesPmWorkOrder();
                searchMesPmWorkOrder.setWorkOrderId(mesSfcWorkOrderBarcode.getWorkOrderId());
                MesPmWorkOrderDto mesPmWorkOrderDto = pmFeignApi.findWorkOrderList(searchMesPmWorkOrder).getData().get(0);
                mesSfcBarcodeProcess.setMaterialId(mesPmWorkOrderDto.getMaterialId());
                mesSfcBarcodeProcess.setMaterialCode(mesPmWorkOrderDto.getMaterialCode());
                mesSfcBarcodeProcess.setMaterialName(mesPmWorkOrderDto.getMaterialName());
                mesSfcBarcodeProcess.setMaterialVer(mesPmWorkOrderDto.getVersion());
                mesSfcBarcodeProcess.setRouteId(mesPmWorkOrderDto.getRouteId());
                mesSfcBarcodeProcess.setRouteCode(mesPmWorkOrderDto.getRouteCode());
                mesSfcBarcodeProcess.setRouteName(mesPmWorkOrderDto.getRouteName());
                if(mesSfcBarcodeProcessMapper.insertSelective(mesSfcBarcodeProcess)<1){
                    throw new BizErrorException("条码过站失败");
                }
            }
            printModel.setQrCode(mesSfcWorkOrderBarcode.getBarcode());
            PrintDto printDto = new PrintDto();
            printDto.setLabelName(labelRuteDto.getLabelName());
            printDto.setLabelVersion(labelRuteDto.getLabelVersion());
            List<PrintModel> printModelList = new ArrayList<>();
            printModelList.add(printModel);
            rabbitProducer.sendPrint(printDto);

            mesSfcWorkOrderBarcode.setBarcodeStatus((byte)0);
            this.update(mesSfcWorkOrderBarcode);
        }
        return 1;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int update(MesSfcWorkOrderBarcode entity) {
        SysUser sysUser = currentUser();
        entity.setModifiedUserId(sysUser.getUserId());
        entity.setModifiedTime(new Date());
        return mesSfcWorkOrderBarcodeMapper.updateByPrimaryKeySelective(entity);
    }

    @Override
    public LabelRuteDto findLabelRute(Long workOrderId, Byte barcodeType) {
        LabelRuteDto labelRuteDto = null;
        switch (barcodeType){
            case 2:
                labelRuteDto = mesSfcWorkOrderBarcodeMapper.findRule("01",workOrderId);
                break;
            case 4:
                labelRuteDto = mesSfcWorkOrderBarcodeMapper.findRule("02",workOrderId);
                break;
        }
        if(StringUtils.isEmpty(labelRuteDto)&&StringUtils.isEmpty(labelRuteDto.getBarcodeRuleId())){
            throw new BizErrorException(barcodeType==1?"未匹配到工单绑定的条码规则":"未匹配到销售订单绑定的条码规则");
        }
        return labelRuteDto;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void checkOutLabel(String labelName, HttpServletRequest request, HttpServletResponse response) {
        if(StringUtils.isEmpty(labelName)){
            throw new BizErrorException("参数错误");
        }
        BaseLabelCategory baseLabelCategory = mesSfcWorkOrderBarcodeMapper.findByOneLabel(labelName);
        if(StringUtils.isEmpty(baseLabelCategory)){
            throw new BizErrorException("获取标签信息失败");
        }
        Path file = Paths.get("../"+ baseLabelCategory.getLabelCategoryName());
        if(Files.exists(file)){
            response.setContentType("application/vnd.android.package-archive");
            try {
                response.addHeader("Content-Disposition",
                        "attachment; filename=" + URLEncoder.encode(labelName, "UTF-8"));

                System.out.println("以输出流的形式对外输出提供下载");
                Files.copy(file, response.getOutputStream());// 以输出流的形式对外输出提供下载
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }else{
            throw new BizErrorException("文件不存在");
        }
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public MesSfcWorkOrderBarcode add(MesSfcWorkOrderBarcode record) {
        SysUser sysUser = currentUser();
        if(StringUtils.isEmpty(record.getWorkOrderId())){
            throw new BizErrorException("绑定单据唯一码不能为空");
        }
        //获取生成的最大条码
        String maxCode = mesSfcWorkOrderBarcodeMapper.findMaxCode(record.getBarcodeType(),record.getWorkOrderId());
        //查询条码规则集合
        LabelRuteDto labelRuteDto = record.getLabelRuteDto();
        SearchBaseBarcodeRuleSpec searchBaseBarcodeRuleSpec = new SearchBaseBarcodeRuleSpec();
        searchBaseBarcodeRuleSpec.setBarcodeRuleId(labelRuteDto.getBarcodeRuleId());
        List<BaseBarcodeRuleSpec> list = pmFeignApi.findSpec(searchBaseBarcodeRuleSpec).getData();
        if(list.size()<1){
            throw new BizErrorException("请设置条码规则");
        }
        //生成条码
        String code = pmFeignApi.generateCode(list,maxCode,null).getData();
        record.setBarcode(code);

        //待打印状态
        record.setBarcodeStatus((byte)3);
        record.setCreateTime(new Date());
        record.setCreateUserId(sysUser.getUserId());
        record.setModifiedTime(new Date());
        record.setModifiedUserId(sysUser.getUserId());
        mesSfcWorkOrderBarcodeMapper.insertUseGeneratedKeys(record);
        return record;
    }

    /**
     * 获取当前登录用户
     * @return
     */
    private SysUser currentUser(){
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        return user;
    }
}
