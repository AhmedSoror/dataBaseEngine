
import java.io.*;
import java.util.Collections;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Vector;

public class Table implements Serializable {
	private String strTableName;
	private String strClusteringKeyColumn;
	private Hashtable<String, String> htblColNameType;
	private Vector<Page> vecPages;
	private int id;
//	--------------------------------------------------(New Attributes)-----------------------------------------------------------------------------------------
	private Vector<BitMapIndex> vecIndecies;
	private  int recordsCount=0;
	private TreeMap<Integer,Integer> mapPageLength;
	
	
//	-----------------------------------------------( Constructor )-------------------------------------------------------------------------------------------
	
	public Table(String strTableName, String strClusteringKeyColumn, Hashtable<String, String> htblColNameType) {
		this.strTableName = strTableName;
		this.strClusteringKeyColumn = strClusteringKeyColumn;
		this.htblColNameType = htblColNameType;
		id = 0;
		vecPages = new Vector<Page>();
		vecIndecies=new Vector<>();						//*******************************
		mapPageLength=new TreeMap();					//*******************************
		try {
			FileWriter fileWriter = new FileWriter("data/"+strTableName+"metadata.csv", true);
			Set<String> colNames = htblColNameType.keySet();
			fileWriter.write("Table Name, Column Name, Column Type, Key, Indexed");
			for (String s : colNames) {
				fileWriter.write(strTableName + "," + s + "," + htblColNameType.get(s) + "," + isKey(s) + "," + "False\n");
			}
			fileWriter.close();
		} catch (IOException e) {
			System.out.println("Error in writing metadata");
		}
	}

	
//	-----------------------------------------------( Insert )-------------------------------------------------------------------------------------------

	public int findPage(Hashtable<String, Object> htblColNameValue) {
		Comparable clusterInserted = (Comparable) htblColNameValue.get(strClusteringKeyColumn);
		for (int i = 0; i < id; i++) {
			if (i == id - 1)
				return i;
			Page page = loadPage(i);
			if (page == null)
				continue;
			Comparable last = (Comparable) page.getRecord(page.size() - 1).get(strClusteringKeyColumn);
			if (clusterInserted.compareTo(last) <= 0)
				return i;
			else {
				Page nextPage = loadPage(i + 1);
				if (nextPage == null)
					continue;
				Comparable firstNextPage = (Comparable) nextPage.getRecord(0).get(strClusteringKeyColumn);
				if (clusterInserted.compareTo(firstNextPage) <= 0)
					return i;
			}
		}
		return -1;
	}
	/*
	
	public void insert_helper(int pageIndex, Hashtable<String, Object> htblColNameValue) {
		if (htblColNameValue == null)
			return;
		Hashtable<String, Object> lastRecord = null;
		Page page = loadPage(pageIndex);
		if (page == null) {
			insert_helper(pageIndex + 1, htblColNameValue);
			return;
		}
		lastRecord = page.insert(htblColNameValue);
		writePage(page, pageIndex);
		if (pageIndex == id - 1 && lastRecord != null)
			try {
				writePage(new Page(strClusteringKeyColumn), id++);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("Error in properties file");
			}
		insert_helper(pageIndex + 1, lastRecord);
	}
	*/
	
	public void insert(Hashtable<String, Object> htblColNameValue) throws DBAppException {
		if(htblColNameValue.get(strClusteringKeyColumn)==null) {
			throw new DBAppException("clustering key must be entered");
		}
		htblColNameValue.put("TouchDate", new Date());
		int pageIndex = findPage(htblColNameValue);
		if (pageIndex != -1) {
			insert_helper(pageIndex, htblColNameValue);
			
		} else {
			Page newPage;
			try {
				newPage = new Page(strClusteringKeyColumn);
				newPage.insert(htblColNameValue);
				mapPageLength.put(id,1);				//*********************( New )*********************
				writePage(newPage, id++);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("error in properties file");
			}
		}
	}

//	-----------------------------------------------( Delete )-------------------------------------------------------------------------------------------

