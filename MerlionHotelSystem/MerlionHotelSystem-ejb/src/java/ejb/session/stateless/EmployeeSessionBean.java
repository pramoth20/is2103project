/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.Employee;
import enums.EmployeeRole;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.InvalidLoginCredentialException;
import util.exception.EmployeeNotFoundException;

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
    
    //Create employee
    public Employee createEmployee(String username, String password, EmployeeRole position) {
        Employee employee = new Employee(username, password, position);
        em.persist(employee);
        em.flush();
        return employee;
    }  
    

    // Method to retrieve an Employee by ID
    @Override
    public Employee getEmployeeById(Long employeeId) {
        return em.find(Employee.class, employeeId);
    }
    
    
    
    @Override
    public List<Employee> retrieveAllEmployees() {
    Query query = em.createQuery("SELECT e FROM Employee e");
    return query.getResultList();
    }
    
    public Employee retrieveEmployeeByUsername(String username) throws EmployeeNotFoundException {
        Query query = em.createQuery("SELECT e FROM Employee e WHERE e.username = :username");
        query.setParameter("username", username);
        
        try {
            return (Employee) query.getSingleResult();
        } catch(NoResultException ex){
            throw new EmployeeNotFoundException("Employee with username: " + username + " does not exist!");
        }

    }

    @Override
    public Employee login(String username, String password) throws InvalidLoginCredentialException {
        try {
            Employee employee = retrieveEmployeeByUsername(username);
            if (employee.getPassword().equals(password)) {
                 if (employee.isLoggedIn()) {
                throw new InvalidLoginCredentialException("Employee is already logged in!");
                 }
                 employee.setLoggedIn(true);
                 setEmployeeRole(employee);
                 return employee;
        } else {
            throw new InvalidLoginCredentialException("Username does not exist or invalid password!");
        }
        }
        catch (EmployeeNotFoundException ex) {
        throw new InvalidLoginCredentialException("Username does not exist or invalid password!");
        }                        
    }
    
    private void setEmployeeRole(Employee employee) {
        EmployeeRole role = employee.getPosition();
        
        switch (role) {
            case SYSTEM_ADMINISTRATOR:
                // Set permissions for system administrator
                break;
            case OPERATION_MANAGER:
                // Set permissions for operation manager
                break;
            case SALES_MANAGER:
                // Set permissions for sales manager
                break;
            case GUEST_RELATION_OFFICER:
                // Set permissions for guest relation officer
                break;
            default:
                throw new IllegalArgumentException("Unknown role: " + role);
    }
    }
    
    @Override
    public void employeeLogout(Employee employee) {
        if (employee.isLoggedIn()) {
            employee.setLoggedIn(false);
        } else {
            throw new IllegalStateException("Employee is not logged in!");
        }
    }
    
    
        
    
    
    
    
}
