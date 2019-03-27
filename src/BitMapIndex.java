import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
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
	
}
