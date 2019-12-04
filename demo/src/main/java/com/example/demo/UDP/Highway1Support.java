package com.example.demo.UDP;


/**
 * @author zwb
 * @date 2019/8/28 - 16:56
 **/
public class Highway1Support {

    //注册返回包生成电流数据发送包
    public static String AmmeterfromConnecttoMsg20Current(String string,String ID,String actionCode){
        String msg = new String();
        String CkSum = "0201"+ID+"1100000000"+string.substring(20,26)+
                "0000008000"+actionCode;
        msg = msg+"5A450201"+ID+"1100000000"+string.substring(20,26)+
                "0000008000"+actionCode+UDPSupport.getCkSum(CkSum)+"16";
        return msg;
    }

    //设备ID——根据返回数据获得
    public static String AmmetergetEquipmentID(String string){
        if (string.length()>26){
            return string.substring(4,6);
        }
        return "未获得ID！";
    }

    //总线ID——根据返回数据获得
    public static String AmmetergetHighwayID(String string){
        return UDPSupport.portToHighwayID(string);
    }

    //时间戳——根据返回数据获得
    public static String AmmetergetTime(String string){
        if (string.length()>26){
            return string.substring(20,26);
        }
        return "未获得时间戳！";
    }

    //IEEE754协议解析电表数据
    public static String AmmetergetData(String string){
        byte[] fromData;
        if (string.length()>26){
            fromData = UDPSupport.getBufStrHex(string.substring(42,50));
            int bits = fromData[3]&0xff|(fromData[2]&0xff)<<8|(fromData[1]&0xff)<<16|(fromData[0]&0xff)<<24;
            int sign = ((bits&0x80000000)==0)?1:-1;
            int exp = ((bits & 0x7f800000)>>23);
            int man = (bits & 0x007fffff);

            man |= 0x00800000;

            float f = (float)(sign*man*Math.pow(2,exp - 150));
            return Float.toString(f);
        }
        return "未获得数据！";
    }

    public static String ActionAnalyze(String action){
        switch (action) {
            case "电流":
                return AmmeterCode.getCurrentCode();
            case "电压":
                return AmmeterCode.getVoltageCode();
            case "功率":
                return AmmeterCode.getPowerCode();
            default:
                return "";
        }
    }

    public static String Tag;
    //存储设备标签
    public static void setTag(String tag) {
        Tag = tag;
    }
    //获取设备标签
    public static String getTag() {
        Tag = "电表";
        return Tag;
    }
}