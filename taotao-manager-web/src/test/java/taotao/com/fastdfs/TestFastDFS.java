package taotao.com.fastdfs;

import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.junit.Test;

import taotao.com.utils.FastDFSClient;

public class TestFastDFS {
	@Test
	public void uploadFile()throws Exception{
		//添加jar包
		//创建配置文件，配置tracker_server 的地址
		//加载配置文件
		ClientGlobal.init("E:/SoftWareWorkSpace/javaEEWorkSpace/taotao-manager-web/src/main/resources/resource/client.conf");
		//创建trakcerClient对象
		TrackerClient client=new TrackerClient();
		//创建TrackerServer对象
		TrackerServer server=client.getConnection();
		//创建TrackerStorage对象
		StorageServer storage=null;
		
		//创建storageClient对象
		StorageClient storageClient=new StorageClient(server,storage);
		
		String[] strings=storageClient.upload_file("C:/Users/liurong/Pictures/Camera Roll/kebi.jpg","jpg", null);
		for(String str:strings){
			System.out.println(str);
		}
	}
	
	@Test
	public void upLoadFileClient()throws Exception{
		FastDFSClient client=new FastDFSClient("E:/SoftWareWorkSpace/javaEEWorkSpace/taotao-manager-web/src/main/resources/resource/client.conf");
		String result=client.uploadFile("C:/Users/liurong/Pictures/Camera Roll/test.jpg");
		System.out.println(result);
		
	}
}
