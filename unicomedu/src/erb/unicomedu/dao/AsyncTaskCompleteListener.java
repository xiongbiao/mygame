package erb.unicomedu.dao;

import erb.unicomedu.util.EuException;

public abstract interface AsyncTaskCompleteListener<T> {
	public abstract void lauchNewHttpTask() throws EuException;

	public abstract void onTaskComplete(T paramT) throws EuException;
}
