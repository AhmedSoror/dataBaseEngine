
import java.io.*;
import java.util.Date;
import java.util.Hashtable;
import java.util.Set;
import java.util.Vector;

public class Table implements Serializable {
	private String strTableName;
	private String strClusteringKeyColumn;
	private Hashtable<String, String>htblColNameType;
	private Vector<Page> vecPages;
	private int id;
	
	public Table(String strTableName,String strClusteringKeyColumn,Hashtable<String, String>htblColNameType) {
		this.strTableName = strTableName;
		this.strClusteringKeyColumn=strClusteringKeyColumn;
		this.htblColNameType=htblColNameType;
		id=0;
		vecPages=new Vector<Page>();
			try {
						FileWriter fileWriter=new FileWriter("data/metadata.csv",true);
						Set<String> colNames=htblColNameType.keySet();
						for(String s :colNames) {
							fileWriter.write(strTableName+","+s+","+htblColNameType.get(s)+","+isKey(s)+","+"False\n");
				}
						fileWriter.close();			
			} catch (IOException e) {
						System.out.println("Error in writing metadata");
				}
		}
		public boolean isKey(String Key) {
			return strClusteringKeyColumn.equals(Key);
		}
	/*
	 *	 finding the page which contains the data if the record contains the clustringKeyColumn by comparing the first and last records
	 *	sreturns the index of the required page if found else returns -1 meaning a new page should be created 
	 */
	
	public int findPage(Hashtable<String,Object>htblColNameValue) {
		
		// Comparable clusterInserted= (Comparable) htblColNameValue.get(strClusteringKeyColumn);
		// for (int i=0;i<vecPages.size();i++) {
		// 	if(i==vecPages.size()-1) return i;
		// 	Comparable last = (Comparable)vecPages.get(i).getRecord(vecPages.get(i).size()-1).get(strClusteringKeyColumn);
		// 	if(clusterInserted.compareTo(last)<=0)return i;
		// 	else {
		// 		Comparable firstNextPage = (Comparable)vecPages.get(i+1).getRecord(0).get(strClusteringKeyColumn);
		// 		if(clusterInserted.compareTo(firstNextPage)<=0)return i;
		// 	}
		// }
		// return -1;
		
		Comparable clusterInserted= (Comparable) htblColNameValue.get(strClusteringKeyColumn);
		for (int i=0;i<id;i++) {
			if(i==id-1) return i;
			Page page =loadPage(i);
			if(page==null)continue;
			Comparable last = (Comparable)page.getRecord(page.size()-1).get(strClusteringKeyColumn);
			if(clusterInserted.compareTo(last)<=0)return i;
			else {
				Page nextPage = loadPage(i+1);
				if(nextPage==null)continue;
				Comparable firstNextPage = (Comparable)nextPage.getRecord(0).get(strClusteringKeyColumn);
				if(clusterInserted.compareTo(firstNextPage)<=0)return i;
			}
		}
		return -1;
	}
	
	// inserting into the table using the clustring column
	
	public void insert(Hashtable<String,Object>htblColNameValue) {
		// htblColNameValue.put("TouchDate", new Date());
		// int pageIndex = findPage(htblColNameValue);
		// if(pageIndex!=-1) {	
		// 	insert_helper(pageIndex, htblColNameValue);
		// }
		// else {
		// 	vecPages.add(new Page(strClusteringKeyColumn));
		// 	vecPages.get(vecPages.size()-1).insert(htblColNameValue);
		// }
		htblColNameValue.put("TouchDate", new Date());
		int pageIndex = findPage(htblColNameValue);
		System.out.println("find Page: "+pageIndex);
		if(pageIndex!=-1) {	
			insert_helper(pageIndex, htblColNameValue);
		}
		else {
			Page newPage = new Page(strClusteringKeyColumn);
			newPage.insert(htblColNameValue);
			writePage(newPage, id++);	
		}
	}
	
	public void insert_helper(int pageIndex,Hashtable<String,Object>htblColNameValue){
		if(htblColNameValue==null)return;
		Hashtable<String,Object> lastRecord = null;
		Page page 	=loadPage(pageIndex);
		if(page==null){insert_helper(pageIndex+1, htblColNameValue);return;}
		lastRecord =page.insert(htblColNameValue);
		writePage(page,pageIndex);
		if(pageIndex==id-1 && lastRecord!=null)writePage(new Page(strClusteringKeyColumn),id++);
		insert_helper(pageIndex+1, lastRecord);
	}
	
	/*
	 * delete from a table by looping through all pages and match records
	 */
	public void delete (Hashtable<String,Object>htblColNameValue) {
		for(int i=id-1;i>=0;i--) {
			Page page = loadPage(i);
			if(page==null)continue;
			page.delete(htblColNameValue);							// in each page delete records matching with the query
			writePage(page, i);
			if (page.isEmpty())
				{
					File file =new File("data/"+this.strTableName+" "+i+".ser"); 
					file.delete();
				}
		}	
	}
	

	public void update(String strKey,Hashtable<String,Object> htblColNameValue) throws DBAppException{
		String s = this.getPrimaryType();
		String[] primary = s.split("#");
		String columnType=primary[0];
		String columnName=primary[1];

		for(int i=id-1;i>=0;i--) {
			Page page = loadPage(i);
			if(page==null)continue;
			page.update(columnType,columnName,strKey,htblColNameValue);							// in each page update records matching with the query
			writePage(page, i);
		}	
	}

	public  String getPrimaryType ()throws DBAppException{
		String columnType=null;
		String columnName=null;
		String currentLine = "";
		try{
		FileReader fileReader= new FileReader("data/metadata.csv");
		BufferedReader br = new BufferedReader(fileReader);
		while ((currentLine = br.readLine()) != null) {
			String[] line =currentLine.split(",");
			String tableName = line[0];
			columnName = line[1];
			columnType = line[2];
			String primaryKey = line[3];
			String indexed = line[4];
			if(tableName.equals(this.strTableName)&&primaryKey.equals("True"))
				break;
		}
		return columnType+"#"+columnName;}
		catch (Exception e){
			throw new DBAppException(e.getMessage());
		}
	}

	public boolean writePage(Page page,int i){					//******** */
		try {
			FileOutputStream fileOut = new FileOutputStream("data/"+this.strTableName+" "+i+".ser");
			ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
			objectOut.writeObject(page);
			objectOut.close();
			return true;

		} catch (Exception ex) {
			return false;
		}
	}
	public Page loadPage(int i){								//********//
		try {
			FileInputStream filein = new FileInputStream("data/"+this.strTableName+" "+i+".ser");
			ObjectInputStream objectin = new ObjectInputStream(filein);
			Page page =(Page)objectin.readObject();
			objectin.close();
			return page;

		} catch (Exception ex) {
			return null;
		}
	}

	
	public static void main(String[] args) {

	
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
		for(int i=0;i<id;i++) {
			Page page = loadPage(i);
			if(page==null)continue;
			sb.append((page).toString()+"\n");
		}
		return sb.toString();
	}
}
