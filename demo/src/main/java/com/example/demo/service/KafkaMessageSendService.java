package com.example.demo.service;

import com.example.demo.UDP.Highway1Client;
import com.example.demo.UDP.Highway1Support;
import com.example.demo.UDP.UDPClient;
import com.example.demo.UDP.UDPSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

import java.io.IOException;
import java.net.*;

@Component
@EnableScheduling
public class KafkaMessageSendService {

    public static String IP = "192.168.0.240";
    public static int port = 2000;
    public static int count=0;
//    public static String datamsg="534E02AAFF16AAAA33337B16";
//    public static String connectToMsg=null;
//    public static String fromAmmeter=null;
    //kafka
    private static final Logger LOG = LoggerFactory.getLogger(KafkaMessageSendService.class);
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    @Value("${kafka.app.topic.foo}")
    private String topic;

//    public static void main(String[] args) throws IOException {
//        KafkaMessageSendService kafkaMessageSendService =new KafkaMessageSendService();
//        DatagramSocket socket = new DatagramSocket();
//        //注册设备，并获取带有是时间戳的返回值
//        datamsg = "534E02AAFF16AAAA33337B16";
//        String connectToMsg = Connect(socket,datamsg);
//        count++;
//        datamsg = Highway1Support.AmmeterfromConnecttoMsg20Current(connectToMsg);
//        String fromAmmeter= Highway1Client.Ammetergetbackmsg(socket,datamsg);
//
//        System.out.println("第"+count+"次："+"设备ID："+ Highway1Support.AmmetergetEquipmentID(fromAmmeter)+"——总线ID："+ Highway1Support.AmmetergetHighwayID(Integer.toString(port))
//                +"——时间戳："+ Highway1Support.AmmetergetTime(fromAmmeter)+"——数据信息："
//                +"电流："+ Highway1Support.AmmetergetData(fromAmmeter)+"——设备标签："+ Highway1Support.getTag());
//
//    }

    @Scheduled(cron = "00/5 * * * * ?")
    public void transfer() throws IOException {
        count++;
        DatagramSocket socket = new DatagramSocket();
        //从连接到数据发送,获取电压数据
//        String datamsg = "534E02AAFF16AAAA33337B16";
//        String connectToMsg = Connect(socket,datamsg);

        String datamsg = "534E02AAFF16AAAA33337B16";
        String connectToMsg = Connect(socket,datamsg);

//        int count = 1;
        //从连接到数据发送,获取电压数据
//        String string,String ID,String actionCode
        datamsg = Highway1Support.AmmeterfromConnecttoMsg20Current(connectToMsg,"02","dianya");
        String fromAmmeter= Highway1Client.Ammetergetbackmsg(socket,datamsg,port,IP);

        System.out.println("第"+count+"次："+"设备ID："+ Highway1Support.AmmetergetEquipmentID(fromAmmeter)+"——总线ID："+ Highway1Support.AmmetergetHighwayID(Integer.toString(port))
                +"——时间戳："+ Highway1Support.AmmetergetTime(fromAmmeter)+"——数据信息："
                +"电流："+ Highway1Support.AmmetergetData(fromAmmeter)+"——设备标签："+ Highway1Support.getTag());

//        kafka message send
        LOG.info("topic="+topic+",message="+datamsg);
        ListenableFuture<SendResult<String, String>> future = kafkaTemplate.send(topic,datamsg);
        future.addCallback(success -> LOG.info("KafkaMessageProducer 发送消息成功！"),
                fail -> LOG.error("KafkaMessageProducer 发送消息失败！"));
    }

    public static String Connect(DatagramSocket socket,String datamsg) throws IOException{
        String back = new String();
        InetAddress address = UDPSupport.IPConvert(IP);
        byte[] data1 = UDPSupport.getBufStrHex(datamsg);
        DatagramPacket packet = new DatagramPacket(data1, data1.length, address, port);
        socket.send(packet);

        byte[] data2 = new byte[14];
        DatagramPacket packet2 = new DatagramPacket(data2, data2.length);
        for (int i=0;i<4;i++)
        {
            try{
                socket.receive(packet2);
                String reply = UDPSupport.getBufHexStr(packet2.getData());
                if(reply.substring(4,6).equals("20")){ back = reply;}
            }catch (Exception e){
                break;
            }
        }
        return back;
    }

}
