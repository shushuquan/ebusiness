package shu.edu.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import shu.edu.common.utils.FastDFSClient;
import shu.edu.common.utils.JsonUtils;

/**
 * 图片上传Controller
 * @author asus
 *
 */
@Controller
public class PictureController {
	
	@Value("${image_server_url}")
	private String image_server_url;
	@RequestMapping("/pic/upload")
	@ResponseBody
	public String uploadFile(MultipartFile uploadFile) {
		try {
			//把图片上传到图片服务器，得到一个图片的地址和文件名
			FastDFSClient fastDFSClient = new FastDFSClient("classpath:conf/client.conf");
			//获得文件扩展名
			String originalFilename = uploadFile.getOriginalFilename();
			String extName = originalFilename.substring(originalFilename.lastIndexOf(".")+1);
			String url = fastDFSClient.uploadFile(uploadFile.getBytes(), extName);
			//拼接图片服务器地址	//补充位完整的url
			url=image_server_url+url;
			//封装到Map中返回
			Map result = new HashMap<>();
			result.put("error", 0);
			result.put("url", url);
			return JsonUtils.objectToJson(result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Map result = new HashMap<>();
			result.put("error", 1);
			result.put("massege", "图片上传失败");	
			return JsonUtils.objectToJson(result);		
		}
	}
}
