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
import com.fantechs.common.base.general.entity.bcm.BcmBarCode;
import com.fantechs.common.base.general.entity.bcm.search.SearchBcmBarCode;
import com.fantechs.common.base.general.entity.mes.pm.SmtBarcodeRuleSpec;
import com.fantechs.common.base.general.entity.mes.pm.SmtWorkOrder;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.security.service.SecurityFeignApi;
import com.fantechs.provider.bcm.mapper.BcmBarCodeMapper;
import com.fantechs.provider.bcm.service.BcmBarCodeService;
import com.fantechs.provider.bcm.util.FTPUtil;
import com.fantechs.provider.bcm.util.SocketClient;
import com.fantechs.provider.mes.pm.mapper.SmtBarcodeRuleSpecMapper;
import com.fantechs.provider.mes.pm.mapper.SmtWorkOrderMapper;
import com.fantechs.provider.mes.pm.utils.BarcodeRuleUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.Socket;
import java.net.URLEncoder;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

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
    SmtBarcodeRuleSpecMapper smtBarcodeRuleSpecMapper;
    @Resource
    SmtWorkOrderMapper smtWorkOrderMapper;
    @Override
    public List<BcmBarCodeDto> findList(SearchBcmBarCode searchBcmBarCode) {
        return bcmBarCodeMapper.findList(searchBcmBarCode);
    }

    @Override
    public BcmBarCodeWorkDto work(SearchBcmBarCode searchBcmBarCode) {
        BcmBarCodeWorkDto bcmBarCodeWorkDto = bcmBarCodeMapper.sel_work_order(searchBcmBarCode);
        //生成规则
        Example example = new Example(SmtBarcodeRuleSpec.class);
        example.createCriteria().andEqualTo("barcodeRuleId",bcmBarCodeWorkDto.getBarcodeRuleId());
        List<SmtBarcodeRuleSpec> list = smtBarcodeRuleSpecMapper.selectByExample(example);
        Example example1 = new Example(BcmBarCode.class);
        example1.createCriteria().andEqualTo("workOrderId",bcmBarCodeWorkDto.getWorkOrderId());
        List<BcmBarCode> bcmBarCodes = bcmBarCodeMapper.selectByExample(example1);
        String maxQty = null;
        if(bcmBarCodes.size()>0){
            Integer num = bcmBarCodes.stream().mapToInt(BcmBarCode::getPrintQuantity).sum();
            //maxQty = bcmBarCodes.get(0).getBarCodeContent().substring(0,bcmBarCodes.get(0).getBarCodeContent().length()-2);
            maxQty = num.toString();
        }
        String code = BarcodeRuleUtils.analysisCode(list,maxQty,bcmBarCodeWorkDto.getMaterialCode());
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

    @Override
    public int print(Long workOrderId) {
        BcmBarCode bcmBarCode = new BcmBarCode();
        bcmBarCode.setWorkOrderId(workOrderId);
        bcmBarCode = bcmBarCodeMapper.selectOne(bcmBarCode);
        if(StringUtils.isEmpty(bcmBarCode)){
            throw new BizErrorException(ErrorCodeEnum.valueOf("工单没有生成条码"));
        }
        //查询绑定标签
        if(!SocketClient.isConnect){
            throw new BizErrorException(ErrorCodeEnum.valueOf("连接打印服务失败"));
        }
        SmtWorkOrderDto smtWorkOrderDto = smtWorkOrderMapper.selectByWorkOrderId(workOrderId);
        Map<String, Object> map = ControllerUtil.dynamicCondition(smtWorkOrderDto);
        map.put("QrCode",bcmBarCode.getBarCodeContent());
        String json = JSON.toJSONString(map);
        SocketClient.out(json);
        return 1;
    }

    @Override
    public int verifyQrCode(String QrCode, Long workOrderId) {
        BcmBarCode bcmBarCode = new BcmBarCode();
        bcmBarCode.setWorkOrderId(workOrderId);
        bcmBarCode = bcmBarCodeMapper.selectOne(bcmBarCode);
        if(StringUtils.isEmpty(bcmBarCode)){
            throw new BizErrorException(ErrorCodeEnum.valueOf("工单没有生成条码"));
        }
        if(bcmBarCode.getBarCodeContent().substring(0,bcmBarCode.getBarCodeContent().length()-2).equals(QrCode.substring(0,QrCode.length()-2))){
            return 1;
        }
        return 0;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int save(BcmBarCode record) {
        SysUser currentUserInfo = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUserInfo)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        //生成条码
        record.setCreateTime(new Date());
        record.setCreateUserId(currentUserInfo.getUserId());
        record.setModifiedTime(new Date());
        record.setModifiedUserId(currentUserInfo.getUserId());

        return bcmBarCodeMapper.insertSelective(record);
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
