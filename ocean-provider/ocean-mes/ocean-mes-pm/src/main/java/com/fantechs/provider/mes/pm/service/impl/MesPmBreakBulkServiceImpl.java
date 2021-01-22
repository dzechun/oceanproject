package com.fantechs.provider.mes.pm.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.mes.pm.MesPmBreakBulkDetDto;
import com.fantechs.common.base.general.dto.mes.pm.MesPmBreakBulkDto;
import com.fantechs.common.base.general.dto.mes.pm.search.SearchMesPmBreakBulk;
import com.fantechs.common.base.general.dto.mes.pm.search.SearchMesPmBreakBulkDet;
import com.fantechs.common.base.general.entity.mes.pm.MesPmBreakBulk;
import com.fantechs.common.base.general.entity.mes.pm.MesPmBreakBulkDet;
import com.fantechs.common.base.general.entity.mes.pm.SmtWorkOrder;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CodeUtils;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.mes.pm.PMFeignApi;
import com.fantechs.provider.mes.pm.mapper.MesPmBreakBulkDetMapper;
import com.fantechs.provider.mes.pm.mapper.MesPmBreakBulkMapper;
import com.fantechs.provider.mes.pm.mapper.SmtWorkOrderMapper;
import com.fantechs.provider.mes.pm.service.MesPmBreakBulkService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author mr.lei
 * @date 2021-01-18 11:30:42
 */
@Service
public class MesPmBreakBulkServiceImpl extends BaseService<MesPmBreakBulk> implements MesPmBreakBulkService {

    @Resource
    private MesPmBreakBulkMapper mesPmBreakBulkMapper;
    @Resource
    private MesPmBreakBulkDetMapper mesPmBreakBulkDetMapper;
    @Resource
    private SmtWorkOrderMapper smtWorkOrderMapper;

    @Override
    public List<MesPmBreakBulkDto> findList(SearchMesPmBreakBulk searchMesPmBreakBulk) {
        List<MesPmBreakBulkDto> list = mesPmBreakBulkMapper.findList(searchMesPmBreakBulk);
        list.forEach(li->{
            Example example = new Example(MesPmBreakBulkDet.class);
            example.createCriteria().andEqualTo("breakBulkId",li.getBreakBulkId());
            List<MesPmBreakBulkDet> dtoList = mesPmBreakBulkDetMapper.selectByExample(example);
            li.setMesPmBreakBulkDets(dtoList);
        });
        return list;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public MesPmBreakBulk saveBreak(MesPmBreakBulk record) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        if(StringUtils.isEmpty(record.getMesPmBreakBulkDets())){
            throw new BizErrorException(ErrorCodeEnum.GL99990100);
        }
        record.setBreakBulkCode(CodeUtils.getId("BREAK"));
        //合批作业生成母批次号
        if(record.getBreakBulkType()==(byte)2){
            String batch = record.getMesPmBreakBulkDets().get(0).getChildLotNo();
            record.setBatchNo(batch.substring(0,batch.length()-2));
        }else{
            if(StringUtils.isEmpty(record.getBatchNo())){
                throw new BizErrorException(ErrorCodeEnum.valueOf("批次不能为空"));
            }
        }
        //查询当前工序
        MesPmBreakBulk mesPmBreakBulk = mesPmBreakBulkMapper.sleProcess(record.getBatchNo());
        if(StringUtils.isEmpty(mesPmBreakBulk)){
             throw new BizErrorException(ErrorCodeEnum.valueOf("获取当前工序失败！"));
        }
        record.setProcessId(mesPmBreakBulk.getProcessId());
        record.setCreateTime(new Date());
        record.setCreateUserId(user.getUserId());
        record.setModifiedTime(new Date());
        record.setModifiedUserId(user.getUserId());
        int num = mesPmBreakBulkMapper.insertUseGeneratedKeys(record);

        SmtWorkOrder smtWorkOrder = smtWorkOrderMapper.selectByPrimaryKey(record.getWorkOrderId());

        //添加明细
        int i = 1;
        for (MesPmBreakBulkDet mesPmBreakBulkDet : record.getMesPmBreakBulkDets()) {

            //拆批作业生产子批次号
            if(record.getBreakBulkType()==(byte)1){
                DecimalFormat df=new DecimalFormat("0");
                String no = df.format(i);
                mesPmBreakBulkDet.setChildLotNo(record.getBatchNo()+no);
            }else{
                //合批：状态：2：已合批生成工单
                mesPmBreakBulkDet.setStatus((byte)2);
            }
            mesPmBreakBulkDet.setBreakBulkId(record.getBreakBulkId());
            mesPmBreakBulkDet.setCreateTime(new Date());
            mesPmBreakBulkDet.setCreateUserId(user.getUserId());
            mesPmBreakBulkDet.setModifiedTime(new Date());
            mesPmBreakBulkDet.setModifiedUserId(user.getUserId());
            mesPmBreakBulkDetMapper.insertUseGeneratedKeys(mesPmBreakBulkDet);

            //拆批生成工单
            smtWorkOrder.setWorkOrderId(null);
            smtWorkOrder.setProductionQuantity(mesPmBreakBulkDet.getBreakBulkQty());
            smtWorkOrder.setCreateTime(new Date());
            smtWorkOrder.setCreateUserId(user.getUserId());
            smtWorkOrder.setModifiedTime(new Date());
            smtWorkOrder.setModifiedUserId(user.getUserId());
            smtWorkOrder.setWorkOrderCode(CodeUtils.getId("WORK"));
            smtWorkOrderMapper.insertSelective(smtWorkOrder);
        }
        if(record.getBreakBulkType()==(byte)2){
            //合批生成工单
            smtWorkOrder.setWorkOrderId(null);
            smtWorkOrder.setProductionQuantity(record.getBreakBulkBatchQty());
            smtWorkOrder.setCreateTime(new Date());
            smtWorkOrder.setCreateUserId(user.getUserId());
            smtWorkOrder.setModifiedTime(new Date());
            smtWorkOrder.setModifiedUserId(user.getUserId());
            smtWorkOrder.setWorkOrderCode(CodeUtils.getId("WORK"));
            smtWorkOrderMapper.insertSelective(smtWorkOrder);
            record.setMesPmBreakBulkDets(null);
        }
        return record;
    }
}
