/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package horsreservationclient;

import ejb.session.stateless.GuestSessionBeanRemote;
import ejb.session.stateless.ReservationSessionBeanRemote;
import ejb.session.stateless.RoomAllocationSessionBeanRemote;
import ejb.session.stateless.RoomRateSessionBeanRemote;
import javax.ejb.EJB;
import util.exception.InvalidLoginCredentialException;

/**
 *
 * @author jwong
 */
public class Main {

    @EJB
    private static GuestSessionBeanRemote guestSessionBeanRemote;
    
    @EJB
    private static ReservationSessionBeanRemote reservationSessionBeanRemote;
    
    @EJB
    private static RoomAllocationSessionBeanRemote roomAllocationSessionBeanRemote;
    
    @EJB
    private static RoomRateSessionBeanRemote roomRateSessionBeanRemote;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)  {
        // TODO code application logic here
         MainApp mainApp = new MainApp(guestSessionBeanRemote, reservationSessionBeanRemote, 
            roomAllocationSessionBeanRemote, roomRateSessionBeanRemote);
         mainApp.runApp();
    }
    
}
