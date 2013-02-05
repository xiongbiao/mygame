package erb.unicomedu.dao;

import erb.unicomedu.util.EuException;

public abstract interface AsyncTaskCompleteListener<T> {
	public abstract void lauchNewHttpTask(T paramT) throws EuException;

	public abstract void onTaskComplete(T paramT) throws EuException;
}
