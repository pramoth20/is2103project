/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Exception.java to edit this template
 */
package util.exception;

/**
 *
 * @author jwong
 */
public class NoApplicableRateException extends Exception {

    /**
     * Creates a new instance of <code>NoApplicableRateException</code> without
     * detail message.
     */
    public NoApplicableRateException() {
    }

    /**
     * Constructs an instance of <code>NoApplicableRateException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public NoApplicableRateException(String msg) {
        super(msg);
    }
}
