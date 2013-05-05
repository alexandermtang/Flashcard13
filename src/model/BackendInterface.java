package model;

/**
 * An interface used for the backend of any photoAlbum application.
 * @author alextang
 */
public interface BackendInterface {

	Object load(String id);
	Object loadAll();
	boolean save(Object o);
	boolean delete(String id);
}
