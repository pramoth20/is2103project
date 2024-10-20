/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB31/SingletonEjbClass.java to edit this template
 */
package ejb.session.singleton;

import ejb.session.stateless.EmployeeSessionBeanLocal;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author pramoth
 */
@Singleton
@LocalBean
@Startup
public class DataInitSessionBean {

    @EJB
    private EmployeeSessionBeanLocal employeeSessionBeanLocal;

    @PersistenceContext(unitName = "MerlionHotelSystem-ejbPU")
    private EntityManager em;

    public void persist(Object object) {
        em.persist(object);
    }
    
    @PostConstruct
    public void postConstruct() {
        if(em.find(Employee.class, 1l) == null) {
            employeeSessionBeanLocal.createEmployee(new Employee("John Doe", "password123", "Manager"));
            employeeSessionBeanLocal.createEmployee(new Employee("Jane Smith", "password456", "Receptionist"));
            employeeSessionBeanLocal.createEmployee(new Employee("Michael Johnson", "password789", "Housekeeping"));
            employeeSessionBeanLocal.createEmployee(new Employee("Emily Davis", "password321", "Chef"));
            employeeSessionBeanLocal.createEmployee(new Employee("William Brown", "password654", "Security"));
        }
    }//not too sure
}

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    
    

