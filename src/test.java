import java.security.KeyStore.Entry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;

public class test {
	public static void main(String[] args) {
		 
        TreeMap<Integer, String> mapIndex = new TreeMap<Integer, String>(); 
 
        mapIndex.put(0, "0123456789");
        mapIndex.put(10, "0123456789");
        mapIndex.put(20, "0123456789");
        mapIndex.put(30, "0123456789");
        mapIndex.put(40, "0123456789");
  
//        System.out.println(mapIndex);
//        int bitNumber=0;
//        mapIndex.forEach((key, value)->mapIndex.put(key, value.substring(0,bitNumber)+value.substring(bitNumber+1)));
        System.out.println(mapIndex);
        
      Set<Integer> list=mapIndex.keySet();
      
      int x=Arrays.binarySearch(list.toArray(), 12);
      x=x<0?x*-1-1:x;
      System.out.println(x);
       
	}
}
