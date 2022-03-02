package com.fantechs.provider.mes.sfc.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysSpecItem;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.entity.security.search.SearchSysSpecItem;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseBarcodeRuleDto;
import com.fantechs.common.base.general.dto.basic.BaseLabelMaterialDto;
import com.fantechs.common.base.general.dto.basic.BatchGenerateCodeDto;
import com.fantechs.common.base.general.dto.mes.pm.MesPmWorkOrderDto;
import com.fantechs.common.base.general.dto.mes.sfc.LabelRuteDto;
import com.fantechs.common.base.general.dto.mes.sfc.MesSfcWorkOrderBarcodeDto;
import com.fantechs.common.base.general.dto.mes.sfc.PrintDto;
import com.fantechs.common.base.general.dto.mes.sfc.PrintModel;
import com.fantechs.common.base.general.dto.om.OmSalesOrderDetDto;
import com.fantechs.common.base.general.dto.om.SearchOmSalesOrderDetDto;
import com.fantechs.common.base.general.dto.wms.in.PalletAutoAsnDto;
import com.fantechs.common.base.general.entity.basic.BaseBarcodeRuleSpec;
import com.fantechs.common.base.general.entity.basic.BaseRouteProcess;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseBarcodeRule;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseBarcodeRuleSpec;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseLabelMaterial;
import com.fantechs.common.base.general.entity.mes.pm.search.SearchMesPmWorkOrder;
import com.fantechs.common.base.general.entity.mes.sfc.MesSfcBarcodeProcess;
import com.fantechs.common.base.general.entity.mes.sfc.MesSfcWorkOrderBarcode;
//import com.fantechs.common.base.general.entity.mes.sfc.MesSfcWorkOrderBarcodeReprint;
import com.fantechs.common.base.general.entity.mes.sfc.MesSfcWorkOrderBarcodeReprint;
import com.fantechs.common.base.general.entity.mes.sfc.SearchMesSfcWorkOrderBarcode;
import com.fantechs.common.base.general.entity.om.OmSalesOrderDet;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.RedisUtil;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.api.mes.pm.PMFeignApi;
import com.fantechs.provider.api.qms.OMFeignApi;
import com.fantechs.provider.api.security.service.SecurityFeignApi;
import com.fantechs.provider.mes.sfc.mapper.MesSfcBarcodeProcessMapper;
import com.fantechs.provider.mes.sfc.mapper.MesSfcWorkOrderBarcodeMapper;
//import com.fantechs.provider.mes.sfc.service.MesSfcWorkOrderBarcodeReprintService;
import com.fantechs.provider.mes.sfc.service.MesSfcWorkOrderBarcodeReprintService;
import com.fantechs.provider.mes.sfc.service.MesSfcWorkOrderBarcodeService;
import com.fantechs.provider.mes.sfc.util.RabbitProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 *
 * Created by Mr.Lei on 2021/04/07.
 */
@Service
@Slf4j
public class MesSfcWorkOrderBarcodeServiceImpl extends BaseService<MesSfcWorkOrderBarcode> implements MesSfcWorkOrderBarcodeService {

    @Resource
    private MesSfcWorkOrderBarcodeMapper mesSfcWorkOrderBarcodeMapper;
    @Resource
    private PMFeignApi pmFeignApi;
    @Resource
    private BaseFeignApi baseFeignApi;
    @Resource
    private RabbitProducer rabbitProducer;
    @Resource
    private MesSfcBarcodeProcessMapper mesSfcBarcodeProcessMapper;
    @Resource
    private RedisUtil redisUtil;
    @Resource
    private SecurityFeignApi securityFeignApi;
    @Resource
    private OMFeignApi omFeignApi;
    @Resource
    private MesSfcWorkOrderBarcodeReprintService mesSfcWorkOrderBarcodeReprintService;

