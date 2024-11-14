/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/WebServices/EjbWebService.java to edit this template
 */
package ejb.session.ws;

import ejb.session.stateless.PartnerSessionBeanLocal;
import entity.Partner;
import entity.RoomType;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.ejb.Stateless;
import javax.jws.WebParam;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.exception.InvalidDateException;
import util.exception.InvalidPasswordException;
import util.exception.PartnerNotFoundException;

/**
 *
 * @author jwong
 */
@WebService(serviceName = "PartnerWebService")
@Stateless()
public class PartnerWebService {

    @PersistenceContext(unitName = "MerlionHotelSystem-ejbPU")
    private EntityManager em;

    
    @EJB
    private PartnerSessionBeanLocal partnerSessionBeanLocal;

    /**
     * This is a sample web service operation
     */
    @WebMethod(operationName = "login")
    public Partner login(
        @WebParam(name = "email") String email,
        @WebParam(name = "password") String password
    ) throws PartnerNotFoundException, InvalidPasswordException {
        
        // Perform login and retrieve the partner entity
        Partner partner = partnerSessionBeanLocal.login(email, password);
        return partner;
    }
    
    @WebMethod(operationName = "searchAvailableRoomsForPartner")
public List<RoomType> searchAvailableRoomsForPartner(
    @WebParam(name = "checkInDate") Date checkInDate,
    @WebParam(name = "checkOutDate") Date checkOutDate,
    @WebParam(name = "numberOfRooms") int numberOfRooms) throws InvalidDateException {
    
    List<RoomType> availableRoomTypes = partnerSessionBeanLocal.searchAvailableRoomsForPartner(checkInDate, checkOutDate, numberOfRooms);

    return availableRoomTypes;
}
    
}
