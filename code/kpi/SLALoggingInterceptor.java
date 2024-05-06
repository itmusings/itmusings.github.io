

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * Base class for all SLA Logging Interceptors..
 * @author raja.kolluru
 *
 */
public  abstract class SLALoggingInterceptor  {
	
	private static final Log logger = LogFactory.getLog(SLALoggingInterceptor.class);
	
	public static final String SLA_TYPE_NAME = "slaType";
	
	public static final String SLA_LOGGER_BEAN = "slaLogger";
	
	/**
	 * The type of SLA this interceptor will measure.
	 */
	protected String slaType;
	protected SLALogger slaLogger;

	public String getSlaType() {
		return slaType;
	}

	public void setSlaType(String name) {
		this.slaType = name;
	}
	
	/**
	 * Called before the invocation of the actual command.
	 * Writes the start time in the sla record. 
	 * Creates a new SLA Record and registers it if necessary.
	 * 
	 */
	protected void beforeCommand(Object context){
		SLARecord slaRecord = getSLARecord(context);
		if (slaRecord == null) {
			slaRecord = new SLARecord();
			slaRecord.setOriginatingSlaType(slaType);
			setSLARecord(context,slaRecord);
		}
		slaRecord.addStartTime(slaType);
	}
	
	/**
	 * Called after invocation of the actual command.
	 * This would commit the changes if it has created the logrecord to begin with.
	 *  Does end time computation.
	 */
	protected void afterCommand(Object context){
		SLARecord slaRecord = getSLARecord(context);
		// slaRecord at this point CAN be null if this interceptor is invoked with an inappropriate method.
		// so ignore it.
		if (slaRecord == null)return;
		slaRecord.addEndTime(slaType);
		// If we are the originator for this command make sure we persist it using the SLALogger.
		if(slaType.equals(slaRecord.getOriginatingSlaType())){
			
			// If SLA Logger is not set ???
			if(slaLogger != null){
				slaLogger.sla(slaRecord);
			}else{
				logger.info(slaRecord);
			}
		}
	}
	
	
	/**
	 * Gets the SLA record of the context that is passed.  
	 * @param context - some place from where the SLA record is extracted. A context is 
	 *   generalized to an Object since it can be very different according to the actual implementation.
	 *   In a web impl of the SLALogger this can be a request scoped variable. If this is an AOP interceptor
	 *   it can be in either a thread local or it can be passed explicitly. 
	 *   See the subclasses for more details.
	 * @return
	 */
	protected  abstract SLARecord getSLARecord(Object context);
	/**
	 * Sets the SLA record in the context.
	 * @param context - {@link #getSLARecord(Object)}
	 * @param slaRecord
	 */
	protected  abstract void setSLARecord(Object context,SLARecord slaRecord);

	public SLALogger getSlaLogger() {
		return slaLogger;
	}

	public void setSlaLogger(SLALogger slaLogger) {
		this.slaLogger = slaLogger;
	}
}
