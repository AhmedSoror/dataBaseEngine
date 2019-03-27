import java.util.Collection;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
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
	public void writeIndex() {
		int i=0;
		for(Page page:getVecPages()) {
			writePage(page, i++);
		}
	
	}
	
	
//	------------------------------------------------------(Added Methods)------------------------------------------------------------------------------
	public void deleteIndex(int bitNumber) {

		mapIndex.forEach( (key, value) -> mapIndex.put( key , value.substring(0,bitNumber)+value.substring(bitNumber+1) ) );
		//		handle vecPageNumber
	}
	
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
	
	
	public static boolean hasNoValues(String s) {
		return s.matches("0*");
	}
	
}
