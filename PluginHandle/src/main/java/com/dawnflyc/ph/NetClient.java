package com.dawnflyc.ph;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;


public class NetClient {

    public static void load() {
        File file = new File("./port");
        if (!file.exists()) {
            System.out.println("端口文件不存在");
            return;
        }
        byte[] bytes;
        try {
            bytes = Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String port = new String(bytes);

        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(workerGroup).channel(NioSocketChannel.class)
                    // KeepAlive
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    // Handler
                    .handler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel channel) throws Exception {
                            channel.pipeline().addLast(new NetClientHandle());
                        }
                    });
            ChannelFuture channelFuture = b.connect("localhost", Integer.parseInt(port)).sync();
            channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {
            System.out.println("链接失败");
        } finally {
            workerGroup.shutdownGracefully();
        }
    }
}
