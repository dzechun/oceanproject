package com.fantechs.provider.wms.storageBills.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.dto.storage.SaveBilssDet;
import com.fantechs.common.base.dto.storage.WmsStorageBillsDTO;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.entity.storage.WmsStorageBills;
import com.fantechs.common.base.entity.storage.WmsStorageBillsDet;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.exception.SQLExecuteException;
import com.fantechs.common.base.utils.CodeUtils;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.provider.wms.storageBills.service.WmsStorageBillsDetService;
import com.fantechs.provider.wms.storageBills.service.WmsStorageBillsService;
import com.fantechs.provider.wms.storageBills.mapper.WmsStorageBillsMapper;
import com.fantechs.common.base.support.BaseService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import com.fantechs.common.base.utils.StringUtils;
import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Auther: bingo.ren
 * @Date: 2020年12月17日 14:52
 * @Description: 仓库清单表接口实现
 * @Version: 1.0
 */
@Service
public class WmsStorageBillsServiceImpl extends BaseService<WmsStorageBills>  implements WmsStorageBillsService{

     @Resource
     private WmsStorageBillsMapper wmsStorageBillsMapper;
     @Resource
     private WmsStorageBillsDetService wmsStorageBillsDetService;

    @Override
    public List<WmsStorageBills> selectAll(Map<String,Object> map) {
        Example example = new Example(WmsStorageBills.class);
        Example.Criteria criteria = example.createCriteria();
        Example.Criteria criteria1 = example.createCriteria();
        criteria1.andEqualTo("IsDelete",1).orIsNull("IsDelete");
        example.and(criteria1);
        if(StringUtils.isNotEmpty(map)){
            map.forEach((k,v)->{
                if(StringUtils.isNotEmpty(v)){
                    switch (k){
                        case "Name":
                            criteria.andLike(k,"%"+v+"%");
                            break;
                        default :
                            criteria.andEqualTo(k,v);
                            break;
                    }
                }
            });
        }
        return wmsStorageBillsMapper.selectByExample(example);
    }

    @Override
    public List<WmsStorageBills> selectLikeAll(Map<String,Object> map) {
        Example example = new Example(WmsStorageBills.class);
        Example.Criteria criteria = example.createCriteria();
        Example.Criteria criteria1 = example.createCriteria();
        criteria1.andEqualTo("IsDelete",1).orIsNull("IsDelete");
        example.and(criteria1);
        if(StringUtils.isNotEmpty(map)){
            map.forEach((k,v)->{
                if(StringUtils.isNotEmpty(v)){
                    criteria.orLike(k,"%"+v+"%");
                }
            });
        }
        return wmsStorageBillsMapper.selectByExample(example);
    }

    @Override
    public WmsStorageBills selectByKey(Object id) {
        WmsStorageBills wmsStorageBills = wmsStorageBillsMapper.selectByPrimaryKey(id);
        if(wmsStorageBills != null && (wmsStorageBills.getIsDelete() != null && wmsStorageBills.getIsDelete() == 0)){
        wmsStorageBills = null;
        }
        return wmsStorageBills;
    }

    @Override
    public WmsStorageBills selectByMap(Map<String,Object> map) {
        List<WmsStorageBills> wmsStorageBills = selectAll(map);
        if(StringUtils.isEmpty(wmsStorageBills)){
            return null;
        }
        if(wmsStorageBills.size()>1){
            return null;
        }
        return wmsStorageBills.get(0);
    }

    @Override
    public int save(WmsStorageBills wmsStorageBills) {
//        SysUser sysUser = this.currentUser();
//        wmsStorageBills.setCreateUserId(sysUser.getUserId());
        wmsStorageBills.setCreateTime(new Date());
        wmsStorageBills.setIsDelete((byte)1);
        wmsStorageBills.setStorageBillsCode(CodeUtils.getId("SKN"));
        return wmsStorageBillsMapper.insertUseGeneratedKeys(wmsStorageBills);
    }

    @Override
    public int deleteByKey(Object id) {
        WmsStorageBills wmsStorageBills = new WmsStorageBills();
        wmsStorageBills.setStorageBillsId((Long)id);
        wmsStorageBills.setIsDelete((byte)0);
        return update(wmsStorageBills);
    }

