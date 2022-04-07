package com.fantechs.provider.mes.sfc.service.impl;

import com.alibaba.fastjson.JSON;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.mes.sfc.*;
import com.fantechs.common.base.general.entity.basic.BaseSignature;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseSignature;
import com.fantechs.common.base.general.entity.mes.sfc.MesSfcBarcodeProcess;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.mes.sfc.service.MesSfcBarcodeOperationService;
import com.fantechs.provider.mes.sfc.service.MesSfcBarcodeProcessService;
import com.fantechs.provider.mes.sfc.service.MesSfcPalletWorkService;
import com.fantechs.provider.mes.sfc.service.ScanBarcodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
public class ScanBarcodeServiceImpl implements ScanBarcodeService {

    @Resource
    private MesSfcBarcodeProcessService barcodeProcessService;
    @Resource
    private MesSfcBarcodeOperationService mesSfcBarcodeOperationService;
    @Resource
    private MesSfcPalletWorkService mesSfcPalletWorkService;
    @Resource
    private BaseFeignApi baseFeignApi;

    @Override
    public WanbaoStackingMQDto doScan(ScanBarcodeDto scanBarcodeDto) throws Exception {
        /**
         * 1、清洗条码
         * 条码类型有：厂内码、销售条码、客户条码（包含三星SN条码）
         */
        // 1、1  分割条码
        if (StringUtils.isEmpty(scanBarcodeDto.getBarCode())){
            // 停产亮灯
            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "获取读头条码数据为空");
        }
        log.info("=========== 读头扫码，条码：" + scanBarcodeDto.getBarCode());
        String[] barcodeArr = scanBarcodeDto.getBarCode().split(",");

