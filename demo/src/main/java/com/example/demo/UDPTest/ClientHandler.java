package com.example.demo.UDPTest;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.time.LocalDateTime;

public class ClientHandler extends SimpleChannelInboundHandler<String> {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println(ctx.channel().remoteAddress());
        System.out.println("client output:"+msg);
        ctx.writeAndFlush("53 4E 02 AA FF 16 AA AA 33 33 7B 16");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush("这条信息来自客户端！");
    }

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, String msg) throws Exception {

    }
}
