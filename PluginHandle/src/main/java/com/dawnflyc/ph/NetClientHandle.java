package com.dawnflyc.ph;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class NetClientHandle extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("链接成功");
        Thread thread = new Thread(() -> {
            Scanner scanner = new Scanner(System.in);
            while (true) {
                String next = scanner.nextLine();
                ChannelFuture channelFuture = ctx.channel().writeAndFlush(Unpooled.copiedBuffer(next, StandardCharsets.UTF_8));
                if (channelFuture.cause() != null) {
                    try {
                        throw channelFuture.cause();
                    } catch (Throwable e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
        thread.start();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf in = (ByteBuf) msg;
        int readableBytes = in.readableBytes();
        byte[] bytes = new byte[readableBytes];
        in.readBytes(bytes);
        String str = new String(bytes);
        System.out.println(str);
    }
}
