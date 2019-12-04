package com.example.demo;

import com.example.demo.UDP.Highway1Client;
import com.example.demo.UDP.Highway1Support;
import com.example.demo.UDP.UDPSupport;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;


@SpringBootApplication
@EnableConfigurationProperties
public class DemoApplication {

    public static void main(String[] args) throws IOException {
        SpringApplication.run(DemoApplication.class, args);
    }

}
