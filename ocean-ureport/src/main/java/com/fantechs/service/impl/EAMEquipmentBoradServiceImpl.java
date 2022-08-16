package com.fantechs.service.impl;

import cn.hutool.core.date.DateUtil;
import com.fantechs.common.base.utils.DateUtils;
import com.fantechs.entity.EAMEquipmentBorad;
import com.fantechs.entity.ProLineBoardModel;
import com.fantechs.entity.search.SearchProLineBoard;
import com.fantechs.mapper.EAMEquipmentBoradMapper;
import com.fantechs.mapper.ProLineBoardMapper;
import com.fantechs.service.EAMEquipmentBoradService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

@Service
public class EAMEquipmentBoradServiceImpl implements EAMEquipmentBoradService {

    @Resource
    private EAMEquipmentBoradMapper eamEquipmentBoradMapper;
    @Resource
    private ProLineBoardMapper proLineBoardMapper;

    @Override
    public List<EAMEquipmentBorad> findList(SearchProLineBoard searchProLineBoard) throws ParseException {

        if (searchProLineBoard.getStartTime() == null || searchProLineBoard.getEndTime() == null) {
            Calendar c = Calendar.getInstance();
            c.setTime(new Date());
            searchProLineBoard.setStartTime(DateUtils.format(c.getTime(), "yyyy-MM-dd"));
            c.add(Calendar.DATE, +1);
            searchProLineBoard.setEndTime(DateUtils.format(c.getTime(), "yyyy-MM-dd"));
        }

        //查询当天日计划的所有排产数量和完工数量
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(new Date());
        calendar.add(calendar.DATE, 1);
        searchProLineBoard.setStartTime(DateUtil.format(new Date(), "yyyy-MM-dd"));
        searchProLineBoard.setEndTime(DateUtil.format(new Date(), "yyyy-MM-dd"));
        searchProLineBoard.setOrgId((long) 1000);
        ProLineBoardModel model = proLineBoardMapper.findPlanList(searchProLineBoard);

        List<EAMEquipmentBorad> eamEquipmentBarcodes = eamEquipmentBoradMapper.findEquipment(searchProLineBoard);

        if (eamEquipmentBarcodes != null && eamEquipmentBarcodes.size() > 0) {

            for (EAMEquipmentBorad eamEquipmentBorad : eamEquipmentBarcodes) {
                if (eamEquipmentBorad.getEquipmentBarCode().isEmpty()) {
                    eamEquipmentBarcodes.remove(eamEquipmentBorad);
                    continue;
                }
                searchProLineBoard.setEquipmentBarcode(eamEquipmentBorad.getEquipmentBarCode());
                eamEquipmentBorad.setProductNum(eamEquipmentBoradMapper.findEquipmentInfo(searchProLineBoard));//总过站数量
                searchProLineBoard.setBarcodeStatus((byte) 0);//NG数量
                eamEquipmentBorad.setNgNum(eamEquipmentBoradMapper.findEquipmentInfo(searchProLineBoard));//NG数量

                if (model != null && model.getScheduledQty() != null) {
                    // 设置精确到小数点后2位,可以写0不带小数位
                    NumberFormat numberFormat = NumberFormat.getInstance();
                    numberFormat.setMaximumFractionDigits(2);

                    //生产数量百分比
                    eamEquipmentBorad.setProductRate(numberFormat.format((float) eamEquipmentBorad.getProductNum() / (float) model.getScheduledQty() * 100));

                    //NG数量百分比
                    eamEquipmentBorad.setNgRate(numberFormat.format((float) eamEquipmentBorad.getNgNum() / (float) model.getScheduledQty() * 100));

                }

                Date nd = new Date();
                Long lnd = nd.getTime();
                if (!eamEquipmentBorad.getCreateDate().isEmpty()) {
                    Date oldDate = DateUtils.getStrToDate("yyyy-MM-dd HH:mm:ss", eamEquipmentBorad.getCreateDate());
                    Long ond = oldDate.getTime();
                    eamEquipmentBorad.setRuningTime(String.valueOf((lnd - ond) / 60000));//运行时间
                }


            }
        }

        return eamEquipmentBarcodes;
    }
}
