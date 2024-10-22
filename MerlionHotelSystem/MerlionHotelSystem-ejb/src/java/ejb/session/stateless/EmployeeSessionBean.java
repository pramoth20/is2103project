/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.Employee;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author pramoth
 */
@Stateless
public class EmployeeSessionBean implements EmployeeSessionBeanRemote, EmployeeSessionBeanLocal {

    @PersistenceContext(unitName = "MerlionHotelSystem-ejbPU")
    private EntityManager em;

    public void persist(Object object) {
        em.persist(object);
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    // Method to create an Employee
//    @Override
//    public void createEmployee(String username, String password, String position) {
//        Employee employee = new Employee();
//        employee.setUsername(username);
//        employee.setPassword(password);
//        employee.setPosition(position);
//
//        em.persist(employee);
//    }
    
    
    public Long createEmployee(Employee employee) {
        em.persist(employee);
        em.flush();
        return employee.getEmployeeId();
    }  
    

    // Method to retrieve an Employee by ID
    @Override
    public Employee getEmployeeById(Long employeeId) {
        return em.find(Employee.class, employeeId);
    }
}
