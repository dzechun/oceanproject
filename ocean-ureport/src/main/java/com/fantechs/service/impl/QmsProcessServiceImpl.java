package com.fantechs.service.impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.fantechs.entity.QmsProcessModel;
import com.fantechs.entity.QmsProcessModelShow;
import com.fantechs.mapper.QmsProcessMapper;
import com.fantechs.service.QmsProcessService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author
 * @Date 2021/12/01
 */
@Service
public class QmsProcessServiceImpl implements QmsProcessService {

    @Resource
    private QmsProcessMapper qmsProcessMapper;

    @Override
    public List<QmsProcessModel> findDevoteQtyList(Map<String, Object> map) {
        map.put("nowDate", DateUtil.format(new Date(), DatePattern.NORM_DATE_PATTERN));
        map.put("proLineId",119L);
        return qmsProcessMapper.findDevoteQtyList(map);
    }

    @Override
    public List<QmsProcessModel> findNotGoodList(Map<String, Object> map) {
        map.put("nowDate", DateUtil.format(new Date(), DatePattern.NORM_DATE_PATTERN));
        map.put("proLineId",119L);
        return qmsProcessMapper.findNotGoodList(map);
    }

    @Override
    public List<QmsProcessModelShow> findList() {
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

            modelShowList.add(modelShow);
        }
        return modelShowList;
    }

    @Override
    public List<QmsProcessModelShow> findProcessRateList() {
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

            modelShowList.add(modelShow);
        }
        return modelShowList;
    }
}
