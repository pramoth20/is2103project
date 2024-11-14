/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionRemote.java to edit this template
 */
package ejb.session.stateless;

import entity.Partner;
import entity.RoomType;
import java.util.Date;
import java.util.List;
import javax.ejb.Remote;
import util.exception.InvalidDateException;
import util.exception.InvalidPasswordException;
import util.exception.PartnerExistsException;
import util.exception.PartnerNotFoundException;

/**
 *
 * @author pramoth
 */
@Remote
public interface PartnerSessionBeanRemote {
    
    public Long createPartner(Partner partner) throws PartnerExistsException;
    public List<Partner> retrieveAllPartners();
    public Partner login(String email, String password) throws PartnerNotFoundException, InvalidPasswordException;
    
    public List<RoomType> searchAvailableRoomsForPartner(Date checkInDate, Date checkOutDate, int numberOfRooms) throws InvalidDateException;
 
}
