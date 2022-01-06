package com.fantechs.service.impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.fantechs.common.base.entity.security.SysSpecItem;
import com.fantechs.common.base.entity.security.search.SearchSysSpecItem;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.entity.QmsProcessModel;
import com.fantechs.entity.QmsProcessModelShow;
import com.fantechs.entity.QmsProcessPassRateModel;
import com.fantechs.entity.search.SearchProLineBoard;
import com.fantechs.mapper.ProLineBoardMapper;
import com.fantechs.mapper.QmsProcessMapper;
import com.fantechs.provider.api.security.service.SecurityFeignApi;
import com.fantechs.service.QmsProcessService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author
 * @Date 2021/12/25
 */
@Service
public class QmsProcessServiceImpl implements QmsProcessService {

    @Resource
    private QmsProcessMapper qmsProcessMapper;
    @Resource
    private SecurityFeignApi securityFeignApi;
    @Resource
    private ProLineBoardMapper proLineBoardMapper;

    /**
     * 获取投入数接口 分时间段
     *
     * @return List<QmsProcessModelShow>
     */
    @Override
    public List<QmsProcessModel> findDevoteQtyList(Map<String, Object> map) {
        map.put("nowDate", DateUtil.format(new Date(), DatePattern.NORM_DATE_PATTERN));
        map.put("proLineId", 2370L);//高压精益线 固定
        map.put("processCode","GY01-01");//投产工序
        return qmsProcessMapper.findDevoteQtyList(map);
    }

    /**
     * 获取不良数接口 分时间段
     *
     * @return List<QmsProcessModelShow>
     */
    @Override
    public List<QmsProcessModel> findNotGoodList(Map<String, Object> map) {
        map.put("nowDate", DateUtil.format(new Date(), DatePattern.NORM_DATE_PATTERN));
        map.put("proLineId", 2370L);//高压精益线 固定
        return qmsProcessMapper.findNotGoodList(map);
    }

    /**
     * 获取合格率接口 分时间段
     *
     * @return List<QmsProcessModelShow>
     */
    @Override
    public List<QmsProcessModelShow> findList() {
        //获取系统配置项 目标合格率 goalQualifiedRate
        String goalQualifiedRate = "0";
        String resultS = getSysSpecItem("goalQualifiedRate");
        if (StringUtils.isNotEmpty(resultS)) {
            goalQualifiedRate = resultS;
        }

        List<QmsProcessModelShow> modelShowList = new ArrayList<>();
        //时间段
        String timeArea = "08-09,09-10,10-11,11-12,12-13,13-14,14-15,15-16,16-17,17-18,18-19,19-20,20-21,21-22";
        Map<String, Object> map = new HashMap<>();
        List<QmsProcessModel> processModels = this.findDevoteQtyList(map);//当天总投入数
        map.put("sectionCode","1000-10");//组装段
        List<QmsProcessModel> processNotGoods = this.findNotGoodList(map);//不良数

        String[] arrayList = timeArea.split(",");
        for (String s : arrayList) {
            QmsProcessModelShow modelShow = new QmsProcessModelShow();

            String[] arrayTime = s.split("-");
            List<QmsProcessModel> processModelList = processModels.stream().filter(u -> (Integer.parseInt(u.getDevoteHour()) == Integer.parseInt(arrayTime[0]))).collect(Collectors.toList());
            List<QmsProcessModel> processNotGoodList = processNotGoods.stream().filter(u -> (Integer.parseInt(u.getDevoteHour()) == Integer.parseInt(arrayTime[0]))).collect(Collectors.toList());
            modelShow.setDevoteHour(s);
            modelShow.setDevoteQty(processModelList.size());
            modelShow.setNotGoodQty(processNotGoodList.size());
            if (processModelList.size() > 0 && processNotGoodList.size() >= 0) {
                Double numerator = Double.valueOf(processNotGoodList.size());
                Double denominator = Double.valueOf(processModelList.size());
                Double result = (1 - numerator / denominator) * 100;
                String rate = String.format("%.2f", result) + "%";
                modelShow.setQualifiedRate(rate);
            }

            //设置目标合格率
            modelShow.setGoalQualifiedRate(goalQualifiedRate);

            modelShowList.add(modelShow);
        }
        return modelShowList;
    }

