package com.fantechs.provider.wms.in.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.dto.storage.SaveBillsDetDTO;
import com.fantechs.common.base.dto.storage.SaveDoubleBillsDTO;
import com.fantechs.common.base.dto.storage.WmsInStorageBillsDTO;
import com.fantechs.common.base.entity.basic.history.WmsInHtStorageBills;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.entity.storage.WmsInStorageBills;
import com.fantechs.common.base.entity.storage.WmsInStorageBillsDet;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.exception.SQLExecuteException;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.utils.BeanUtils;
import com.fantechs.common.base.utils.CodeUtils;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.provider.wms.in.service.WmsInStorageBillsDetService;
import com.fantechs.provider.wms.in.service.WmsInStorageBillsService;
import com.fantechs.provider.wms.in.mapper.WmsInStorageBillsMapper;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.wms.in.service.history.WmsInHtStorageBillsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
@Slf4j
public class WmsInStorageBillsServiceImpl extends BaseService<WmsInStorageBills>  implements WmsInStorageBillsService {

     @Resource
     private WmsInStorageBillsMapper wmsStorageBillsMapper;
     @Resource
     private WmsInStorageBillsDetService wmsStorageBillsDetService;
     @Resource
     private WmsInHtStorageBillsService wmsHtStorageBillsService;

