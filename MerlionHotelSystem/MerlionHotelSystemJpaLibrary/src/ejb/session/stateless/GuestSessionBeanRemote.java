/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionRemote.java to edit this template
 */
package ejb.session.stateless;

import entity.Guest;
import entity.RoomType;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import javax.ejb.Remote;
import util.exception.GuestAlreadyExistException;
import util.exception.GuestNotFoundException;
import util.exception.InvalidDateException;
import util.exception.InvalidLoginCredentialException;

/**
 *
 * @author jwong
 */
@Remote
public interface GuestSessionBeanRemote {
    public Guest retrieveGuestByEmail(String email) throws GuestNotFoundException;
    
    public Long guestLogin(String email, String password) throws InvalidLoginCredentialException;
    
    public Long createGuest(Guest newGuest) throws GuestAlreadyExistException;

    public List<RoomType> searchHotelRooms(Date checkInDate, Date checkOutDate, int numberOfRooms) throws InvalidDateException, ParseException;
    
    public Guest retrieveGuestById(Long guestId) throws GuestNotFoundException;
    
}
