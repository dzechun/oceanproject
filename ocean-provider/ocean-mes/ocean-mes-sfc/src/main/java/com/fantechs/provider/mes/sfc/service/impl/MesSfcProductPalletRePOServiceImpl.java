package com.fantechs.provider.mes.sfc.service.impl;


import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.general.dto.mes.sfc.MesSfcProductPalletRePODto;
import com.fantechs.common.base.general.dto.om.OmSalesCodeReSpcDto;
import com.fantechs.common.base.general.entity.mes.sfc.MesSfcBarcodeProcess;
import com.fantechs.common.base.general.entity.om.OmSalesCodeReSpc;
import com.fantechs.common.base.general.entity.om.search.SearchOmSalesCodeReSpc;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.qms.OMFeignApi;
import com.fantechs.provider.mes.sfc.mapper.MesSfcBarcodeProcessMapper;
import com.fantechs.provider.mes.sfc.mapper.MesSfcProductPalletRePOMapper;
import com.fantechs.provider.mes.sfc.service.MesSfcBarcodeProcessService;
import com.fantechs.provider.mes.sfc.service.MesSfcProductPalletRePOService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/04/09.
 */
@Service
public class MesSfcProductPalletRePOServiceImpl extends BaseService<MesSfcProductPalletRePODto> implements MesSfcProductPalletRePOService {

    @Resource
    private MesSfcProductPalletRePOMapper mesSfcProductPalletRePOMapper;
    @Resource
    private MesSfcBarcodeProcessMapper mesSfcBarcodeProcessMapper;
    @Resource
    private MesSfcBarcodeProcessService mesSfcBarcodeProcessService;
    @Resource
    private OMFeignApi omFeignApi;

    @Override
    public List<MesSfcProductPalletRePODto> findList(Map<String, Object> map) {
        List<MesSfcProductPalletRePODto> palletRePODtos=null;
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        palletRePODtos= mesSfcProductPalletRePOMapper.findList(map);
        for (MesSfcProductPalletRePODto palletRePODto : palletRePODtos) {
            SearchOmSalesCodeReSpc searchOmSalesCodeReSpc = new SearchOmSalesCodeReSpc();
            searchOmSalesCodeReSpc.setSalesCode(palletRePODto.getSalesCode());
            searchOmSalesCodeReSpc.setSamePackageCodeStatus((byte)1);
            searchOmSalesCodeReSpc.setOrgId(user.getOrganizationId());
            List<OmSalesCodeReSpcDto> codeReSpcDtos = omFeignApi.findAll(searchOmSalesCodeReSpc).getData();
            palletRePODto.setOmSalesCodeReSpcList(codeReSpcDtos);
        }
        return palletRePODtos;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateBarcodePO(MesSfcProductPalletRePODto mesSfcProductPalletRePODto) throws Exception{
        String oldSamePackageCode=mesSfcProductPalletRePODto.getOldSamePackageCode();
        String newSamePackageCode=mesSfcProductPalletRePODto.getNewSamePackageCode();
        String barcode=mesSfcProductPalletRePODto.getBarcode();

        if(StringUtils.isEmpty(oldSamePackageCode)){
            throw new Exception("原PO号不能为空");
        }
        if(StringUtils.isEmpty(newSamePackageCode)){
            throw new Exception("新PO号不能为空");
        }
        if(oldSamePackageCode.equals(newSamePackageCode)){
            throw new Exception("原PO号不能与新PO号相同");
        }

        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        Example example = new Example(MesSfcBarcodeProcess.class);
        Example.Criteria criteria = example.createCriteria();

        criteria.andEqualTo("barcode",barcode);
        criteria.andEqualTo("orgId",user.getOrganizationId());

        MesSfcBarcodeProcess mesSfcBarcodeProcess=mesSfcBarcodeProcessMapper.selectOneByExample(example);
        if(StringUtils.isEmpty(mesSfcBarcodeProcess)){
            throw new Exception("找不到条码的过站记录-->"+barcode);
        }

        if(StringUtils.isEmpty(mesSfcBarcodeProcess.getSamePackageCode())){
            throw new Exception("条码未绑定PO号-->"+barcode);
        }

        // 绑定关系
        mesSfcBarcodeProcess.setSamePackageCode(newSamePackageCode);
        mesSfcBarcodeProcessService.update(mesSfcBarcodeProcess);

        SearchOmSalesCodeReSpc searchOmSalesCodeReSpc = new SearchOmSalesCodeReSpc();
        searchOmSalesCodeReSpc.setSamePackageCode(newSamePackageCode);
        List<OmSalesCodeReSpcDto> codeReSpcDtos = omFeignApi.findAll(searchOmSalesCodeReSpc).getData();
        OmSalesCodeReSpc omSalesCodeReSpcNew=codeReSpcDtos.get(0);

        // 新PO增加以已配数 并判断是否可以关闭 1:激活;2:失效;3：关闭；
        omSalesCodeReSpcNew.setMatchedQty(omSalesCodeReSpcNew.getMatchedQty() != null ? BigDecimal.ONE.add(omSalesCodeReSpcNew.getMatchedQty()) : BigDecimal.ONE);
        if (omSalesCodeReSpcNew.getMatchedQty().compareTo(omSalesCodeReSpcNew.getSamePackageCodeQty()) == 0){
            omSalesCodeReSpcNew.setSamePackageCodeStatus((byte) 3);
        }
        omFeignApi.update(omSalesCodeReSpcNew);

        //旧PO减少已匹配数 状态为 1 激活
        SearchOmSalesCodeReSpc searchOmSalesCodeReSpcOld = new SearchOmSalesCodeReSpc();
        searchOmSalesCodeReSpcOld.setSamePackageCode(oldSamePackageCode);
        List<OmSalesCodeReSpcDto> codeReSpcDtosOld = omFeignApi.findAll(searchOmSalesCodeReSpcOld).getData();
        OmSalesCodeReSpc omSalesCodeReSpcOld=codeReSpcDtosOld.get(0);
        omSalesCodeReSpcOld.setMatchedQty(omSalesCodeReSpcOld.getMatchedQty() !=null ? omSalesCodeReSpcOld.getMatchedQty().subtract(new BigDecimal(1)) : BigDecimal.ONE);
        omSalesCodeReSpcOld.setSamePackageCodeStatus((byte)1);

        omFeignApi.update(omSalesCodeReSpcOld);

        return 1;
    }

}
