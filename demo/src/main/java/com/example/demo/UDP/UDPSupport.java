package com.example.demo.UDP;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UDPSupport {
    //连接函数
    public static String Connect(DatagramSocket socket, int port, String IP) throws IOException{
        String datamsg = "534E02AAFF16AAAA33337B16";
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

    //IP地址转换
    public static InetAddress IPConvert(String str) throws IOException {
        String[] ipStr = str.split("\\.");
        byte[] ipBuf = new byte[4];
        for (int i = 0; i < 4; i++) {
            ipBuf[i] = (byte) (Integer.parseInt(ipStr[i]) & 0xff);
        }
        InetAddress address = InetAddress.getByAddress(ipBuf);
        return address;
    }

    //port 转换为 总线ID
    public static String portToHighwayID(String string){
        String ID="";
        switch (string){
            case "2000":
                ID = "1";
                break;
            case "3000":
                ID = "2";
                break;
            case "4000":
                ID = "3";
                break;
            case "5000":
                ID = "4";
                break;
            case "6000":
                ID = "5";
                break;
            case "7000":
                ID = "6";
                break;
            default:
                break;
        }
        return ID;
    }

    //总线ID 转换为 port
    public static String HighwayIDToport(String string){
        String port="";
        switch (string){
            case "1":
                port = "2000";
                break;
            case "2":
                port = "3000";
                break;
            case "3":
                port = "4000";
                break;
            case "4":
                port = "5000";
                break;
            case "5":
                port = "6000";
                break;
            case "6":
                port = "7000";
                break;
            default:
                break;
        }
        return port;
    }

    //String型包转换为16进制byte型
    public static byte[] getBufStrHex(String str){
        if (str == null || str.trim().equals("")){
            return new byte[0];
        }
        byte[] bytes = new byte[str.length()/2];
        for (int i=0; i<str.length()/2;i++){
            String subStr = str.substring(i*2,i*2+2);
            bytes[i]= (byte)Integer.parseInt(subStr,16);
        }
        return bytes;
    }

    //16进制byte转换为String
    public static String getBufHexStr(byte[] raw){
        String HEXES="0123456789ABCDEF";
        if(raw==null){
            return  null;
        }
        final StringBuilder hex=new StringBuilder(2*raw.length);
        for(final byte b:raw){
            hex.append(HEXES.charAt((b&0xF0)>>4)).append(HEXES.charAt((b&0x0F)));
        }
        return hex.toString();
    }

    //计算CkSum
    public static String getCkSum(String CkSum){
        byte[] ck = UDPSupport.getBufStrHex(CkSum);
        int sum = 0 ;
        int temp;
        for (int i = 0;i<ck.length;i++){
            if(ck[i]<0){
                temp=ck[i]+256;
            }
            else temp=ck[i];
            if((sum+temp)>=256) {
                sum = sum + temp - 256;
            }
            else {
                sum = sum + temp;
            }
        }
        String s =Integer.toHexString(sum);
        if(s.length()==1){
            s = "0" + s;
        }
        return s;
    }

    //计算十六进制转化为十进制

    /*
     * 将时间戳转换为时间
     */
    public static String stampToDate(String s){
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long lt = new Long(s);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }


}
