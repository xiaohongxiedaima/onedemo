package com.xiaohongxiedaima.demo.javacore.net;

import com.xiaohongxiedaima.demo.javacore.reactor.IoBuffer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Set;
import java.util.concurrent.Executors;

public class NIOServerDemo {

    private static final Integer PORT = 8080;

    public static void main(String[] args) throws IOException {
        Selector selector = Selector.open();

        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.socket().bind(new InetSocketAddress(PORT));
        serverSocketChannel.configureBlocking(false);

        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        while (true) {
            selector.select(); // 此处会阻塞， 直到某一个操作已就绪
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            for (SelectionKey sk : selectionKeys) {
                if (sk.isAcceptable()) {
                    final SocketChannel socketChannel = serverSocketChannel.accept();
                    Executors.newFixedThreadPool(10).execute(() -> {
                        new Handler(selector, socketChannel);
                    });
                }
            }
        }
    }

    static class Handler implements Runnable {
        private SocketChannel socketChannel;
        private Selector selector;
        private String req;
        public Handler(Selector selector, SocketChannel socketChannel) {
            this.selector = selector;
            this.socketChannel = socketChannel;
            try {
                socketChannel.configureBlocking(false);
                socketChannel.register(this.selector, SelectionKey.OP_READ);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            try {
                selector.select();
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                for (SelectionKey sk : selectionKeys) {
                    if (sk.isReadable()) {
                        readChannel(socketChannel);
                    }
                }
                System.out.println(req);
                socketChannel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void readChannel(SocketChannel socketChannel) throws IOException {
            ByteBuffer data = ByteBuffer.allocate(1024 * 2);
            IoBuffer readBuffer = IoBuffer.allocate(1024 * 2);

            while (socketChannel.read(data) > 0) {
                data.flip();
                readBuffer.put(data.array(), data.position(), data.remaining());
                data.clear();
            }

            byte[] bArr = new byte[readBuffer.remaining()];
            readBuffer.get(bArr, 0, bArr.length);
            this.req = new String(bArr);
        }

    }
}