    @Override
    public List<WmsInStorageBills> selectAll(Map<String,Object> map) {
        Example example = new Example(WmsInStorageBills.class);
        Example.Criteria criteria = example.createCriteria();
        Example.Criteria criteria1 = example.createCriteria();
        criteria1.andEqualTo("isDelete",1).orIsNull("isDelete");
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
    public List<WmsInStorageBills> selectLikeAll(Map<String,Object> map) {
        Example example = new Example(WmsInStorageBills.class);
        Example.Criteria criteria = example.createCriteria();
        Example.Criteria criteria1 = example.createCriteria();
        criteria1.andEqualTo("isDelete",1).orIsNull("isDelete");
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
    public WmsInStorageBills selectByKey(Object id) {
        WmsInStorageBills wmsStorageBills = wmsStorageBillsMapper.selectByPrimaryKey(id);
        if(wmsStorageBills != null && (wmsStorageBills.getIsDelete() != null && wmsStorageBills.getIsDelete() == 0)){
        wmsStorageBills = null;
        }
        return wmsStorageBills;
    }

    @Override
    public WmsInStorageBills selectByMap(Map<String,Object> map) {
        List<WmsInStorageBills> wmsStorageBills = selectAll(map);
        if(StringUtils.isEmpty(wmsStorageBills)){
            return null;
        }
        if(wmsStorageBills.size()>1){
            return null;
        }
        return wmsStorageBills.get(0);
    }

    @Override
    public int save(WmsInStorageBills wmsStorageBills) {
//        SysUser sysUser = this.currentUser();
//        wmsStorageBills.setCreateUserId(sysUser.getUserId());
        wmsStorageBills.setIsDelete((byte)1);
        wmsStorageBills.setStorageBillsCode(CodeUtils.getId("STB"));
        if(wmsStorageBillsMapper.insertSelective(wmsStorageBills)<=0){
            return 0;
        }
        recordHistory(wmsStorageBills.getStorageBillsId(),"新增");
        return 1;
    }

    @Override
    public int deleteByKey(Object id) {
        WmsInStorageBills wmsStorageBills = new WmsInStorageBills();
        wmsStorageBills.setStorageBillsId((Long)id);
        wmsStorageBills.setIsDelete((byte)0);
        return update(wmsStorageBills);
    }

    @Override
    public int deleteByMap(Map<String,Object> map){
        List<WmsInStorageBills> wmsStorageBillss = selectAll(map);
        if (StringUtils.isNotEmpty(wmsStorageBillss)) {
            for (WmsInStorageBills wmsStorageBills : wmsStorageBillss) {
                if(deleteByKey(wmsStorageBills.getStorageBillsId())<=0){
                    return 0;
                }
            }
        }
        return 1;
    }

    @Override
    public int update(WmsInStorageBills wmsStorageBills) {
        /*SysUser sysUser = this.currentUser();
        wmsStorageBills.setModifiedUserId(sysUser.getUserId());*/
        if(wmsStorageBillsMapper.updateByPrimaryKeySelective(wmsStorageBills)<=0){
            return 0;
        }
        recordHistory(wmsStorageBills.getStorageBillsId(),"更新");
        return 1;
    }

    @Override
    public String selectUserName(Object id) {
        return wmsStorageBillsMapper.selectUserName(id);
    }

    @Override
    public List<WmsInStorageBillsDTO> selectFilterAll(Map<String, Object> map) {
        return wmsStorageBillsMapper.selectFilterAll(map);
    }

    /**
     * 新增或更新仓库清单及详情
     * @param saveDoubleBillsDTO
     * @return
     * @throws SQLExecuteException
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int saveDouble(SaveDoubleBillsDTO saveDoubleBillsDTO) throws SQLExecuteException {
        WmsInStorageBills wmsInStorageBills = saveDoubleBillsDTO.getWmsInStorageBills();
        List<WmsInStorageBillsDet> wmsInStorageBillsDetList = saveDoubleBillsDTO.getWmsInStorageBillsDetList();
        if(StringUtils.isEmpty(wmsInStorageBills.getStorageBillsId())){
            //ID为空做新增
            if(this.save(wmsInStorageBills)<=0){
                return 0;
            }
        }else {
            //更新
            if (this.update(wmsInStorageBills) <= 0) {
                return 0;
            }
        }
        //删除仓库清单下的所有详情信息，重新新增
        wmsStorageBillsDetService.deleteByMap(ControllerUtil.dynamicCondition("storageBillsId",wmsInStorageBills.getStorageBillsId()));

        //=====如果不为空则重新全部进行新增
        if(StringUtils.isNotEmpty(wmsInStorageBillsDetList)){
            for (WmsInStorageBillsDet wmsInStorageBillsDet : wmsInStorageBillsDetList) {
                wmsInStorageBillsDet.setStorageBillsId(wmsInStorageBills.getStorageBillsId());
                wmsInStorageBillsDet.setCreateTime(new Date());
                wmsInStorageBillsDet.setModifiedTime(new Date());
                wmsInStorageBillsDet.setIsDelete((byte)1);
            }
            if(wmsStorageBillsDetService.batchSave(wmsInStorageBillsDetList)<=0){
                log.error("添加仓库清单详情数据错误："+ JSONObject.toJSONString(wmsInStorageBillsDetList));
                throw new SQLExecuteException(ErrorCodeEnum.OPT20012006,"添加仓库清单详情数据错误");
            }
        }
        //=====

        //重新计算各种数据下的各种状态
        SaveBillsDetDTO saveBillsDetDTO = new SaveBillsDetDTO();
        saveBillsDetDTO.setStorageBillsId(wmsInStorageBills.getStorageBillsId());
        saveBillsDetDTO.setWmsStorageBillsDetList(wmsInStorageBillsDetList);
        pdaSaveBilssDet(saveBillsDetDTO);
        return 1;
    }


    @Override
    public WmsInStorageBills pdaSaveBilssDet(SaveBillsDetDTO saveBillsDet) throws SQLExecuteException {
        WmsInStorageBills wmsStorageBills = selectByKey(saveBillsDet.getStorageBillsId());
        if(StringUtils.isEmpty(wmsStorageBills)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012005);
        }
        int income=0;
        boolean finished=true;
        List<WmsInStorageBillsDet> wmsStorageBillsDetList = saveBillsDet.getWmsStorageBillsDetList();
        if(StringUtils.isEmpty(wmsStorageBillsDetList)){
            return null;
        }
        Byte allowBatch = wmsStorageBills.getAllowBatch();
        for (WmsInStorageBillsDet wmsStorageBillsDet : wmsStorageBillsDetList) {
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
    public List<WmsInStorageBillsDTO> pdaSelectFilterAll(Map<String, Object> map) {
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

    /**
     * 记录操作历史
     * @param id
     * @param operation
     */
    private void recordHistory(Long id,String operation){
        WmsInHtStorageBills wmsHtStorageBills = new WmsInHtStorageBills();
        wmsHtStorageBills.setOperation(operation);
        WmsInStorageBills wmsStorageBills = selectByKey(id);
        if (StringUtils.isEmpty(wmsStorageBills)){
            return;
        }
        BeanUtils.autoFillEqFields(wmsStorageBills,wmsHtStorageBills);
        wmsHtStorageBillsService.save(wmsHtStorageBills);
    }
}
