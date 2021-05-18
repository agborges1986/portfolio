package com.kaymansoft.proximity.client;

import android.os.AsyncTask;

abstract class NetworkAsyncTask<T, E> extends AsyncTask<T, Void, E>{

	private Throwable throwable;
	private Reportable<E> reportable;
	
	public NetworkAsyncTask<T, E> setReportable(Reportable<E> reportable){
		this.reportable = reportable;
		return this;
	}
	
	public abstract E doBack(T... params) throws RuntimeException;
	
	@Override
	protected E doInBackground(T... params) {
		try{
			return doBack(params);
		}catch(RuntimeException rt){
			throwable = rt.getCause() == null ? rt : rt.getCause();
			return null;
		}
	}

	@Override
	protected void onPostExecute(E ret) {
		reportable.report(ret,  ret != null? "OK" : "Error : "+throwable.getMessage());
	}
}
