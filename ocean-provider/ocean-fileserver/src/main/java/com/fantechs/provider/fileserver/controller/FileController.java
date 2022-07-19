package com.fantechs.provider.fileserver.controller;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.*;
import com.fantechs.provider.fileserver.config.BaidubceConfig;
import com.fantechs.provider.fileserver.entity.ImageData;
import com.fantechs.provider.fileserver.utils.BASE64DecodedMultipartFile;
import com.fantechs.provider.fileserver.utils.MinioUtils;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Decoder;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/file")
public class FileController {
    @Autowired
    private MinioUtils minioUtils;

    @Autowired
    private BaidubceConfig baidubceConfig;

    /**
     * 单文件上传
     * @param file
     * @return
     * @throws IOException
     */
    @ApiOperation("文件上传")
    @PostMapping(value="/upload",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadMultipleFile(@ApiParam(value = "文件必传",required = true) @RequestPart(value = "file") MultipartFile file) throws IOException {
        Map<String, Object> data = new HashMap<>();
        String fileName = file.getOriginalFilename();
        if(!FileCheckUtil.checkFileType(fileName)){
            return ControllerUtil.returnFail("文件格式错误", ErrorCodeEnum.GL99990100.getCode());
        }
        String fileUrl = minioUtils.upload(file);
        data.put("url", fileUrl);
        return  ControllerUtil.returnDataSuccess(data,1);
    }

    /**
     * 文件下载
     * @param fileUrl  url 开头从文件夹开始
     */
    @ApiOperation(value = "文件下载",notes = "文件下载")
    @PostMapping("/download")
    public void  download(@ApiParam(value = "传入文件地址",required = true) @RequestParam(value = "fileUrl")
                              String fileUrl, HttpServletResponse response) throws Exception{
        String fileName = fileUrl.substring(fileUrl.lastIndexOf("/")+1);
        byte[] data = minioUtils.download(fileUrl);

        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/octet-stream");
        response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));

        // 写出
        ServletOutputStream outputStream = response.getOutputStream();
        IOUtils.write(data, outputStream);
    }

    /**
     * 上传单个文件
     */
    @PostMapping(value="multipleFileBase64")
    @ApiOperation("Baes64图片上传")
    public ResponseEntity<?> uploadMultipleFileBASE64(@ApiParam(value = "图片Baes64",required = true) @RequestBody ImageData imageData) throws IOException {
        Map<String, Object> data = new HashMap<String, Object>();
        String path = "";
        if (StringUtils.isNotEmpty(imageData.getImaData())) {
            path = getVoiceBase64(imageData.getImaData().replace(" ",""));
        }
        if(StringUtils.isEmpty(path)){
            return ControllerUtil.returnFail("上传失败", ErrorCodeEnum.GL99990100.getCode());
        }
        data.put("url", path);
        return ControllerUtil.returnDataSuccess(data, 1);
    }

    /**
     * 上传单个文件
     */
    @ApiOperation("文件上传")
    @PostMapping(value="/uploadToSVG",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadMultipleFileToSVG(@ApiParam(value = "文件必传",required = true) @RequestPart(value = "file") MultipartFile file) throws IOException {
        Map<String, Object> data = new HashMap<String, Object>();
        String fileName = file.getOriginalFilename();
        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
        if (!suffix.equalsIgnoreCase("jpg")&&!suffix.equalsIgnoreCase("png")) {
            return ControllerUtil.returnFail("文件格式错误", ErrorCodeEnum.GL99990100.getCode());
        }
        byte[]  fileByte = file.getBytes();
        String voiceBase64= Base64.getEncoder().encodeToString(fileByte);
        String path=getVoiceBase64(voiceBase64);
        if(StringUtils.isEmpty(path)){
            return ControllerUtil.returnFail("上传失败", ErrorCodeEnum.GL99990100.getCode());
        }
        data.put("url", path);
        return  ControllerUtil.returnDataSuccess(data,1);
    }


    /**
     * 获取百度云access_token
     * @return access_token
     * @throws IOException
     */
    public  String getToken() throws IOException {
        String access_token = null;
        Map<String,Object> bodyMap = new HashMap<>();
        bodyMap.put("grant_type", baidubceConfig.getGrant_type());
        bodyMap.put("client_id",baidubceConfig.getClient_id());
        bodyMap.put("client_secret",baidubceConfig.getClient_secret());
        String  result = HTTPUtils.postMap(baidubceConfig.getOauth_url(),new HashMap<>(),bodyMap);
        if(StringUtils.isNotEmpty(result)){
            Map<String,Object> map = JsonUtils.jsonToMap(result);
            access_token = map.get("access_token").toString();
            RedisUtil redisUtil=(RedisUtil) SpringUtil.getBean("redisUtil");
            redisUtil.set("BAIDUBCE_ACCESS_TOKEN",access_token,29*24*60*60);
        }
        return access_token;
    }

    public  String getVoiceBase64 (String voiceBase64)throws IOException{
        String path=null;
        String access_token=null;
        RedisUtil redisUtil=(RedisUtil) SpringUtil.getBean("redisUtil");
        Object o  = redisUtil.get("BAIDUBCE_ACCESS_TOKEN");
        if(StringUtils.isEmpty(o)){
            access_token =getToken();
        }else{
            access_token = o.toString();
        }
        Map<String,String > heardMap =  new HashMap<>();
        heardMap.put("Content-Type", "application/x-www-form-urlencoded");

        Map<String,Object > bodyMap =  new HashMap<>();
        bodyMap.put("image",voiceBase64);
        bodyMap.put("option","pencil");
        bodyMap.put("access_token",access_token);

        String  result = HTTPUtils.postMap(baidubceConfig.getRimage_process_url(),heardMap,bodyMap);
        Map<String,Object> map = JsonUtils.jsonToMap(result);
        String imgData = map.get("image").toString();
        if (StringUtils.isNotEmpty(imgData)) {
            BASE64Decoder decoder = new BASE64Decoder();
            byte[] b = decoder.decodeBuffer(imgData);
            for(int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {
                    b[i] += 256;
                }
            }
            //BASE64转MultipartFile
            BASE64DecodedMultipartFile imgByte = new BASE64DecodedMultipartFile(b, "data:image/jpg;base64");
            path = minioUtils.upload(imgByte);
        }
        return  path;
    }



}
