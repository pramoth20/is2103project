/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Exception.java to edit this template
 */
package util.exception;

/**
 *
 * @author jwong
 */
public class RoomUnavailableException extends Exception {

    /**
     * Creates a new instance of <code>RoomUnavailableException</code> without
     * detail message.
     */
    public RoomUnavailableException() {
    }

    /**
     * Constructs an instance of <code>RoomUnavailableException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public RoomUnavailableException(String msg) {
        super(msg);
    }
}
