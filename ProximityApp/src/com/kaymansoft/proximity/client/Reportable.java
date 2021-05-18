package com.kaymansoft.proximity.client;

/**
 * @author
 *
 * This interface is intended to provide the ability of asynchronous operations. Objects implementing it 
 * will be given to the network client so that it can return the result of its operations back to the App
 * when they are ready without blocking. It is guaranteed that the methods of this class will only run on
 * the GUI thread. The use of anonymous classes to implement this interface is encouraged.
 * 
 * @param <T> The type returned by the operation in question
 */
public interface Reportable<T> {
	
	/**
	 * This method is called by the network client when new data is ready for processing. The method may be
	 * called more than once, in which case the number of calls will be set by the <code>setRemainingReportsNumber</code> method
	 * before the last expected call to <code>report</code>. It is guaranteed that the reporting action will
	 * run on the GUI thread.
	 * 
	 * @param report The object returned by the operation, null if there was some error. 
	 * @param errorMessage If <code>report</code> is null it contains a message with a short description of the error.
	 */
	public void report(T report, String errorMessage);
	
	/**
	 * This method is only called when there is the need to return data more than once to the app, such as
	 * when a single operation is divided into several simpler operations. It sets the number of times the
	 * <code>report</code> method will be called in the future regardless of what has happen in the past.
	 * It is assumed that the number is set to <code>1</code> on construction. A value of <code>0</code> means
	 * that no more calls will be made and the object can be safely disposed.
	 * 
	 * @param cant the number of expected remaining calls to <code>report</code>
	 */
	public void setRemainingReportsNumber(int cant);
}
