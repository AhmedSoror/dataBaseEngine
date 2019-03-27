import java.security.KeyStore.Entry;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;

public class test {
	public static void main(String[] args) {
		 
        TreeMap<Comparable, String> mapIndex = new TreeMap<Comparable, String>(); 
 
        mapIndex.put(10, "10000");
        mapIndex.put(20, "01000");
        mapIndex.put(30, "00100");
        mapIndex.put(40, "00010");
        mapIndex.put(50, "00001");
  
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
//        
        /*
        System.out.println(mapIndex);

        Set<Integer> list=mapIndex.keySet();
        int x=Arrays.binarySearch(list.toArray(), 45);
        x=x<0?x*-1-2:x;
        
        System.out.println(x);
        System.out.println(list.toArray()[x]); 
        */
    /*    
        mapIndex.forEach((key, value)->
        		mapIndex.put(key,editBits("01000"))
        );
        
        System.out.println(mapIndex);
  *//*
        System.out.println(mapIndex);
        int x=findLastOne(mapIndex.get(30));
        insert(x,mapIndex);
        */

//	{10=10000, 20=01000, 30=00100, 40=00010, 50=00001}
        System.out.println(mapIndex);
        insertIndex( new Integer(40), new Integer(45), mapIndex);
        System.out.println(mapIndex);
        
	}

	public static void insertIndex( Comparable prviosRecord, Comparable insertedRecordValue,TreeMap<Comparable,String> mapIndex) {

		String prviosRecordBits = mapIndex.get(prviosRecord);
		int bitSreamIndex = findLastOne(prviosRecordBits);
		insert(bitSreamIndex,mapIndex);
		String zeros = String.join("", Collections.nCopies(prviosRecordBits.length(), "0"));
		String insertedIndex = zeros.substring(0, bitSreamIndex) + "1" + zeros.substring(bitSreamIndex);
		mapIndex.put(insertedRecordValue, insertedIndex);

	}

	   public static void insert(int stringIndex,TreeMap<Comparable, String> mapIndex){
		   mapIndex.forEach((key, value)->
		   			mapIndex.put(key,editBits(value,stringIndex))
			);
	   }

	   public static  String editBits(String s,int stringIndex) {
			int x=stringIndex;
		    String r=s.substring(0,x)+"0"+s.substring(x);
		    return r;
		}
		
	
	public static  String editBits(String s) {
		int x=findLastOne( s);
	    String r=s.substring(0,x)+"0"+s.substring(x);
	    return r;
	}
	
	public static int findLastOne(String bitStream) {
		for(int i=bitStream.length()-1;i>=0;i--) {
			if(bitStream.charAt(i)=='1')
				return i+1;
		}
		return -1;
	}
	
	
	public static boolean hasNoValues(String s) {
		return s.matches("0*");
	}
	
	
}
