package com.fantechs.service.impl;

import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.utils.DateUtils;
import com.fantechs.entity.ProductDailyPlanModel;
import com.fantechs.entity.search.ProductionBatch;
import com.fantechs.entity.search.SearchProductDailyPlan;
import com.fantechs.mapper.ProductDailyPlanMapper;
import com.fantechs.service.ProductDailyPlanService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.NumberFormat;
import java.util.*;

/**
 * @Author
 * @Date 2021/12/01
 */
@Service
public class ProductDailyPlanServiceImpl implements ProductDailyPlanService {

    @Resource
    private ProductDailyPlanMapper productDailyPlanMapper;

    @Override
    public List<ProductDailyPlanModel> findList(Map<String, Object> map) {
        return productDailyPlanMapper.findList(map);
    }

    @Override
    public List<ProductionBatch> findBatchList() {

        Calendar c= Calendar.getInstance();
        SearchProductDailyPlan searchProductDailyPlan = new SearchProductDailyPlan();

        //查询前七天的批次完成率
        List<ProductionBatch> productionBatches = new ArrayList<>();
        for (int i = 1; i <= 7; i++) {

            // 设置精确到小数点后2位,可以写0不带小数位
            NumberFormat numberFormat = NumberFormat.getInstance();
            numberFormat.setMaximumFractionDigits(2);

            c.setTime(new Date());
            c.add(Calendar.DATE, -i);
            searchProductDailyPlan.setPlanDateBegin(DateUtils.format(c.getTime(),"yyyy-MM-dd"));//设置日期
            searchProductDailyPlan.setPlanDateEnd(DateUtils.format(c.getTime(),"yyyy-MM-dd"));//设置日期
//            searchProductDailyPlan.setProLineId(2370L);

            //查询日计划
            List<ProductDailyPlanModel> productDailyPlanModels = productDailyPlanMapper.findList(ControllerUtil.dynamicConditionByEntity(searchProductDailyPlan));

            //计算完成工单的数量
            int l = 0;
            for (ProductDailyPlanModel productDailyPlanModel : productDailyPlanModels) {
                if(productDailyPlanModel.getWorkOrderStatus().equals("6")){
                    l++;
                }
            }

            //计算完成率
            ProductionBatch productionBatch = new ProductionBatch();
            productionBatch.setUdate(searchProductDailyPlan.getPlanDateBegin());
            if (l == 0) {
                productionBatch.setUnumber("0");
            }else {
                productionBatch.setUnumber(numberFormat.format((float) l / (float)productDailyPlanModels.size() * 100));
            }
            productionBatches.add(productionBatch);
        }

        return productionBatches;
    }

    public static void main(String[] args) {


        Calendar c = Calendar.getInstance();

        for (int i = 1; i <= 7; i++) {
            c.setTime(new Date());
            c.add(Calendar.DATE, -i);
            Date d = c.getTime();
            System.out.print("d = " + DateUtils.format(d,"yyyy-MM-dd") + "///");
            c.setTime(new Date());
            c.add(Calendar.DATE, -i + 1);
            d = c.getTime();
            System.out.println("d = " + DateUtils.format(d,"yyyy-MM-dd"));
        }
    }
}
