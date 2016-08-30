package exetuor.MultithreadReactor;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.road.yishi.log.Log;

public class Processor {
	private static final ExecutorService service = Executors.newFixedThreadPool(16);

	public void process(final SelectionKey selectionKey) {
		service.submit(new Runnable() {

			@Override
			public void run() {
				try {
					ByteBuffer buffer = ByteBuffer.allocate(1024);
					SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
					int count = socketChannel.read(buffer);
					if (count < 0) {
						socketChannel.close();
						selectionKey.cancel();
						Log.info("{}\t Read ended");
					}
					Log.info("{}\t Read message {}" + ";infomations:" + new String(buffer.array()));
				} catch (IOException e) {
					Log.error("", e);
				}
			}
		});
	}
}
