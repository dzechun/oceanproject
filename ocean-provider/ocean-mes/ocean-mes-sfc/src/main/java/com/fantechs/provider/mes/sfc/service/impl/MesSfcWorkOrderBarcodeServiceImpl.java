package com.fantechs.provider.mes.sfc.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.mes.sfc.PrintDto;
import com.fantechs.common.base.general.dto.mes.sfc.PrintModel;
import com.fantechs.common.base.general.dto.mes.pm.search.SearchSmtBarcodeRuleSpec;
import com.fantechs.common.base.general.dto.mes.sfc.LabelRuteDto;
import com.fantechs.common.base.general.dto.mes.sfc.MesSfcWorkOrderBarcodeDto;
import com.fantechs.common.base.general.entity.bcm.BcmLabel;
import com.fantechs.common.base.general.entity.mes.pm.SmtBarcodeRuleSpec;
import com.fantechs.common.base.general.entity.mes.sfc.MesSfcWorkOrderBarcode;
import com.fantechs.common.base.general.entity.mes.sfc.SearchMesSfcWorkOrderBarcode;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.mes.pm.PMFeignApi;
import com.fantechs.provider.bcm.mapper.BcmLabelMapper;
import com.fantechs.provider.mes.sfc.util.RabbitProducer;
import com.fantechs.provider.mes.sfc.mapper.MesSfcWorkOrderBarcodeMapper;
import com.fantechs.provider.mes.sfc.service.MesSfcWorkOrderBarcodeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

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
 * Created by leifengzhi on 2021/04/07.
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
    private BcmLabelMapper bcmLabelMapper;

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
            printModel.setQrCode(mesSfcWorkOrderBarcode.getBarcode());
            PrintDto printDto = new PrintDto();
            printDto.setLabelName(labelRuteDto.getLabelName());
            printDto.setLabelVersion(labelRuteDto.getLabelVersion());
            List<PrintModel> printModelList = new ArrayList<>();
            printModelList.add(printModel);
            rabbitProducer.sendPrint(printDto);

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
        Example example = new Example(BcmLabel.class);
        example.createCriteria().andEqualTo("labelName",labelName);
        BcmLabel bcmLabel = bcmLabelMapper.selectOneByExample(example);
        if(StringUtils.isEmpty(bcmLabel)){
            throw new BizErrorException("获取标签信息失败");
        }
        Path file = Paths.get("../"+bcmLabel.getLabelCategoryId());
        if(Files.exists(file)){
            response.setContentType("application/vnd.android.package-archive");
            try {
                response.addHeader("Content-Disposition",
                        "attachment; filename=" + URLEncoder.encode(bcmLabel.getLabelName(), "UTF-8"));

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
        SearchSmtBarcodeRuleSpec searchSmtBarcodeRuleSpec = new SearchSmtBarcodeRuleSpec();
        searchSmtBarcodeRuleSpec.setBarcodeRuleId(labelRuteDto.getBarcodeRuleId());
        List<SmtBarcodeRuleSpec> list = pmFeignApi.findSpec(searchSmtBarcodeRuleSpec).getData();
        if(list.size()<1){
            throw new BizErrorException("请设置条码规则");
        }
        //生成条码
        String code = pmFeignApi.generateCode(list,maxCode,null).getData();
        record.setBarcode(code);

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
