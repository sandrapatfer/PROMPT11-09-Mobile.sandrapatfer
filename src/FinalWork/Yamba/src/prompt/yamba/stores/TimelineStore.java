package prompt.yamba.stores;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

import winterwell.jtwitter.Twitter.Status;

public class TimelineStore implements Iterable<Status>
{
	private AtomicReference<ConcurrentHashMap<Long, Status>> _store;
	
	public TimelineStore()
	{
		_store = new AtomicReference<ConcurrentHashMap<Long, Status>>();
		_store.set(new ConcurrentHashMap<Long, Status>());
	}

	public Iterator<Status> iterator()
	{
		return _store.get().values().iterator();
	}
	
	public Status Get(long id)
	{
		return _store.get().get(id);
	}
	
	public void Add(List<Status> list)
	{		
		// just replace the whole list
		ConcurrentHashMap<Long, Status> newStore = new ConcurrentHashMap<Long, Status>();
		for (Status s : list)
		{
			newStore.put(s.getId(), s);
		}
		_store.set(newStore);
	}	
}