	public void delete(Hashtable<String, Object> htblColNameValue) {
		for (int i = id - 1; i >= 0; i--) {
			Page page = loadPage(i);
			if (page == null)
				continue;
			page.delete(htblColNameValue); // in each page delete records matching with the query
			writePage(page, i);
			mapPageLength.put(i, page.size());
			if (page.isEmpty()) {
				File file = new File("data/" + this.strTableName + " " + i + ".class");
				file.delete();
			}
		}
	}
	
//	-----------------------------------------------( Update )-------------------------------------------------------------------------------------------
	public String getPrimaryType() throws DBAppException {					// return clustringKey type#name
		String columnType = null;
		String columnName = null;
		String currentLine = "";
		try {
			FileReader fileReader = new FileReader("data/"+strTableName+"metadata.csv");
			BufferedReader br = new BufferedReader(fileReader);
			br.readLine();
			while ((currentLine = br.readLine()) != null) {
				String[] line = currentLine.split(",");
				String tableName = line[0];
				columnName = line[1];
				columnType = line[2];
				String primaryKey = line[3];
				String indexed = line[4];
				if (tableName.equals(this.strTableName) && primaryKey.equals("True"))
					break;
			}
			return columnType + "#" + columnName;
		} catch (Exception e) {
			throw new DBAppException(e.getMessage());
		}
	}
	
	public void update(String strKey, Hashtable<String, Object> htblColNameValue) throws DBAppException {
		String s = this.getPrimaryType();
		String[] primary = s.split("#");
		String columnType = primary[0];
		String columnName = primary[1];
		for (int i = id - 1; i >= 0; i--) {
			Page page = loadPage(i);
			if (page == null)
				continue;
			page.update(columnType, columnName, strKey, htblColNameValue); // in each page update records matching with
																			// the query
			writePage(page, i);
		}
	}
	
//	-----------------------------------------------( Create Bitmap Index )-------------------------------------------------------------------------------------------
	public void createBitmapIndex(String strColName) {
		Hashtable<String, String> htblColNameType=new Hashtable<>();
		htblColNameType.put(strColName, "java.lang.String");
		htblColNameType.put("BitMapBits", "java.lang.String");
		BitMapIndex index=new BitMapIndex(strTableName, strColName,htblColNameType);
		vecIndecies.add(index);
		
		String zeros=String.join("", Collections.nCopies(recordsCount, "0"));
		for (int i = 0; i <= id-1; i++) {
			Page page = loadPage(i);
			if (page == null)
				continue;
			
			for(Hashtable<String, Object> htblRecord:page.getVecData()) {
//				index.mapIndex.put((String)(htblRecord.get(strColName).toString()),zeros);
				index.mapIndex.put((Comparable)( htblRecord.get(strColName)),zeros);
			}
		}
		int count=0;		//no of zeros 
		for (int i = 0; i <= id - 1; i++) {
			Page page = loadPage(i);
			if (page == null) {
				continue;
			}
			for(Hashtable<String, Object> htblRecord:page.getVecData()) {
				String oldBits=index.mapIndex.get( htblRecord.get(strColName));
				String newBits="";
				if(count>0) {
					newBits=oldBits.substring(0, count)+"1"+oldBits.substring(count+1);
				}
				else {
					newBits="1"+oldBits.substring(count+1);
					
				}				
				index.mapIndex.put((Comparable) htblRecord.get(strColName),newBits);
				count++;
			}
		}
		index.writeIndex();
		System.out.println("Table l215 "+index.mapIndex);
		System.out.println("Table l217 "+mapPageLength);
		 Vector <RecordLocation> newVec=findPageUsingIndex("name", new String("John Noor")) ;
		 System.out.println(newVec);
	}
		
		
	
//	-----------------------------------------------( Get special record )-------------------------------------------------------------------------------------------
	
	public Hashtable<String, Object>  get (Hashtable<String, Object> htblColNameValue) {
		for (int i = id - 1; i >= 0; i--) {
			Page page = loadPage(i);
			if (page == null)
				continue;
			Hashtable<String, Object> htblResult=page.get(htblColNameValue); // in each page delete records matching with the query
			if(htblResult!=null)
				return htblResult;
		}
		return null;
	}
	
	
//	-----------------------------------------------( Write & read pages  )-------------------------------------------------------------------------------------------
	
