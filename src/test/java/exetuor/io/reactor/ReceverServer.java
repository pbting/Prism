package exetuor.io.reactor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import com.road.yishi.log.Log;

public class ReceverServer {

	// 初始化一个通过管理器
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

			// 设置该通道为非阻塞模式
			ssc.configureBlocking(false);

			// 将该通道的ServerSOCKET 绑定到这定的端口
			ssc.socket().bind(new InetSocketAddress(port));

			//将这个管道绑定到指定的选择器上:使用selector 实现多路复用
			ssc.register(selector, SelectionKey.OP_ACCEPT);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void listen() {
		try {
			while (true) {
				System.out.println("等待客户端连接");
				selector.select();//如果有新的链接过来，将会触发下面的逻辑，否则一直阻塞状态：这里的阻塞是因为没有满足条件：即没有新的链接触发
				
				//这里将会拿到多个channel 发生的事件。这里只注册了一个，因为迭代只有一个SelectionKey
				Iterator<SelectionKey> iter = this.selector.selectedKeys().iterator();
				while (iter.hasNext()) {
					SelectionKey key = iter.next();
					// 删除已选的key,以防重复处理
					iter.remove();
					// 客户端请求连接时间
					if (key.isAcceptable()) {
						ServerSocketChannel channel = (ServerSocketChannel) key.channel();

						// 获得和客户端进行通信的channel
						SocketChannel socketChannel = channel.accept();
						socketChannel.configureBlocking(false);

						// 在这里可以给客户端发送信息哦
						socketChannel.write(ByteBuffer.wrap(new String("向客户端发送了一条信息").getBytes()));
						// 在和客户端连接成功之后，为了可以接收到客户端的信息，需要给通道设置读的权限。
						socketChannel.register(this.selector, SelectionKey.OP_READ);
					} else if (key.isReadable()) {
						SocketChannel socketChannel = (SocketChannel) key.channel();
						ByteBuffer buffer = ByteBuffer.allocate(1024);
						socketChannel.read(buffer);
						byte[] data = buffer.array();
						String msg = new String(data).trim();
						System.out.println("服务端收到信息：" + msg);
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
