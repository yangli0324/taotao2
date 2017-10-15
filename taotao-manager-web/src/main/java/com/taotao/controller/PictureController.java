package com.taotao.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.taotao.tuils.FastDFSClient;
import com.taotao.tuils.JsonUtils;

/**
 * @Title: PictureController.java
 * @Package com.taotao.controller
 * @Description: 图片上传
 */
@Controller
public class PictureController {

	@Value("${IMAGE_SERVER_URL}")
	private String IMAGE_SERVER_URL;

	@RequestMapping(value = "/pic/upload", produces = MediaType.TEXT_PLAIN_VALUE + ";charset=UTF-8")
	@ResponseBody
	public String fileUpload(MultipartFile uploadFile) {

		try {
			// 1:接受上传的文件
			byte[] content = uploadFile.getBytes();
			// 2:获取上传文件名
			String originalFilename = uploadFile.getOriginalFilename();

			// 3:获取扩展名
			String ext = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);

			// 4:创建FastDFS的客服端
			FastDFSClient fastDFSClient = new FastDFSClient("classpath:resource/fast_dfs.conf");

			// 5:图片上传到服务器
			String url = fastDFSClient.uploadFile(content, ext);

			// 6:获取返回结果
			Map result = new HashMap<>();

			result.put("error", 0);
			result.put("url", IMAGE_SERVER_URL + url);

			return JsonUtils.objectToJson(result);
		} catch (Exception e) {
			e.printStackTrace();
			Map result = new HashMap<>();

			result.put("error", 0);
			result.put("message", "图片上传失败！");

			return JsonUtils.objectToJson(result);
		}

	}
}
