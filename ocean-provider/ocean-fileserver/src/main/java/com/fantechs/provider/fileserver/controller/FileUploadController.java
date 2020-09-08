package com.fantechs.provider.fileserver.controller;



import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.ConstantUtils;
import com.fantechs.common.base.utils.FileCheckUtil;
import com.fantechs.provider.fileserver.common.FastDFSClient;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;


/**
 * 文件上传Controller
 * @author Luolihong
 *
 */
@RestController
@RequestMapping("/file")
public class FileUploadController {
	@Autowired
	private FastDFSClient fdfsClient;
	
	/**
	 * 上传单个文件
	 * @param
	 * @param
	 * @param
	 * @return
	 * @throws IOException
	 */
	@PostMapping(value="/upload",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
		public ResponseEntity uploadMultipleFile(@RequestPart(value = "file") MultipartFile file) throws IOException {

		Map<String, Object> data = new HashMap<String, Object>();
		String fileName = file.getOriginalFilename();
		if(!FileCheckUtil.checkFileType(fileName)){
			return ControllerUtil.returnFail("文件格式错误", ErrorCodeEnum.GL99990100.getCode());
		}
		String path = "";
		path = fdfsClient.uploadFile(file);
		data.put("url", path);
		return  ControllerUtil.returnDataSuccess(data,1);
	}

	/**
	 * 文件下载
	 * @param fileUrl  url 开头从组名开始
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/download")
	public void  download(String fileUrl, HttpServletResponse response) throws Exception{
		if(StringUtils.isEmpty(fileUrl)){
			new Exception();
		}
		String fileName = fileUrl.substring(fileUrl.lastIndexOf("/")+1);
		byte[] data = fdfsClient.download(fileUrl);

		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/octet-stream");
		response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));

		// 写出
		ServletOutputStream outputStream = response.getOutputStream();
		IOUtils.write(data, outputStream);
	}
}