    @Override
    public List<MesSfcWorkOrderBarcodeDto> findList(SearchMesSfcWorkOrderBarcode searchMesSfcWorkOrderBarcode) {
        if(StringUtils.isEmpty(searchMesSfcWorkOrderBarcode.getOrgId())) {
            SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
            searchMesSfcWorkOrderBarcode.setOrgId(sysUser.getOrganizationId());
        }
        return mesSfcWorkOrderBarcodeMapper.findList(searchMesSfcWorkOrderBarcode);
    }

    /**
     * 打印/补打条码
     * @param ids 条码唯一标识
     * @param printType 打印类型
     * @return 1
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int print(String ids,Byte printType,String printName,String userCode,String password) {
        if(printType==2){
            //获取程序配置项
            SearchSysSpecItem searchSysSpecItem = new SearchSysSpecItem();
            searchSysSpecItem.setSpecCode("ReprintCode");
            List<SysSpecItem> itemList = securityFeignApi.findSpecItemList(searchSysSpecItem).getData();
            String isRoot = itemList.get(0).getParaValue().trim();
            if(isRoot.equals("true")){
                String hashPass = mesSfcWorkOrderBarcodeMapper.findSysUser(userCode);
                if(StringUtils.isEmpty(hashPass)){
                    throw new BizErrorException("用户无权限，请联系管理员");
                }
                BCryptPasswordEncoder bcryptPasswordEncoder = new BCryptPasswordEncoder();
                boolean flag = bcryptPasswordEncoder.matches(password,hashPass);
                if(!flag){
                    throw new BizErrorException("密码错误");
                }
            }
        }
        if(StringUtils.isEmpty(ids,printName)){
            throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"参数传递错误");
        }
        if(StringUtils.isEmpty(printName)){
            throw new BizErrorException("请设置打印机名称");
        }
        String[] arrId = ids.split(",");
        Map<Long,PrintDto> map = new HashMap<>();
        for (String s : arrId) {
            //查询模版信息
            MesSfcWorkOrderBarcode mesSfcWorkOrderBarcode = mesSfcWorkOrderBarcodeMapper.selectByPrimaryKey(s);
            LabelRuteDto labelRuteDto = null;
            Byte barcodeType;
            if (mesSfcWorkOrderBarcode.getLabelCategoryId() == 56) {//获取工单类别模版
                labelRuteDto = mesSfcWorkOrderBarcodeMapper.findRule("01", mesSfcWorkOrderBarcode.getWorkOrderId());
                if (StringUtils.isEmpty(labelRuteDto) && StringUtils.isEmpty(labelRuteDto.getLabelName())) {
                    //获取默认模版
                    labelRuteDto = mesSfcWorkOrderBarcodeMapper.DefaultLabel("01");
                }
                mesSfcWorkOrderBarcode.setBarcodeType((byte) 2);
            } else if (mesSfcWorkOrderBarcode.getLabelCategoryId() == 57 && "4".equals(mesSfcWorkOrderBarcode.getOption1())) {//获取销售类别模版
                labelRuteDto = mesSfcWorkOrderBarcodeMapper.findRule("02", mesSfcWorkOrderBarcode.getWorkOrderId());
                if (StringUtils.isEmpty(labelRuteDto) || StringUtils.isEmpty(labelRuteDto.getLabelName())) {
                    //获取默认模版
                    labelRuteDto = mesSfcWorkOrderBarcodeMapper.DefaultLabel("02");
                    if (StringUtils.isEmpty(labelRuteDto)) {
                        throw new BizErrorException("未匹配到默认模版");
                    }
                }
                mesSfcWorkOrderBarcode.setBarcodeType((byte) 4);
            }else if(mesSfcWorkOrderBarcode.getLabelCategoryId() == 57 && "5".equals(mesSfcWorkOrderBarcode.getOption1())){
                labelRuteDto = mesSfcWorkOrderBarcodeMapper.findOmRule(mesSfcWorkOrderBarcode.getWorkOrderId());
                mesSfcWorkOrderBarcode.setBarcodeType((byte) 1);
            }
            //PrintModel printModel = mesSfcWorkOrderBarcodeMapper.findPrintModel(mesSfcWorkOrderBarcode.getLabelCategoryId(), mesSfcWorkOrderBarcode.getWorkOrderId());

            //查询是否有模版视图
            String labelView = mesSfcWorkOrderBarcodeMapper.findLabelView(labelRuteDto.getLabelCode());
            if(StringUtils.isEmpty(labelView)){
                throw new BizErrorException("未查询到模版视图");
            }

            PrintModel printModel = mesSfcWorkOrderBarcodeMapper.findPrintModel(ControllerUtil.dynamicCondition("labelCode",labelRuteDto.getLabelCode(),"id",mesSfcWorkOrderBarcode.getWorkOrderBarcodeId()));
            if(StringUtils.isEmpty(printModel)){
                throw new BizErrorException("获取视图数据信息失败，请检查视图");
            }
            if(StringUtils.isEmpty(labelRuteDto.getSize())){
                labelRuteDto.setSize(1);
            }
            printModel.setSize(labelRuteDto.getSize());
            if(StringUtils.isEmpty(labelRuteDto)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(),"获取标签信息失败");
            }
            if (printType==1){
                mesSfcWorkOrderBarcode.setBarcodeStatus((byte)0);
                mesSfcWorkOrderBarcode.setPrintTime(new Date());
                this.update(mesSfcWorkOrderBarcode);
            }else if (printType==2){
                SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
                MesSfcWorkOrderBarcodeReprint reprint = new MesSfcWorkOrderBarcodeReprint();
                reprint.setWorkOrderBarcodeId(mesSfcWorkOrderBarcode.getWorkOrderBarcodeId());
                reprint.setBarcodeCode(mesSfcWorkOrderBarcode.getBarcode());
                reprint.setBarcodeType(mesSfcWorkOrderBarcode.getBarcodeType());
                reprint.setCreateTime(new Date());
                reprint.setCreateUserId(sysUser.getUserId());
                reprint.setModifiedTime(new Date());
                reprint.setModifiedUserId(sysUser.getUserId());
                reprint.setReprintTime(new Date());
                reprint.setReprintUserId(sysUser.getUserId());
                reprint.setOrgId(sysUser.getOrganizationId());
                reprint.setIsDelete((byte) 1);
                mesSfcWorkOrderBarcodeReprintService.save(reprint);
            }
            printModel.setQrCode(mesSfcWorkOrderBarcode.getBarcode());

            if(map.containsKey(mesSfcWorkOrderBarcode.getWorkOrderId())){
                PrintDto printDto = map.get(mesSfcWorkOrderBarcode.getWorkOrderId());
                List<PrintModel> printModelList = printDto.getPrintModelList();
                printModelList.add(printModel);
                map.put(mesSfcWorkOrderBarcode.getWorkOrderId(),printDto);
            }else {
                PrintDto printDto = new PrintDto();
                printDto.setLabelName(labelRuteDto.getLabelName()+".btw");
                printDto.setLabelVersion(labelRuteDto.getLabelVersion());
                printDto.setPrintName(printName);
                List<PrintModel> printModelList = new ArrayList<>();
                printModelList.add(printModel);
                printDto.setPrintModelList(printModelList);
                map.put(mesSfcWorkOrderBarcode.getWorkOrderId(),printDto);
            }

        }
        for (Map.Entry<Long, PrintDto> m : map.entrySet()) {
            rabbitProducer.sendPrint(m.getValue());
        }
        return 1;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int update(MesSfcWorkOrderBarcode entity) {
        if(entity.getOrgId()==1000L){
            entity.setModifiedTime(new Date());
        }
        else {
            SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
            entity.setModifiedUserId(sysUser.getUserId());
            entity.setModifiedTime(new Date());
            entity.setOrgId(sysUser.getOrganizationId());
        }
        return mesSfcWorkOrderBarcodeMapper.updateByPrimaryKeySelective(entity);
    }

    @Override
    public LabelRuteDto findLabelRute(Long workOrderId, Byte barcodeType) {
        LabelRuteDto labelRuteDto = null;
        Long materialId = null;
        if(barcodeType==5){
            //查询销售订单明细
            SearchOmSalesOrderDetDto salesOrderDetDto = new SearchOmSalesOrderDetDto();
            salesOrderDetDto.setSalesOrderDetId(workOrderId);
            OmSalesOrderDetDto omSalesOrderDetDto = omFeignApi.findList(salesOrderDetDto).getData().get(0);
            materialId = omSalesOrderDetDto.getMaterialId();
        }else {
            //查询工单是否绑定条码规则
            SearchMesPmWorkOrder searchMesPmWorkOrder = new SearchMesPmWorkOrder();
            searchMesPmWorkOrder.setWorkOrderId(workOrderId);
            MesPmWorkOrderDto mesPmWorkOrder = pmFeignApi.findWorkOrderList(searchMesPmWorkOrder).getData().get(0);
            if(StringUtils.isEmpty(mesPmWorkOrder.getBarcodeRuleSetId())){
                throw new BizErrorException("工单未绑定条码规则");
            }
            materialId = mesPmWorkOrder.getMaterialId();
        }
        if(StringUtils.isEmpty(materialId)){
            throw new BizErrorException("获取物料信息失败");
        }
        //查询物料是否绑定标签模版
        SearchBaseLabelMaterial searchBaseLabelMaterial = new SearchBaseLabelMaterial();
        searchBaseLabelMaterial.setMaterialId(materialId.toString());
        if(barcodeType==2){
            searchBaseLabelMaterial.setLabelCategoryCode("01");
        }else if(barcodeType==4){
            searchBaseLabelMaterial.setLabelCategoryCode("02");
        }
        List<BaseLabelMaterialDto> baseLabelMaterialDtos = baseFeignApi.findLabelMaterialList(searchBaseLabelMaterial).getData();
        if(StringUtils.isEmpty(baseLabelMaterialDtos) || baseLabelMaterialDtos.size()<1){
            throw new BizErrorException("物料未绑定标签模版");
        }
        switch (barcodeType){
            case 2:
                labelRuteDto = mesSfcWorkOrderBarcodeMapper.findRule("01",workOrderId);
                break;
            case 4:
                labelRuteDto = mesSfcWorkOrderBarcodeMapper.findRule("02",workOrderId);
                break;
            case 5:
                labelRuteDto = mesSfcWorkOrderBarcodeMapper.findOmRule(workOrderId);
                break;
            default:
                break;
        }
        if(StringUtils.isEmpty(labelRuteDto)||StringUtils.isEmpty(labelRuteDto.getBarcodeRuleId())){
            throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(),barcodeType==2?"匹配工单条码规则失败":"匹配到销售订单条码规则失败");
        }
        return labelRuteDto;
    }

    /**
     * 打印客户端标签版本校验下载
     * @param labelName 标签名称
     * @param request
     * @param response
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void checkOutLabel(String labelName, HttpServletRequest request, HttpServletResponse response) {
        if(StringUtils.isEmpty(labelName)){
            throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"参数错误");
        }
        //String fileName =labelName.substring(0,labelName.indexOf("."));
        String baseLabelCategory = mesSfcWorkOrderBarcodeMapper.findByOneLabel(labelName);
        if(StringUtils.isEmpty(baseLabelCategory)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(),"获取标签信息失败");
        }
        //查询版本号
        String version = mesSfcWorkOrderBarcodeMapper.findVersion(labelName);
        if(StringUtils.isEmpty(version)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(),"标签版本获取失败");
        }
        String userAgent = request.getHeader("User-Agent");
        labelName = labelName+".btw";
        Path file = Paths.get("/label/"+ baseLabelCategory+"/"+version+"/"+labelName);
        if(Files.exists(file)){
            response.setContentType("application/vnd.android.package-archive;charset=utf-8");
            try {
                if(userAgent.contains("MSIE")||userAgent.contains("Trident")) {
                    labelName=java.net.URLEncoder.encode(labelName,"UTF-8");
                }else {
                    labelName=new String(labelName.getBytes("UTF-8"),"ISO-8859-1");
                }
                response.addHeader("Content-Disposition",
                        "attachment; filename="+labelName);

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
    public List<MesSfcWorkOrderBarcode> add(MesSfcWorkOrderBarcode record) {
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
//        if(record.getBarcodeType()==(byte)4){
//            MesPmWorkOrder mesPmWorkOrder = pmFeignApi.workOrderDetail(record.getWorkOrderId()).getData();
//            record.setWorkOrderId(mesPmWorkOrder.getSalesOrderId());
//        }
        switch (record.getBarcodeType()){
            case 2:
                if(StringUtils.isEmpty(record.getWorkOrderId())){
                    throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"绑定单据唯一码不能为空");
                }
                record.setLabelCategoryId(mesSfcWorkOrderBarcodeMapper.finByTypeId("产品条码"));
                record.setOption1(record.getBarcodeType().toString());
                break;
            case 4:
                if(StringUtils.isEmpty(record.getWorkOrderId())){
                    throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"绑定单据唯一码不能为空");
                }
                record.setLabelCategoryId(mesSfcWorkOrderBarcodeMapper.finByTypeId("销售订单条码"));
                record.setOption1(record.getBarcodeType().toString());
                break;
            case 5:
                if(StringUtils.isEmpty(record.getSalesOrderDetId())){
                    throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"绑定单据唯一码不能为空");
                }
                record.setLabelCategoryId(Long.parseLong("57"));
                record.setWorkOrderId(record.getSalesOrderDetId());
                record.setWorkOrderQty(StringUtils.isEmpty(record.getOrderQty())? BigDecimal.ZERO:record.getOrderQty());
                record.setWorkOrderCode(record.getSalesOrderCode());
                record.setOption1(record.getBarcodeType().toString());
                break;
        }
        //判断条码产生数量不能大于工单数量
        Integer count = mesSfcWorkOrderBarcodeMapper.findCountCode(record.getLabelCategoryId(),record.getWorkOrderId());
        if(count+ record.getQty()>record.getWorkOrderQty().doubleValue()){
            throw new BizErrorException(ErrorCodeEnum.OPT20012009);
        }
        List<MesSfcWorkOrderBarcode> mesSfcWorkOrderBarcodeList = new ArrayList<>();

        //查询条码规则集合
        LabelRuteDto labelRuteDto = record.getLabelRuteDto();
        SearchBaseBarcodeRuleSpec searchBaseBarcodeRuleSpec = new SearchBaseBarcodeRuleSpec();
        searchBaseBarcodeRuleSpec.setBarcodeRuleId(labelRuteDto.getBarcodeRuleId());
        ResponseEntity<List<BaseBarcodeRuleSpec>> responseEntity= baseFeignApi.findSpec(searchBaseBarcodeRuleSpec);
        if(responseEntity.getCode()!=0){
            throw new BizErrorException(responseEntity.getMessage());
        }
        List<BaseBarcodeRuleSpec> list = responseEntity.getData();
        if(list.size()<1){
            throw new BizErrorException("请设置条码规则");
        }

        SearchBaseBarcodeRule searchBaseBarcodeRule = new SearchBaseBarcodeRule();
        searchBaseBarcodeRule.setBarcodeRuleId(list.get(0).getBarcodeRuleId());
        List<BaseBarcodeRuleDto> barcodeRulList = baseFeignApi.findBarcodeRulList(searchBaseBarcodeRule).getData();
        if(StringUtils.isEmpty(barcodeRulList)) throw new BizErrorException("未查询到编码规则");

        MesPmWorkOrderDto mesPmWorkOrderDto = new MesPmWorkOrderDto();
        BaseRouteProcess routeProcess = new BaseRouteProcess();
        if(labelRuteDto.getBarcodeType()==(byte)1){
            SearchMesPmWorkOrder searchMesPmWorkOrder = new SearchMesPmWorkOrder();
            searchMesPmWorkOrder.setWorkOrderId(record.getWorkOrderId());
            mesPmWorkOrderDto = pmFeignApi.findWorkOrderList(searchMesPmWorkOrder).getData().get(0);

            if(StringUtils.isEmpty(mesPmWorkOrderDto.getRouteId())){
                throw new BizErrorException("工单未选择工艺路线");
            }

            //查询工艺路线
            ResponseEntity<List<BaseRouteProcess>> res = baseFeignApi.findConfigureRout(mesPmWorkOrderDto.getRouteId());
            if(res.getCode()!=0){
                throw new BizErrorException(res.getCode(),res.getMessage());
            }
            if(res.getData().isEmpty()){
                throw new BizErrorException("请检查工单工艺路线");
            }
            routeProcess = res.getData().get(0);
        }



        //boolean hasKey = redisUtil.hasKey(barcodeRulList.get(0).getBarcodeRule());
        // 不同组织使用同一个编码规则可能产生相同的条码 key值在原有基础上加组织ID作为键值 huangshuijun 2021-10-08
        String orgIDStr=sysUser.getOrganizationId().toString();

        String key = barcodeRulList.get(0).getBarcodeRule()+orgIDStr;
        //万宝销售条码打印 按销售订单更新流水号
        if(StringUtils.isNotEmpty(record.getOption1()) && record.getOption1().equals("5")){
            key = barcodeRulList.get(0).getBarcodeRule()+orgIDStr+":"+record.getSalesOrderId().toString();
        }
        //生成条码
        BatchGenerateCodeDto dto = new BatchGenerateCodeDto();
        dto.setList(list);
        dto.setQty(record.getQty());
        dto.setKey(key);
        dto.setCode(record.getMaterialCode());
        dto.setParams(record.getWorkOrderId().toString());
        ResponseEntity<List<String>> rs = baseFeignApi.batchGenerateCode(dto);
        if(rs.getCode()!=0){
            throw new BizErrorException(rs.getMessage());
        }
        List<MesSfcBarcodeProcess> processList = new ArrayList<>();
        for (String barcode : rs.getData()){
            record.setWorkOrderBarcodeId(null);
            //Integer max = mesSfcWorkOrderBarcodeMapper.findCountCode(record.getBarcodeType(),record.getWorkOrderId())
            record.setBarcode(barcode);

            if(StringUtils.isNotEmpty(record.getOption1()) && record.getOption1().equals("5")){
                //判断是否已经打完
                BigDecimal barCodeTotalQty = mesSfcWorkOrderBarcodeMapper.saleOrderTotalQty(ControllerUtil.dynamicCondition("type",1,
                        "salesOrderId", record.getSalesOrderId(),
                        "barcodeTypeId",record.getLabelCategoryId()));
                BigDecimal salesTotalQty = mesSfcWorkOrderBarcodeMapper.saleOrderTotalQty(ControllerUtil.dynamicCondition("type",1,"salesOrderId", record.getSalesOrderId()));
                if(salesTotalQty.compareTo(barCodeTotalQty)==0){
                    //三秒后失效
                    redisUtil.expire(key,3);
                }
            }

            //待打印状态
            record.setBarcodeStatus((byte)3);
            record.setCreateTime(new Date());
            record.setCreateUserId(sysUser.getUserId());
            record.setModifiedTime(new Date());
            record.setModifiedUserId(sysUser.getUserId());
            record.setOrgId(sysUser.getOrganizationId());
            record.setCreateBarcodeTime(new Date());
            mesSfcWorkOrderBarcodeMapper.insertUseGeneratedKeys(record);

            MesSfcWorkOrderBarcode mesSfcWorkOrderBarcode = new MesSfcWorkOrderBarcode();
            BeanUtil.copyProperties(record,mesSfcWorkOrderBarcode);
            if(labelRuteDto.getBarcodeType()==(byte)1){
                //生成条码过站记录
                MesSfcBarcodeProcess mesSfcBarcodeProcess = new MesSfcBarcodeProcess();
                mesSfcBarcodeProcess.setWorkOrderId(mesSfcWorkOrderBarcode.getWorkOrderId());
                mesSfcBarcodeProcess.setWorkOrderCode(mesSfcWorkOrderBarcode.getWorkOrderCode());
                mesSfcBarcodeProcess.setWorkOrderBarcodeId(mesSfcWorkOrderBarcode.getWorkOrderBarcodeId());
                mesSfcBarcodeProcess.setBarcodeType((byte)2);
                mesSfcBarcodeProcess.setBarcode(mesSfcWorkOrderBarcode.getBarcode());
                mesSfcBarcodeProcess.setProLineId(mesPmWorkOrderDto.getProLineId());
                mesSfcBarcodeProcess.setProcessCode(mesPmWorkOrderDto.getProCode());
                mesSfcBarcodeProcess.setMaterialId(mesPmWorkOrderDto.getMaterialId());
                mesSfcBarcodeProcess.setMaterialCode(mesPmWorkOrderDto.getMaterialCode());
                mesSfcBarcodeProcess.setMaterialName(mesPmWorkOrderDto.getMaterialName());
                mesSfcBarcodeProcess.setMaterialVer(mesPmWorkOrderDto.getMaterialVersion());
                mesSfcBarcodeProcess.setRouteId(mesPmWorkOrderDto.getRouteId());
                mesSfcBarcodeProcess.setRouteCode(mesPmWorkOrderDto.getRouteCode());
                mesSfcBarcodeProcess.setRouteName(mesPmWorkOrderDto.getRouteName());
                mesSfcBarcodeProcess.setProcessId(routeProcess.getProcessId());
                mesSfcBarcodeProcess.setProcessName(routeProcess.getProcessName());
                mesSfcBarcodeProcess.setNextProcessId(routeProcess.getProcessId());
                mesSfcBarcodeProcess.setNextProcessName(routeProcess.getProcessName());
                mesSfcBarcodeProcess.setSectionId(routeProcess.getSectionId());
                mesSfcBarcodeProcess.setSectionName(routeProcess.getSectionName());
                processList.add(mesSfcBarcodeProcess);
            }

            mesSfcWorkOrderBarcodeList.add(mesSfcWorkOrderBarcode);
        }
        // 批量保存条码过站表
        if (!processList.isEmpty()){
            mesSfcBarcodeProcessMapper.insertList(processList);
        }
        return mesSfcWorkOrderBarcodeList;
    }

    @Override
    public MesSfcWorkOrderBarcode findBarcode(String barcode) {
        Example example = new Example(MesSfcWorkOrderBarcode.class);
        example.createCriteria().andEqualTo("barcode",barcode);
        MesSfcWorkOrderBarcode mesSfcWorkOrderBarcode = mesSfcWorkOrderBarcodeMapper.selectOneByExample(example);
        return mesSfcWorkOrderBarcode;
    }

    @Override
    public int batchUpdate(List<MesSfcWorkOrderBarcode> workOrderBarcodes) {
        return mesSfcWorkOrderBarcodeMapper.batchUpdate(workOrderBarcodes);
    }

    @Override
    public List<MesSfcWorkOrderBarcodeDto> findByWorkOrderGroup(Map<String, Object> map) {
        return mesSfcWorkOrderBarcodeMapper.findByWorkOrderGroup(map);
    }

    @Override
    public List<PalletAutoAsnDto> findListGroupByWorkOrder(Map<String, Object> map) {
        return mesSfcWorkOrderBarcodeMapper.findListGroupByWorkOrder(map);
    }

    @Override
    public List<MesSfcWorkOrderBarcodeDto> findListByPalletDet(Map<String, Object> map) {
        return mesSfcWorkOrderBarcodeMapper.findListByPalletDet(map);
    }

    @Override
    public List<MesSfcWorkOrderBarcodeDto> findListByCartonDet(Map<String, Object> map) {
        return mesSfcWorkOrderBarcodeMapper.findListByCartonDet(map);
    }

    @Override
    public List<String> wanbaoAddCustomerBarcode(Long salesOrderDetId, String fixedValue, String initialValue, String finalValue) {
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        // 保存固定值/初始值/最终值
        Example example = new Example(MesSfcWorkOrderBarcode.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("option3", salesOrderDetId);
        List<MesSfcWorkOrderBarcode> list = this.selectByExample(example);
        if (!list.isEmpty()){
            throw new BizErrorException(ErrorCodeEnum.OPT20012004.getCode(), "该销售明细单下的客户条码还未删除，不能保存");
        }
        OmSalesOrderDet salesOrderDet = new OmSalesOrderDet();
        salesOrderDet.setSalesOrderDetId(salesOrderDetId);
        salesOrderDet.setFixedValue(fixedValue);
        salesOrderDet.setInitialValue(initialValue);
        salesOrderDet.setFinalValue(finalValue);
        omFeignApi.update(salesOrderDet).getData();

        // 生成条码
        Integer start = Integer.valueOf(initialValue);
        Integer end = Integer.valueOf(finalValue);
        int length = finalValue.length();
        List<MesSfcWorkOrderBarcode> workOrderBarcodes = new ArrayList<>();
        List<String> barcodeList = new ArrayList<>();
        for (int i = start; i <= end; i++){
            String code = String.valueOf(i);
            while(true){
                if (length == code.length()){
                    break;
                }
                code = "0" + code;
            }
            MesSfcWorkOrderBarcode record = new MesSfcWorkOrderBarcode();
            record.setOption3(salesOrderDetId.toString());
            record.setWorkOrderId(salesOrderDetId);
            record.setLabelCategoryId(mesSfcWorkOrderBarcodeMapper.finByTypeId("客户条码"));
            record.setBarcode(fixedValue + code);
            record.setBarcodeStatus((byte) 0);
            record.setCreateTime(new Date());
            record.setCreateUserId(sysUser.getUserId());
            record.setModifiedTime(new Date());
            record.setModifiedUserId(sysUser.getUserId());
            record.setOrgId(sysUser.getOrganizationId());
            record.setCreateBarcodeTime(new Date());
            workOrderBarcodes.add(record);
            barcodeList.add(fixedValue + code);
        }
        mesSfcWorkOrderBarcodeMapper.insertList(workOrderBarcodes);
        return barcodeList;
    }

    @Override
    public int wanbaoDeleteCustomerBarcode(Long salesOrderDetId, String fixedValue) {
        // 查找销售明细行下所有客户条码，判断条码状态是否为待投产
        Example example = new Example(MesSfcWorkOrderBarcode.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("option3", salesOrderDetId);
        List<MesSfcWorkOrderBarcode> workOrderBarcodes = this.selectByExample(example);
        long count = workOrderBarcodes.stream().filter(item -> !item.getBarcodeStatus().equals((byte) 0)).count();
        if (count > 0){
            throw new BizErrorException(ErrorCodeEnum.OPT20012004.getCode(), "存在被使用的客户条码，不可删除");
        }
        criteria.andLike("barcode", fixedValue + "%");
        return this.deleteByExample(example);
    }

    @Override
    public List<MesSfcWorkOrderBarcode> wanbaoFindCustomerBarcode(Long salesOrderDetId){
        Example example = new Example(MesSfcWorkOrderBarcode.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("option3", salesOrderDetId);
        List<MesSfcWorkOrderBarcode> workOrderBarcodes = this.selectByExample(example);
        return workOrderBarcodes;
    }


    public String sub(List<BaseBarcodeRuleSpec> list){
        StringBuffer sb = new StringBuffer();
        for (BaseBarcodeRuleSpec baseBarcodeRuleSpec : list) {
            sb.append(baseBarcodeRuleSpec.getSpecification());
        }
        return sb.toString();
    }
}
