package com.fantechs.provider.guest.wanbao.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.general.entity.basic.BaseProLine;
import com.fantechs.common.base.general.entity.wanbao.WanbaoStackingDet;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.common.base.general.dto.wanbao.WanbaoStackingDto;
import com.fantechs.provider.guest.wanbao.mapper.WanbaoStackingMapper;
import com.fantechs.common.base.general.entity.wanbao.WanbaoStacking;
import com.fantechs.provider.guest.wanbao.service.WanbaoStackingDetService;
import com.fantechs.provider.guest.wanbao.service.WanbaoStackingService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.*;

/**
 *
 * Created by leifengzhi on 2021/11/29.
 */
@Service
public class WanbaoStackingServiceImpl extends BaseService<WanbaoStacking> implements WanbaoStackingService {

    @Resource
    private WanbaoStackingMapper wanbaoStackingMapper;
    @Resource
    private WanbaoStackingDetService wanbaoStackingDetService;
    @Resource
    private BaseFeignApi baseFeignApi;

    @Override
    public List<WanbaoStackingDto> findList(Map<String, Object> map) {
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId", sysUser.getOrganizationId());
        return wanbaoStackingMapper.findList(map);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importExcel(List<WanbaoStackingDto> list) {
        Map<String, Object> resultMap = new HashMap<>();  //封装操作结果
        List<Integer> fail = new ArrayList<>();  //记录操作失败行数

        // 产线
        List<BaseProLine> proLines = baseFeignApi.findProLineAll().getData();
        // 所有堆垛
        List<WanbaoStackingDto> stackingDtos = this.findList(new HashMap<>());
        List<WanbaoStacking> entitys = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            // 匹配产线
            if (StringUtils.isEmpty(list.get(i).getProName(), list.get(i).getStackingCode())){
                fail.add(i + 2);
                continue;
            }
            for (BaseProLine proLine : proLines){
                if (proLine.getProName().equals(list.get(i).getProName())){
                    list.get(i).setProLineId(proLine.getProLineId());
                    break;
                }
            }
            if (list.get(i).getProLineId() == null){
                fail.add(i + 2);
                continue;
            }

            // 判断已有数据中是否存在已有堆垛编码
            boolean flag = false;
            for (WanbaoStackingDto dto : stackingDtos){
                if (dto.getStackingCode().equals(list.get(i).getStackingCode()) && dto.getProLineId().equals(list.get(i).getProLineId())){
                    flag = true;
                    break;
                }
            }
            if(flag){
                fail.add(i + 2);
                continue;
            }
            flag = false;
            for (WanbaoStacking stacking : entitys){
                if (stacking.getStackingCode().equals(list.get(i).getStackingCode()) && stacking.getProLineId().equals(list.get(i).getProLineId())){
                    flag = true;
                    break;
                }
            }
            if(flag){
                fail.add(i + 2);
                continue;
            }


            SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
            WanbaoStacking entity = new WanbaoStacking();
            BeanUtil.copyProperties(list.get(i), entity);
            entity.setCreateTime(new Date());
            entity.setCreateUserId(sysUser.getUserId());
            entity.setModifiedTime(new Date());
            entity.setModifiedUserId(sysUser.getUserId());
            entity.setOrgId(sysUser.getOrganizationId());
            entity.setStatus((byte) 1);
            entity.setUsageStatus((byte) 1);
            entitys.add(entity);
        }
        if (!entitys.isEmpty()) {
            this.batchSave(entitys);
        }

        resultMap.put("操作成功总数", entitys.size());
        resultMap.put("操作失败行数", fail);
        return resultMap;
    }

    @Override
    public int updateAndClearBarcode(WanbaoStacking wanbaoStacking) {
        Example example = new Example(WanbaoStackingDet.class);
        example.createCriteria().andEqualTo("stackingId", wanbaoStacking.getStackingId());
        wanbaoStackingDetService.deleteByExample(example);
        return this.update(wanbaoStacking);
    }
}
