package com.fantechs.provider.mes.sfc.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysSpecItem;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.entity.security.search.SearchSysSpecItem;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseBarcodeRuleDto;
import com.fantechs.common.base.general.dto.basic.BaseLabelMaterialDto;
import com.fantechs.common.base.general.dto.mes.pm.MesPmWorkOrderDto;
import com.fantechs.common.base.general.dto.mes.sfc.LabelRuteDto;
import com.fantechs.common.base.general.dto.mes.sfc.MesSfcWorkOrderBarcodeDto;
import com.fantechs.common.base.general.dto.mes.sfc.PrintDto;
import com.fantechs.common.base.general.dto.mes.sfc.PrintModel;
import com.fantechs.common.base.general.dto.wms.in.PalletAutoAsnDto;
import com.fantechs.common.base.general.entity.basic.BaseBarcodeRuleSpec;
import com.fantechs.common.base.general.entity.basic.BaseRouteProcess;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseBarcodeRule;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseBarcodeRuleSpec;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseLabelMaterial;
import com.fantechs.common.base.general.entity.mes.pm.MesPmWorkOrder;
import com.fantechs.common.base.general.entity.mes.pm.search.SearchMesPmWorkOrder;
import com.fantechs.common.base.general.entity.mes.sfc.MesSfcBarcodeProcess;
import com.fantechs.common.base.general.entity.mes.sfc.MesSfcWorkOrderBarcode;
import com.fantechs.common.base.general.entity.mes.sfc.SearchMesSfcWorkOrderBarcode;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.RedisUtil;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.api.mes.pm.PMFeignApi;
import com.fantechs.provider.api.security.service.SecurityFeignApi;
import com.fantechs.provider.mes.sfc.mapper.MesSfcBarcodeProcessMapper;
import com.fantechs.provider.mes.sfc.mapper.MesSfcWorkOrderBarcodeMapper;
import com.fantechs.provider.mes.sfc.service.MesSfcWorkOrderBarcodeService;
import com.fantechs.provider.mes.sfc.util.RabbitProducer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

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
    private BaseFeignApi baseFeignApi;
    @Resource
    private RabbitProducer rabbitProducer;
    @Resource
    private MesSfcBarcodeProcessMapper mesSfcBarcodeProcessMapper;

    @Resource
    private RedisUtil redisUtil;
    @Resource
    private SecurityFeignApi securityFeignApi;

    @Override
    public List<MesSfcWorkOrderBarcodeDto> findList(SearchMesSfcWorkOrderBarcode searchMesSfcWorkOrderBarcode) {
        if(StringUtils.isEmpty(searchMesSfcWorkOrderBarcode.getOrgId())) {
            SysUser sysUser = currentUser();
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
            //获取配置项
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
            if (mesSfcWorkOrderBarcode.getLabelCategoryId() == 56) {//获取工单类别模版
                labelRuteDto = mesSfcWorkOrderBarcodeMapper.findRule("01", mesSfcWorkOrderBarcode.getWorkOrderId());
                if (StringUtils.isEmpty(labelRuteDto) && StringUtils.isEmpty(labelRuteDto.getLabelName())) {
                    //获取默认模版
                    labelRuteDto = mesSfcWorkOrderBarcodeMapper.DefaultLabel("01");
                }
            } else if (mesSfcWorkOrderBarcode.getLabelCategoryId() == 57) {//获取销售类别模版
                labelRuteDto = mesSfcWorkOrderBarcodeMapper.findRule("02", mesSfcWorkOrderBarcode.getWorkOrderId());
                if (StringUtils.isEmpty(labelRuteDto) || StringUtils.isEmpty(labelRuteDto.getLabelName())) {
                    //获取默认模版
                    labelRuteDto = mesSfcWorkOrderBarcodeMapper.DefaultLabel("02");
                    if (StringUtils.isEmpty(labelRuteDto)) {
                        throw new BizErrorException("未匹配到默认模版");
                    }
                }
            }
            //PrintModel printModel = mesSfcWorkOrderBarcodeMapper.findPrintModel(mesSfcWorkOrderBarcode.getLabelCategoryId(), mesSfcWorkOrderBarcode.getWorkOrderId());

            //查询是否有模版视图
            String labelView = mesSfcWorkOrderBarcodeMapper.findLabelView(labelRuteDto.getLabelCode());
            if(StringUtils.isEmpty(labelView)){
                throw new BizErrorException("未查询到模版视图");
            }

            PrintModel printModel = mesSfcWorkOrderBarcodeMapper.findPrintModel(ControllerUtil.dynamicCondition("labelCode",labelRuteDto.getLabelCode(),"id",mesSfcWorkOrderBarcode.getWorkOrderId()));
            printModel.setSize(1);
            if(StringUtils.isEmpty(labelRuteDto)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(),"获取标签信息失败");
            }
//            if(labelRuteDto.getBarcodeType()==(byte)1 &&printType==(byte)1){
//                //生成条码过站记录
//                MesSfcBarcodeProcess mesSfcBarcodeProcess = new MesSfcBarcodeProcess();
//                mesSfcBarcodeProcess.setWorkOrderId(mesSfcWorkOrderBarcode.getWorkOrderId());
//                mesSfcBarcodeProcess.setWorkOrderCode(mesSfcWorkOrderBarcode.getWorkOrderCode());
//                mesSfcBarcodeProcess.setWorkOrderBarcodeId(mesSfcWorkOrderBarcode.getWorkOrderBarcodeId());
//                mesSfcBarcodeProcess.setBarcodeType((byte)2);
//                mesSfcBarcodeProcess.setBarcode(mesSfcWorkOrderBarcode.getBarcode());
//
//                SearchMesPmWorkOrder searchMesPmWorkOrder = new SearchMesPmWorkOrder();
//                searchMesPmWorkOrder.setWorkOrderId(mesSfcWorkOrderBarcode.getWorkOrderId());
//                MesPmWorkOrderDto mesPmWorkOrderDto = pmFeignApi.findWorkOrderList(searchMesPmWorkOrder).getData().get(0);
//
//                mesSfcBarcodeProcess.setProLineId(mesPmWorkOrderDto.getProLineId());
//                mesSfcBarcodeProcess.setProcessCode(mesPmWorkOrderDto.getProCode());
//                mesSfcBarcodeProcess.setMaterialId(mesPmWorkOrderDto.getMaterialId());
//                mesSfcBarcodeProcess.setMaterialCode(mesPmWorkOrderDto.getMaterialCode());
//                mesSfcBarcodeProcess.setMaterialName(mesPmWorkOrderDto.getMaterialName());
//                mesSfcBarcodeProcess.setMaterialVer(mesPmWorkOrderDto.getMaterialVersion());
//                mesSfcBarcodeProcess.setRouteId(mesPmWorkOrderDto.getRouteId());
//                mesSfcBarcodeProcess.setRouteCode(mesPmWorkOrderDto.getRouteCode());
//                mesSfcBarcodeProcess.setRouteName(mesPmWorkOrderDto.getRouteName());
//
//                //查询工艺路线
//                ResponseEntity<List<BaseRouteProcess>> responseEntity = baseFeignApi.findConfigureRout(mesPmWorkOrderDto.getRouteId());
//                if(responseEntity.getCode()!=0){
//                    throw new BizErrorException("工艺路线查询失败");
//                }
//                mesSfcBarcodeProcess.setProcessId(responseEntity.getData().get(0).getProcessId());
//                mesSfcBarcodeProcess.setProcessName(responseEntity.getData().get(0).getProcessName());
//                mesSfcBarcodeProcess.setNextProcessId(responseEntity.getData().get(0).getProcessId());
//                mesSfcBarcodeProcess.setNextProcessName(responseEntity.getData().get(0).getProcessName());
//                mesSfcBarcodeProcess.setSectionId(responseEntity.getData().get(0).getSectionId());
//                mesSfcBarcodeProcess.setSectionName(responseEntity.getData().get(0).getSectionName());
//                if(mesSfcBarcodeProcessMapper.insertSelective(mesSfcBarcodeProcess)<1){
//                    throw new BizErrorException(ErrorCodeEnum.GL99990005.getCode(),"条码过站失败");
//                }
//            }
            mesSfcWorkOrderBarcode.setBarcodeStatus((byte)0);
            mesSfcWorkOrderBarcode.setPrintTime(new Date());
            this.update(mesSfcWorkOrderBarcode);
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
        SysUser sysUser = currentUser();
        entity.setModifiedUserId(sysUser.getUserId());
        entity.setModifiedTime(new Date());
        entity.setOrgId(sysUser.getOrganizationId());
        return mesSfcWorkOrderBarcodeMapper.updateByPrimaryKeySelective(entity);
    }

    @Override
    public LabelRuteDto findLabelRute(Long workOrderId, Byte barcodeType) {
        LabelRuteDto labelRuteDto = null;
        //查询工单是否绑定条码规则
        SearchMesPmWorkOrder searchMesPmWorkOrder = new SearchMesPmWorkOrder();
        searchMesPmWorkOrder.setWorkOrderId(workOrderId);
        MesPmWorkOrderDto mesPmWorkOrder = pmFeignApi.findWorkOrderList(searchMesPmWorkOrder).getData().get(0);
        if(StringUtils.isEmpty(mesPmWorkOrder.getBarcodeRuleSetId())){
            throw new BizErrorException("工单未绑定条码规则");
        }
        //查询物料是否绑定标签模版
        SearchBaseLabelMaterial searchBaseLabelMaterial = new SearchBaseLabelMaterial();
        searchBaseLabelMaterial.setMaterialId(mesPmWorkOrder.getMaterialId().toString());
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
        SysUser sysUser = currentUser();
        if(StringUtils.isEmpty(record.getWorkOrderId())){
            throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"绑定单据唯一码不能为空");
        }
