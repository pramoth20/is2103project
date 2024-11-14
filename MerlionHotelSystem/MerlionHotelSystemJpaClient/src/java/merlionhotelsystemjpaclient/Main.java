/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
// Main.java
package merlionhotelsystemjpaclient;

import ejb.session.stateless.EmployeeSessionBeanRemote;
import ejb.session.stateless.ExceptionReportSessionBeanRemote;
import ejb.session.stateless.PartnerSessionBeanRemote;
import ejb.session.stateless.ReservationSessionBeanRemote;
import ejb.session.stateless.RoomAllocationSessionBeanRemote;
import ejb.session.stateless.RoomRateSessionBeanRemote;
import ejb.session.stateless.RoomSessionBeanRemote;
import ejb.session.stateless.RoomTypeSessionBeanRemote;
import javax.ejb.EJB;
import util.exception.InputDataValidationException;
import util.exception.PartnerExistsException;
import util.exception.RoomNotFoundException;
import util.exception.RoomRateNotFoundException;
import util.exception.UpdateRoomRateException;
import util.exception.EmployeeNotFoundException;

/**
 *
 * @author pramoth
 */
public class Main {

    @EJB
    private static EmployeeSessionBeanRemote employeeSessionBeanRemote;

    @EJB
    private static RoomSessionBeanRemote roomSessionBeanRemote;

    @EJB
    private static ReservationSessionBeanRemote reservationSessionBeanRemote;

    @EJB
    private static RoomAllocationSessionBeanRemote roomAllocationSessionBeanRemote;

    @EJB
    private static RoomTypeSessionBeanRemote roomTypeSessionBeanRemote;

    @EJB
    private static RoomRateSessionBeanRemote roomRateSessionBeanRemote;

    @EJB
    private static PartnerSessionBeanRemote partnerSessionBeanRemote;

    @EJB
    private static ExceptionReportSessionBeanRemote exceptionReportSessionBeanRemote;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            MainApp mainApp = new MainApp(employeeSessionBeanRemote, roomSessionBeanRemote, reservationSessionBeanRemote,
                    roomAllocationSessionBeanRemote, roomTypeSessionBeanRemote, roomRateSessionBeanRemote,
                    partnerSessionBeanRemote, exceptionReportSessionBeanRemote);
            mainApp.start();
        } catch (InputDataValidationException | PartnerExistsException | RoomNotFoundException | RoomRateNotFoundException | UpdateRoomRateException e) {
            System.out.println("An error occurred during startup: " + e.getMessage());
        }
    }
}