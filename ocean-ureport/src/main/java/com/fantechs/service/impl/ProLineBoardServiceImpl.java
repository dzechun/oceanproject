package com.fantechs.service.impl;

import cn.hutool.core.date.DateUtil;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseProductYield;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.entity.BaseProductYield;
import com.fantechs.entity.ProLineBoardModel;
import com.fantechs.entity.search.SearchProLineBoard;
import com.fantechs.mapper.ProLineBoardMapper;
import com.fantechs.service.ProLineBoardService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ProLineBoardServiceImpl implements ProLineBoardService {

    @Resource
    private ProLineBoardMapper proLineBoardMapper;

    @Override
    public ProLineBoardModel findList(SearchProLineBoard searchProLineBoard) {
        //查询当天日计划的所有排产数量和完工数量

        searchProLineBoard.setStartTime(DateUtil.format(new Date(),"yyyy-MM-dd"));
        searchProLineBoard.setEndTime(DateUtil.format(new Date(),"yyyy-MM-dd"));
        searchProLineBoard.setOrgId((long)1000);
        ProLineBoardModel model = proLineBoardMapper.findPlanList(searchProLineBoard);
        if(StringUtils.isNotEmpty(model)) {

            // 设置精确到小数点后2位,可以写0不带小数位
            NumberFormat numberFormat = NumberFormat.getInstance();
            numberFormat.setMaximumFractionDigits(2);



            searchProLineBoard.setProLineId(model.getProLineId());
            //查询对应产线全部设备
            Long equipMentNum = proLineBoardMapper.findEquipMentList(searchProLineBoard);
            //查询对应产线使用设备
            List<Byte> equipmentStatus = new ArrayList<>();
            equipmentStatus.add((byte)4);
            equipmentStatus.add((byte)5);
            equipmentStatus.add((byte)6);
            searchProLineBoard.setEquipmentStatus(equipmentStatus);
            Long equipMentUseingNum = proLineBoardMapper.findEquipMentList(searchProLineBoard);

            //查询LQC工序OK的过站记录，需去重
            searchProLineBoard.setProcessCode("GY01-20");
            searchProLineBoard.setBarcodeStatus((byte)1);
            searchProLineBoard.setIsDistinct((byte)1);//去重
            Long passNum = proLineBoardMapper.findBarCodeRecordList(searchProLineBoard);//
            model.setOutputQty(passNum);//完成数

            String outputRate = "0";
            if(StringUtils.isNotEmpty(model.getScheduledQty()) && StringUtils.isNotEmpty(model.getOutputQty()) && model.getScheduledQty()>0 ) {
                outputRate = numberFormat.format((float) model.getOutputQty() / (float)model.getScheduledQty() * 100);
            }

            //查询LQC工序第一次OK的过站记录
            searchProLineBoard.setPassStationCount((byte)1);
            Long passNum2 = proLineBoardMapper.findBarCodeRecordList(searchProLineBoard);
            String passRate = "0";
            if(StringUtils.isNotEmpty(passNum) && StringUtils.isNotEmpty(passNum2) && (passNum + passNum2)!=0 ) {
                passRate = numberFormat.format((float) passNum2 / (float) model.getOutputQty() * 100);
            }

            String operationRatio = numberFormat.format((float) equipMentUseingNum / (float)equipMentNum * 100);

            //查询预警良率和停线良率
            SearchBaseProductYield searchBaseProductYield = new SearchBaseProductYield();
            searchBaseProductYield.setProLineId(model.getProLineId());
            searchBaseProductYield.setYieldType((byte)2);
            BaseProductYield yieldList = null;
            yieldList = proLineBoardMapper.findYieldList(searchBaseProductYield);
            if(StringUtils.isEmpty(yieldList)){
                SearchBaseProductYield searchBaseProductYield1 = new SearchBaseProductYield();
                searchBaseProductYield1.setYieldType((byte)1);
                yieldList = proLineBoardMapper.findYieldList(searchBaseProductYield1);
                if(StringUtils.isEmpty(yieldList)) throw new BizErrorException("未查询到默认的产品良率配置");
            }

            model.setEquipmentQty(equipMentNum);//设备数量
            model.setUseQty(equipMentUseingNum);//使用数量
            model.setWarningRate(numberFormat.format(yieldList.getWarningYield()));
            model.setStopProLineRate (numberFormat.format(yieldList.getProductlineStopYield()));

            model.setOutputRate(outputRate);//完成率
            model.setPassRate(passRate);//直通率
            model.setOperationRatio(operationRatio);//稼动率
        }
        return model;
    }

    public static void main(String[] args) {


        int num1 = 10;

        int num2 = 100;

        // 创建一个数值格式化对象

        NumberFormat numberFormat = NumberFormat.getInstance();

        // 设置精确到小数点后2位,可以写0不带小数位

        numberFormat.setMaximumFractionDigits(2);

        String result = numberFormat.format((float) num1 / (float) num2 * 100)+"%";

        System.out.println("num1和num2的百分比为:" + result);
    }



}
