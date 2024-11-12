/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionRemote.java to edit this template
 */
package ejb.session.stateless;

import entity.Employee;
import enums.EmployeeRole;
import java.util.List;
import javax.ejb.Remote;
import util.exception.EmployeeNotFoundException;
import util.exception.InvalidLoginCredentialException;

/**
 *
 * @author pramoth
 */
@Remote
public interface EmployeeSessionBeanRemote {
    public Employee getEmployeeById(Long employeeId);
    //public Long createEmployee(Employee employee);   
    public List<Employee> retrieveAllEmployees();
    
    public Employee retrieveEmployeeByUsername(String username) throws EmployeeNotFoundException;
    
    public Employee login(String username, String password) throws InvalidLoginCredentialException;
    
    public void employeeLogout(Employee employee);
    
    public Employee createEmployee(String username, String password, EmployeeRole position);
}
