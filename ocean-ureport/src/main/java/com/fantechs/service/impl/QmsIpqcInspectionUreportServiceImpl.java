package com.fantechs.service.impl;

import com.fantechs.common.base.general.entity.qms.search.SearchQmsIpqcInspectionOrder;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.entity.QmsIpqcDtaticElectricityModel;
import com.fantechs.entity.QmsIpqcFirstArticleModel;
import com.fantechs.entity.QmsIpqcProcessInspectionModel;
import com.fantechs.entity.QmsIpqcSamplingModel;
import com.fantechs.mapper.QmsIpqcInspectionUreportMapper;
import com.fantechs.service.QmsIpqcInspectionUreportService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class QmsIpqcInspectionUreportServiceImpl implements QmsIpqcInspectionUreportService {

    @Resource
    public QmsIpqcInspectionUreportMapper qmsIpqcInspectionUreportMapperMapper;


    //静电地线检测
    @Override
    public List<QmsIpqcDtaticElectricityModel> findDtaticElectricityList(SearchQmsIpqcInspectionOrder searchQmsIpqcInspectionOrder){
        searchQmsIpqcInspectionOrder.setInspectionWayCode("SYS-JY010");
        //searchQmsIpqcInspectionOrder.setOrgId((long)29);
        List<QmsIpqcDtaticElectricityModel> list = qmsIpqcInspectionUreportMapperMapper.findDtaticElectricityList(ControllerUtil.dynamicConditionByEntity(searchQmsIpqcInspectionOrder));
        return list;
    }

    //抽检日报表
    @Override
    public List<QmsIpqcSamplingModel> findSamplingList(SearchQmsIpqcInspectionOrder searchQmsIpqcInspectionOrder){
        searchQmsIpqcInspectionOrder.setInspectionWayCode("SYS-JY011");
        //searchQmsIpqcInspectionOrder.setOrgId((long)29);
        List<QmsIpqcSamplingModel> samplingList = qmsIpqcInspectionUreportMapperMapper.findSamplingList(ControllerUtil.dynamicConditionByEntity(searchQmsIpqcInspectionOrder));
        return samplingList;
    }

    //首件检测
    @Override
    public List<QmsIpqcFirstArticleModel> findFirstArticleList(SearchQmsIpqcInspectionOrder searchQmsIpqcInspectionOrder) {
        searchQmsIpqcInspectionOrder.setInspectionWayCode("SYS-JY012");
        //searchQmsIpqcInspectionOrder.setOrgId((long)29);
        List<QmsIpqcFirstArticleModel> firstArticleList = qmsIpqcInspectionUreportMapperMapper.findFirstArticleList(ControllerUtil.dynamicConditionByEntity(searchQmsIpqcInspectionOrder));
        return firstArticleList;
    }

    //制程巡检
    @Override
    public List<QmsIpqcProcessInspectionModel> findProcessInspectionList(SearchQmsIpqcInspectionOrder searchQmsIpqcInspectionOrder) {
        searchQmsIpqcInspectionOrder.setInspectionWayCode("SYS-JY013");
        //searchQmsIpqcInspectionOrder.setOrgId((long)29);
        List<QmsIpqcProcessInspectionModel> processInspectionList = qmsIpqcInspectionUreportMapperMapper.findProcessInspectionList(ControllerUtil.dynamicConditionByEntity(searchQmsIpqcInspectionOrder));
        return processInspectionList;
    }


}
