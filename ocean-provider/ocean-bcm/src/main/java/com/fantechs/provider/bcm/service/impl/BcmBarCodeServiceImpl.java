package com.fantechs.provider.bcm.service.impl;

import com.alibaba.fastjson.JSON;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysSpecItem;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.entity.security.search.SearchSysSpecItem;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.bcm.BcmBarCodeDto;
import com.fantechs.common.base.general.dto.bcm.BcmBarCodeWorkDto;
import com.fantechs.common.base.general.dto.mes.pm.SmtWorkOrderDto;
import com.fantechs.common.base.general.dto.mes.pm.search.SearchSmtBarcodeRuleSpec;
import com.fantechs.common.base.general.dto.mes.pm.search.SearchSmtWorkOrder;
import com.fantechs.common.base.general.entity.bcm.BcmBarCode;
import com.fantechs.common.base.general.entity.bcm.BcmBarCodeDet;
import com.fantechs.common.base.general.entity.bcm.search.SearchBcmBarCode;
import com.fantechs.common.base.general.entity.mes.pm.SmtBarcodeRuleSpec;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.mes.pm.PMFeignApi;
import com.fantechs.provider.api.security.service.SecurityFeignApi;
import com.fantechs.provider.bcm.mapper.BcmBarCodeDetMapper;
import com.fantechs.provider.bcm.mapper.BcmBarCodeMapper;
import com.fantechs.provider.bcm.service.BcmBarCodeService;
import com.fantechs.provider.bcm.util.FTPUtil;
import com.fantechs.provider.bcm.util.SocketClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Collectors;

/**
* @author Mr.Lei
* @create 2020/12/22.
*/
@Service
public class BcmBarCodeServiceImpl  extends BaseService<BcmBarCode> implements BcmBarCodeService {

    @Resource
    private BcmBarCodeMapper bcmBarCodeMapper;
    @Autowired
    private FTPUtil ftpUtil;
    @Resource
    private SecurityFeignApi securityFeignApi;
    @Resource
    BcmBarCodeDetMapper bcmBarCodeDetMapper;
    @Resource
    private PMFeignApi pmFeignApi;

    @Override
    public List<BcmBarCodeDto> findList(SearchBcmBarCode searchBcmBarCode) {
        return bcmBarCodeMapper.findList(searchBcmBarCode);
    }

