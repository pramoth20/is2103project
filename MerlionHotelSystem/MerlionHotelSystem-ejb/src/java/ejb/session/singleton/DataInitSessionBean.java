/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB31/SingletonEjbClass.java to edit this template
 */
package ejb.session.singleton;

import ejb.session.stateless.EmployeeSessionBeanLocal;
import entity.Employee;
import enums.EmployeeRole;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
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
            employeeSessionBeanLocal.createEmployee("John Doe", "password123", EmployeeRole.SYSTEM_ADMINISTRATOR);
            employeeSessionBeanLocal.createEmployee("Jane Smith", "password456", EmployeeRole.SYSTEM_ADMINISTRATOR);
            employeeSessionBeanLocal.createEmployee("Michael Johnson", "password789", EmployeeRole.OPERATION_MANAGER);
            employeeSessionBeanLocal.createEmployee("Emily Davis", "password321", EmployeeRole.SALES_MANAGER);
            employeeSessionBeanLocal.createEmployee("William Brown", "password654", EmployeeRole.GUEST_RELATION_OFFICER);
        }
    }
}

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    
    

