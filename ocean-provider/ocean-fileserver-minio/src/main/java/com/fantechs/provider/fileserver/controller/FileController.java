package com.fantechs.provider.fileserver.controller;


import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.*;
import com.fantechs.provider.fileserver.common.BASE64DecodedMultipartFile;
import com.fantechs.provider.fileserver.config.BaidubceConfig;
import com.fantechs.provider.fileserver.entity.ImageData;
import com.fantechs.provider.fileserver.utils.MinioUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Decoder;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 文件上传Controller
 * @author Luolihong
 *
 */
@RestController
@RequestMapping("/file")
@Api(tags = "文件管理")
public class FileController {


}