    @Override
    public int deleteByMap(Map<String,Object> map){
        List<WmsStorageBills> wmsStorageBillss = selectAll(map);
        if (StringUtils.isNotEmpty(wmsStorageBillss)) {
            for (WmsStorageBills wmsStorageBills : wmsStorageBillss) {
                if(deleteByKey(wmsStorageBills.getStorageBillsId())<=0){
                    return 0;
                }
            }
        }
        return 1;
    }

    @Override
    public int update(WmsStorageBills wmsStorageBills) {
        /*SysUser sysUser = this.currentUser();
        wmsStorageBills.setModifiedUserId(sysUser.getUserId());*/
        wmsStorageBills.setModifiedTime(new Date());
        return wmsStorageBillsMapper.updateByPrimaryKeySelective(wmsStorageBills);
    }

    @Override
    public String selectUserName(Object id) {
        return wmsStorageBillsMapper.selectUserName(id);
    }

    @Override
    public List<WmsStorageBillsDTO> selectFilterAll(Map<String, Object> map) {
        return wmsStorageBillsMapper.selectFilterAll(map);
    }








    @Override
    public WmsStorageBills pdaSaveBilssDet(SaveBilssDet saveBilssDet) throws SQLExecuteException {
        WmsStorageBills wmsStorageBills = selectByKey(saveBilssDet.getStorageBillsId());
        if(StringUtils.isEmpty(wmsStorageBills)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012005);
        }
        int income=0;
        boolean finished=true;
        List<WmsStorageBillsDet> wmsStorageBillsDetList = saveBilssDet.getWmsStorageBillsDetList();
        if(StringUtils.isEmpty(wmsStorageBillsDetList)){
            return null;
        }
        Byte allowBatch = wmsStorageBills.getAllowBatch();
        for (WmsStorageBillsDet wmsStorageBillsDet : wmsStorageBillsDetList) {
            double willIncaomeTotal = wmsStorageBillsDet.getWillIncomeTotal().doubleValue();
            double realIncomeTotal = wmsStorageBillsDet.getRealIncomeTotal().doubleValue();
            if(willIncaomeTotal<=0 || realIncomeTotal<=0){
                finished=false;
            }else{
                if(realIncomeTotal>willIncaomeTotal){
                    throw new BizErrorException("实际数量大于应需数量");
                }
                if(realIncomeTotal<willIncaomeTotal){
                    if(allowBatch == 0 ){
                        wmsStorageBillsDet.setStatus((byte)4);
                    }else{
                        wmsStorageBillsDet.setStatus((byte)2);
                    }
                    finished=false;
                }else{
                    wmsStorageBillsDet.setStatus((byte)3);
                }
                income++;
            }

            if(wmsStorageBillsDetService.update(wmsStorageBillsDet)<=0){
                throw new SQLExecuteException(ErrorCodeEnum.OPT20012006);
            }
        }
        wmsStorageBills.setFinishedTotal(new BigDecimal(income));
        double waitTypeTotal = wmsStorageBills.getMaterialTypeTotal().doubleValue();
        double finishedTypeTotal = wmsStorageBills.getFinishedTotal().doubleValue();
        if(finished && waitTypeTotal == finishedTypeTotal){
            wmsStorageBills.setStatus((byte)3);
        }else if(income>0){
            wmsStorageBills.setStatus((byte)2);
        }else{
            wmsStorageBills.setStatus((byte)1);
        }
        if(allowBatch == 0){
            wmsStorageBills.setStatus((byte)4);
        }
        if(this.update(wmsStorageBills)<=0){
            throw new SQLExecuteException(ErrorCodeEnum.OPT20012006);
        }
        return wmsStorageBills;
    }

    @Override
    public List<WmsStorageBillsDTO> pdaSelectFilterAll(Map<String, Object> map) {
        return wmsStorageBillsMapper.pdaSelectFilterAll(map);
    }

    /**
     * 获取当前登录用户
     * @return
     */
    private SysUser currentUser(){
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        return user;
    }
}
