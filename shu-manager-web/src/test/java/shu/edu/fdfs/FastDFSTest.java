package shu.edu.fdfs;

import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.junit.Test;

import shu.edu.common.utils.FastDFSClient;

public class FastDFSTest {

	@Test
	public void uploadTest() throws Exception {
		
		//创建一个配置文件，就是tracker服务器的地址
		//使用全局对象加载配置文件
		ClientGlobal.init("D:/engineering_soft/Javawebcode/shu-manager-web/src/main/resources/conf/client.conf");
		//创建一个TrackerClient对象
		TrackerClient trackerClient = new TrackerClient();
		//通过TrackerClient对象获得一个TrackerServer对象
		TrackerServer trackerServer = trackerClient.getConnection();
		//创建一个StorageServer的引用
		StorageServer storageServer = null;
		//创建一个StorageClient,参数需要TrackerSerrver和StorageServer
		StorageClient storageClient = new StorageClient(trackerServer,storageServer);
		//使用StorageClient上传文件
	    String[] strings = storageClient.upload_file("E:/resource/meinv.png", "png", null);
		//返回数组，包含组名和文件路径
		for(String s : strings) {
			System.out.println(s);
		}
	}
	@Test
	public void fdfsClientTest() throws Exception{
		
		FastDFSClient fastDFSClient = new FastDFSClient("D:/engineering_soft/Javawebcode/shu-manager-web/src/main/resources/conf/client.conf");
		String string = fastDFSClient.uploadFile("E:/resource/timg.jpg");
		System.out.println(string);
	}
}