	public boolean writePage(Page page, int i) { // ******** */
		try {
			FileOutputStream fileOut = new FileOutputStream("data/" + this.strTableName + " " + i + ".class");
			ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
			objectOut.writeObject(page);
			objectOut.close();
			return true;

		} catch (Exception ex) {
			return false;
		}
	}

	public Page loadPage(int i) { // ********//
		try {
			FileInputStream filein = new FileInputStream("data/" + this.strTableName + " " + i + ".class");
			ObjectInputStream objectin = new ObjectInputStream(filein);
			Page page = (Page) objectin.readObject();
			objectin.close();
			return page;

		} catch (Exception ex) {
			return null;
		}
	}
//	-----------------------------------------------( Getters & setters )-------------------------------------------------------------------------------------------

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
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Vector<BitMapIndex> getVecIndecies() {
		return vecIndecies;
	}
	public void setVecIndecies(Vector<BitMapIndex> vecIndecies) {
		this.vecIndecies = vecIndecies;
	}
	public  int getRecordsCount() {
		 return recordsCount;
	}
	public  void decRecordsCount() {
		recordsCount=recordsCount-1;
	}
	public  void incRecordsCount() {
		recordsCount=recordsCount+1;
	}
	public boolean isKey(String Key) {
		return strClusteringKeyColumn.equals(Key);
	}
//	-----------------------------------------------( toString )-------------------------------------------------------------------------------------------
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(strTableName + "\t" + strClusteringKeyColumn);
		for (int i = 0; i < id; i++) {
			Page page = loadPage(i);
			if (page == null)
				continue;
			sb.append((page).toString() + "\n");
		}
		return sb.toString();
	}
	
//	-----------------------------------------------( Added Mathods )-------------------------------------------------------------------------------------------
	
	public void deleteByIndex(Hashtable<String, Object> htblColNameValue) {
		/*
		 * Search for record using search method
		 * using the page number from the search, delete the record
		 * 
		 */
		
		
	}
	
	public void deleteWithoutIndex(Hashtable<String, Object> htblColNameValue) {
		for (int i = id - 1; i >= 0; i--) {
			Page page = loadPage(i);
			if (page == null)
				continue;
			page.delete(htblColNameValue); 					// in each page delete records matching with the query
			
			writePage(page, i);
			if (page.isEmpty()) {
				File file = new File("data/" + this.strTableName + " " + i + ".class");
				file.delete();
			}
		}
	}
	public void deleteIndex(Hashtable<String, Object> htblColNameValue,int pageNumber) {
		for(BitMapIndex index:vecIndecies) {
			int count=0;
			/*
			 * loop through the bits,
			 * 	 if bit==1 && vecPageNumber[i]==pageNumber
			 * 		load page()
			 * 		check record by record in page
			 * 		if pagerecord == htblColNameValue
			 * 			delete the coresponding bit
			 * 
			 */
		
		
		}
	}
	
	
	
