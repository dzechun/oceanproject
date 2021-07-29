package com.fantechs.provider.smt.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.smt.SmtSolderPasteDto;
import com.fantechs.common.base.general.dto.smt.SmtSolderPasteJobDto;
import com.fantechs.common.base.general.dto.smt.SmtSolderPasterConfig;
import com.fantechs.common.base.general.entity.smt.SmtSolderPaste;
import com.fantechs.common.base.general.entity.smt.SmtSolderPasteJob;
import com.fantechs.common.base.general.entity.smt.history.SmtHtSolderPaste;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.*;
import com.fantechs.provider.smt.mapper.SmtHtSolderPasteMapper;
import com.fantechs.provider.smt.mapper.SmtSolderPasteJobMapper;
import com.fantechs.provider.smt.mapper.SmtSolderPasteMapper;
import com.fantechs.provider.smt.service.SmtSolderPasteService;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 *
 * Created by Mr.Lei on 2021/07/22.
 */
@Service
public class SmtSolderPasteServiceImpl extends BaseService<SmtSolderPaste> implements SmtSolderPasteService {

    @Resource
    private SmtSolderPasteMapper smtSolderPasteMapper;
    @Resource
    private RedisUtil redisUtil;
    @Resource
    private SmtHtSolderPasteMapper smtHtSolderPasteMapper;
    @Resource
    private SmtSolderPasteJobMapper smtSolderPasteJobMapper;

    //存入redis前缀
    private static String DEFAULT_NAME="SOLDER:";

