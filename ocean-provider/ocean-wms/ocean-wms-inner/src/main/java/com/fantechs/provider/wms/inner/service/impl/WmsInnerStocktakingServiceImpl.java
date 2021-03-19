package com.fantechs.provider.wms.inner.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.basic.SmtMaterial;
import com.fantechs.common.base.entity.basic.SmtProLine;
import com.fantechs.common.base.entity.basic.search.SearchSmtMaterial;
import com.fantechs.common.base.entity.basic.search.SearchSmtProLine;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerStocktakingDto;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerStocktaking;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CodeUtils;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.imes.basic.BasicFeignApi;
import com.fantechs.provider.wms.inner.mapper.WmsInnerStocktakingMapper;
import com.fantechs.provider.wms.inner.service.WmsInnerStocktakingService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by leifengzhi on 2020/12/16.
 */
@Service
public class WmsInnerStocktakingServiceImpl extends BaseService<WmsInnerStocktaking> implements WmsInnerStocktakingService {

    @Resource
    private WmsInnerStocktakingMapper wmsInnerStocktakingMapper;
    @Resource
    private BasicFeignApi basicFeignApi;

    @Override
    public int save(WmsInnerStocktaking wmsInnerStocktaking) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        Example example = new Example(WmsInnerStocktaking.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("palletCode",wmsInnerStocktaking.getPalletCode());
        WmsInnerStocktaking wmsInnerStocktaking1 = wmsInnerStocktakingMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(wmsInnerStocktaking1)){
            throw new BizErrorException("该栈板的盘点信息已经存在");
        }
        wmsInnerStocktaking.setProfitLossQuantity(wmsInnerStocktaking.getBookInventory().subtract(wmsInnerStocktaking.getCountedQuantity()));//计算盈亏数量
        wmsInnerStocktaking.setProfitLossRate(wmsInnerStocktaking.getProfitLossQuantity().subtract(wmsInnerStocktaking.getBookInventory()));//计算盈亏率
        wmsInnerStocktaking.setStocktakingCode(CodeUtils.getId("ST-"));
        wmsInnerStocktaking.setCreateTime(new Date());
        wmsInnerStocktaking.setCreateUserId(user.getUserId());
        wmsInnerStocktaking.setModifiedTime(new Date());
        wmsInnerStocktaking.setModifiedUserId(user.getUserId());
        wmsInnerStocktaking.setStatus(StringUtils.isEmpty(wmsInnerStocktaking.getStatus()) ? 1 : wmsInnerStocktaking.getStatus());
        return wmsInnerStocktakingMapper.insertUseGeneratedKeys(wmsInnerStocktaking);

    }

    @Override
    public int update(WmsInnerStocktaking wmsInnerStocktaking) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        Example example = new Example(WmsInnerStocktaking.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("palletCode",wmsInnerStocktaking.getPalletCode())
                .andNotEqualTo("stocktakingId",wmsInnerStocktaking.getStocktakingId());
        WmsInnerStocktaking wmsInnerStocktaking1 = wmsInnerStocktakingMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(wmsInnerStocktaking1)){
            throw new BizErrorException("该栈板的盘点信息已经存在");
        }
        wmsInnerStocktaking.setProfitLossQuantity(wmsInnerStocktaking.getBookInventory().subtract(wmsInnerStocktaking.getCountedQuantity()));//计算盈亏数量
        wmsInnerStocktaking.setProfitLossRate(wmsInnerStocktaking.getProfitLossQuantity().subtract(wmsInnerStocktaking.getBookInventory()));//计算盈亏率
        wmsInnerStocktaking.setModifiedUserId(user.getUserId());
        wmsInnerStocktaking.setModifiedTime(new Date());

        return wmsInnerStocktakingMapper.updateByPrimaryKeySelective(wmsInnerStocktaking);
    }

    @Override
    public int batchDelete(String ids) {

        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        String[] idsArr = ids.split(",");
        for (String id : idsArr) {
            WmsInnerStocktaking wmsInnerStocktaking = wmsInnerStocktakingMapper.selectByPrimaryKey(id);
            if (StringUtils.isEmpty(wmsInnerStocktaking)) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }

        }

        return wmsInnerStocktakingMapper.deleteByIds(ids);
    }

    @Override
    public List<WmsInnerStocktakingDto> findList(Map<String, Object> map) {
        return wmsInnerStocktakingMapper.findList(map);
    }

    @Override
    public Map<String, Object> importStocktaking(List<WmsInnerStocktakingDto> wmsInnerStocktakingDtos) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(currentUser)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        Map<String, Object> resutlMap = new HashMap<>();  //封装操作结果
        int success = 0;  //记录操作成功数
        List<Integer> fail = new ArrayList<>();  //记录操作失败数

        List<WmsInnerStocktaking> wmsInnerStocktakings = new ArrayList<>();//盘点明细集合
        for (int i = 1; i < wmsInnerStocktakingDtos.size(); i++){
            WmsInnerStocktakingDto wmsInnerStocktakingDto = wmsInnerStocktakingDtos.get(i);
            String proCode = wmsInnerStocktakingDto.getProCode();
            String materialCode = wmsInnerStocktakingDto.getMaterialCode();
            if (StringUtils.isEmpty(proCode,materialCode)){
                fail.add(i+3);
                continue;
            }
            //判断线别是否存在
            SearchSmtProLine searchSmtProLine = new SearchSmtProLine();
            searchSmtProLine.setProCode(proCode);
            searchSmtProLine.setCodeQueryMark((byte) 1);
            ResponseEntity<List<SmtProLine>> responseEntity1 = basicFeignApi.selectProLines(searchSmtProLine);
            if (StringUtils.isEmpty(responseEntity1.getData())){
                fail.add(i+3);
                continue;
            }

            //判断物料是否存在
            SearchSmtMaterial searchSmtMaterial = new SearchSmtMaterial();
            searchSmtMaterial.setCodeQueryMark(1);
            searchSmtMaterial.setMaterialCode(materialCode);
            ResponseEntity<List<SmtMaterial>> responseEntity2 = basicFeignApi.findSmtMaterialList(searchSmtMaterial);
            if (StringUtils.isEmpty(responseEntity2.getData())){
                fail.add(i+3);
                continue;
            }

            WmsInnerStocktaking wmsInnerStocktaking = new WmsInnerStocktaking();
            BeanUtils.copyProperties(wmsInnerStocktakingDto,wmsInnerStocktaking);
            wmsInnerStocktaking.setCreateTime(new Date());
            wmsInnerStocktaking.setCreateUserId(currentUser.getUserId());
            wmsInnerStocktaking.setModifiedTime(new Date());
            wmsInnerStocktaking.setModifiedUserId(currentUser.getUserId());
            wmsInnerStocktaking.setStocktakingCode(CodeUtils.getId("ST-"));
            wmsInnerStocktaking.setMaterialId(responseEntity2.getData().get(0).getMaterialId());
            wmsInnerStocktaking.setProLineId(responseEntity1.getData().get(0).getProLineId());
            wmsInnerStocktakings.add(wmsInnerStocktaking);
        }

        if (StringUtils.isNotEmpty(wmsInnerStocktakings)){
            success = wmsInnerStocktakingMapper.insertList(wmsInnerStocktakings);
        }
        resutlMap.put("操作成功总数",success);
        resutlMap.put("操作失败行数",fail);
        return resutlMap;
    }
}