//	-----------------------------------------------( Modified Mathods )-------------------------------------------------------------------------------------------
	/*
	public void delete(Hashtable<String, Object> htblColNameValue) {
		boolean indexOnClusterKey=false;					// references index on cluster key
		for(BitMapIndex index:vecIndecies) {
			if(index.strColName.equals(strClusteringKeyColumn)) {
				indexOnClusterKey=true;
				break;
			}
		}
		if(indexOnClusterKey) {
			deleteByIndex(htblColNameValue);			
		}
		else {
			deleteWithoutIndex(htblColNameValue);
		}
		
		
	}
	*/


	public void insert_helper(int pageIndex, Hashtable<String, Object> htblColNameValue) {
		if (htblColNameValue == null) {
			return;
		}
		Hashtable<String, Object> lastRecord = null;
		Page page = loadPage(pageIndex);
		if (page == null) {
			insert_helper(pageIndex + 1, htblColNameValue);
			return;
		}
		lastRecord = page.insert(htblColNameValue);
		writePage(page, pageIndex);
		if (pageIndex == id - 1 && lastRecord != null)
			try {
				writePage(new Page(strClusteringKeyColumn), id++);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("Error in properties file");
			}
		mapPageLength.put(pageIndex, page.size());
		insert_helper(pageIndex + 1, lastRecord);
	}
	/*
	 test on page with empty fields
	 
	 */
		
	public Vector <RecordLocation> findPageUsingIndex(String strColumnName, Object objColumnValue) {
		String bitStream = "";
		int pageNumber = 0;
		int recordNumber = 0;
		Vector<RecordLocation> occurences = new Vector <RecordLocation>();
		for (BitMapIndex index : vecIndecies) {
			if (index.strColName.equals(strColumnName)) {
				if (index.mapIndex.get(objColumnValue)==null)
					return occurences;
				bitStream = index.mapIndex.get(objColumnValue);
				int bit = 0;

				for (int i = 0; i < mapPageLength.size(); i++) {
					for (int j = 0; j < mapPageLength.get(i); j++) {
						recordNumber++;
						if (bitStream.charAt(bit)=='1') {
							occurences.addElement(new RecordLocation(pageNumber,recordNumber));
						}
						bit++;
					}
					pageNumber++;
					recordNumber = 0;
				}

			}
		}

		return occurences;
	}
	
	
	///////////////////////////////////////////////////////////////////SEARCH///////////////////////////////////////////////////////
	
//	public Vector<Hashtable<String, Object>> 
//	public String findBitStream(SQLTerm term) {
//		for (BitMapIndex index : vecIndecies) {
//			if (index.strColName.equals(term._strColumnName)) {
//				if(term._strOperator.equals("=")) {
//					if (index.mapIndex.get(term._objValue)==null)
//						return  String.join("", Collections.nCopies(recordsCount, "0"));
//					else {
//						return index.mapIndex.get(term._objValue);
//					}
//				}
//				else if(term._strOperator.equals(">")) {
//					if (index.mapIndex.get(term._objValue)==null)
//						return  String.join("", Collections.nCopies(recordsCount, "0"));
//					else {
//						return index.mapIndex.get(term._objValue);
//					}
//				}
//			}
//		}
//		
//	}
	
	public Vector<Hashtable<String , Object>> select (Vector<SQLTerm> vecTerms,Vector<String> vecOperators){
		System.out.println(mapPageLength);
		vecTerms = evaluateIndexedColumns(vecTerms);
		//loop on and
		while(vecOperators.contains("AND")) {
			int index = vecOperators.indexOf("AND");
			SQLTerm term1 = vecTerms.get(index);
			SQLTerm term2 = vecTerms.get(index+1);
			term1._evaluation=evaluateOperation(term1,term2,"AND");
			vecOperators.remove(index);
			vecTerms.remove(index+1);
		}
		//loop on xor
		while(vecOperators.contains("XOR")) {
			int index = vecOperators.indexOf("XOR");
			SQLTerm term1 = vecTerms.get(index);
			SQLTerm term2 = vecTerms.get(index+1);
			term1._evaluation=evaluateOperation(term1,term2,"XOR");
			vecOperators.remove(index);
			vecTerms.remove(index+1);
		}
		//loop on or
		while(vecOperators.contains("OR")) {
			int index = vecOperators.indexOf("OR");
			SQLTerm term1 = vecTerms.get(index);
			SQLTerm term2 = vecTerms.get(index+1);
			System.out.println(term1 +"  "+term2);
			term1._evaluation=evaluateOperation(term1,term2,"OR");
			System.out.println(term1._evaluation);
			vecOperators.remove(index);
			vecTerms.remove(index+1);
		}
		String result = vecTerms.get(0)._evaluation;
		Vector<Hashtable<String, Object>> vecResult =new Vector<>();
		for(int i=0;i<result.length();i++) {
			if(result.charAt(i)=='1') {
				RecordLocation loc = getLocation(i);
				System.out.println(loc.pageNumber+"  "+loc.recordNumber );
				vecResult.add(loadPage(loc.pageNumber).getByIndex(loc.recordNumber));
			}
		}
		return vecResult;
	}
		//loop and evaluate those who have index   (fill the extra attribute)
	public Vector <SQLTerm> evaluateIndexedColumns (Vector <SQLTerm>  allTerms){
		Vector<RecordLocation> occurences = new Vector <RecordLocation>();
		Vector <SQLTerm> evaluated =new Vector <SQLTerm>();
		String evaluation="";
		for (SQLTerm term :allTerms) {
			for (BitMapIndex index : vecIndecies) {
				if (term._strColumnName.equals(index.strColName)) {
					evaluation=index.getBitStream((Comparable)term._objValue,term._strOperator); //mmkn teragga3 string bardo 5alli balak
					//evaluation=this.locationsToBitStream(occurences);
					term._evaluation=evaluation;
				}
				else {
					term._evaluation="";
				}
			}
			evaluated.add(term);
		}
		return evaluated;
		
		
	}
	
	
	public String locationsToBitStream (Vector<RecordLocation> occurences) {
		String zeros=String.join("", Collections.nCopies(recordsCount, "0"));
		for (RecordLocation occurence:occurences) 
			zeros=zeros.substring(0, occurence.pageNumber+occurence.recordNumber)+'1'+zeros.substring(occurence.pageNumber+occurence.recordNumber+1);
		return zeros;
	}
	
	
	

































