import java.security.KeyStore.Entry;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;

public class test {
	public static void main(String[] args) {
		 
        TreeMap<String, String> mapIndex = new TreeMap<String, String>(); 
 
//        mapIndex.put("USA", "0123456789");
//        mapIndex.put("India", "0123456789");
//        mapIndex.put("UK", "0123456789");
//        mapIndex.put("Japan", "0123456789");
//        mapIndex.put("UAE", "0123456789");
  
//        System.out.println(mapIndex);
//        int bitNumber=0;
//        mapIndex.forEach((key, value)->mapIndex.put(key, value.substring(0,bitNumber)+value.substring(bitNumber+1)));
//        System.out.println(mapIndex);
        
//        System.out.println(mapIndex.get("as"));
//        System.out.println(hasNoValues("00000000000001000100"));
//        TreeMap<Comparable, String> tm = new TreeMap<>();
//        tm.put("a", "1");
//        tm.put("b", "2");
//        tm.put("v", "0");
//        tm.put("w", "11");
//        System.out.println(tm);
//        
//        System.out.println(tm);
        
//        Vector<Integer> v = new Vector<>();
//        v.add(3);
//        v.add(5);
//        v.add(2);
//        
//        Iterator rs =v.iterator();
//        while(rs.hasNext()) {
//        	System.out.println(rs.next());
//        }
        Integer[] a =new Integer[3];
        a[0]=1;
        a[1]=3;
        a[2]=2;
        Vector<Integer> vec = new Vector(Arrays.asList(a));
        System.out.println(vec);
	}
	public static boolean hasNoValues(String s) {
		return s.matches("0*");
	}
	
	
}
