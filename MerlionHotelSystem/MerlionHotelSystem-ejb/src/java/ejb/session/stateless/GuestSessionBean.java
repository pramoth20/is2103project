/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
package ejb.session.stateless;

import entity.Guest;
import entity.RoomType;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import oracle.jrockit.jfr.parser.ParseException;
import util.exception.GuestAlreadyExistException;
import util.exception.GuestNotFoundException;
import util.exception.InvalidDateException;
import util.exception.InvalidLoginCredentialException;

/**
 *
 * @author jwong
 */
@Stateless
public class GuestSessionBean implements GuestSessionBeanRemote, GuestSessionBeanLocal {

    @PersistenceContext(unitName = "MerlionHotelSystem-ejbPU")
    private EntityManager em;
    
    @EJB
    private RoomAllocationSessionBeanLocal roomAllocationSessionBean;
    
    
    public Guest retrieveGuestById(Long guestId) throws GuestNotFoundException {
        Guest guest = em.find(Guest.class, guestId);
        
        if (guest == null) {
            throw new GuestNotFoundException("Guest ID " + guestId + " does not exist.");
        }
        
        return guest;
    }
    //retrieving email 
    @Override
    public Guest retrieveGuestByEmail(String email) throws GuestNotFoundException {
        Query query = em.createQuery("SELECT g FROM Guest g WHERE g.email = :email");
        query.setParameter("email", email);

        try {
            return (Guest) query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new GuestNotFoundException("Guest with email " + email + " does not exist!");
        }
    }

    //login method for guest
    @Override
    public Long guestLogin(String email, String password) throws InvalidLoginCredentialException {
        try {
            Guest guest = retrieveGuestByEmail(email);
            
            if (guest.getPassword().equals(password)) {
                // Return the guest if the password is correct
                return guest.getCustomerId();
            } else {
                throw new InvalidLoginCredentialException("Invalid email or password!");
            }
        } catch (GuestNotFoundException ex) {
            throw new InvalidLoginCredentialException("Invalid email or password!");
        }
    }
    
    //Create Guest 
    public Long createGuest(Guest newGuest) throws GuestAlreadyExistException {
        if (isGuestExists(newGuest.getEmail(), newGuest.getPhoneNumber())) {
            throw new GuestAlreadyExistException("Email or phone number already in use.");
        }

        try {
            em.persist(newGuest);
            em.flush(); // to ensure the ID is generated
            return newGuest.getCustomerId();
        } catch (PersistenceException ex) {
            throw new GuestAlreadyExistException("An unexpected error occurred while creating the guest: " + ex.getMessage());
        }
    }
    
    private boolean isGuestExists(String email, String phoneNumber) {
        Query query = em.createQuery("SELECT g FROM Guest g WHERE g.email = :email OR g.phoneNumber = :phoneNumber");
        query.setParameter("email", email);
        query.setParameter("phoneNumber", phoneNumber);

        try {
            query.getSingleResult();
            return true; // Guest exists
        } catch (NoResultException ex) {
            return false; // Guest does not exist
        }
    }
    
    @Override
    public List<RoomType> searchHotelRooms(Date checkInDate, Date checkOutDate, int numberOfRooms) throws InvalidDateException, java.text.ParseException {
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    //Date checkIn;
    //Date checkOut;
    //checkIn = dateFormat.parse(checkInDate);
    //checkOut = dateFormat.parse(checkOutDate);

    // Validate that check-in date is before check-out date
    if (!checkInDate.before(checkOutDate)) {
        throw new InvalidDateException("Check-out date must be after check-in date.");
    }

    
    return roomAllocationSessionBean.findAvailableRoomTypes(checkInDate, checkOutDate, numberOfRooms);
}
    
    
        
    
}
