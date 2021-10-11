package com.fantechs.service.impl;

import cn.hutool.core.date.DateUtil;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
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
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        //查询当天日计划的所有排产数量和完工数量

        searchProLineBoard.setStartTime(DateUtil.format(new Date(),"yyyy-MM-dd"));
        searchProLineBoard.setEndTime(DateUtil.format(new Date(),"yyyy-MM-dd"));
    //    searchProLineBoard.setStartTime("2021-09-30");
    //    searchProLineBoard.setEndTime("2021-09-30");
        searchProLineBoard.setOrgId(user.getOrganizationId());
        ProLineBoardModel model = proLineBoardMapper.findPlanList(searchProLineBoard);
        if(StringUtils.isNotEmpty(model)) {

            // 设置精确到小数点后2位,可以写0不带小数位
            NumberFormat numberFormat = NumberFormat.getInstance();
            numberFormat.setMaximumFractionDigits(2);

            String outputRate = "0%";
            if(StringUtils.isNotEmpty(model.getScheduledQty()) && StringUtils.isNotEmpty(model.getOutputQty())) {
                outputRate = numberFormat.format((float) model.getOutputQty() / (float)model.getScheduledQty() * 100)+"%";
            }

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

            //查询过站记录
            searchProLineBoard.setSectionName("组装");
            Long zzNum = proLineBoardMapper.findBarCodeRecordList(searchProLineBoard);
            searchProLineBoard.setSectionName("LQC测试");
            Long lqcNum = proLineBoardMapper.findBarCodeRecordList(searchProLineBoard);
            searchProLineBoard.setBarcodeStatus((byte)1);
            Long passNum = proLineBoardMapper.findBarCodeRecordList(searchProLineBoard);

            String passRate = "0%";
            if(StringUtils.isNotEmpty(passNum) && StringUtils.isNotEmpty(lqcNum) && StringUtils.isNotEmpty(zzNum)
            && (lqcNum + zzNum)!=0 ) {
                passRate = numberFormat.format((float) passNum / (float) (lqcNum + zzNum) * 100) + "%";
            }


            model.setEquipmentQty(equipMentNum);
            model.setUseQty(equipMentUseingNum);

            model.setOutputRate(outputRate);
            model.setPassRate(passRate);
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
