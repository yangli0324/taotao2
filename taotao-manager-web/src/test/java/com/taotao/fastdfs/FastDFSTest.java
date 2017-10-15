package com.taotao.fastdfs;

import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.junit.Test;

import com.taotao.tuils.FastDFSClient;

/**
 * @Title: FastDFSTest.java
 * @Package com.taotao.fastdfs
 * @Description: 使用FastDFS图片上传测试
 */
public class FastDFSTest {

	@Test
	public void fileUpload() {
		try {
			// 1:加载resource配置文件、其配置的内容是指向TrackerServer地址
			ClientGlobal.init("D:/Workspace-taotao/taotao-manager-web/src/main/resources/resource/fast_dfs.conf");

			// 2：创建TrackerClient
			TrackerClient trackerClient = new TrackerClient();

			// 3:使用TrackerClient创建连接，获取TrackerServer对象
			TrackerServer trackerServer = trackerClient.getConnection();

			// 4:创建StorageServer的引用、职位null
			StorageServer storageServer = null;

			// 5:创建StorageClient对象
			StorageClient storageClient = new StorageClient(trackerServer, storageServer);
			// 6:使用StorageClient上传图片
			String[] upload_file = storageClient.upload_file("C:/Users/15378/Pictures/Saved Pictures/th.jpg", "jpg",
					null);

			for (String string : upload_file) {
				System.out.println(string);
			}
		} catch (Exception e) {

			e.printStackTrace();
		}

	}

	// 使用fastFSD工具上传图片
	@Test
	public void uploadTool() throws Exception {
		FastDFSClient client = new FastDFSClient(
				"D:/Workspace-taotao/taotao-manager-web/src/main/resources/resource/fast_dfs.conf");

		String file = client.uploadFile("C:/Users/15378/Pictures/Saved Pictures/th.jpg");
		System.out.println(file);

	}
}