    /**
     * 获取工序合格率接口
     *
     * @return List<QmsProcessModelShow>
     */
    @Override
    public Map<String,List<QmsProcessModelShow>> findProcessRateList() {
        Map<String,List<QmsProcessModelShow>> resultmap = new HashMap<>();
        List<String> sectionList = new ArrayList<>();
        sectionList.add("1000-10");//组装
        sectionList.add("1000-20");//LQC
        sectionList.add("1000-40");//FQC

        for (String str : sectionList) {
            //获取系统配置项 目标合格率 goalQualifiedRate
            String goalQualifiedRate = "0";
            String resultS = getSysSpecItem("goalQualifiedRate");
            if (StringUtils.isNotEmpty(resultS)) {
                goalQualifiedRate = resultS;
            }
            List<QmsProcessModelShow> modelShowList = new ArrayList<>();
            String timeArea = "08-09,09-10,10-11,11-12,12-13,13-14,14-15,15-16,16-17,17-18,18-19,19-20,20-21,21-22";
            Map<String, Object> map = new HashMap<>();

            List<QmsProcessModel> processModels = this.findDevoteQtyList(map);

            map.put("sectionCode",str);//组装段
            List<QmsProcessModel> processNotGoods = this.findNotGoodList(map);

            String[] arrayList = timeArea.split(",");
            for (String s : arrayList) {
                QmsProcessModelShow modelShow = new QmsProcessModelShow();

                String[] arrayTime = s.split("-");
                List<QmsProcessModel> processModelList = processModels.stream().filter(u -> (Integer.parseInt(u.getDevoteHour()) == Integer.parseInt(arrayTime[0]))).collect(Collectors.toList());
                List<QmsProcessModel> processNotGoodList = processNotGoods.stream().filter(u -> (Integer.parseInt(u.getDevoteHour()) == Integer.parseInt(arrayTime[0]))).collect(Collectors.toList());
                modelShow.setDevoteHour(s);
                modelShow.setDevoteQty(processModelList.size());
                modelShow.setNotGoodQty(processNotGoodList.size());
                if (processModelList.size() > 0 && processNotGoodList.size() >= 0) {
                    Double numerator = Double.valueOf(processNotGoodList.size());
                    Double denominator = Double.valueOf(processModelList.size());
                    Double result = (1 - numerator / denominator) * 100;
                    String rate = String.format("%.2f", result) + "%";
                    modelShow.setQualifiedRate(rate);
                }

                //设置目标合格率
                modelShow.setGoalQualifiedRate(goalQualifiedRate);

                modelShowList.add(modelShow);
                if(str.equals("1000-10")){
                    resultmap.put("组装段",modelShowList);
                }
                if(str.equals("1000-20")){
                    resultmap.put("LQC",modelShowList);
                }
                if(str.equals("1000-40")){
                    resultmap.put("FQC",modelShowList);
                }
        }

        }
        return resultmap;
    }

    /**
     * 获取工序过站数接口
     *
     * @return List<QmsProcessModel>
     */
    @Override
    public List<QmsProcessModel> findProcessQtyList(Map<String, Object> map) {
        map.put("nowDate", DateUtil.format(new Date(), DatePattern.NORM_DATE_PATTERN));
        map.put("proLineId", 2370L);
        return qmsProcessMapper.findProcessQtyList(map);
    }

    /**
     * 获取工序不良数接口
     *
     * @return List<QmsProcessModel>
     */
    @Override
    public List<QmsProcessModel> findProcessNotGoodQtyList(Map<String, Object> map) {
        map.put("nowDate", DateUtil.format(new Date(), DatePattern.NORM_DATE_PATTERN));
        map.put("proLineId", 2370L);
        return qmsProcessMapper.findProcessNotGoodQtyList(map);
    }

