package getAtteributesFromHost;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;



public class EntryMethod extends SystemInfoTools{
    public static void main(String[] args) {
         BigDecimal DIVISOR = BigDecimal.valueOf(1024);
        MonitorInfoBean monitorInfoBean = new MonitorInfoBean();
        SystemInfoTools systemInfoTools = new SystemInfoTools();
//        final boolean isNotWindows =
//                System.getProperties().getProperty("os.name").toLowerCase().indexOf("windows") < 0;
//
//        System.out.println(isNotWindows);
//        Properties properties = System.getProperties();
//        Set<Map.Entry<Object, Object>> entrySet = properties.entrySet();
//        for (Map.Entry<Object,Object> map:entrySet) {
//            System.out.println(
//                    map.getKey().toString() +"\t" + ":" +"\t"+ map.getValue().toString()
//            );
//        }
//        Iterator<Map.Entry<Object, Object>> entries = properties.entrySet().iterator();
//        while (entries.hasNext()) {
//            System.out.println(entries.next());
//        }
//        System.out.println(System.getProperties().getProperty("os.name").toLowerCase().indexOf("windows"));
        System.out.println(systemInfoTools.getPid());
        BigDecimal abc = BigDecimal.valueOf(102400000);
        System.out.println(abc.divide(DIVISOR).divide(BigDecimal.valueOf(1)));

    }
}
