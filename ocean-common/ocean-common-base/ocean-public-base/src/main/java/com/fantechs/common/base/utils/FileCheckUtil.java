/*
 * Copyright 2015-2016 com.fenger. All rights reserved.
 * Support: http://www.fenger-init.com
 * License: http://www.fenger-init.com/license
 */
package com.fantechs.common.base.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * 文件信息
 * 
 * @author leifengzhi
 * @version 1.0
 */
public class FileCheckUtil {

	public final static Map<String, String> FILE_TYPE_MAP = new HashMap<String, String>();
	//文件类型
	public static void init() {
		FILE_TYPE_MAP.put("jpg", "ffd8ffe0"); // JPEG (jpg)
		FILE_TYPE_MAP.put("png", "89504e47"); // PNG (png)
		FILE_TYPE_MAP.put("gif", "47494638"); // GIF (gif)
		FILE_TYPE_MAP.put("bmp", "ffd8ffe1"); // BMP (jpg)
		FILE_TYPE_MAP.put("tif", "ffd8ffe2"); // TIF (tif)
		FILE_TYPE_MAP.put("pcx", "ffd8ffe3"); // PCX (pcx)
		FILE_TYPE_MAP.put("psd", "ffd8ffe4"); // PSD (psd)
		FILE_TYPE_MAP.put("jpeg", "ffd8ffe5"); // JPEG (jpeg)
		FILE_TYPE_MAP.put("ico", "41564921"); // ICO (ico)
		FILE_TYPE_MAP.put("html", "3c21444f"); // html (html)
		FILE_TYPE_MAP.put("css", "48544d4c"); // css
		FILE_TYPE_MAP.put("js", "696b2e71"); // js
		FILE_TYPE_MAP.put("doc", "d0cf11e0"); //
		FILE_TYPE_MAP.put("pdf", "25504446"); // (pdf)
		FILE_TYPE_MAP.put("zip", "504b0304");
		FILE_TYPE_MAP.put("rar", "52617221");
		FILE_TYPE_MAP.put("docx", "504b0304");
		FILE_TYPE_MAP.put("xml", "3C3F786D6C");
		FILE_TYPE_MAP.put("dwg", "41433130");
		FILE_TYPE_MAP.put("xls", "D0CF11E0");
		FILE_TYPE_MAP.put("xlsx", "504B0304");
		FILE_TYPE_MAP.put("rar", "52617221");
		FILE_TYPE_MAP.put("wav", "57415645");
		FILE_TYPE_MAP.put("avi", "41564920");
		FILE_TYPE_MAP.put("apk", "61A6D9C0");
		FILE_TYPE_MAP.put("rmvb", "2e524d46");
		FILE_TYPE_MAP.put("flv", "464c5601");
		FILE_TYPE_MAP.put("mp4", "00000020");
		FILE_TYPE_MAP.put("wmv", "3026b275");
		FILE_TYPE_MAP.put("exe", "3026b123");

	}

	static {
		init();
	}
	private FileCheckUtil() {

	}

		/**
         * 校验type
         * @param fileName
         * @return
         */
	public static boolean checkFileType(String fileName) {
		if (null == fileName || fileName.equals("")) {
			return false;
		}
		// 文件后缀,统一转换小写
		String suffix = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();

		if("txt".equals(suffix)){
			return true;
		}

		String realCode = FILE_TYPE_MAP.get(suffix);
		if (StringUtils.isNotEmpty(realCode)) {
			return true;
		}

		// 全都不匹配，校验不通过
		return false;
	}
}