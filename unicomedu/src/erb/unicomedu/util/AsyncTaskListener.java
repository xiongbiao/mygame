package erb.unicomedu.util;


public  abstract interface AsyncTaskListener<T> {
	
	public abstract void lauchNewHttpTask();
	/**
	 *加载开始
	 */
	public abstract void onPreExecute();
	public abstract void onTaskComplete(T paramT);
	public abstract void onExclption(Exception e);
	/**
	 * 
	 * @param showFoot
	 */
	public abstract void showFoot(boolean showFoot);
}
