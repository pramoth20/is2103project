/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Exception.java to edit this template
 */
package util.exception;

/**
 *
 * @author jwong
 */
public class GuestAlreadyExistException extends Exception {

    /**
     * Creates a new instance of <code>GuestAlreadyExistException</code> without
     * detail message.
     */
    public GuestAlreadyExistException() {
    }

    /**
     * Constructs an instance of <code>GuestAlreadyExistException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public GuestAlreadyExistException(String msg) {
        super(msg);
    }
}
