package shtykh.nekki.util;

/**
 * Created by shtykh on 28/02/15.
 */
public interface Receiver<T> {
	void receive(T msg, Object sender);
}
