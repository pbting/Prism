package exetuor.net.core;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

import com.road.yishi.log.Log;
import com.road.yishi.log.util.FileUtil;

public class ReceverServer {

	private static Selector selector = null;

	static {
		try {
			selector = Selector.open();
		} catch (IOException e) {
			Log.error("", e);
		}
	}

	public void initServer(int port) {
		try {
			ServerSocketChannel ssc = ServerSocketChannel.open();
			ssc.configureBlocking(false);
			ssc.socket().bind(new InetSocketAddress(port));
			ssc.register(selector, SelectionKey.OP_ACCEPT);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void listen() {
		try {
			while (true) {
				System.out.println("等待客户端连接");
				selector.select();
				
				Iterator<SelectionKey> iter = ReceverServer.selector.selectedKeys().iterator();
				while (iter.hasNext()) {
					SelectionKey key = iter.next();
					iter.remove();
					if (key.isAcceptable()) {
						ServerSocketChannel channel = (ServerSocketChannel) key.channel();
						// 获得和客户端进行通信的channel
						SocketChannel socketChannel = channel.accept();
						socketChannel.configureBlocking(false);
						// 在这里可以给客户端发送信息哦
//						socketChannel.write(ByteBuffer.wrap(new String("向客户端发送了一条信息").getBytes()));
						socketChannel.register(ReceverServer.selector, SelectionKey.OP_READ);
					} else if (key.isReadable()) {
						SocketChannel socketChannel = (SocketChannel) key.channel();
						File out = new File("D:/demo_2.txt");
						FileUtil.checkFile(out);
						FileOutputStream fos = new FileOutputStream(out);
						fos.getChannel().transferFrom(socketChannel, 0,1024);
						fos.close();
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		ReceverServer server = new ReceverServer();
		server.initServer(8002);
		server.listen();
	}
}
