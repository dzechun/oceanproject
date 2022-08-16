package com.fantechs.provider.fileserver.common;


import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.proto.storage.DownloadByteArray;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Component
public class FastDFSClient {

		@Autowired
		private FastFileStorageClient storageClient;
	/**
	 * 上传文件
	 * @param file 文件对象
	 * @return 文件访问地址
	 * @throws IOException
	 */
	public String uploadFile(MultipartFile file) throws IOException {
		StorePath storePath = storageClient.uploadFile(file.getInputStream(),file.getSize(), FilenameUtils.getExtension(file.getOriginalFilename()),null);
		return storePath.getFullPath();
	}
	/**
	 * 下载文件
	 * @param fileUrl 文件url
	 * @return
	 */
	public byte[]  download(String fileUrl) {
		String group = fileUrl.substring(0, fileUrl.indexOf("/"));
		String path = fileUrl.substring(fileUrl.indexOf("/") + 1);
		byte[] bytes = storageClient.downloadFile(group, path, new DownloadByteArray());
		return bytes;
	}

}
