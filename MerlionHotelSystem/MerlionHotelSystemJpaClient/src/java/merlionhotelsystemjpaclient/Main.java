/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package merlionhotelsystemjpaclient;

import ejb.session.stateless.EmployeeSessionBeanRemote;
import entity.Employee;
import java.util.List;
import javax.ejb.EJB;

/**
 *
 * @author pramoth
 */
public class Main {

    @EJB
    private static EmployeeSessionBeanRemote employeeSessionBeanRemote;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
         List<Employee> employee = employeeSessionBeanRemote.retrieveAllEmployees();
         for(Employee e: employee) {
             //System.out.println(e + " " +  e.getEmployeeId());
             System.out.println(e);
         }
             
        //Employee employee = employeeSessionBeanRemote.getEmployeeById(1l);
        
        //System.out.println(employee.getEmployeeId());
        
        
        
    }
}
