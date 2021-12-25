package com.fantechs.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.fantechs.common.base.entity.security.SysSpecItem;
import com.fantechs.common.base.entity.security.search.SearchSysSpecItem;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.entity.QmsProcessModel;
import com.fantechs.entity.QmsProcessModelShow;
import com.fantechs.entity.QmsProcessPassRateModel;
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

    /**
     * 获取投入数接口 分时间段
     * @return List<QmsProcessModelShow>
     */
    @Override
    public List<QmsProcessModel> findDevoteQtyList(Map<String, Object> map) {
        map.put("nowDate", DateUtil.format(new Date(), DatePattern.NORM_DATE_PATTERN));
        map.put("proLineId",119L);
        return qmsProcessMapper.findDevoteQtyList(map);
    }

    /**
     * 获取不良数接口 分时间段
     * @return List<QmsProcessModelShow>
     */
    @Override
    public List<QmsProcessModel> findNotGoodList(Map<String, Object> map) {
        map.put("nowDate", DateUtil.format(new Date(), DatePattern.NORM_DATE_PATTERN));
        map.put("proLineId",119L);
        return qmsProcessMapper.findNotGoodList(map);
    }

    /**
     * 获取合格率接口 分时间段
     * @return List<QmsProcessModelShow>
     */
    @Override
    public List<QmsProcessModelShow> findList() {
        //获取系统配置项 目标合格率 goalQualifiedRate
        String goalQualifiedRate="0";
        String resultS=getSysSpecItem("goalQualifiedRate");
        if(StringUtils.isNotEmpty(resultS)){
            goalQualifiedRate=resultS;
        }

        List<QmsProcessModelShow> modelShowList=new ArrayList<>();
        String timeArea="08-09,09-10,10-11,11-12,12-13,13-14,14-15,15-16,16-17,17-18,18-19,19-20,20-21,21-22";
        Map<String, Object> map=new HashMap<>();

        List<QmsProcessModel> processModels=this.findDevoteQtyList(map);
        List<QmsProcessModel> processNotGoods=this.findNotGoodList(map);
        
        String[] arrayList=timeArea.split(",");
        for (String s : arrayList) {
            QmsProcessModelShow modelShow=new QmsProcessModelShow();

            String[] arrayTime=s.split("-");
            List<QmsProcessModel> processModelList = processModels.stream().filter(u -> (Integer.parseInt(u.getDevoteHour())==Integer.parseInt(arrayTime[0]))).collect(Collectors.toList());
            List<QmsProcessModel> processNotGoodList=processNotGoods.stream().filter(u -> (Integer.parseInt(u.getDevoteHour())==Integer.parseInt(arrayTime[0]))).collect(Collectors.toList());
            modelShow.setDevoteHour(s);
            modelShow.setDevoteQty(processModelList.size());
            modelShow.setNotGoodQty(processNotGoodList.size());
            if(processModelList.size()>0 && processNotGoodList.size()>0){
                Double numerator=Double.valueOf(processNotGoodList.size());
                Double denominator=Double.valueOf(processModelList.size());
                Double result=numerator/denominator*100;
               String rate= String.format("%.2f",result)+"%";
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
     * @return List<QmsProcessModelShow>
     */
    @Override
    public List<QmsProcessModelShow> findProcessRateList() {
        //获取系统配置项 目标合格率 goalQualifiedRate
        String goalQualifiedRate="0";
        String resultS=getSysSpecItem("goalQualifiedRate");
        if(StringUtils.isNotEmpty(resultS)){
            goalQualifiedRate=resultS;
        }
        List<QmsProcessModelShow> modelShowList=new ArrayList<>();
        String timeArea="08-09,09-10,10-11,11-12,12-13,13-14,14-15,15-16,16-17,17-18,18-19,19-20,20-21,21-22";
        Map<String, Object> map=new HashMap<>();
        List<Long> processIdList= new ArrayList<>();
        //按工序过滤
        processIdList.add(3L);
        processIdList.add(4L);
        map.put("processIdList",processIdList);

        List<QmsProcessModel> processModels=this.findDevoteQtyList(map);
        List<QmsProcessModel> processNotGoods=this.findNotGoodList(map);

        String[] arrayList=timeArea.split(",");
        for (String s : arrayList) {
            QmsProcessModelShow modelShow=new QmsProcessModelShow();

            String[] arrayTime=s.split("-");
            List<QmsProcessModel> processModelList = processModels.stream().filter(u -> (Integer.parseInt(u.getDevoteHour())==Integer.parseInt(arrayTime[0]))).collect(Collectors.toList());
            List<QmsProcessModel> processNotGoodList=processNotGoods.stream().filter(u -> (Integer.parseInt(u.getDevoteHour())==Integer.parseInt(arrayTime[0]))).collect(Collectors.toList());
            modelShow.setDevoteHour(s);
            modelShow.setDevoteQty(processModelList.size());
            modelShow.setNotGoodQty(processNotGoodList.size());
            if(processModelList.size()>0 && processNotGoodList.size()>0){
                Double numerator=Double.valueOf(processNotGoodList.size());
                Double denominator=Double.valueOf(processModelList.size());
                Double result=numerator/denominator*100;
                String rate= String.format("%.2f",result)+"%";
                modelShow.setQualifiedRate(rate);
            }

            //设置目标合格率
            modelShow.setGoalQualifiedRate(goalQualifiedRate);

            modelShowList.add(modelShow);
        }
        return modelShowList;
    }

    /**
     * 获取工序过站数接口
     * @return List<QmsProcessModel>
     */
    @Override
    public List<QmsProcessModel> findProcessQtyList(Map<String, Object> map) {
        map.put("nowDate", DateUtil.format(new Date(), DatePattern.NORM_DATE_PATTERN));
        map.put("proLineId",119L);
        return qmsProcessMapper.findProcessQtyList(map);
    }

    /**
     * 获取工序不良数接口
     * @return List<QmsProcessModel>
     */
    @Override
    public List<QmsProcessModel> findProcessNotGoodQtyList(Map<String, Object> map) {
        map.put("nowDate", DateUtil.format(new Date(), DatePattern.NORM_DATE_PATTERN));
        map.put("proLineId",119L);
        return qmsProcessMapper.findProcessNotGoodQtyList(map);
    }

    /**
     * 通过率接口
     * @return List<QmsProcessPassRateModel>
     */
    @Override
    public List<QmsProcessPassRateModel> findProcessPassRateList() {
        List<QmsProcessPassRateModel> resultList=new ArrayList<>();
        Map<String, Object> map=new HashMap<>();
        //获取系统配置项 预警良率 alarmRate
        String alarmRate="0";
        String resultAlarm=getSysSpecItem("alarmRate");
        if(StringUtils.isNotEmpty(resultAlarm)){
            alarmRate=resultAlarm;
        }

        //获取系统配置项 停线良率 stopLineRate
        String stopLineRate="0";
        String resultStop=getSysSpecItem("stopLineRate");
        if(StringUtils.isNotEmpty(resultStop)){
            stopLineRate=resultStop;
        }
        //取数
        List<QmsProcessModel> processQtys=this.findProcessQtyList(map);
        List<QmsProcessModel> processNotGoods=this.findProcessNotGoodQtyList(map);

        List<Long> processIdList=new ArrayList<>();
        processIdList.add(129L);//组装
        processIdList.add(130L);//LQC
        processIdList.add(131L);//FQC
        Double totalRate=new Double("1");

        for (Long aLong : processIdList) {
            QmsProcessPassRateModel passRateModel=new QmsProcessPassRateModel();
            String processName="";
            if(aLong.longValue()==129L)
                processName="组装";
            else if(aLong.longValue()==130L)
                processName="LQC";
            else if(aLong.longValue()==131L)
                processName="FQC";

            Double tempRate=new Double("0");
            List<QmsProcessModel> processQtyList = processQtys.stream().filter(u -> (u.getProcessId().longValue()==(aLong.longValue()))).collect(Collectors.toList());
            List<QmsProcessModel> processNotGoodList = processNotGoods.stream().filter(u -> (u.getProcessId().longValue()==(aLong.longValue()))).collect(Collectors.toList());
            if(processQtyList.size()>0 && processNotGoodList.size()>0){
                if(processQtyList.get(0).getDevoteQty()>0 && processNotGoodList.get(0).getNotGoodQty()>0){
                    Double numerator=Double.valueOf(processNotGoodList.get(0).getNotGoodQty());
                    Double denominator=Double.valueOf(processQtyList.get(0).getDevoteQty());
                    Double result=numerator/denominator*100;
                    tempRate=Double.parseDouble(String.format("%.2f",numerator/denominator));
                    String rate= String.format("%.2f",result)+"%";
                    passRateModel.setPassRate(rate);

                    totalRate=totalRate*tempRate;
                    if(aLong.longValue()==130L) {
                        //如果是LQC工序 总的一次通过率乘多一次LQC工序的通过率(老化的通过率)
                        totalRate = totalRate * tempRate;
                    }
                }
            }
            else if(processQtyList.size()>0 && processNotGoodList.size()<=0){
                passRateModel.setPassRate("100%");
            }
            passRateModel.setProcessName(processName);
            passRateModel.setAlarmRate(alarmRate);
            passRateModel.setStopLineRate(stopLineRate);

            resultList.add(passRateModel);

            //老化==LQC
            if(aLong.longValue()==130L){
                QmsProcessPassRateModel passRateModelLao=new QmsProcessPassRateModel();
                BeanUtil.copyProperties(passRateModel,passRateModelLao);
                passRateModelLao.setProcessName("老化");
                resultList.add(passRateModelLao);
            }
        }

        //总通过率
        String totalRateS=String.format("%.2f",totalRate*100)+"%";
        for (QmsProcessPassRateModel passRateModel : resultList) {
            passRateModel.setTotalPassRate(totalRateS);
        }

        return resultList;
    }

    public String getSysSpecItem(String specCode){
        String result="";
        SearchSysSpecItem searchSysSpecItem = new SearchSysSpecItem();
        searchSysSpecItem.setSpecCode(specCode);
        ResponseEntity<List<SysSpecItem>> sysSpecItemList = securityFeignApi.findSpecItemList(searchSysSpecItem);
        if(StringUtils.isNotEmpty(sysSpecItemList.getData().get(0))){
            result=sysSpecItemList.getData().get(0).getParaValue();
        }
        return result;
    }
}
