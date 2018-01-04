package taotao.com.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import taotao.com.common.utils.JsonUtils;
import taotao.com.utils.FastDFSClient;

@Controller
public class PictureController {
	
	@Value("${IMAGE_SERVER_URL}")
	private String IMAGE_SERVER_URL;
	
	@RequestMapping("/pic/upload")
	@ResponseBody
	public String uploadFile(MultipartFile uploadFile){
		try{
			//取得上传文件的文件名
			String originalFileName=uploadFile.getOriginalFilename();
			//取得文件的扩展名
			String extName=originalFileName.substring(originalFileName.lastIndexOf(".")+1);
			//上传文件
			FastDFSClient client=new FastDFSClient("classpath:resource/client.conf");
			//返回文件的url
			String url=client.uploadFile(uploadFile.getBytes(),extName);
			url=IMAGE_SERVER_URL+url;
			Map<Object, Object> map=new HashMap<>();
			map.put("error", 0);
			map.put("url", url);
			System.out.println(map);
			return JsonUtils.objectToJson(map);
		}catch(Exception e){
			e.printStackTrace();
			Map<String, Comparable> map=new HashMap<String, Comparable>();
			map.put("error", 1);
			map.put("message", "上传图片错误");
			System.out.println("qqqqqqqqqq");
			System.out.println(map);
			return  JsonUtils.objectToJson(map);
		}
		
	}
}
