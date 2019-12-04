package com.example.demo.UDP;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Highway1Client {
    //查询电表数据
    //        不变：5A450201201100000000
//                时间戳：A7E700
//                不变：0000008000
//                查询电表数据：0304000000027029
//                距离计算：E4
//                结束：16
//        datamsg = "5A450201201100000000A7E70000000080000304000000027029E416";
//    public static String Start_time;
//    public static String End_time;
    public static String Start_formatter;
    public static String End_formatter;


    public static String Ammetergetbackmsg(DatagramSocket socket, String datamsg , int port,String IP)throws IOException {
        InetAddress address = UDPSupport.IPConvert(IP);
        byte[] data1 = UDPSupport.getBufStrHex(datamsg);
        DatagramPacket packet1 = new DatagramPacket(data1, data1.length, address, port);
        socket.send(packet1);
//        Long Starttime = new Date().getTime();
//        Start_time=UDPSupport.stampToDate(Long.toString(Starttime));
        Start_formatter = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss.SSS").format(new Date());
        byte[] data2 = new byte[29];
        DatagramPacket packet2 = new DatagramPacket(data2, data2.length);
        socket.receive(packet2);
//        Long Endtime = new Date().getTime();
//        End_time =UDPSupport.stampToDate(Long.toString(Endtime));
        End_formatter = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss.SSS").format(new Date());
        String reply = UDPSupport.getBufHexStr(packet2.getData());
        return reply;
    }
    public static void AmmeterDeal(DatagramSocket socket, String IP,String connectToMsg,String ID ,int port, String action) throws IOException{
        String actionCode = Highway1Support.ActionAnalyze(action);
        int count = 1;
        //从连接到数据发送,获取电压数据
        String datamsg = Highway1Support.AmmeterfromConnecttoMsg20Current(connectToMsg,ID,actionCode);
        String fromAmmeter= Highway1Client.Ammetergetbackmsg(socket,datamsg,port,IP);

        System.out.println("第"+count+"次："+"设备ID："+ Highway1Support.AmmetergetEquipmentID(fromAmmeter)+"——总线ID："+ Highway1Support.AmmetergetHighwayID(Integer.toString(port))
                +"——时间戳："+ Highway1Support.AmmetergetTime(fromAmmeter)+"——数据信息："
                +action+ Highway1Support.AmmetergetData(fromAmmeter)+"——设备标签："+ Highway1Support.getTag()+"——发送前时间片："+Start_formatter+"——发送后时间片："+End_formatter);
        count++;
        while (true) {
            try {
                Thread.sleep(2000);

                datamsg = Highway1Support.AmmeterfromConnecttoMsg20Current(fromAmmeter,ID,actionCode);
                fromAmmeter = Highway1Client.Ammetergetbackmsg(socket, datamsg,port,IP);

                System.out.println("第"+count+"次："+"设备ID："+ Highway1Support.AmmetergetEquipmentID(fromAmmeter)+"——总线ID："+ Highway1Support.AmmetergetHighwayID(Integer.toString(port))
                        +"——时间戳："+ Highway1Support.AmmetergetTime(fromAmmeter)+"——数据信息："
                        +action+ Highway1Support.AmmetergetData(fromAmmeter)+"——设备标签："+ Highway1Support.getTag()+"——发送前时间片："+Start_formatter+"——发送后时间片："+End_formatter);
                count++;
            } catch (Exception e) {
                System.out.println("失败:" + e);
            }
        }
    }

}