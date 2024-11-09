/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Exception.java to edit this template
 */
package util.exception;

/**
 *
 * @author jwong
 */
public class SaleTransactionNotFoundException extends Exception
{

    /**
     * Creates a new instance of <code>SaleTransactionNotFoundException</code>
     * without detail message.
     */
    public SaleTransactionNotFoundException() {
    }

    /**
     * Constructs an instance of <code>SaleTransactionNotFoundException</code>
     * with the specified detail message.
     *
     * @param msg the detail message.
     */
    public SaleTransactionNotFoundException(String msg) {
        super(msg);
    }
}
