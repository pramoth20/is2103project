/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionRemote.java to edit this template
 */
package ejb.session.stateless;

import entity.Employee;
import javax.ejb.Remote;

/**
 *
 * @author pramoth
 */
@Remote
public interface EmployeeSessionBeanRemote {
    public Employee getEmployeeById(Long employeeId);
    public Long createEmployee(Employee employee);
}
