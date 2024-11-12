/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionRemote.java to edit this template
 */
package ejb.session.stateless;

import entity.ExceptionReport;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author jwong
 */
@Remote
public interface ExceptionReportSessionBeanRemote {
    
    public List<ExceptionReport> getTodayUpgradeAvailableExceptions();
    
    public List<ExceptionReport> getTodayNoUpgradeAvailableExceptions();
    
    public void displayRoomAllocationExceptionReport();
}
