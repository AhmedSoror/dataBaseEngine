import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;

public class BitMapIndex extends Table {
	String strTableName;
	String strColName;
	TreeMap<Comparable, String> mapIndex;
	
//	------------------------------------------------------(Constructor)------------------------------------------------------------------------------
	public BitMapIndex(String strTableName,String strColName,Hashtable<String, String> htblColNameType){
//		String strTableName, String strClusteringKeyColumn, Hashtable<String, String> htblColNameType       (Table constructor)
		super(strTableName+"_"+strColName,strColName,htblColNameType);
		this.strTableName=strTableName;
		this.strColName=strColName;
		this.mapIndex=new TreeMap<>();
	}
	
//	------------------------------------------------------(Write to disk)------------------------------------------------------------------------------
	/*
	public void writeIndex() {
		int i=0;
		for(Page page:getVecPages()) {
			writePage(page, i++);
		}
	}
*/	
	
//	------------------------------------------------------(Added Methods)------------------------------------------------------------------------------
	// public void deleteIndex(int bitNumber) {

	// 	mapIndex.forEach( (key, value) -> mapIndex.put( key , value.substring(0,bitNumber)+value.substring(bitNumber+1) ) );
	// 	//		handle vecPageNumber
	// }
	
	public void update (int ind,Comparable oldVal,Comparable  newVal) {						//BO-G
		String sNew;
		String sOld = mapIndex.get(oldVal);
		sOld=sOld.substring(0, ind)+"0"+sOld.substring(ind+1);
		if(hasNoValues(sOld))mapIndex.remove(oldVal);
		
		if(mapIndex.containsKey(newVal)) 
			 sNew = mapIndex.get(newVal);
		else 
			 sNew = String.join("", Collections.nCopies(sOld.length(), "0"));
		
		sNew=sNew.substring(0, ind)+"1"+sNew.substring(ind+1);
		mapIndex.put(newVal,sNew);
		
	}
	
	public void delete (int ind) {						//BO-G
		for(Map.Entry<Comparable,String> entry : mapIndex.entrySet()) {
			  Comparable key = entry.getKey();
			  StringBuilder sb = new StringBuilder(entry.getValue());
			  sb.deleteCharAt(ind);

			  mapIndex.put(key, sb.toString());
			}
		
	}
	
	public String getBitStream(Comparable key,String operator) {
		String res="";
		if(operator.equals("<")) {
			for(Map.Entry<Comparable,String> entry : mapIndex.entrySet()) {
				if(entry.getKey().compareTo(key)<0)res=orOperation(res, entry.getValue());
				}
		}
		else if(operator.equals("<=")) {
			for(Map.Entry<Comparable,String> entry : mapIndex.entrySet()) {
				if(entry.getKey().compareTo(key)<=0)res=orOperation(res, entry.getValue());
				}
		}
		else if(operator.equals(">")) {
			for(Map.Entry<Comparable,String> entry : mapIndex.entrySet()) {
				if(entry.getKey().compareTo(key)>0)res=orOperation(res, entry.getValue());
				}
		}
		else if(operator.equals(">=")) {
			for(Map.Entry<Comparable,String> entry : mapIndex.entrySet()) {
				if(entry.getKey().compareTo(key)>=0)res=orOperation(res, entry.getValue());
				}
		}
		else if(operator.equals("!=")) {
			for(Map.Entry<Comparable,String> entry : mapIndex.entrySet()) {
				if(entry.getKey().compareTo(key)!=0)res=orOperation(res, entry.getValue());
				}
		}
		else if(operator.equals("=")) {
			for(Map.Entry<Comparable,String> entry : mapIndex.entrySet()) {
				if(entry.getKey().compareTo(key)==0)res=orOperation(res, entry.getValue());
				}
		}
		return res;
	}
	
	public static String orOperation(String s1,String s2) {
		String res="";
		for(int i=0;i<s1.length();i++) {
			if(s1.charAt(i)=='1'||s2.charAt(i)=='1')res+="1";
			else res+="0";
		}
		return res;
	}
	public static boolean hasNoValues(String s) {
		return s.matches("0*");
	}
	//-----------------------------------------------------------------------------------------------------------------------------------------
		public void insertIndex(Hashtable<String, Object> htblColNameValue,int bitSreamIndex) {
			Comparable insertedRecordValue= (Comparable) htblColNameValue.get(strColName);
			String s=mapIndex.firstEntry().getValue();
			insert(bitSreamIndex);
			String zeros="";
			if(mapIndex.containsKey(insertedRecordValue)) {
				zeros=mapIndex.get(insertedRecordValue);
				zeros=zeros.substring(0, bitSreamIndex)+zeros.substring(bitSreamIndex+1);
			}
			else {
				zeros=String.join("", Collections.nCopies(s.length(), "0"));
			}
		    String insertedIndex=zeros.substring(0, bitSreamIndex)+"1"+zeros.substring(bitSreamIndex);
		    mapIndex.put(insertedRecordValue, insertedIndex);
//			System.out.println("BitMapp class 86:mapIndex  "+mapIndex);
		}
		public static int findLastOne(String bitStream) {
			for(int i=bitStream.length()-1;i>=0;i--) {
				if(bitStream.charAt(i)=='1')
					return i+1;
			}
			return -1;
		}
		
	   public void insert(int stringIndex){
		   mapIndex.forEach((key, value)->
		   			mapIndex.put(key,editBits(value,stringIndex))
			);
	   }
	   

	   public static String editBits(String s,int stringIndex) {
			int x=stringIndex;
		    String r=s.substring(0,x)+"0"+s.substring(x);
		    return r;
		}
	   
	   public Comparable getPreviousRecord(Comparable insertedRecord) {
			Set<Comparable> list=mapIndex.keySet();
		    int x=Arrays.binarySearch(list.toArray(), insertedRecord);
		    x=x<0?x*-1-2:x;
		   try {
			   return ((Comparable) list.toArray()[x]);
		   }
		   catch(Exception e) {
			   return null;
		   }
		}
		
	//----------------------------------------------------(Encryption)--------------------------------------------------------------------------
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
