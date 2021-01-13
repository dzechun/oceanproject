package com.fantechs.provider.pm.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.dto.apply.SmtWorkOrderReportDto;
import com.fantechs.common.base.entity.apply.SmtWorkOrder;
import com.fantechs.common.base.entity.apply.SmtWorkOrderReport;
import com.fantechs.common.base.entity.apply.search.SearchSmtWorkOrderReport;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.pm.mapper.SmtWorkOrderMapper;
import com.fantechs.provider.pm.mapper.SmtWorkOrderReportMapper;
import com.fantechs.provider.pm.service.SmtWorkOrderReportService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
* @author Mr.Lei
* @create 2020/11/21
*/
@Service
public class SmtWorkOrderReportServiceImpl  extends BaseService<SmtWorkOrderReport> implements SmtWorkOrderReportService {

    @Resource
    private SmtWorkOrderReportMapper smtWorkOrderReportMapper;
    @Resource
    private SmtWorkOrderMapper smtWorkOrderMapper;


    @Override
    public List<SmtWorkOrderReportDto> findList(SearchSmtWorkOrderReport searchSmtWorkOrderReport) {
        return smtWorkOrderReportMapper.findList(searchSmtWorkOrderReport);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(SmtWorkOrderReport record) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        SmtWorkOrder smtWorkOrder = smtWorkOrderMapper.selectByWorkOrderId(record.getWorkOrderId());
        if(record.getCompletedQuantity().intValue()>smtWorkOrder.getWorkOrderQuantity().intValue()){
            throw new BizErrorException("报工数量大于工单数量");
        }

        //保留此功能
//        Map<String,Object> map = new HashMap<>();
//        map.put("specCode","orderReport");
//        List<SysSpecItem> sysSpecItemList = RestTemplateUtil.postForListEntity("http://127.0.0.1:8768/sysSpecItem/findList",map,SysSpecItem.class);
//        if(sysSpecItemList.size()>1){
//            //判断系统配置是否开启ERP开启则将报工数量上传ERP
//        }

        record.setCreateUserId(currentUser.getUserId());
        record.setCreateTime(new Date());
        record.setModifiedUserId(currentUser.getUserId());
        record.setModifiedTime(new Date());
        return smtWorkOrderReportMapper.insertSelective(record);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(SmtWorkOrderReport entity) {
        SysUser sys = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(sys)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        entity.setModifiedUserId(sys.getUserId());
        entity.setModifiedTime(new Date());
        return smtWorkOrderReportMapper.updateByPrimaryKeySelective(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDelete(String ids) {
        SysUser sys = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(sys)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        String[] items = ids.split(",");
        for (String item : items) {
            SmtWorkOrderReport smtWorkOrderReport = smtWorkOrderReportMapper.selectByPrimaryKey(item);
            if(StringUtils.isEmpty(smtWorkOrderReport)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
        }
        return smtWorkOrderReportMapper.deleteByIds(ids);
    }
}
