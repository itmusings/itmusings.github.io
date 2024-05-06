

import java.util.HashMap;
import java.util.Map;


public class SLARecord {
	/**
	 * A unique key to track this request end to end.
	 */
	private String requestId;
	/**
	 * The Originating SLA Type. This needs to be stored so that the SLALoggingInterceptor would
	 * know when to commit a record to a persistent store. 
	 */
	private String originatingSlaType;
	private Map<String,SLATime> slatimes = new HashMap<String, SLATime>();
	
	public SLARecord(String originatingSLAType){
		this.originationSlaType = originatingSLAType;
		// set the default request Id as a random number.
		requestId = ... // use some way of computing a random number depending on timestamp,ip address etc.
	
	}
	
	public String getRequestId() {
		return requestId;
	}
	
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
	
	public Map<String,SLATime> getSlatimes() {
		return slatimes;
	}
	
	public void setSlatimes(Map<String,SLATime> slatimes) {
		this.slatimes = slatimes;
	}
	
	/** Convenience methods for adding start and end times */
	
	public void addStartTime(String name){
		createOrAccessTime(name);
	}
	
	public void addEndTime(String name){
		SLATime t = createOrAccessTime(name);
		t.addEndTime();
	}
	
	protected SLATime createOrAccessTime(String name){
		SLATime t = slatimes.get(name);
		if (t == null) {
			slatimes.put(name,t = new SLATime(name));
		}
		return t;
	}

	
	public static class SLATime {
		private String slaType;
		private java.util.Date startTime;
		private java.util.Date endTime;
		
		// getters, setters etc. 
	}
	
}
