package com.baiyu.nettywebsocket.demo3;

import com.baiyu.nettywebsocket.demo1.NettyServer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * @author baiyu
 * @data 2019-12-25 13:08
 */
@Slf4j
@Service
public class NioWebSocketServer {
    @PostConstruct
    public void initNetty(){
        new Thread(){
            public void run(){
                new NioWebSocketServer().init();
            }
        }.start();
    }

    private void init(){
        log.info("正在启动websocket服务器");
        NioEventLoopGroup boss=new NioEventLoopGroup();
        NioEventLoopGroup work=new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap=new ServerBootstrap();
            bootstrap.group(boss,work);
            bootstrap.channel(NioServerSocketChannel.class);
            bootstrap.childHandler(new NioWebSocketChannelInitializer());
            Channel channel = bootstrap.bind(8081).sync().channel();
            log.info("webSocket服务器启动成功："+channel);
            channel.closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
            log.info("运行出错："+e);
        }finally {
            boss.shutdownGracefully();
            work.shutdownGracefully();
            log.info("websocket服务器已关闭");
        }
    }

    public static void main(String[] args) {
        new NioWebSocketServer().init();
    }
}
