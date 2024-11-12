/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Exception.java to edit this template
 */
package util.exception;

/**
 *
 * @author jwong
 */
public class RoomAllocationException extends Exception {

    /**
     * Creates a new instance of <code>RoomAllocationException</code> without
     * detail message.
     */
    public RoomAllocationException() {
    }

    /**
     * Constructs an instance of <code>RoomAllocationException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public RoomAllocationException(String msg) {
        super(msg);
    }
}
