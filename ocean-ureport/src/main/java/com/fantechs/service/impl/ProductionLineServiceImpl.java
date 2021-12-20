package com.fantechs.service.impl;

import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.entity.*;
import com.fantechs.entity.search.SearchProLineBoard;
import com.fantechs.entity.search.SearchProductDailyPlan;
import com.fantechs.mapper.ProLineBoardMapper;
import com.fantechs.mapper.ProductDailyPlanMapper;
import com.fantechs.mapper.ProductionLineMapper;
import com.fantechs.service.ProductionLineService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.NumberFormat;
import java.util.List;

@Service
public class ProductionLineServiceImpl implements ProductionLineService {

    @Resource
    private ProductionLineMapper productionLineMapper;
    @Resource
    private ProLineBoardMapper proLineBoardMapper;
    @Resource
    private ProductDailyPlanMapper productDailyPlanMapper;

    @Override
    public ProductionLine findList(SearchProductDailyPlan searchProductDailyPlan) {

        // 设置精确到小数点后2位,可以写0不带小数位
        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setMaximumFractionDigits(2);

        //返回对象
        ProductionLine productionLine = new ProductionLine();

        //Top内容
        //查询当日计划排产数
//        searchProductDailyPlan.setPlanDateBegin(DateUtil.format(new Date(),"yyyy-MM-dd"));
//        searchProductDailyPlan.setPlanDateEnd(DateUtil.format(new Date(),"yyyy-MM-dd"));
        ProductLineTop productLineTop = productionLineMapper.findTopList(ControllerUtil.dynamicConditionByEntity(searchProductDailyPlan));

        if(productLineTop == null){
            productLineTop = new ProductLineTop();
            productLineTop.setScheduledQty(0L);
            productLineTop.setOutputQty(0L);
            productLineTop.setNoOutputQty(0L);
        }
        //查询当日LQC完工数
        SearchProLineBoard searchProLineBoard = new SearchProLineBoard();
        searchProLineBoard.setStartTime(searchProductDailyPlan.getPlanDateBegin());
        searchProLineBoard.setEndTime(searchProductDailyPlan.getPlanDateEnd());
        searchProLineBoard.setProcessCode("GY01-20");
        searchProLineBoard.setBarcodeStatus((byte)1);
        searchProLineBoard.setIsDistinct((byte)1);//去重
        productLineTop.setOutputQty(proLineBoardMapper.findBarCodeRecordList(searchProLineBoard));//

        if(!productLineTop.getScheduledQty().toString().isEmpty() && !productLineTop.getOutputQty().toString().isEmpty()){
            productLineTop.setNoOutputQty(productLineTop.getScheduledQty() - productLineTop.getOutputQty());
        }

        productionLine.setProductLineTop(productLineTop);

        //left工单内容
        ProductLineLeft productLineLeft;
        productLineLeft = new ProductLineLeft();
        //查询最新的LQC过站数据，当作当前工单
        searchProLineBoard = new SearchProLineBoard();
        searchProLineBoard.setProcessCode("GY01-20");
//        searchProLineBoard.setProLineId("");
        productLineLeft = proLineBoardMapper.findLastBarCideRecirdList(searchProLineBoard);//

        //一张工单可能存在多个排产计划
        List<ProductDailyPlanModel> productDailyPlanModels = productDailyPlanMapper.findList(ControllerUtil.dynamicCondition("workOrderCode",productLineLeft.getWorkOrderCode()));

        if(productDailyPlanModels.size() > 0){

            productLineLeft.setWorkOrderCode(productDailyPlanModels.get(0).getWorkOrderCode());//工单编号
            productLineLeft.setScheduledQty(productDailyPlanModels.get(0).getScheduledQty().longValue());//排产数
            productLineLeft.setMaterialName(productDailyPlanModels.get(0).getMaterialName());//物料名称
            productLineLeft.setWorkOrderQty(productDailyPlanModels.get(0).getWorkOrderQty().longValue());//工单数

            //排产总数
            Long scheduledQtyL = 0L;
            //投产总数
            Long productionQtyL = 0L;
            for (ProductDailyPlanModel productDailyPlanModel : productDailyPlanModels) {
                scheduledQtyL += productDailyPlanModel.getScheduledQty().longValue();
                productionQtyL += productDailyPlanModel.getProductionQty().longValue();
            }
            productLineLeft.setProductionQty(productionQtyL);//投产总数
            productLineLeft.setNoProductionQty(productLineLeft.getWorkOrderQty() - scheduledQtyL);//尚欠，工单数量减去总排产数

            searchProLineBoard.setBarcodeStatus((byte)1);
            searchProLineBoard.setIsDistinct((byte)1);//去重
            searchProLineBoard.setWorkOrderCode(productLineLeft.getWorkOrderCode());
            productLineLeft.setOutputQty(proLineBoardMapper.findBarCodeRecordList(searchProLineBoard));//LQC总下线数量
            productLineLeft.setOutputRate(numberFormat.format((float)productLineLeft.getOutputQty()/(float)scheduledQtyL * 100));//完成率 实际产出/排产总数*100%

            //查询当前工单序号
            searchProLineBoard = new SearchProLineBoard();
            searchProLineBoard.setWorkOrderId(productLineTop.getWorkOrderId());
//            searchProLineBoard.setStartTime(DateUtils.format(new Date(),"yyyy-MM-dd"));
//            searchProLineBoard.setEndTime(DateUtils.format(new Date(),"yyyy-MM-dd"));
            searchProLineBoard.setStartTime(searchProductDailyPlan.getPlanDateBegin());
            searchProLineBoard.setEndTime(searchProductDailyPlan.getPlanDateEnd());
            ProductDailyPlanModel productDailyPlanModel = proLineBoardMapper.findNextPlan(searchProLineBoard);
            if(productDailyPlanModel != null){
                //下一工单
                searchProLineBoard.setSeqNum(productDailyPlanModel.getSeqNum() +1);
                searchProLineBoard.setWorkOrderId(null);
                productDailyPlanModel = proLineBoardMapper.findNextPlan(searchProLineBoard);

                if(productDailyPlanModel != null){
                    productLineLeft.setNextMaterialName(productDailyPlanModel.getMaterialName());
                    productLineLeft.setNextscheduledQty(productDailyPlanModel.getScheduledQty().longValue());
                    productLineLeft.setNextWorkOrderCode(productDailyPlanModel.getWorkOrderCode());
                }
            }
            productionLine.setProductLineLeft(productLineLeft);
        }

        //right节拍
        if(productLineTop != null && productLineTop.getWorkOrderId() != null){
            List<ProductLineRight> productLineRights = proLineBoardMapper.findProductLineRight(ControllerUtil.dynamicCondition("workOrderId",productLineTop.getWorkOrderId()));
            productionLine.setProductLineRights(productLineRights);
        }

        return productionLine;
    }

}
