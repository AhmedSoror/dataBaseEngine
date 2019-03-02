

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.Hashtable;
import java.util.Set;
import java.util.Vector;

public class Table implements Serializable {
	private String strTableName;
	private String strClusteringKeyColumn;
	private Hashtable<String, String>htblColNameType;
	private Vector<Page> vecPages;

	
	public Table(String strTableName,String strClusteringKeyColumn,Hashtable<String, String>htblColNameType) {
		this.strTableName = strTableName;
		this.strClusteringKeyColumn=strClusteringKeyColumn;
		this.htblColNameType=htblColNameType;
		vecPages=new Vector<Page>();

		
		//	Table Name, Column Name, Column Type, Key, Indexed
		try {
//			File file=new File("data/metadata.csv");
//			file.createNewFile();
			FileWriter fileWriter=new FileWriter("data/metadata.csv",true);
			Set<String> colNames=htblColNameType.keySet();
			for(String s :colNames) {
				fileWriter.write(strTableName+","+s+","+htblColNameType.get(s)+","+isKey(s)+","+"False\n");

			}
//			fileWriter.flush();
			fileWriter.close();			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public boolean isKey(String Key) {
		return strClusteringKeyColumn.equals(Key);
	}
	/*
	 *	 finding the page which contains the data if the record contains the clustringKeyColumn by comparing the first and last records
	 *	returns the index of the required page if found else returns -1 meaning a new page should be created 
	 */
	
	public int findPage(Hashtable<String,Object>htblColNameValue) {
		/*
		Comparable clusterInserted= (Comparable) htblColNameValue.get(strClusteringKeyColumn);
		//for each page we get first and last elements and compare with the inserted one
		for(int i=vecPages.size()-1;i>=0;i--) {
			Page p =vecPages.get(i);
			Comparable first = (Comparable) p.getVecData().get(0).get(strClusteringKeyColumn);
			if((first.compareTo(clusterInserted)<=0)) {
				return i;
				}
			}
		return -1;
		*/
//		int index=-1;
//		Comparable clusterInserted= (Comparable) htblColNameValue.get(strClusteringKeyColumn);
//		for(int i=0;i<vecPages.size();i++) {
//			Page p=vecPages.get(i);
//			Comparable last = (Comparable) p.getRecord(p.size()-1).get(strClusteringKeyColumn);
//			if(last.compareTo(clusterInserted)<=0) {
//				index=i;
//			}
//			else {
//				Comparable first = (Comparable) p.getRecord(0).get(strClusteringKeyColumn);
//				if(first.compareTo(clusterInserted)<=0) {
//					index=i;
//				}
//			}
//		}
//		return index;
		Comparable clusterInserted= (Comparable) htblColNameValue.get(strClusteringKeyColumn);
		for (int i=0;i<vecPages.size();i++) {
			if(i==vecPages.size()-1) return i;
			Comparable last = (Comparable)vecPages.get(i).getRecord(vecPages.get(i).size()-1).get(strClusteringKeyColumn);
			if(clusterInserted.compareTo(last)<=0)return i;
			else {
				Comparable firstNextPage = (Comparable)vecPages.get(i+1).getRecord(0).get(strClusteringKeyColumn);
				if(clusterInserted.compareTo(firstNextPage)<=0)return i;
			}
		}
		return -1;
		
		
		
	}
	
	// inserting into the table using the clustring column
	
	public void insert(Hashtable<String,Object>htblColNameValue) {
		int pageIndex = findPage(htblColNameValue);
		if(pageIndex!=-1) {	
			insert_helper(pageIndex, htblColNameValue);
		}
		else {
			vecPages.add(new Page(strClusteringKeyColumn));
			vecPages.get(vecPages.size()-1).insert(htblColNameValue);
		}
	}
	
	public void insert_helper(int pageIndex,Hashtable<String,Object>htblColNameValue){
		if(htblColNameValue==null)return;
		Hashtable<String,Object> lastRecord = null;
		Page page 	=vecPages.get(pageIndex);
		lastRecord =page.insert(htblColNameValue);
		if(pageIndex==vecPages.size()-1 && lastRecord!=null)vecPages.add(new Page(strClusteringKeyColumn));
		insert_helper(pageIndex+1, lastRecord);
	}
	
	/*
	 * delete from a table by looping through all pages and match records
	 */
	public void delete (Hashtable<String,Object>htblColNameValue) {
		for(int i=vecPages.size()-1;i>=0;i--) {
			Page page = vecPages.get(i);
			page.delete(htblColNameValue);							// in each page delete records matching with the query
			if (page.isEmpty())
				vecPages.remove(page);
		}	
	}

	public void update(String strKey,Hashtable<String,Object> htblColNameValue){
		for(int i=vecPages.size()-1;i>=0;i--) {
			Page page = vecPages.get(i);
			page.update(strKey,htblColNameValue);							// in each page delete records matching with the query
			if (page.isEmpty())
				vecPages.remove(page);
		}	
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

	public String getStrTableName() {
		return strTableName;
	}

	public void setStrTableName(String strTableName) {
		this.strTableName = strTableName;
	}

	public String getStrClusteringKeyColumn() {
		return strClusteringKeyColumn;
	}

	public void setStrClusteringKeyColumn(String strClusteringKeyColumn) {
		this.strClusteringKeyColumn = strClusteringKeyColumn;
	}

	public Hashtable<String, String> getHtblColNameType() {
		return htblColNameType;
	}

	public void setHtblColNameType(Hashtable<String, String> htblColNameType) {
		this.htblColNameType = htblColNameType;
	}

	public Vector<Page> getVecPages() {
		return vecPages;
	}

	public void setVecPages(Vector<Page> vecPages) {
		this.vecPages = vecPages;
	}
	public String toString() {
		StringBuilder sb=new StringBuilder();
		sb.append(strTableName+"\t"+strClusteringKeyColumn);
//		sb.append("\t"+htblColNameType);
		for(Page p:vecPages) {
			sb.append(p.toString()+"\n");
		}
		
		return sb.toString();
	}
}
