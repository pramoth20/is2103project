/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Exception.java to edit this template
 */
package util.exception;

/**
 *
 * @author jwong
 */
public class NoAvailableRoomsException extends Exception {

    /**
     * Creates a new instance of <code>NoAvailableRoomsException</code> without
     * detail message.
     */
    public NoAvailableRoomsException() {
    }

    /**
     * Constructs an instance of <code>NoAvailableRoomsException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public NoAvailableRoomsException(String msg) {
        super(msg);
    }
}
