
public class RecordLocation {
	int pageNumber;
	int recordNumber;

	public RecordLocation(int pageNumber, int recordNumber) {
		this.pageNumber = pageNumber;
		this.recordNumber = recordNumber;
	}
	public String toString() {
		return "PGn: "+pageNumber+" R:"+recordNumber;
	}
}