        /**
         * 2、 业务场景如下
         * 2、1  一个条码，先判断是否为厂内码，是则往下走流程。不是，则判断是不是三星客户条码。都不是则停线亮灯
         * 2、2  两个条码，先判断是否存在厂内码，不存在则停线亮灯。若存在则判断另一个条码是否销售条码，不是则认为是客户条码
         * 2、3  三个条码，厂内码和销售条码是否都存在，只要其中一个不存在就停线亮灯，若存在，剩下的条码则认为是客户条码
         * 2、4  五个条码，厂内码和销售条码是否都存在，只要其中一个不存在就停线亮灯，若存在，剩下三个条码则认为是客户条码。
         *       另，取其中位数最小的条码去匹配特征码表中的特征码字段（通过物料匹配）
         *
         * 3、 条码清洗依据如下：
         *      条码长度为23位且前12位是物料编码 -> 厂内码
         *      前缀为391-的 -> 销售条码
         *      剩余条码中，取位数最少移位去校验特征码表中特征码字段，一致的 -> 客户条码
         */
        CleanBarcodeDto cleanBarcodeDto = new CleanBarcodeDto();
        Example example = new Example(MesSfcBarcodeProcess.class);
        Example.Criteria criteria = example.createCriteria();
        if (barcodeArr.length == 1){
            // 判断是厂内码还是三星客户条码
            if (barcodeArr[0].length() == 23){
                criteria.andEqualTo("barcode", barcodeArr[0]);
            }else {
                // 三星客户条码情况，读头扫到的条码是少一位的，用模糊匹配
                criteria.andLike("customerBarcode", barcodeArr[0] + "%");
            }
            List<MesSfcBarcodeProcess> mesSfcBarcodeProcesses = barcodeProcessService.selectByExample(example);
            if (mesSfcBarcodeProcesses.isEmpty()){
                throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "条码BarcodeUtils：" + barcodeArr[0] + "在系统中不存在");
            }
            MesSfcBarcodeProcess barcodeProcess = mesSfcBarcodeProcesses.get(0);
            if(StringUtils.isNotEmpty(barcodeProcess.getCustomerBarcode())){
                cleanBarcodeDto.setCutsomerBarcode(barcodeProcess.getCustomerBarcode());
            }
            cleanBarcodeDto.setOrderBarCode(barcodeProcess.getBarcode());
        }else {
            for (String str : barcodeArr){
                if (str.length() == 23){
                    criteria.andEqualTo("barcode", str);
                    List<MesSfcBarcodeProcess> mesSfcBarcodeProcesses = barcodeProcessService.selectByExample(example);
                    if (!mesSfcBarcodeProcesses.isEmpty()){
                        cleanBarcodeDto.setOrderBarCode(mesSfcBarcodeProcesses.get(0).getBarcode());
                    }
                }else if (str.contains("391-")){
                    cleanBarcodeDto.setSalesBarcode(str);
                }else {
                    cleanBarcodeDto.setCutsomerBarcode(str);
                }
            }
            if(StringUtils.isEmpty(cleanBarcodeDto.getOrderBarCode())){
                throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "平台中不存在此条码，不允许操作");
            }
            if (barcodeArr.length >= 3){
                if(StringUtils.isEmpty(cleanBarcodeDto.getOrderBarCode()) || StringUtils.isEmpty(cleanBarcodeDto.getSalesBarcode())){
                    throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(), "平台中不存在此销售条码，不允许操作");
                }
            }
        }

        /**
         * 4、判断包箱作业、栈板作业，区分方法
         */
        if ("1".equals(scanBarcodeDto.getType())){
            // 包箱
            PdaCartonWorkDto dto = new PdaCartonWorkDto();
            dto.setBarCode(cleanBarcodeDto.getOrderBarCode());
            dto.setStationId(scanBarcodeDto.getStationId());
            dto.setProcessId(scanBarcodeDto.getProcessId());
            dto.setProLineId(scanBarcodeDto.getProLineId());
            dto.setAnnex(false);
            dto.setCheckOrNot(false);
            dto.setPrint(false);
            dto.setPackType("1");
            if (StringUtils.isNotEmpty(cleanBarcodeDto.getCutsomerBarcode()) || StringUtils.isNotEmpty(cleanBarcodeDto.getSalesBarcode())){
                dto.setAnnex(true);
                if (StringUtils.isNotEmpty(cleanBarcodeDto.getCutsomerBarcode())){
                    dto.setBarAnnexCode(cleanBarcodeDto.getCutsomerBarcode());
                    mesSfcBarcodeOperationService.pdaCartonWork(dto);
                }
                if (StringUtils.isNotEmpty(cleanBarcodeDto.getSalesBarcode())){
                    dto.setBarAnnexCode(cleanBarcodeDto.getSalesBarcode());
                    mesSfcBarcodeOperationService.pdaCartonWork(dto);
                }
            }else {
                mesSfcBarcodeOperationService.pdaCartonWork(dto);
            }
        }else if ("2".equals(scanBarcodeDto.getType())){
            // 栈板
            RequestPalletWorkScanDto dto = new RequestPalletWorkScanDto();
            dto.setStationId(scanBarcodeDto.getStationId());
            dto.setProcessId(scanBarcodeDto.getProcessId());
            dto.setProLineId(scanBarcodeDto.getProLineId());
            dto.setBarcode(cleanBarcodeDto.getOrderBarCode());
            dto.setMaxPalletNum(1);
            dto.setCheckdaliyOrder((byte) 0);
            dto.setPrintBarcode((byte) 0);
            dto.setPalletType((byte) 2);
//            dto.setIsReadHead(true);
            PalletWorkScanDto scanDto = mesSfcPalletWorkService.palletWorkScanBarcode(dto);

            /*WanbaoStackingMQDto wanbaoStackingMQDto = new WanbaoStackingMQDto();
            wanbaoStackingMQDto.setCode(0);
            wanbaoStackingMQDto.setStackingCode(scanDto.getPalletCode());
            String stackingLine = scanDto.getPalletCode().substring(scanDto.getPalletCode().length() - 1);
            wanbaoStackingMQDto.setStackingLine(stackingLine);
            return  wanbaoStackingMQDto;*/
        }else if ("3".equals(scanBarcodeDto.getType())){
            // 出库

        }
        return new WanbaoStackingMQDto();
    }

}
