import java.security.KeyStore.Entry;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;

public class test {
	public static void main(String[] args) {
		 
        TreeMap<String, String> mapIndex = new TreeMap<String, String>(); 
 
        mapIndex.put("USA", "0123456789");
        mapIndex.put("India", "0123456789");
        mapIndex.put("UK", "0123456789");
        mapIndex.put("Japan", "0123456789");
        mapIndex.put("UAE", "0123456789");
  
        System.out.println(mapIndex);
        int bitNumber=0;
        mapIndex.forEach((key, value)->mapIndex.put(key, value.substring(0,bitNumber)+value.substring(bitNumber+1)));
        System.out.println(mapIndex);

	
	}
}