//-----------------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------	
public String evaluateOperation(SQLTerm term1,SQLTerm term2,String op) {
	//el etnein malhomsh index
	String term1Evaluation="";
	String term2Evaluation="";
	String result = "";
	if(term1._evaluation.equals("") && term2._evaluation.equals("")) {
		if (op.equals("AND")) {
			term1Evaluation=this.getBitStreamForNonIndexed(term1);
			for (int i =0 ;i<term1Evaluation.length();i++){
				if (term1Evaluation.charAt(i)=='1'){
					RecordLocation loc = getLocation(i);
					Hashtable <String,Object> htbl = loadPage(loc.pageNumber).getByIndex(loc.recordNumber);
					if (satisfy(htbl,term2)){
						result+='1';
					}
					else{
						result+='0';
					}
				}
				else{
					result+='0';
				}
			}
		}
		else if (op.equals("XOR")) {
			term1Evaluation=this.getBitStreamForNonIndexed(term1);
			for (int i =0 ;i<term1Evaluation.length();i++){
				RecordLocation loc = getLocation(i);
				Hashtable <String,Object> htbl = loadPage(loc.pageNumber).getByIndex(loc.recordNumber);
				if (term1Evaluation.charAt(i)=='1'){
					if (satisfy(htbl,term2)){
						result+='0';
					}
					else{
						result+='1';
					}
				}
				else{
					if (satisfy(htbl,term2)){
						result+='1';
					}
					else{
						result+='0';
					}
				}
			}
		}
		else if (op.equals("OR")) {
			term1Evaluation=this.getBitStreamForNonIndexed(term1);
			term2Evaluation=this.getBitStreamForNonIndexed(term2);
			result=orOperation(term1Evaluation, term2Evaluation);
		}
	}
	// el etnein lehom index
	else if(!term1._evaluation.equals("") && !term2._evaluation.equals("")) {
		term1Evaluation = term1._evaluation;
		term2Evaluation = term2._evaluation;
		if (op.equals("AND")) {
			result=andOperation(term1Evaluation, term2Evaluation);
		}
		else if (op.equals("XOR")) {
			result=xorOperation(term1Evaluation, term2Evaluation);
		}
		else if (op.equals("OR")) {
			result=orOperation(term1Evaluation, term2Evaluation);
		}
	}
	// wa7ed fehom leh
	else {
		String HasIndex;
		SQLTerm termNoIndex;
		if(!term1._evaluation.equals("")){
			HasIndex = term1._evaluation;
			termNoIndex = term2;
		}
		else{
			HasIndex = term2._evaluation;
			termNoIndex = term1;
		}
		if (op.equals("AND")) {
			for (int i =0 ;i<HasIndex.length();i++){
				if (HasIndex.charAt(i)=='1'){
					RecordLocation loc = getLocation(i);
					Hashtable <String,Object> htbl = loadPage(loc.pageNumber).getByIndex(loc.recordNumber);
					if (satisfy(htbl,termNoIndex)){
						result+='1';
					}
					else{
						result+='0';
					}
				}
				else{
					result+='0';
				}
			}
		}
		else if (op.equals("XOR")) {
			for (int i =0 ;i<HasIndex.length();i++){
				RecordLocation loc = getLocation(i);
				Hashtable <String,Object> htbl = loadPage(loc.pageNumber).getByIndex(loc.recordNumber);
				if (HasIndex.charAt(i)=='1'){
					if (satisfy(htbl,termNoIndex)){
						result+='0';
					}
					else{
						result+='1';
					}
				}
				else{
					if (satisfy(htbl,termNoIndex)){
						result+='1';
					}
					else{
						result+='0';
					}
				}
			}		
		}
		else if (op.equals("OR")) {
			result=orOperation(HasIndex, this.getBitStreamForNonIndexed(termNoIndex));
		}
	}
	return result;
}


