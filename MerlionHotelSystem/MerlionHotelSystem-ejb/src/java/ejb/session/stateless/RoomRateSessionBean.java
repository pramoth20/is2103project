package ejb.session.stateless;

import entity.Rate;
import entity.RoomType;
import enums.RateType;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.RoomRateNotFoundException;

/**
 *
 * @author jwong
 */
@Stateless
public class RoomRateSessionBean implements RoomRateSessionBeanRemote, RoomRateSessionBeanLocal {

    @PersistenceContext(unitName = "MerlionHotelSystem-ejbPU")
    private EntityManager em;

    // Use Case 17: Create New Room Rate
    @Override
    public Rate createRoomRate(String name, RoomType roomType, RateType rateType, BigDecimal ratePerNight, Date startDate, Date endDate) {
        Rate roomRate = new Rate(name, roomType, rateType, ratePerNight, startDate, endDate);
        em.persist(roomRate);
        em.flush();
        return roomRate;
    }  
    
    // Use Case 18: View Room Rate Details
    @Override
    public Rate viewRoomRateDetails(Long rateId) throws RoomRateNotFoundException {
        Rate roomRate = em.find(Rate.class, rateId);
        if (roomRate == null) {
            throw new RoomRateNotFoundException("Room Rate with ID " + rateId + " cannot be found.");
        } 
        return roomRate;
    }
    
    // Use Case 19: Update Room Rate
    @Override
    public Rate updateRoomRateDetails(Long rateId, String name, RoomType roomType, RateType rateType, BigDecimal ratePerNight, Date startDate, Date endDate) throws RoomRateNotFoundException {
        Rate rate = em.find(Rate.class, rateId);
        if (rate == null) {
            throw new RoomRateNotFoundException("Rate with ID " + rateId + " not found.");
        }
        
        // Update the fields only if they are provided (not null)
        if (name != null) {
            rate.setName(name);
        }
        if (roomType != null) {
            rate.setRoomType(roomType);
        }
        if (rateType != null) {
            rate.setRateType(rateType);
        }
        if (ratePerNight != null) {
            rate.setRatePerNight(ratePerNight);
        }
        if (startDate != null) {
            rate.setStartDate(startDate);
        }
        if (endDate != null) {
            rate.setEndDate(endDate);
        }

        em.merge(rate);
        return rate;
    }   

    // Use Case 20: Delete Room Rate
    @Override
    public void deleteRoomRate(Long rateId) throws RoomRateNotFoundException {
        Rate roomRate = em.find(Rate.class, rateId);
        if (roomRate == null) {
            throw new RoomRateNotFoundException("Room Rate with ID " + rateId + " cannot be found.");
        } 

        // If the rate is in use, mark it as disabled. Otherwise, delete it.
        if (isRateInUse(roomRate)) {
            roomRate.setIsDisabled(true);  // Mark as disabled if it's in use
            em.merge(roomRate);  // Update to save disabled status
        } else {
            em.remove(roomRate);  // Delete if not in use
        }
    }

    // Helper method to check if the rate is currently in use by any reservation
    private boolean isRateInUse(Rate rate) {
        Query query = em.createQuery("SELECT r FROM Reservation r WHERE :rate MEMBER OF r.rates");
        query.setParameter("rate", rate);
        return !query.getResultList().isEmpty();
    }

    // Use Case 21: View All Room Rates
    @Override
    public List<Rate> retrieveAllRoomRates() {
        Query query = em.createQuery("SELECT r FROM Rate r");
        return query.getResultList();
    }
    
    @Override
    public BigDecimal getPublishedRateForRoomType(RoomType roomType) throws RoomRateNotFoundException {
        Query query = em.createQuery("SELECT r FROM Rate r WHERE r.roomType = :roomType AND r.rateType = :rateType AND r.isDisabled = false");
        query.setParameter("roomType", roomType);
        query.setParameter("rateType", RateType.PUBLISHED);

        try {
            Rate rate = (Rate) query.getSingleResult();
            return rate.getRatePerNight();
        } catch (NoResultException ex) {
            throw new RoomRateNotFoundException("Published rate for room type " + roomType.getName() + " cannot be found.");
        }
    }
}