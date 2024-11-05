/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.ExceptionReport;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author jwong
 */
@Stateless
public class ExceptionReportSessionBean implements ExceptionReportSessionBeanRemote, ExceptionReportSessionBeanLocal {

    @PersistenceContext(unitName = "MerlionHotelSystem-ejbPU")
    private EntityManager em;

    public ExceptionReport createExceptionReport(Date reportDate, Boolean isResolved, Date resolvedDate, ReservationRoom reservationRoom, String exceptionType) {
        ExceptionReport er = new ExceptionReport(reportDate, isResolved, resolvedDate, reservationRoom, exceptionType);
        em.persist(er);
        em.flush();
        return er;
    }
    
    public List<ExceptionReport> viewRoomAllocationExceptionReport() {
        Query query = em.createQuery("Select e FROM ExceptionReport e ");
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
        
    }
    

}