    @Override
    public List<SmtSolderPasteDto> findList(Map<String, Object> map) {
        SysUser sysUser = currentUser();
        map.put("orgId",sysUser.getOrganizationId());
        List<SmtSolderPasteDto> list = smtSolderPasteMapper.findList(map);
        for (SmtSolderPasteDto smtSolderPasteDto : list) {
            if (redisUtil.hasKey(DEFAULT_NAME+smtSolderPasteDto.getSolderPasteId())){
                SmtSolderPasterConfig smtSolderPasterConfig = new SmtSolderPasterConfig();
                BeanUtils.mapToObject((Map<String, Object>) redisUtil.get(DEFAULT_NAME+smtSolderPasteDto.getSolderPasteId()),smtSolderPasterConfig);
                smtSolderPasteDto.setSmtSolderPasterConfig(smtSolderPasterConfig);
            }
        }
        return list;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public SmtSolderPasteDto scanSolder(String barCode,Byte solderPasteStatus,Integer PASS) {
        SysUser sysUser  = currentUser();
        //查询锡膏条码是否存在来料条码
        Map<String,Object> map = smtSolderPasteMapper.findInvDet(barCode);
        if(StringUtils.isEmpty(map)){
            throw new BizErrorException("条码错误");
        }
        //用批号匹配是否存在
        Example example = new Example(SmtSolderPasteJob.class);
        example.createCriteria().andEqualTo("materialBarcodeId",map.get("material_barcode_id"));
        example.orderBy("operatorTime").desc();
        List<SmtSolderPasteJob> list = smtSolderPasteJobMapper.selectByExample(example);
        SmtSolderPasteJob smtSolderPasteJob = null;
        if(list.size()>0){
            smtSolderPasteJob= list.get(0);
        }else{
            smtSolderPasteJob = null;
        }
        if(StringUtils.isNotEmpty(smtSolderPasteJob) && smtSolderPasteJob.getCurrentSolderPasteStatus()==solderPasteStatus){
            throw new BizErrorException("当前已是"+this.StatusToName(solderPasteStatus)+"状态");
        }
        SmtSolderPaste smtSolderPaste = null;
        if(StringUtils.isEmpty(smtSolderPasteJob)){
            example = new Example(SmtSolderPaste.class);
            example.createCriteria().andEqualTo("materialId",map.get("material_id"));
            smtSolderPaste = smtSolderPasteMapper.selectOneByExample(example);
        }else {
            smtSolderPaste = smtSolderPasteMapper.selectByPrimaryKey(smtSolderPasteJob.getSolderPasteId());
            //当前状态
            smtSolderPaste.setSolderPasteStatus(smtSolderPasteJob.getCurrentSolderPasteStatus());
            //更新时间
            smtSolderPaste.setSpStatusUpdateTime(smtSolderPasteJob.getOperatorTime());
            //回冰次数
            smtSolderPaste.setCurrentReturnIceTime(smtSolderPasteJob.getCurrentReturnIceTime());
        }


        if (redisUtil.hasKey(DEFAULT_NAME+smtSolderPaste.getSolderPasteId())){
            SmtSolderPasterConfig smtSolderPasterConfig = new SmtSolderPasterConfig();
            BeanUtils.mapToObject((Map<String, Object>) redisUtil.get(DEFAULT_NAME+smtSolderPaste.getSolderPasteId()),smtSolderPasterConfig);
            smtSolderPaste.setSmtSolderPasterConfig(smtSolderPasterConfig);
        }else {
            throw new BizErrorException("锡膏配置获取失败");
        }
//        smtSolderPaste.setSmtSolderPasterConfig((SmtSolderPasterConfig) redisUtil.get(DEFAULT_NAME+smtSolderPaste.getSolderPasteId().toString()));
        if(smtSolderPasteJob.getCurrentSolderPasteStatus()==8){
            throw new BizErrorException("锡膏已报废,无法执行");
        }
        if(smtSolderPasteJob.getCurrentSolderPasteStatus()==6){
            throw new BizErrorException("锡膏已经用完");
        }
        //上一个状态
        Byte lastSolderPasteStatus = smtSolderPasteJob.getCurrentSolderPasteStatus();
        smtSolderPaste = this.checkStatus(smtSolderPaste,solderPasteStatus,PASS);
        if(StringUtils.isEmpty(smtSolderPaste.getMessage())){
            smtSolderPasteMapper.updateByPrimaryKeySelective(smtSolderPaste);
            smtSolderPaste.setExecuteStatus(0);
            smtSolderPaste.setIsDate(0);

            //生产状态更新记录
            smtSolderPasteJob = new SmtSolderPasteJobDto();
            smtSolderPasteJob.setLastSolderPasteStatus(lastSolderPasteStatus);
            smtSolderPasteJob.setCurrentSolderPasteStatus(solderPasteStatus);
            smtSolderPasteJob.setMaterialBarcodeId(Long.parseLong(map.get("material_barcode_id").toString()));
            smtSolderPasteJob.setSolderPasteId(smtSolderPasteJob.getSolderPasteId());
            smtSolderPasteJob.setOperatorUserId(sysUser.getUserId());
            smtSolderPasteJob.setOperatorTime(new Date());
            smtSolderPasteJob.setCreateTime(new Date());
            smtSolderPasteJob.setCreateUserId(sysUser.getUserId());
            smtSolderPasteJob.setModifiedTime(new Date());
            smtSolderPasteJob.setModifiedUserId(sysUser.getUserId());
            smtSolderPasteJob.setOrgId(sysUser.getOrganizationId());
            smtSolderPasteJob.setCurrentReturnIceTime(smtSolderPaste.getCurrentReturnIceTime());
            smtSolderPasteJobMapper.insertSelective(smtSolderPasteJob);
            smtSolderPaste.setSmtSolderPasteJob(smtSolderPasteJob);
        }
        map = new HashMap<>();
        map.put("solderPasteId",smtSolderPaste.getSolderPasteId());
        SmtSolderPasteDto smtSolderPasteDto = smtSolderPasteMapper.findList(map).get(0);
        smtSolderPasteDto.setMessage(smtSolderPaste.getMessage());
        smtSolderPasteDto.setExecuteStatus(smtSolderPaste.getExecuteStatus());
        smtSolderPasteDto.setIsDate(smtSolderPaste.getIsDate());

//        SmtHtSolderPaste smtHtSolderPaste = new SmtHtSolderPaste();
//        BeanUtil.copyProperties(smtSolderPaste,smtHtSolderPaste);
//        smtHtSolderPasteMapper.insertSelective(smtHtSolderPaste);
        return smtSolderPasteDto;
    }

    @Override
    public List<SmtSolderPasteDto> findHtList(Map<String, Object> map) {
        SysUser sysUser  = currentUser();
        map.put("orgId",sysUser.getOrganizationId());
        return smtHtSolderPasteMapper.findHtList(map);
    }

    private SmtSolderPaste checkStatus(SmtSolderPaste smtSolderPaste,Byte solderPasteStatus,Integer PASS){
        //ps: pass==1强制执行 PASS==2过期执行
        if(PASS==0 || PASS==2){
            Map<String,Object> map = new HashMap<>();
            if(StringUtils.isNotEmpty(smtSolderPaste.getSpStatusUpdateTime()) && solderPasteStatus!=1){
                map = this.getDatePoor(smtSolderPaste.getSpStatusUpdateTime(),new Date());
            }
            if(PASS==0){
                //判断锡膏是否过期
                if(smtSolderPaste.getExpirationDate().before(new Date())){
                    if(smtSolderPaste.getSmtSolderPasterConfig().getProductionDateStatus()==1){
                        smtSolderPaste.setMessage("锡膏已过期，已强制停止执行");
                        smtSolderPaste.setExecuteStatus(1);
                        smtSolderPaste.setIsDate(1);
                        return smtSolderPaste;
                    }else{
                        smtSolderPaste.setMessage("锡膏已过期，是否继续执行");
                        smtSolderPaste.setExecuteStatus(2);
                        smtSolderPaste.setIsDate(1);
                        return smtSolderPaste;
                    }
                }
            }
            switch (solderPasteStatus){
                case 1:
                    //入冰窟
                    if(smtSolderPaste.getSolderPasteStatus()>solderPasteStatus){
                        throw new BizErrorException("锡膏已经"+this.StatusToName(solderPasteStatus));
                    }
                    break;
                case 2:
                    //回温
                    //ps：回冰后可以再次回温
                    if(smtSolderPaste.getSolderPasteStatus()!=6 && smtSolderPaste.getSolderPasteStatus()>solderPasteStatus){
                        throw new BizErrorException("锡膏已经"+this.StatusToName(solderPasteStatus));
                    }
                    break;
                case 3:
                    //搅拌
                    //判断回温是否达到要求时间
                    if(smtSolderPaste.getSolderPasteStatus()!=2){
                        throw new BizErrorException("锡膏未回温，无法执行");
                    }
                    if(smtSolderPaste.getTBackTime().compareTo(new BigDecimal(map.get("min").toString()))==-1){
                        if(smtSolderPaste.getSmtSolderPasterConfig().getTBackTimeStatus()==1){
                            //强制执行
                            smtSolderPaste.setMessage("该锡膏超出设定时间，已强制停止执行");
                            smtSolderPaste.setExecuteStatus(1);
                            smtSolderPaste.setIsDate(0);
                            return smtSolderPaste;
                        }else{
                            smtSolderPaste.setMessage("该锡膏超出设定时间，是否继续执行");
                            smtSolderPaste.setExecuteStatus(2);
                            smtSolderPaste.setIsDate(0);
                            return smtSolderPaste;
                        }

                    }else  if(smtSolderPaste.getTBackTime().compareTo(new BigDecimal(map.get("min").toString()))==1){
                        if(smtSolderPaste.getSmtSolderPasterConfig().getTBackTimeStatus()==1){
                            smtSolderPaste.setMessage("该锡膏未到达设定时间,已强制停止执行");
                            smtSolderPaste.setExecuteStatus(1);
                            smtSolderPaste.setIsDate(0);
                            return smtSolderPaste;
                        }else{
                            smtSolderPaste.setMessage("该锡膏未到达设定时间，是否继续执行");
                            smtSolderPaste.setExecuteStatus(2);
                            smtSolderPaste.setIsDate(0);
                            return smtSolderPaste;
                        }
                    }
                    break;
                case 4:
                    if(smtSolderPaste.getSolderPasteStatus()!=3){
                        throw new BizErrorException("锡膏未搅拌，执行失败!");
                    }
                    //开封
                    //ps:判断搅拌时间是否达到设定时间
                    if(smtSolderPaste.getStirTime().compareTo(new BigDecimal(map.get("min").toString()))==-1){
                        //判断是否超过未开封日期
                        if(smtSolderPaste.getNotOpenTimeLimit().compareTo(new BigDecimal(map.get("hour").toString()))==-1){
                            if(smtSolderPaste.getSmtSolderPasterConfig().getStirTimeStatus()==0 ||smtSolderPaste.getSmtSolderPasterConfig().getNotOpenTimeLimitStatus()==0){
                                smtSolderPaste.setMessage("该锡膏超过搅拌时间及未开封时间，是否继续执行");
                                smtSolderPaste.setExecuteStatus(2);
                                smtSolderPaste.setIsDate(0);
                                return smtSolderPaste;
                            }else{
                                smtSolderPaste.setMessage("该锡膏超出搅拌时间及未开封时间，已强制停止执行");
                                smtSolderPaste.setExecuteStatus(1);
                                smtSolderPaste.setIsDate(0);
                                return smtSolderPaste;
                            }
                        }
                    }else if(smtSolderPaste.getStirTime().compareTo(new BigDecimal(map.get("min").toString()))==1){
                        if(smtSolderPaste.getSmtSolderPasterConfig().getStirTimeStatus()==1){
                            smtSolderPaste.setMessage("该锡膏未到达设定的搅拌时间，已强制停止执行");
                            smtSolderPaste.setExecuteStatus(1);
                            smtSolderPaste.setIsDate(0);
                            return smtSolderPaste;
                        }else{
                            smtSolderPaste.setMessage("该锡膏未到达设定的搅拌时间，是否继续执行");
                            smtSolderPaste.setExecuteStatus(2);
                            smtSolderPaste.setIsDate(0);
                            return smtSolderPaste;
                        }
                    }
                    break;
                case 5:
                    //上料
                    if(smtSolderPaste.getSolderPasteStatus()!=4){
                        throw new BizErrorException("锡膏未开封,执行失败");
                    }
                    //判断是否超过开封日期
                    if(smtSolderPaste.getOpenTimeLimit().compareTo(new BigDecimal(map.get("min").toString()))==1){
                        if(smtSolderPaste.getSmtSolderPasterConfig().getOpenTimeLimitStatus()==1){
                            smtSolderPaste.setMessage("该锡膏超过未开封时间，已强制停止执行");
                            smtSolderPaste.setExecuteStatus(1);
                            smtSolderPaste.setIsDate(0);
                            return smtSolderPaste;
                        }else{
                            smtSolderPaste.setMessage("该锡膏超过未开封时间，是否继续执行");
                            smtSolderPaste.setExecuteStatus(2);
                            smtSolderPaste.setIsDate(0);
                            return smtSolderPaste;
                        }
                    }
                    break;
                case 6:
                    break;
                case 7:
                    //回冰
                    //ps：判断回冰次数
                    if(smtSolderPaste.getReturnIceTime()==smtSolderPaste.getCurrentReturnIceTime()){
                        if(smtSolderPaste.getSmtSolderPasterConfig().getReturnIceTimeStatus()==1){
                            smtSolderPaste.setMessage("该锡膏超过回冰次数，已强制停止执行");
                            smtSolderPaste.setExecuteStatus(1);
                            smtSolderPaste.setIsDate(0);
                            return smtSolderPaste;
                        }else {
                            smtSolderPaste.setMessage("该锡膏超过回冰次数，是否继续执行");
                            smtSolderPaste.setExecuteStatus(2);
                            smtSolderPaste.setIsDate(0);
                            return smtSolderPaste;
                        }
                    }
                    smtSolderPaste.setReturnIceTime(smtSolderPaste.getReturnIceTime()+1);
                    break;

                case 8:
                    //报废
                    redisUtil.del(smtSolderPaste.getSolderPasteId().toString());
                    break;
            }
        }
        smtSolderPaste.setSolderPasteStatus(solderPasteStatus);
        smtSolderPaste.setSpStatusUpdateTime(new Date());
        return smtSolderPaste;
    }

    /**
     * 状态名称
     * @param solderPasteStatus 状态编码
     * @return
     */
    private String StatusToName(Byte solderPasteStatus){
        switch (solderPasteStatus){
            case 1:
                //入冰窟
                return "入冰库";
            case 2:
                //回温
                return "回温";
            case 3:
                //搅拌
                return "搅拌";
            case 4:
                //开封
                return "开封";
            case 5:
                //上料
                return "上料";
            case 6:
                return "用完";
            case 7:
                //报废
                return "回冰";
            case 8:
                return "报废";
        }
        return null;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int save(SmtSolderPaste record) {
        SysUser sysUser = currentUser();
        record.setCreateTime(new Date());
        record.setCreateUserId(sysUser.getUserId());
        record.setModifiedTime(new Date());
        record.setModifiedUserId(sysUser.getUserId());
        record.setOrgId(sysUser.getOrganizationId());
        if(StringUtils.isNotEmpty(record.getSolderPasteStatus())){
            record.setSpStatusUpdateTime(new Date());
        }
        //查询是否存在相同批次记录
        Example example = new Example(SmtSolderPaste.class);
        example.createCriteria().andEqualTo("materialId",record.getMaterialId());
        SmtSolderPaste smtSolderPaste = smtSolderPasteMapper.selectOneByExample(example);
        if(StringUtils.isNotEmpty(smtSolderPaste)){
            throw new BizErrorException("批次重复，请检查批次");
        }
        int num = smtSolderPasteMapper.insertSelective(record);

        //SmtSolderPasterConfig smtSolderPasterConfig = record.getSmtSolderPasterConfig();
//        smtSolderPasterConfig.setSolderPasteId(record.getSolderPasteId());
//        //将锡膏执行状态存入redis
//        redisUtil.set(record.getSolderPasteId().toString(),smtSolderPasterConfig);
        return num;
    }

    @Override
    public int update(SmtSolderPaste entity) {
        SysUser sysUser = currentUser();
        entity.setModifiedUserId(sysUser.getUserId());
        entity.setModifiedTime(new Date());

        int num = smtSolderPasteMapper.updateByPrimaryKeySelective(entity);

        if(StringUtils.isNotEmpty(entity.getSolderPasteStatus())){
            entity.setSpStatusUpdateTime(new Date());
        }

        //查询是否存在相同批次记录
        Example example = new Example(SmtSolderPaste.class);
        example.createCriteria().andEqualTo("materialId",entity.getMaterialId()).andNotEqualTo("solderPasteId",entity.getSolderPasteId());
        SmtSolderPaste smtSolderPaste = smtSolderPasteMapper.selectOneByExample(example);
        if(StringUtils.isNotEmpty(smtSolderPaste)){
            throw new BizErrorException("批次重复，请检查批次");
        }

        if(StringUtils.isNotEmpty(entity.getSmtSolderPasterConfig())){
            SmtSolderPasterConfig smtSolderPasterConfig = entity.getSmtSolderPasterConfig();
            smtSolderPasterConfig.setSolderPasteId(entity.getSolderPasteId());
            //将锡膏执行状态存入redis
            redisUtil.set(DEFAULT_NAME+entity.getSolderPasteId().toString(),smtSolderPasterConfig);
        }
        SmtHtSolderPaste smtHtSolderPaste = new SmtHtSolderPaste();
        BeanUtil.copyProperties(entity,smtHtSolderPaste);
        num+=smtHtSolderPasteMapper.insertSelective(smtHtSolderPaste);
        return num;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int batchDelete(String ids) {
        SysUser sysUser = currentUser();
        String[] arrId = ids.split(",");
        for (String id : arrId) {
            SmtSolderPaste smtSolderPaste = smtSolderPasteMapper.selectByPrimaryKey(id);
            if(StringUtils.isEmpty(smtSolderPaste)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012000,id);
            }
            //删除锡膏执行状态
            redisUtil.del(DEFAULT_NAME+id);

            SmtHtSolderPaste smtHtSolderPaste = new SmtHtSolderPaste();
            BeanUtil.copyProperties(smtSolderPaste,smtHtSolderPaste);
            smtHtSolderPasteMapper.insertSelective(smtHtSolderPaste);
        }
        return super.batchDelete(ids);
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

    public Map<String,Object> getDatePoor(Date endDate, Date nowDate) {

        Map<String,Object> map = new HashMap<>();
        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        // long ns = 1000;
        // 获得两个时间的毫秒时间差异
        long diff = endDate.getTime() - nowDate.getTime();
        // 计算差多少天
        long day = diff / nd;
        // 计算差多少小时
        long hour = diff % nd / nh;
        // 计算差多少分钟
        long min = diff % nd % nh / nm;
        // 计算差多少秒//输出结果
         //long sec = diff % nd % nh % nm / ns;
        map.put("day",day);
        map.put("hour",hour);
        map.put("min",min);
        return map;
    }
}
