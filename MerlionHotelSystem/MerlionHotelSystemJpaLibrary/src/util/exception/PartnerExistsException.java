/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Exception.java to edit this template
 */
package util.exception;

/**
 *
 * @author jwong
 */
public class PartnerExistsException extends Exception {

    /**
     * Creates a new instance of <code>PartnerExistsException</code> without
     * detail message.
     */
    public PartnerExistsException() {
    }

    /**
     * Constructs an instance of <code>PartnerExistsException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public PartnerExistsException(String msg) {
        super(msg);
    }
}
