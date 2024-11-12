/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.ExceptionReport;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author jwong
 */
@Stateless
public class ExceptionReportSessionBean implements ExceptionReportSessionBeanRemote, ExceptionReportSessionBeanLocal {

    @PersistenceContext(unitName = "MerlionHotelSystem-ejbPU")
    private EntityManager em;

    /*public ExceptionReport createExceptionReport(Date reportDate, Boolean isResolved, Date resolvedDate, ReservationRoom reservationRoom, String exceptionType) {
        ExceptionReport er = new ExceptionReport(reportDate, isResolved, resolvedDate, reservationRoom, exceptionType);
        em.persist(er);
        em.flush();
        return er;
    }*/
    
    /*@Override
    public ExceptionReport createExceptionReport(ExceptionReport er) {
        em.persist(er);
        em.flush();
        return er;
    }
    
    //View all reports
    @Override
    public List<ExceptionReport> viewRoomAllocationExceptionReport() {
        Query query = em.createQuery("Select e FROM ExceptionReport e ");
        return query.getResultList();
    }
    
    //View unresolved reports
    public List<ExceptionReport> viewUnresolvedExceptionReports() {
    Query query = em.createQuery("SELECT e FROM ExceptionReport e WHERE e.isResolved = false");
    return query.getResultList();
    }
    
    public void resolveExceptionReport(Long exceptionReportId) throws Exception {
        ExceptionReport exceptionReport = em.find(ExceptionReport.class, exceptionReportId);
        if (exceptionReport == null) {
            throw new Exception("Exception Report with ID " + exceptionReportId + " not found.");
        }
        exceptionReport.setIsResolved(true);
        exceptionReport.setResolvedDate(new Date());  // Set the date the issue was resolved
        em.merge(exceptionReport); 
        
    }*/
    
     // Get today's Upgrade Available exceptions (Type 1)
    @Override
    public List<ExceptionReport> getTodayUpgradeAvailableExceptions() {
        Date startOfBatch = getTodayBatchStartTime();
        Date endOfBatch = new Date(); // Current time

        return em.createQuery("SELECT e FROM ExceptionReport e WHERE e.isResolved = false AND e.exceptionType = :type AND e.reportDate BETWEEN :startOfBatch AND :endOfBatch", ExceptionReport.class)
                .setParameter("type", "Upgrade Available")
                .setParameter("startOfBatch", startOfBatch)
                .setParameter("endOfBatch", endOfBatch)
                .getResultList();
    }

    // Get today's No Upgrade Available exceptions (Type 2)
    @Override
    public List<ExceptionReport> getTodayNoUpgradeAvailableExceptions() {
        Date startOfBatch = getTodayBatchStartTime();
        Date endOfBatch = new Date(); // Current time

        return em.createQuery("SELECT e FROM ExceptionReport e WHERE e.isResolved = false AND e.exceptionType = :type AND e.reportDate BETWEEN :startOfBatch AND :endOfBatch", ExceptionReport.class)
                .setParameter("type", "No Upgrade Available")
                .setParameter("startOfBatch", startOfBatch)
                .setParameter("endOfBatch", endOfBatch)
                .getResultList();
    }

    // Helper method to get the start time for today's batch (2 am)
    private Date getTodayBatchStartTime() {
        Calendar startOfBatch = Calendar.getInstance();
        startOfBatch.set(Calendar.HOUR_OF_DAY, 2);
        startOfBatch.set(Calendar.MINUTE, 0);
        startOfBatch.set(Calendar.SECOND, 0);
        startOfBatch.set(Calendar.MILLISECOND, 0);
        return startOfBatch.getTime();
    }
    
    @Override
    public void displayRoomAllocationExceptionReport() {
    List<ExceptionReport> upgradeAvailableReports = getTodayUpgradeAvailableExceptions();
    List<ExceptionReport> noUpgradeAvailableReports = getTodayNoUpgradeAvailableExceptions();

    System.out.println("Room Allocation Exception Report for Today:");

    // Display Type 1: Upgrade Available
    System.out.println("\nType 1: No available room for reserved room type but upgrade to next higher room type is available:");
    for (ExceptionReport exception : upgradeAvailableReports) {
        System.out.println("Report ID: " + exception.getReportId());
        System.out.println("Date: " + exception.getReportDate());
        // Additional details can be printed here
    }

    // Display Type 2: No Upgrade Available
    System.out.println("\nType 2: No available room for reserved room type and no upgrade to next higher room type is available:");
    for (ExceptionReport exception : noUpgradeAvailableReports) {
        System.out.println("Report ID: " + exception.getReportId());
        System.out.println("Date: " + exception.getReportDate());
        // Additional details can be printed here
    }
}
    
    
    

}

