import java.util.Collections;
import java.util.Hashtable;
import java.util.Vector;

public class Table {
	private String strTableName;
	private Vector<pair> vecPairs;
	private String strClusteringKeyColumn;
	private Hashtable<String, String>htblColNameType;
	
	
	public Table(String strTableName,String strClusteringKeyColumn,Hashtable<String, String>htblColNameType) {
		this.strTableName=strTableName;
		this.strClusteringKeyColumn=strClusteringKeyColumn;
		this.htblColNameType=htblColNameType;
		vecPairs=new Vector<pair>();	
	}
	public void insert(Hashtable<String,Object>htblColNameValue) {
		Hashtable<String,Object>htblReturnValue=null;
		if(htblColNameValue==null) {
			return;
		}
		else {
			int intPageIndex=Collections.binarySearch(vecPairs,new pair(null,strClusteringKeyColumn));
			intPageIndex=intPageIndex<0?Math.abs(intPageIndex)-1:intPageIndex;
			htblReturnValue=vecPairs.get(intPageIndex).page.insert(htblColNameValue);
			
		}
		Collections.sort(vecPairs);
		insert(htblReturnValue);
	}
	public static void main(String[] args) {
		String strTableName = "Student";
		Hashtable htblColNameType = new Hashtable( );
		htblColNameType.put("id", "java.lang.String");
		htblColNameType.put("name", "java.lang.String");
		htblColNameType.put("gpa", "java.lang.String");
		Table t=new Table(strTableName, "id", htblColNameType);
		
		
		Hashtable htblColNameValue1 = new Hashtable( );

		Hashtable htblColNameValue2 = new Hashtable( );
		
		
		htblColNameValue1.put("id", new Integer( 453455 ));
		htblColNameValue1.put("name", new String("Ahmed Noor" ) );
		htblColNameValue1.put("gpa", new Double( 0.95 ) );
		t.insert( htblColNameValue1 );
		htblColNameValue2.put("id", new Integer( 5674567 ));
		htblColNameValue2.put("name", new String("Dalia Noor" ) );
		htblColNameValue2.put("gpa", new Double( 1.25 ) );
		t.insert(  htblColNameValue2 );
		
		
		System.out.println(t.vecPairs);
//		t.insert(htblColNameType1);
//		t.insert(htblColNameType2);
//		t.insert(htblColNameType3);
//		t.insert(htblColNameType4);
//		System.out.println(t.vecPairs);
		
	}
	
}
class pair implements Comparable<pair>{
	Page page;
	Object firstKey;
	public pair(Page page,Object firstkey){
		this.page=page;
		this.firstKey=firstkey;
	}
	
	
	@Override
	public int compareTo(pair pair2) {
		// TODO Auto-generated method stub
		return ((Comparable)(firstKey)).compareTo((Comparable)(pair2.firstKey));
	}
	
	public String toString(){
		return page.toString();
	}
	
}

