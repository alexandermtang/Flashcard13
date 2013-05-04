package model;

/**
 * An interface used for the backend of any photoAlbum application.
 * @author alextang
 */
public interface BackendInterface {

	/**
	 * Loads Serialized Object from memory
	 * @param id 				id that identifies Object to load
	 * @return					Object loaded from memory, null if caught exception
	 */
	Object load(String id);

	/**
	 * Saves Serialized Object to memory
	 * @param o			object to save
	 * @return 			true if saved; false otherwise
	 */
	boolean save(Object o);
	
	/**
	 * Deletes Serialized Object from memory
	 * @param id 		id that identifies Object to delete
	 */
	boolean delete(String id);
	
	Object loadAll();
}
