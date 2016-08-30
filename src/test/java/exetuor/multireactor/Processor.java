package exetuor.multireactor;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.road.yishi.log.Log;

public class Processor {
	private static final ExecutorService service = Executors.newFixedThreadPool(1);

	private Selector selector;
	private boolean isRunning;

	public Processor() throws IOException {
		this.selector = SelectorProvider.provider().openSelector();
	}

	public void addChannel(SocketChannel socketChannel) throws ClosedChannelException {
		socketChannel.register(this.selector, SelectionKey.OP_READ);
		if (!isRunning) {
			start();
			isRunning = true;
		}
	}

	public void start() {
		service.submit(new Runnable() {

			@Override
			public void run() {

				while (true) {
					try {
						if (selector.selectNow() <= 0) {
							continue;
						}

						Set<SelectionKey> keys = selector.selectedKeys();
						Iterator<SelectionKey> iterator = keys.iterator();
						while (iterator.hasNext()) {
							SelectionKey key = iterator.next();
							iterator.remove();
							if (key.isReadable()) {
								ByteBuffer buffer = ByteBuffer.allocate(1024);
								SocketChannel socketChannel = (SocketChannel) key.channel();
								int count = socketChannel.read(buffer);
								if (count < 0) {
									socketChannel.close();
									key.cancel();
									Log.info("{}\t Read ended");
									continue;
								} else if (count == 0) {
									Log.info("{}\t Message size is 0");
									continue;
								} else {
									Log.info("{}\t Read message {}" + ";infomation:" + new String(buffer.array()));
								}
							}
						}
					} catch (IOException e) {
						Log.error("", e);
					}
				}
			}
		});
	}

}
