package com.example.demo.UDPTest;

import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.socket.nio.NioSocketChannel;

public class Client {

    public static void main(String[] args){
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(eventLoopGroup).channel(NioSocketChannel.class).handler(new Clientinitializer());

            ChannelFuture channelFuture = bootstrap.connect("192.168.0.102",8899).sync();
            channelFuture.channel().closeFuture().sync();
        }catch (Exception e){

        }finally {
            eventLoopGroup.shutdownGracefully();
        }
    }
}
