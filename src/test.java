import java.security.KeyStore.Entry;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
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
        /*
         Integer[] a =new Integer[3];
        a[0]=1;
        a[1]=3;
        a[2]=2;
        Vector<Integer> vec = new Vector(Arrays.asList(a));
        System.out.println(vec);
        */
        /*
        System.out.println(mapIndex);

       
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
/*
        System.out.println(mapIndex);
        insertIndex( new Integer(40), new Integer(45), mapIndex);
        System.out.println(mapIndex);
        */
        /*
        System.out.println(mapIndex);
        Set<Comparable> list=mapIndex.keySet();
        int x=Arrays.binarySearch(list.toArray(), "Ahmed Noor");
        x=x<0?x*-1-2:x;
        System.out.println(x);
        */
        System.out.println(Encrypt("00100111"));
        String[]arr=Encrypt("00100111").split(" ");
        System.out.println(Arrays.toString(arr));
        System.out.println(Decrypt(Encrypt("00100111")));
        
	}
	public static boolean hasNoValues(String s) {
		return s.matches("0*");
	}
	public static String Encrypt(String bits) {
		String r="";
		int count=0;
		char currentCharacter=bits.charAt(0);
		char c=currentCharacter;
		for(int i=0;i<bits.length();i++) {
			if(bits.charAt(i)==currentCharacter) {
				count++;
			}
			else {
				 c=currentCharacter=='0'?'Z':'O';
				currentCharacter=bits.charAt(i);
				r+=count+"/"+c+" ";
				count=1;
			}
		}
		c=currentCharacter=='0'?'Z':'O';
		r+=count+"/"+c+" ";
		return r;
	}
	public static String Decrypt(String bits) {
		String r="";
		String[]arr=bits.split(" ");
		for(int i=0;i<arr.length;i++) {
			String[]split=arr[i].split("/");
			int count=Integer.parseInt(split[0]);
			if(split[1].equals("O")) {
				r+=String.join("", Collections.nCopies(count, "1"));
			}
			else {
				r+=String.join("", Collections.nCopies(count, "0"));
			}
		}
		return r;
	}
	
}
