/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/WebServices/EjbWebService.java to edit this template
 */
package ejb.session.ws;

import ejb.session.stateless.ReservationSessionBeanLocal;
import entity.Customer;
import entity.Reservation;
import entity.RoomType;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.exception.ReservationNotFoundException;

/**
 *
 * @author jwong
 */
@WebService(serviceName = "ReservationWebService")
@Stateless()
public class ReservationWebService {

    @EJB(name = "ReservationSessionBeanLocal")
    private ReservationSessionBeanLocal reservationSessionBeanLocal;

    @PersistenceContext(unitName = "MerlionHotelSystem-ejbPU")
    private EntityManager em;
    
    /**
     * This is a sample web service operation
     */
    
    @WebMethod(operationName = "createReservationForOnline")
    public Long createReservationForOnline(
        @WebParam(name = "customer") Customer customer,
        @WebParam(name = "roomType") RoomType roomType,
        @WebParam(name = "checkInDate") Date checkInDate,
        @WebParam(name = "checkOutDate") Date checkOutDate,
        @WebParam(name = "numberOfRooms") int numberOfRooms
    ) {
        // Create the reservation and persist it
        Long reservationId = reservationSessionBeanLocal.createReservationForOnline(
            customer, roomType, checkInDate, checkOutDate, numberOfRooms
        );
        
        // Detach customer and roomType entities to avoid cyclic references
        em.detach(customer);
        customer.setReservations(null);

        return reservationId;
    }
    
    @WebMethod(operationName = "getAllReservationsForPartner")
public List<Reservation> getAllReservationsForPartner(@WebParam(name = "partnerId") Long partnerId) {
    return reservationSessionBeanLocal.getAllReservationsForPartner(partnerId);
}

@WebMethod(operationName = "findReservation")
public Reservation findReservation(@WebParam(name = "reservationId") Long reservationId) throws ReservationNotFoundException {
    return reservationSessionBeanLocal.findReservation(reservationId);
}

@WebMethod(operationName = "calculateTotalCostForOnlineReservation")
    public BigDecimal calculateTotalCostForOnlineReservation(
            @WebParam(name = "roomType") RoomType roomType,
            @WebParam(name = "checkInDate") Date checkInDate,
            @WebParam(name = "checkOutDate") Date checkOutDate,
            @WebParam(name = "numberOfRooms") int numberOfRooms) {
        
        return reservationSessionBeanLocal.calculateTotalCostForOnlineReservation(roomType, checkInDate, checkOutDate, numberOfRooms);
    }

}
