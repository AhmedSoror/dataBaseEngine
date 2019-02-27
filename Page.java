

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.Set;
import java.util.Vector;

public class Page implements Serializable {
	
	private final int intMaxRecords=3;
	private int intId;
	private static int intSerial;
	private String strClusteringKeyColumn;
	private Vector<Hashtable<String, Object>> vecData;
	
	// checking if two records are equal
	public static boolean recordMatching (Hashtable<String,Object>htblQuery  ,  Hashtable<String,Object>htblRecord) {
		Set<String> colNames=htblQuery.keySet();
		for(String s :colNames) {
			Comparable query= (Comparable) htblQuery.get(s);
			Comparable record= (Comparable) htblQuery.get(s);
			if(!query.equals(record))return false;
			}
		return true;
	}
	
	public boolean isFull() {
		return vecData.size()==intMaxRecords;
	}
	
	public boolean isEmpty() {
		return vecData.size()==0;
	}
	
	/*
	 * inserting into a page by checking if it has a free space then insert  the record into it then sort 
	 * else remove the last element, insert the record in the hande and sort , then return the removed record to be inserted in another page
	 */
	public Hashtable<String, Object> insert  (Hashtable<String, Object>htblRecord) {
		Hashtable <String, Object> htblLastRecord=null;
		if(isFull()) {
			htblLastRecord=vecData.lastElement();
			vecData.removeElement(htblLastRecord);
			
		}
		vecData.add(htblRecord);
		sortPage();
		return htblLastRecord;
		
	}
	/*
	 * to delete a record in the page, loop through all records and match it with the record in hand using the method
	 * "static boolean recordMatching(Hashtable<String,Object>htblQuery  ,  Hashtable<String,Object>htblRecord)"
	 * if the records match, delete and continue;  (don't use for each)
	 */
	public void delete(Hashtable<String, Object>htblRecord) {
		
		
	
	}
	
	public Page() {
		this.vecData=new Vector<Hashtable<String, Object>>();
		intId=intSerial++;

	}
	public void sortPage() {
		vecData.sort(new Comparator<Hashtable<String, Object>>() {
			@Override
			public int compare(Hashtable<String, Object> htblRecord1, Hashtable<String, Object> htblRecord2) {
				Object obj1=htblRecord1.get(strClusteringKeyColumn);
				Object obj2=htblRecord2.get(strClusteringKeyColumn);
				return ((Comparable)obj1).compareTo((Comparable)(obj2));
			}
			
		});
	}
	public String toString() {
		return ""+intId;
	}
	
	public int getIntId() {
		return intId;
	}

	public void setIntId(int intId) {
		this.intId = intId;
	}

	public static int getIntSerial() {
		return intSerial;
	}

	public static void setIntSerial(int intSerial) {
		Page.intSerial = intSerial;
	}

	public String getStrClusteringKeyColumn() {
		return strClusteringKeyColumn;
	}

	public void setStrClusteringKeyColumn(String strClusteringKeyColumn) {
		this.strClusteringKeyColumn = strClusteringKeyColumn;
	}

	public Vector<Hashtable<String, Object>> getVecData() {
		return vecData;
	}

	public void setVecData(Vector<Hashtable<String, Object>> vecData) {
		this.vecData = vecData;
	}

	public int getIntMaxRecords() {
		return intMaxRecords;
	}

}
