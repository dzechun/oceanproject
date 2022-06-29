package com.fantechs.provider.mes.sfc.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.mes.sfc.MesSfcKeyPartRelevanceDto;
import com.fantechs.common.base.general.entity.basic.BaseRoute;
import com.fantechs.common.base.general.entity.basic.BaseRouteProcess;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseRoute;
import com.fantechs.common.base.general.entity.mes.sfc.MesSfcBarcodeProcess;
import com.fantechs.common.base.general.entity.mes.sfc.MesSfcKeyPartRelevance;
import com.fantechs.common.base.general.entity.mes.sfc.MesSfcWorkOrderBarcode;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.api.mes.pm.PMFeignApi;
import com.fantechs.provider.mes.sfc.mapper.MesSfcBarcodeProcessMapper;
import com.fantechs.provider.mes.sfc.mapper.MesSfcKeyPartRelevanceMapper;
import com.fantechs.provider.mes.sfc.mapper.MesSfcWorkOrderBarcodeMapper;
import com.fantechs.provider.mes.sfc.service.MesSfcKeyPartRelevanceService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * Created by leifengzhi on 2021/05/08.
 */
@Service
public class MesSfcKeyPartRelevanceServiceImpl extends BaseService<MesSfcKeyPartRelevance> implements MesSfcKeyPartRelevanceService {

    @Resource
    private MesSfcKeyPartRelevanceMapper mesSfcKeyPartRelevanceMapper;
    @Resource
    private MesSfcBarcodeProcessMapper mesSfcBarcodeProcessMapper;
    @Resource
    private MesSfcWorkOrderBarcodeMapper mesSfcWorkOrderBarcodeMapper;
    @Resource
    private PMFeignApi pmFeignApi;
    @Resource
    private BaseFeignApi baseFeignApi;


    @Override
    public List<MesSfcKeyPartRelevanceDto> findList(Map<String, Object> map) {
        return mesSfcKeyPartRelevanceMapper.findList(map);
    }

    @Override
    public List<MesSfcKeyPartRelevanceDto> findListByPallet(Map<String, Object> map) {
        return mesSfcKeyPartRelevanceMapper.findListByPallet(map);
    }

    @Override
    public List<MesSfcKeyPartRelevanceDto> findListForGroup(Map<String, Object> map) {
        return mesSfcKeyPartRelevanceMapper.findListForGroup(map);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LcnTransaction
    public boolean barcodeUnbinding(String barcode) {
        // 条码解绑步骤
        // 1、条码校验
        // 2、修改：工单表、产品条码过站表、生产订单条码表
        // 3、删除：关键部件关联表

        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        List<String> barcodeList = mesSfcKeyPartRelevanceMapper.findBarcodeCodeByBarcode(barcode);
        if (ObjectUtil.isNull(barcodeList) || barcodeList.size() == 0) {
            throw new BizErrorException(ErrorCodeEnum.PDA40012038);
        }

        // 判断是否条码已过入库下线站点，已过入库下线站点不允许解绑
        Example example = new Example(MesSfcBarcodeProcess.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("barcode", barcodeList.get(0));
        List<MesSfcBarcodeProcess> mesSfcBarcodeProcessList = mesSfcBarcodeProcessMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(mesSfcBarcodeProcessList)) {
            throw new BizErrorException(ErrorCodeEnum.PDA40012038);
        }
        if (ObjectUtil.isNotNull(mesSfcBarcodeProcessList.get(0).getProductionTime())) {
            throw new BizErrorException(ErrorCodeEnum.PDA40012037);
        }

        // 修改工单表，已投产数
        logger.info("=============开始修改工单表");
        List<MesSfcKeyPartRelevance> mesSfcKeyPartRelevances = mesSfcKeyPartRelevanceMapper.findMesSfcKeyPartRelevance(barcodeList.get(0));
        List<Long> workOrderIds = mesSfcKeyPartRelevances.stream().map(MesSfcKeyPartRelevance::getWorkOrderId).collect(Collectors.toList());
        List<String> workOrderIdList = workOrderIds.stream().map(x -> x + "").collect(Collectors.toList());
        ResponseEntity responseEntity = pmFeignApi.updateProductionQty(workOrderIdList.stream().distinct().collect(Collectors.toList()));
        if (ObjectUtil.isNull(responseEntity) || responseEntity.getCode() != 0) {
            throw new BizErrorException(ErrorCodeEnum.PDA40012039);
        }

        // 产品条码过站表，投入时间、入站时间置空、过站次数 重置为空
        logger.info("=============开始修改产品条码过站表");
        MesSfcBarcodeProcess mesSfcBarcodeProcess = mesSfcBarcodeProcessList.get(0);
        mesSfcBarcodeProcess.setDevoteTime(null);
        mesSfcBarcodeProcess.setInProcessTime(null);
        mesSfcBarcodeProcess.setPassStationCount(0);
        mesSfcBarcodeProcess.setModifiedUserId(currentUser.getUserId());
        mesSfcBarcodeProcess.setModifiedTime(new Date());
        // 通过条码找工单表，工单找工艺路线，工艺路线首工序，还原成投产前：next_process_id、next_process_code、next_process_name
        ResponseEntity<List<BaseRouteProcess>> responseEntityRoute = baseFeignApi.findConfigureRout(mesSfcBarcodeProcess.getRouteId());
        if (ObjectUtil.isNull(responseEntityRoute) || responseEntityRoute.getCode() != 0 || responseEntityRoute.getData().size() == 0) {
            throw new BizErrorException(ErrorCodeEnum.PDA40012040);
        }
        BaseRouteProcess baseRouteProcess = responseEntityRoute.getData().get(0);
        mesSfcBarcodeProcess.setNextProcessId(baseRouteProcess.getNextProcessId());
        mesSfcBarcodeProcess.setNextProcessCode(baseRouteProcess.getProcessCode());
        mesSfcBarcodeProcess.setNextProcessName(baseRouteProcess.getNextProcessName());
        int processUpdateCount = mesSfcBarcodeProcessMapper.updateByPrimaryKey(mesSfcBarcodeProcess);
        logger.info("=============修改产品条码过站表条数：{}", processUpdateCount);

        // 修改生产订单条码表，流转卡状态=待投产
        MesSfcWorkOrderBarcode mesSfcWorkOrderBarcode = new MesSfcWorkOrderBarcode();
        mesSfcWorkOrderBarcode.setBarcode(barcodeList.get(0));
        mesSfcWorkOrderBarcode.setBarcodeStatus((byte)0);
        mesSfcWorkOrderBarcode.setModifiedUserId(currentUser.getUserId());
        mesSfcWorkOrderBarcode.setModifiedTime(new Date());
        int orderBarcodeUpdateCount = mesSfcWorkOrderBarcodeMapper.updateBarcodeStatusByBarcode(mesSfcWorkOrderBarcode);
        logger.info("=============修改生产订单条码表条数：{}", orderBarcodeUpdateCount);

        // 根据产内码删除关键部件关联表数据
        int deleteCount = mesSfcKeyPartRelevanceMapper.deleteByBarcode(barcodeList.get(0));
        logger.info("=============删除删除关键部件关联表条数：{}", deleteCount);
        return true;
    }
}