public String getBitStreamForNonIndexed(SQLTerm term) {
	String bitStream="";
	for (int i = 0; i < id; i++) {
		Page page = loadPage(i);
		if (page == null)
			continue;
			bitStream +=page.generateBitStream(term); // in each page delete records matching with the query
	}
return bitStream;

}


//public RecordLocation getLocation(int ind){
//	int recordNumber=0;
//	for (int i = 0; i < mapPageLength.size(); i++) {
//		for (int j = 0; j < mapPageLength.get(i); j++) {
//			recordNumber++;
//			if (recordNumber==ind) {
//				return (new RecordLocation(i,j));
//			}
//		}
//		recordNumber = 0;
//	}
//	return null;
//
//}
public RecordLocation getLocation(int ind){
	int recordNumber=0;
	int count = 0;
	for(Map.Entry<Integer,Integer> entry : mapPageLength.entrySet()) {
		Integer pageNumber = entry.getKey();
		Integer pageCount = entry.getValue(); 
		if(count+pageCount>ind) return new RecordLocation(pageNumber, ind-count);
		count+= pageCount;
	}
	
	return null;

}


public static String orOperation(String s1,String s2) {
	String res="";
	for(int i=0;i<s1.length();i++) {
		if(s1.charAt(i)=='1'||s2.charAt(i)=='1')res+="1";
		else res+="0";
	}
	return res;
}


public static String andOperation(String s1,String s2) {
	String res="";
	for(int i=0;i<s1.length();i++) {
		if(s1.charAt(i)=='1'&&s2.charAt(i)=='1')res+="1";
		else res+="0";
	}
	return res;
}


public static String xorOperation(String s1,String s2) {
	String res="";
	for(int i=0;i<s1.length();i++) {
		if((s1.charAt(i)=='1'&&s2.charAt(i)=='0')||(s1.charAt(i)=='0'&&s2.charAt(i)=='1'))res+="1";
		else res+="0";
	}
	return res;
}


public static boolean satisfy(Hashtable<String,Object>htbl,SQLTerm term)
{
	String operator = term._strOperator;
	Comparable value = (Comparable) htbl.get(term._strColumnName);
	if(operator.equals("<")){
		return (value.compareTo(term._objValue)<0);
	}
	else if(operator.equals(">")){
		return (value.compareTo(term._objValue)>0);
	}
	else if(operator.equals("=")){
		return (value.compareTo(term._objValue)==0);
	}
	else if(operator.equals("<=")){
		return (value.compareTo(term._objValue)<=0);
	}
	else if(operator.equals(">=")){
		return (value.compareTo(term._objValue)>=0);
	}
	else{
		return (value.compareTo(term._objValue)!=0);
	}

}






//--------------------------------------------------------------------------------
	
	// class table 
	//public String evaluateOperation(SQLTerm term1,SQLTerm term2,String op)   bet evaluate etnein terms
	//public String getBitStreamForNonIndexed(SQLTerm term) 
	
	//class page 
	//public String generateBitStream (Hashtable<String, Object> htblSearching) { bta5od el record elli hwa 3obara 3n column wa7ed w tragga3 bit stream leh













}