    @Override
    public BcmBarCodeWorkDto work(SearchBcmBarCode searchBcmBarCode) {
        BcmBarCodeWorkDto bcmBarCodeWorkDto = bcmBarCodeMapper.sel_work_order(searchBcmBarCode);
        if(StringUtils.isEmpty(bcmBarCodeWorkDto.getBarcodeRuleId())){
            throw new BizErrorException(ErrorCodeEnum.valueOf("此工单没有绑定条码规则"));
        }
        //生成规则
        SearchSmtBarcodeRuleSpec searchSmtBarcodeRuleSpec = new SearchSmtBarcodeRuleSpec();
        searchSmtBarcodeRuleSpec.setBarcodeRuleId(bcmBarCodeWorkDto.getBarcodeRuleId());
        List<SmtBarcodeRuleSpec> list = pmFeignApi.findSpec(searchSmtBarcodeRuleSpec).getData();
        Example example1 = new Example(BcmBarCode.class);
        example1.createCriteria().andEqualTo("workOrderId",bcmBarCodeWorkDto.getWorkOrderId());
        List<BcmBarCode> bcmBarCodes = bcmBarCodeMapper.selectByExample(example1);
        String maxQty = bcmBarCodeMapper.selMaxCode(bcmBarCodeWorkDto.getWorkOrderId());
        String code = pmFeignApi.generateCode(list,maxQty,bcmBarCodeWorkDto.getMaterialCode()).getData();
        bcmBarCodeWorkDto.setBarcode(code);
        return bcmBarCodeWorkDto;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void download(List<String> savePath, HttpServletResponse response) throws UnsupportedEncodingException {
            SearchSysSpecItem searchSysSpecItem = new SearchSysSpecItem();
            searchSysSpecItem.setSpecCode("FTP");
            ResponseEntity<List<SysSpecItem>> itemList= securityFeignApi.findSpecItemList(searchSysSpecItem);
            List<SysSpecItem> sysSpecItemList = itemList.getData();
            Map map = (Map) JSON.parse(sysSpecItemList.get(0).getParaValue());
//            List<InputStream> inputStreams = downloadFile(map,savePath);
//            List<String> path = new ArrayList<>();
//            for (String s : savePath) {
//                path.add(s.split("/")[1]);
//            }
//            String[] fileName = path.toArray(new String[path.size()]);
//            this.ftpUtil.downloadZipFiles(response,inputStreams,"打印文件",fileName);
            String[] filName = savePath.get(0).split("/");
            // 配置文件下载
            response.setHeader("content-type", "application/octet-stream");
            response.setContentType("application/octet-stream");
            // 下载文件能正常显示中文
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(filName[1], "UTF-8"));
            // 实现文件下载
            byte[] buffer = new byte[1024];
            InputStream fis = null;
            BufferedInputStream bis = null;
            try {
                fis = downloadFile(map,savePath).get(0);
                bis = new BufferedInputStream(fis);
                OutputStream os = response.getOutputStream();
                int i = bis.read(buffer);
                while (i != -1) {
                    os.write(buffer, 0, i);
                    i = bis.read(buffer);
                }
                System.out.println("Download  successfully!");

            } catch (Exception e) {
                System.out.println("Download  failed!");

            } finally {
                if (bis != null) {
                    try {
                        bis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
    }

    /**
     * 打印
     * @param workOrderId
     * @return
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int print(Long workOrderId) {
        SysUser currentUserInfo = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUserInfo)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        Example example = new Example(BcmBarCode.class);
        example.createCriteria().andEqualTo("workOrderId",workOrderId).andEqualTo("status",(byte)1);
        List<BcmBarCode> bcmBarCodes = bcmBarCodeMapper.selectByExample(example);
        if(StringUtils.isEmpty(bcmBarCodes)){
            throw new BizErrorException(ErrorCodeEnum.valueOf("工单没有生成可打印的条码"));
        }
        if(!SocketClient.isConnect){
            throw new BizErrorException(ErrorCodeEnum.valueOf("连接打印服务失败"));
        }
        try {
            for (BcmBarCode bcmBarCode : bcmBarCodes) {
                Example example1 = new Example(BcmBarCodeDet.class);
                example1.createCriteria().andEqualTo("barCodeId",bcmBarCode.getBarCodeId());
                List<BcmBarCodeDet> bcmBarCodeDets = bcmBarCodeDetMapper.selectByExample(example1);
                for (BcmBarCodeDet bcmBarCodeDet : bcmBarCodeDets) {
                    //打印
                    SearchSmtWorkOrder searchSmtWorkOrder = new SearchSmtWorkOrder();
                    searchSmtWorkOrder.setWorkOrderId(workOrderId);
                    List<SmtWorkOrderDto> smtWorkOrderDto = pmFeignApi.findWorkOrderList(searchSmtWorkOrder).getData();
                    if(smtWorkOrderDto.size()<1){
                        throw new BizErrorException(ErrorCodeEnum.valueOf("获取工单信息失败"));
                    }
                    Map<String, Object> map = ControllerUtil.dynamicCondition(smtWorkOrderDto.get(0));
                    map.put("QrCode",bcmBarCodeDet.getBarCodeContent());
                    String json = JSON.toJSONString(map);
                    SocketClient.out(json);
                }
                //更新已打印状态
                bcmBarCode.setStatus((byte)2);
                bcmBarCode.setModifiedTime(new Date());
                bcmBarCode.setModifiedUserId(currentUserInfo.getUserId());
                bcmBarCodeMapper.updateByPrimaryKeySelective(bcmBarCode);
            }
        }catch (Exception e){
            throw new BizErrorException(ErrorCodeEnum.valueOf("打印失败！"));
        }
        return 1;
    }

    @Override
    public BcmBarCodeDet verifyQrCode(String QrCode, Long workOrderId) {
        Example example = new Example(BcmBarCode.class);
        example.createCriteria().andEqualTo("workOrderId",workOrderId).andEqualTo("status",(byte)2);
        List<BcmBarCode> list = bcmBarCodeMapper.selectByExample(example);
        List<Long> ids = list.stream().map(BcmBarCode::getBarCodeId).collect(Collectors.toList());
        Example example1 =new Example(BcmBarCodeDet.class);
        example1.createCriteria().andIn("barCodeId",ids).andEqualTo("barCodeContent",QrCode).andEqualTo("status",(byte)1);
        List<BcmBarCodeDet> bcmBarCodeDets = bcmBarCodeDetMapper.selectByExample(example1);
        if(bcmBarCodeDets.size()>0){
            return bcmBarCodeDets.get(0);
        }
        return null;
    }

    /**
     * 生成条码
     * @param record
     * @return
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int saveCode(BcmBarCodeWorkDto record) {
        SysUser currentUserInfo = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUserInfo)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        //查询产生数量是否大于工单目标数量
        Example example = new Example(BcmBarCode.class);
        example.createCriteria().andEqualTo("workOrderId",record.getWorkOrderId());
        List<BcmBarCode> bcmBarCodes = bcmBarCodeMapper.selectByExample(example);
        Integer num = bcmBarCodes.stream().mapToInt(BcmBarCode::getPrintQuantity).sum();

        if(record.getWorkOrderQuantity().compareTo(BigDecimal.valueOf(num+record.getPrintQuantity()))==1){
            throw new BizErrorException(ErrorCodeEnum.valueOf("产生数量不能大于工单数量"));
        }
        record.setCreateTime(new Date());
        record.setCreateUserId(currentUserInfo.getUserId());
        record.setModifiedTime(new Date());
        record.setModifiedUserId(currentUserInfo.getUserId());
        int nm = bcmBarCodeMapper.insertUseGeneratedKeys(record);

        for (Integer i = 0; i < record.getPrintQuantity(); i++) {
            BcmBarCodeDet bcmBarCodeDet = new BcmBarCodeDet();
            bcmBarCodeDet.setBarCodeId(record.getBarCodeId());
            SearchSmtBarcodeRuleSpec searchSmtBarcodeRuleSpec = new SearchSmtBarcodeRuleSpec();
            searchSmtBarcodeRuleSpec.setBarcodeRuleId(record.getBarcodeRuleId());
            List<SmtBarcodeRuleSpec> list = pmFeignApi.findSpec(searchSmtBarcodeRuleSpec).getData();
            String maxCode = bcmBarCodeMapper.selMaxCode(record.getWorkOrderId());
            //String code = BarcodeRuleUtils.analysisCode(list,maxCode,record.getMaterialCode());
            //生成流水号
            String code = pmFeignApi.generateCode(list,maxCode,record.getMaterialCode()).getData();
            bcmBarCodeDet.setBarCodeContent(code);
            bcmBarCodeDet.setCreateTime(new Date());
            bcmBarCodeDet.setCreateUserId(currentUserInfo.getUserId());
            bcmBarCodeDet.setModifiedTime(new Date());
            bcmBarCodeDet.setModifiedUserId(currentUserInfo.getUserId());
            bcmBarCodeDetMapper.insertSelective(bcmBarCodeDet);
        }

        return nm;
    }

    @Override
    public int updateByContent(List<BcmBarCodeDet> bcmBarCodeDets) {
        SysUser currentUserInfo = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUserInfo)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        int num = 0;
        for (BcmBarCodeDet bcmBarCodeDet : bcmBarCodeDets) {
            bcmBarCodeDet.setModifiedUserId(currentUserInfo.getUserId());
            bcmBarCodeDet.setModifiedTime(new Date());
            bcmBarCodeDet.setStatus((byte)2);
            num=+bcmBarCodeDetMapper.updateByPrimaryKeySelective(bcmBarCodeDet);
        }
        return num;
    }

    /**
     * 下载FTP文件
     * @param map
     * @param savePath
     * @return
     */
    @Transactional(rollbackFor = RuntimeException.class)
    public List<InputStream> downloadFile(Map map, List<String> savePath) {
        boolean isLogin = false;
        List<InputStream> ins = new ArrayList<>();
        //上传FTP服务器
        for (String s : savePath) {
            try {
                String ip = map.get("ip").toString();
                Integer port = Integer.parseInt(map.get("port").toString());
                String username = map.get("username").toString();
                String password = map.get("password").toString();
                isLogin = this.ftpUtil.connectFTP(ip, port, username, password);
                if (isLogin) {
                    String[] path = s.split("/");
                    InputStream in = this.ftpUtil.downFile(path[0], path[1]);
                    ins.add(in);
                }
            } catch (Exception e) {
                throw new BizErrorException(ErrorCodeEnum.valueOf("下载失败"));
            } finally {
                this.ftpUtil.loginOut();
            }
        }
        return ins;
    }
}
