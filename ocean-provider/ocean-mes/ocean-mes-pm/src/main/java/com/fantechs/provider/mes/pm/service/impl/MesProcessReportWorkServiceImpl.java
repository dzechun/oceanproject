package com.fantechs.provider.mes.pm.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.mes.pm.MesProcessReportWorkDto;
import com.fantechs.common.base.general.dto.mes.pm.search.SearchMesProcessReportWork;
import com.fantechs.common.base.general.entity.basic.BaseStaffProcess;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseStaffProcess;
import com.fantechs.common.base.general.entity.mes.pm.MesProcessReportWork;
import com.fantechs.common.base.general.entity.mes.pm.MesPmWorkOrder;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CodeUtils;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.mes.pm.mapper.MesProcessReportWorkMapper;
import com.fantechs.provider.mes.pm.service.MesProcessReportWorkService;
import com.fantechs.provider.mes.pm.service.MesPmWorkOrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 *
 * Created by leifengzhi on 2021/03/19.
 */
@Service
public class MesProcessReportWorkServiceImpl extends BaseService<MesProcessReportWork> implements MesProcessReportWorkService {

    @Resource
    private MesProcessReportWorkMapper mesProcessReportWorkMapper;

    @Resource
    private MesPmWorkOrderService mesPmWorkOrderService;

    @Resource
    private BaseFeignApi baseFeignApi;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(MesProcessReportWork mesProcessReportWork) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        if (StringUtils.isNotEmpty(mesProcessReportWork.getStartTime(),mesProcessReportWork.getEndTime())) {
            if (mesProcessReportWork.getEndTime().before(mesProcessReportWork.getStartTime())) {
                throw new BizErrorException("报工结束时间不能小于报工开始时间！");
            }
        }
        if (mesProcessReportWork.getProcessAndStaff() == 0) {
            SearchBaseStaffProcess searchBaseStaffProcess = new SearchBaseStaffProcess();
            searchBaseStaffProcess.setProcessId(mesProcessReportWork.getProcessId());
            searchBaseStaffProcess.setStaffId(mesProcessReportWork.getStaffId());
            List<BaseStaffProcess> baseStaffProcessList = baseFeignApi.findStaffProcessList(searchBaseStaffProcess).getData();
            if (StringUtils.isEmpty(baseStaffProcessList)) {
                throw new BizErrorException("没有找到工序与报工人员的绑定关系！");
            }
        }
        MesPmWorkOrder mesPmWorkOrder = mesPmWorkOrderService.selectByKey(mesProcessReportWork.getWorkOrderId());
        SearchMesProcessReportWork searchMesProcessReportWork = new SearchMesProcessReportWork();
        searchMesProcessReportWork.setWorkOrderId(mesProcessReportWork.getWorkOrderId());
        searchMesProcessReportWork.setProcessId(mesProcessReportWork.getProcessId());
        List<MesProcessReportWorkDto> mesProcessReportWorkDtoList = mesProcessReportWorkMapper.findList(searchMesProcessReportWork);
        BigDecimal totalQuantity = mesProcessReportWork.getQuantity();
        if (StringUtils.isNotEmpty(mesProcessReportWorkDtoList)) {
            totalQuantity = mesProcessReportWorkDtoList.get(0).getTotalQuantity().add(mesProcessReportWork.getQuantity());
        }

        if (totalQuantity.compareTo(mesPmWorkOrder.getWorkOrderQty()) > 0) {
            throw new BizErrorException("本次报工数量累计超过工单数量！");
        } else if (totalQuantity.compareTo(mesPmWorkOrder.getWorkOrderQty()) == 0) {
            Example example = new Example(MesProcessReportWork.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("workOrderId", mesProcessReportWork.getWorkOrderId()).andEqualTo("processId", mesProcessReportWork.getProcessId()).andEqualTo("isDelete", 1);
            MesProcessReportWork mesProcessReportWorkUpdate = new MesProcessReportWork();
            mesProcessReportWorkUpdate.setStatus((byte) 1);
            mesProcessReportWorkMapper.updateByExampleSelective(mesProcessReportWorkUpdate, example);

            mesProcessReportWork.setStatus((byte) 1);
        }

        mesProcessReportWork.setProcessReportWorkCode(CodeUtils.getId("PRWC"));
        mesProcessReportWork.setCreateUserId(user.getUserId());
        mesProcessReportWork.setModifiedUserId(user.getUserId());
        mesProcessReportWork.setOrganizationId(user.getOrganizationId());
        mesProcessReportWorkMapper.insertSelective(mesProcessReportWork);

        return 1;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDelete(String ids) {

        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        String[] processReportWorkIds = ids.split(",");
        for (String processReportWorkId : processReportWorkIds) {
            MesProcessReportWork mesProcessReportWork = mesProcessReportWorkMapper.selectByPrimaryKey(processReportWorkId);
            if (mesProcessReportWork.getStatus() == 1) {
                throw new BizErrorException("工序手动报工已完工，不能删除！");
            }
            if (!user.getCreateUserId().equals(mesProcessReportWork.getCreateUserId())) {
                throw new BizErrorException("选中的报工数据不是您本人进行报工，无权删除！");
            }
            mesProcessReportWork.setIsDelete((byte) 0);
            mesProcessReportWork.setModifiedUserId(user.getUserId());
            mesProcessReportWork.setModifiedTime(new Date());
            mesProcessReportWorkMapper.updateByPrimaryKeySelective(mesProcessReportWork);
        }
        return processReportWorkIds.length;
    }

    @Override
    public List<MesProcessReportWorkDto> findList(SearchMesProcessReportWork searchMesProcessReportWork) {
        List<MesProcessReportWorkDto> list = mesProcessReportWorkMapper.findList(searchMesProcessReportWork);
        return list;
    }
}
