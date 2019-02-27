

import java.util.Hashtable;
import java.util.Vector;

public class Table {
	private String strTableName;
	private String strClusteringKeyColumn;
	private Hashtable<String, String>htblColNameType;
	private Vector<Page> vecPages;
	
	public Table(String strTableName,String strClusteringKeyColumn,Hashtable<String, String>htblColNameType) {
		this.strTableName = strTableName;
		this.strClusteringKeyColumn=strClusteringKeyColumn;
		this.htblColNameType=htblColNameType;
		vecPages=new Vector<Page>();
	}
	
	// inserting into the table using the clustring column
	
	public void insert(Hashtable<String,Object>htblColNameValue) {
		int pageIndex = findPage(htblColNameValue);
		if(pageIndex!=-1)	
			vecPages.get(pageIndex).insert(htblColNameValue);
		else {
			vecPages.add(new Page());
			vecPages.get(vecPages.size()-1).insert(htblColNameValue);
		}
	}
	
	public int findPage(Hashtable<String,Object>htblColNameValue) {
		Comparable clusterInserted= (Comparable) htblColNameValue.get(strClusteringKeyColumn);
		//for each page we get first and last elements and compare with the inserted one
		for(int i=0;i<vecPages.size();i++) {
			Page p =vecPages.get(i);
			Comparable first = (Comparable) p.getVecData().get(0).get(strClusteringKeyColumn);
			Comparable last = (Comparable) p.getVecData().get(p.getVecData().size()-1).get(strClusteringKeyColumn);
			if((first.compareTo(clusterInserted)<=0)&&(last.compareTo(clusterInserted)>=0)) {
				return i;	
				}
			}
		return -1;
	}
	
	public static void main(String[] args) {

		/*
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
		
		
//		t.insert(htblColNameType1);
//		t.insert(htblColNameType2);
//		t.insert(htblColNameType3);
//		t.insert(htblColNameType4);
//		System.out.println(t.vecPairs);
		
		*/
	}
	
}
/*
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

*/