    /**
     * 通过率接口
     *
     * @return List<QmsProcessPassRateModel>
     */
    @Override
    public List<QmsProcessPassRateModel> findProcessPassRateList() {
        List<QmsProcessPassRateModel> resultList = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        //获取系统配置项 预警良率 alarmRate
        String alarmRate = "0";
        String resultAlarm = getSysSpecItem("alarmRate");
        if (StringUtils.isNotEmpty(resultAlarm)) {
            alarmRate = resultAlarm;
        }

        //获取系统配置项 停线良率 stopLineRate
        String stopLineRate = "0";
        String resultStop = getSysSpecItem("stopLineRate");
        if (StringUtils.isNotEmpty(resultStop)) {
            stopLineRate = resultStop;
        }

        List<String> processList = new ArrayList<>();
        processList.add("GY01-14");//;组装最后一道
        processList.add("GY01-20");//;Lqc
        processList.add("GY01-40");//;Fqc

        SearchProLineBoard searchProLineBoard = new SearchProLineBoard();
        //当天总投入数
        long putIn = (long)this.findDevoteQtyList(map).size();
        if(putIn != 0){
            //每一个工段的产出
            searchProLineBoard.setBarcodeStatus((byte)1);
            searchProLineBoard.setIsDistinct((byte)1);//去重
            searchProLineBoard.setPassStationCount((byte)1);
            searchProLineBoard.setNowDate(DateUtil.format(new Date(), DatePattern.NORM_DATE_PATTERN));

            searchProLineBoard.setProcessCode("GY01-50");
            long BZ = proLineBoardMapper.findBarCodeRecordList(searchProLineBoard);
            long rlt = BZ / putIn * 100;
            String allRate = String.format("%.2f", rlt) + "%"; //总通过率

            for (String str : processList) {
                QmsProcessPassRateModel passRateModel = new QmsProcessPassRateModel();
                passRateModel.setAlarmRate(alarmRate);
                passRateModel.setStopLineRate(stopLineRate);

                if(str.equals("GY01-14")){

                    searchProLineBoard.setProcessCode(str);
                    long ZZ = proLineBoardMapper.findBarCodeRecordList(searchProLineBoard);
                    long result = ZZ / putIn * 100;
                    String rate = String.format("%.2f", result) + "%";

                    passRateModel.setPassRate(rate);
                    passRateModel.setProcessName("组装");
                    passRateModel.setTotalPassRate(allRate);
                    resultList.add(passRateModel);
                }

                if(str.equals("GY01-20")){
                    searchProLineBoard.setProcessCode(str);
                    long LQC = proLineBoardMapper.findBarCodeRecordList(searchProLineBoard);
                    long result = LQC / putIn * 100;
                    String rate = String.format("%.2f", result) + "%";

                    passRateModel.setPassRate(rate);
                    passRateModel.setProcessName("LQC");
                    passRateModel.setTotalPassRate(allRate);
                    resultList.add(passRateModel);
                }

                if(str.equals("GY01-40")){
                    searchProLineBoard.setProcessCode(str);
                    long FQC = proLineBoardMapper.findBarCodeRecordList(searchProLineBoard);
                    long result = FQC / putIn * 100;
                    String rate = String.format("%.2f", result) + "%";

                    passRateModel.setPassRate(rate);
                    passRateModel.setProcessName("FQC");
                    passRateModel.setTotalPassRate(allRate);
                    resultList.add(passRateModel);
                }
            }
        }

        return resultList;
    }

    public String getSysSpecItem(String specCode) {
        String result = "";
        SearchSysSpecItem searchSysSpecItem = new SearchSysSpecItem();
        searchSysSpecItem.setSpecCode(specCode);
        ResponseEntity<List<SysSpecItem>> sysSpecItemList = securityFeignApi.findSpecItemList(searchSysSpecItem);
        if (StringUtils.isNotEmpty(sysSpecItemList.getData().get(0))) {
            result = sysSpecItemList.getData().get(0).getParaValue();
        }
        return result;
    }
}
