package com.fantechs.provider.wms.inner.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerJobOrderDetBarcode;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.BeanUtils;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.wms.inner.mapper.WmsInnerJobOrderDetBarcodeMapper;
import com.fantechs.provider.wms.inner.service.WmsInnerJobOrderDetBarcodeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * Created by Mr.Lei on 2021/05/07.
 */
@Service
public class WmsInnerJobOrderDetBarcodeServiceImpl extends BaseService<WmsInnerJobOrderDetBarcode> implements WmsInnerJobOrderDetBarcodeService {

    @Resource
    private WmsInnerJobOrderDetBarcodeMapper wmsInPutawayOrderDetBarcodeMapper;

    @Override
    public WmsInnerJobOrderDetBarcode findBarCode(String barCode) {
        WmsInnerJobOrderDetBarcode wmsInPutawayOrderDetBarcode =  wmsInPutawayOrderDetBarcodeMapper.selectOne(WmsInnerJobOrderDetBarcode.builder()
                .remark(barCode)
                .build());
        if(!StringUtils.isEmpty(wmsInPutawayOrderDetBarcode)){
            throw new BizErrorException("");
        }
        return wmsInPutawayOrderDetBarcode;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int batchAddJobOrderDetBarcode(List<WmsInnerJobOrderDetBarcode> listDetBarcode) {
        int num=0;
        SysUser sysUser=currentUser();
        List<WmsInnerJobOrderDetBarcode> list=new ArrayList<>();
        if(listDetBarcode.size()>0){
            for (WmsInnerJobOrderDetBarcode wmsInnerJobOrderDetBarcode : listDetBarcode) {
                WmsInnerJobOrderDetBarcode detBarcodeNew=new WmsInnerJobOrderDetBarcode();
                BeanUtil.copyProperties(wmsInnerJobOrderDetBarcode,detBarcodeNew);
                detBarcodeNew.setJobOrderDetBarcodeId(null);
                detBarcodeNew.setCreateUserId(sysUser.getUserId());
                detBarcodeNew.setCreateTime(new Date());
                detBarcodeNew.setOrgId(sysUser.getOrganizationId());
                list.add(detBarcodeNew);
            }
            num=wmsInPutawayOrderDetBarcodeMapper.insertList(list);
            if(num<=0){
                throw new BizErrorException(ErrorCodeEnum.OPT20012006.getCode(),"批量新增上架条码明细失败");
            }
        }

        return num;
    }

    /**
     * 获取当前登录用户
     *
     * @return
     */
    private SysUser currentUser() {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        return user;
    }
}
