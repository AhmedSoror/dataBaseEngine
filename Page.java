import java.io.Serializable;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.Vector;

public class Page implements Serializable {
	
	private final int intMaxRecords=3;
	private int intId;
	private static int intSerial;
	private String strClusteringKeyColumn;
	private Vector<Hashtable<String, Object>> vecData;
	
	
	public boolean isFull() {
		return vecData.size()==intMaxRecords;
	}
	
	public boolean isEmpty() {
		return vecData.size()==0;
	}
	
	public Hashtable<String, Object> insert(Hashtable<String, Object>htblRecord) {
		Hashtable<String, Object>htblLastRecord=null;
		if(isFull()) {
			htblLastRecord=vecData.lastElement();
			vecData.removeElement(htblLastRecord);
			
		}
		vecData.add(htblRecord);
		sortPage();
		return htblLastRecord;
		
	}
	
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

}
