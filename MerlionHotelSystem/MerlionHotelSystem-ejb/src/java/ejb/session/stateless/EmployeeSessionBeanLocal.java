/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package ejb.session.stateless;

import entity.Employee;
import enums.EmployeeRole;
import java.util.List;
import javax.ejb.Local;
import util.exception.EmployeeNotFoundException;
import util.exception.InvalidLoginCredentialException;

/**
 *
 * @author pramoth
 */
@Local
public interface EmployeeSessionBeanLocal {

    public Employee getEmployeeById(Long employeeId);

    //public Long createEmployee(Employee employee);

    public List<Employee> retrieveAllEmployees();

    public Employee login(String username, String password) throws InvalidLoginCredentialException;

    public Employee retrieveEmployeeByUsername(String username) throws EmployeeNotFoundException;

    public void employeeLogout(Employee employee);

    public Employee createEmployee(String username, String password, EmployeeRole position);
    
}
