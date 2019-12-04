package com.example.demo.UDP;

import com.example.demo.Locate.SpringContextUtil;
import com.example.demo.service.KafkaMessageSendService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.support.SendResult;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

//@Component
public class UDPClient {

        public static void main(String[] args) throws IOException {
            String IP = "192.168.0.240";

            String ID="20";
            String inputport = "2000";
            int port = Integer.parseInt(inputport);
            String action="电压";

            DatagramSocket socket = new DatagramSocket();

            //注册设备，并获取带有是时间戳的返回值
            String connectToMsg = UDPSupport.Connect(socket,port,IP);

            Highway1Client.AmmeterDeal(socket,IP,connectToMsg,ID,port,action);
        }


}