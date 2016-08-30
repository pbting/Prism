package exetuor.net.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URL;
import java.nio.channels.SocketChannel;
import exetuor.io.reactor.ResourceRead;

public class SendClient {

	/**
	 * 启动客户端测试
	 * 
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		SocketChannel socketChannel = SocketChannel.open();
		socketChannel.configureBlocking(false);
		socketChannel.connect(new InetSocketAddress("127.0.0.1", 8002));
		if (socketChannel.isConnectionPending()) {
			socketChannel.finishConnect();
		}
		// 设置成非阻塞
		socketChannel.configureBlocking(false);
		URL url = ResourceRead.class.getResource("demo.txt");
		FileInputStream fis = new FileInputStream(url.getFile());
		fis.getChannel().transferTo(0, new File(url.getFile()).length(), socketChannel);
		fis.close();
	}
}