//        if(record.getBarcodeType()==(byte)4){
//            MesPmWorkOrder mesPmWorkOrder = pmFeignApi.workOrderDetail(record.getWorkOrderId()).getData();
//            record.setWorkOrderId(mesPmWorkOrder.getSalesOrderId());
//        }
        switch (record.getBarcodeType()){
            case 2:
                record.setLabelCategoryId(mesSfcWorkOrderBarcodeMapper.finByTypeId("产品条码"));
                break;
            case 4:
                record.setLabelCategoryId(mesSfcWorkOrderBarcodeMapper.finByTypeId("销售订单条码"));
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

        for (Integer i = 0; i < record.getQty(); i++) {
            record.setWorkOrderBarcodeId(null);
            //Integer max = mesSfcWorkOrderBarcodeMapper.findCountCode(record.getBarcodeType(),record.getWorkOrderId())

            String lastBarCode = null;
            //boolean hasKey = redisUtil.hasKey(barcodeRulList.get(0).getBarcodeRule());
            // 不同组织使用同一个编码规则可能产生相同的条码 key值在原有基础上加组织ID作为键值 huangshuijun 2021-10-08
            String orgIDStr=sysUser.getOrganizationId().toString();
            boolean hasKey = redisUtil.hasKey(barcodeRulList.get(0).getBarcodeRule()+orgIDStr);
            if(hasKey){
                // 从redis获取上次生成条码
                //Object redisRuleData = redisUtil.get(barcodeRulList.get(0).getBarcodeRule());
                // 不同组织使用同一个编码规则可能产生相同的条码 key值在原有基础上加组织ID作为键值 huangshuijun 2021-10-08
                Object redisRuleData = redisUtil.get(barcodeRulList.get(0).getBarcodeRule()+orgIDStr);
                lastBarCode = String.valueOf(redisRuleData);
            }
            //获取最大流水号
            String maxCode = baseFeignApi.generateMaxCode(list, lastBarCode).getData();
            //生成条码
            ResponseEntity<String> rs = baseFeignApi.generateCode(list,maxCode,record.getMaterialCode(),record.getWorkOrderId().toString());
            if(rs.getCode()!=0){
                throw new BizErrorException(rs.getMessage());
            }
            record.setBarcode(rs.getData());

            // 更新redis最新条码
            // 不同组织使用同一个编码规则可能产生相同的条码 key值在原有基础上加组织ID作为键值 huangshuijun 2021-10-08
            //redisUtil.set(barcodeRulList.get(0).getBarcodeRule(), rs.getData());
            redisUtil.set(barcodeRulList.get(0).getBarcodeRule()+orgIDStr, rs.getData());

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

                SearchMesPmWorkOrder searchMesPmWorkOrder = new SearchMesPmWorkOrder();
                searchMesPmWorkOrder.setWorkOrderId(mesSfcWorkOrderBarcode.getWorkOrderId());
                MesPmWorkOrderDto mesPmWorkOrderDto = pmFeignApi.findWorkOrderList(searchMesPmWorkOrder).getData().get(0);

                mesSfcBarcodeProcess.setProLineId(mesPmWorkOrderDto.getProLineId());
                mesSfcBarcodeProcess.setProcessCode(mesPmWorkOrderDto.getProCode());
                mesSfcBarcodeProcess.setMaterialId(mesPmWorkOrderDto.getMaterialId());
                mesSfcBarcodeProcess.setMaterialCode(mesPmWorkOrderDto.getMaterialCode());
                mesSfcBarcodeProcess.setMaterialName(mesPmWorkOrderDto.getMaterialName());
                mesSfcBarcodeProcess.setMaterialVer(mesPmWorkOrderDto.getMaterialVersion());
                mesSfcBarcodeProcess.setRouteId(mesPmWorkOrderDto.getRouteId());
                mesSfcBarcodeProcess.setRouteCode(mesPmWorkOrderDto.getRouteCode());
                mesSfcBarcodeProcess.setRouteName(mesPmWorkOrderDto.getRouteName());

                if(StringUtils.isEmpty(mesPmWorkOrderDto.getRouteId())){
                    throw new BizErrorException("工单未选择工艺路线");
                }
                //查询工艺路线
                ResponseEntity<List<BaseRouteProcess>> res = baseFeignApi.findConfigureRout(mesPmWorkOrderDto.getRouteId());
                if(res.getCode()!=0){
                    throw new BizErrorException("工艺路线查询失败");
                }
                mesSfcBarcodeProcess.setProcessId(res.getData().get(0).getProcessId());
                mesSfcBarcodeProcess.setProcessName(res.getData().get(0).getProcessName());
                mesSfcBarcodeProcess.setNextProcessId(res.getData().get(0).getProcessId());
                mesSfcBarcodeProcess.setNextProcessName(res.getData().get(0).getProcessName());
                mesSfcBarcodeProcess.setSectionId(res.getData().get(0).getSectionId());
                mesSfcBarcodeProcess.setSectionName(res.getData().get(0).getSectionName());
                if(mesSfcBarcodeProcessMapper.insertSelective(mesSfcBarcodeProcess)<1){
                    throw new BizErrorException(ErrorCodeEnum.GL99990005.getCode(),"条码过站失败");
                }
            }

            mesSfcWorkOrderBarcodeList.add(mesSfcWorkOrderBarcode);
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

    public String sub(List<BaseBarcodeRuleSpec> list){
        StringBuffer sb = new StringBuffer();
        for (BaseBarcodeRuleSpec baseBarcodeRuleSpec : list) {
            sb.append(baseBarcodeRuleSpec.getSpecification());
        }
        return sb.toString();
    }
}
