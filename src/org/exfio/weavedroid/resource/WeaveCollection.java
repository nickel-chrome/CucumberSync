package org.exfio.weavedroid.resource;

import java.util.List;

import org.exfio.weave.WeaveException;
import org.exfio.weave.client.WeaveClient;
import org.exfio.weave.client.WeaveBasicObject;
import org.exfio.weave.client.WeaveCollectionInfo;

public abstract class WeaveCollection<T extends Resource> { 

	protected WeaveClient weaveClient;
	protected String collection;
	protected WeaveCollectionInfo colinfo;
	protected List<T> vobjResources;
	protected List<WeaveBasicObject> weaveResources;

	public WeaveCollection(WeaveClient weaveClient, String collection) {
		this.weaveClient = weaveClient;
		this.collection  = collection;
		this.colinfo     = null;
	}
	
	abstract protected String memberContentType();	
	
	/* collection operations */
	
	public Double getModifiedTime() throws WeaveException {
		if ( colinfo == null ) {
			colinfo = weaveClient.getCollectionInfo(collection);
		}
		return colinfo.getModified();
	}

	public String[] getObjectIds() throws WeaveException {
		return weaveClient.getCollectionIds(collection, null, null, null, null, null, null, null, null);
	}

	public String[] getObjectIdsModifiedSince(Double modifiedDate) throws WeaveException {
		return weaveClient.getCollectionIds(collection, null, null, modifiedDate, null, null, null, null, null);
	}

	public abstract T[] multiGet(String[] ids) throws WeaveException;

	public T[] multiGet(Resource[] resources) throws WeaveException {
		String[] ids = new String[resources.length];
		for (int i = 0; i < resources.length; i++) {
			ids[i] = resources[i].getUid();
		}
		return multiGet(ids);
	}

	public abstract T get(String id) throws WeaveException;

	public T get(Resource res) throws WeaveException {
		return get(res.getUid());
	}

	public abstract void add(Resource res) throws WeaveException;
	
	public abstract void update(Resource res) throws WeaveException;

	public abstract void delete(String id) throws WeaveException;
	
	public void delete(Resource res) throws WeaveException {
		delete(res.getUid());
	}
}