package exetuor.multireactor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Set;

import com.road.yishi.log.Log;

public class NIOServer {

  public static void main(String[] args) throws IOException {
    Selector selector = Selector.open();
    ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
    serverSocketChannel.configureBlocking(false);
    serverSocketChannel.bind(new InetSocketAddress(1234));
    serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
    
    Processor[] processors = new Processor[1];
    for(int i = 0; i < processors.length; i++) {
      processors[i] = new Processor();
//      processors[i].start();
    }

    while (selector.select() > 0) {
      Set<SelectionKey> keys = selector.selectedKeys();
      for (SelectionKey key : keys) {
        keys.remove(key);
        if (key.isAcceptable()) {
          ServerSocketChannel acceptServerSocketChannel = (ServerSocketChannel) key.channel();
          SocketChannel socketChannel = acceptServerSocketChannel.accept();
          socketChannel.configureBlocking(false);
          Log.info("Accept request from {}"+";remote address:"+socketChannel.getRemoteAddress());
//          Processor processor = processors[(int)((index++)/1)];
          Processor processor = processors[0];
          processor.addChannel(socketChannel);
        }
      }
    }
  }

}
