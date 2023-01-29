package com.dawnflyc.ps.handle;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Random;

public class NetServer {

    private static final Random RANDOM = new Random();

    public static void load() {
        //NioEventLoopGroup是用来处理IO操作的多线程事件循环器
        EventLoopGroup bossGroup = new NioEventLoopGroup();  // 用来接收进来的连接
        EventLoopGroup workerGroup = new NioEventLoopGroup();// 用来处理已经被接收的连接
        try {
            ServerBootstrap server = new ServerBootstrap();//是一个启动NIO服务的辅助启动类
            server.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)  // 这里告诉Channel如何接收新的连接
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            // 自定义处理类
                            ch.pipeline().addLast(new NetServerHandler());
                        }
                    });
            server.option(ChannelOption.SO_BACKLOG, 128);
            server.childOption(ChannelOption.SO_KEEPALIVE, true);
            ChannelFuture f = server.bind(getPort()).sync();// 绑定端口，开始接收进来的连接
            System.out.println("服务端启动成功...");
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }

    private static int getPort() {
        int port = RANDOM.nextInt(8999) + 1000;
        File file = new File("./port");
        if (!file.exists()) {
            try {
                if (!file.createNewFile()) {
                    throw new IOException("创建失败");
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        try {
            Files.write(file.toPath(), (port + "").getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return port;
    }

    public static String DataToStr(ByteBuf in) {
        int readableBytes = in.readableBytes();
        byte[] bytes = new byte[readableBytes];
        in.readBytes(bytes);
        return new String(bytes);
    }

    public static ByteBuf StrToData(String str) {
        return Unpooled.copiedBuffer(str, StandardCharsets.UTF_8);
    }
}
