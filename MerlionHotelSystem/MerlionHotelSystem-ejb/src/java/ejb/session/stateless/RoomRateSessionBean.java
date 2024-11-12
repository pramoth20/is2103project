/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/StatelessEjbClass.java to edit this template
 */
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

//for bean validation in the future
/* 
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set; 
*/


@Stateless
public class RoomRateSessionBean implements RoomRateSessionBeanRemote, RoomRateSessionBeanLocal {

    @PersistenceContext(unitName = "MerlionHotelSystem-ejbPU")
    private EntityManager em;
    
    //for bean validation in the future
    /*    private final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = validatorFactory.getValidator();

    // Create room rate with validation
    public Rate createRoomRate(String name, RoomType roomType, RateType rateType, BigDecimal ratePerNight, Date startDate, Date endDate) {
        Rate roomRate = new Rate(name, roomType, rateType, ratePerNight, startDate, endDate);
        
        Set<ConstraintViolation<Rate>> violations = validator.validate(roomRate);
        if (!violations.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (ConstraintViolation<Rate> violation : violations) {
                sb.append(violation.getMessage()).append("\n");
            }
            throw new IllegalArgumentException("Room rate validation failed: " + sb.toString());
        }
        
        em.persist(roomRate);
        em.flush();
        return roomRate;
    }*/

    //create room rate
    @Override
    public Rate createRoomRate(String name, RoomType roomType, RateType rateType, BigDecimal ratePerNight, Date startDate, Date endDate) {
        Rate roomRate = new Rate(name, roomType, rateType, ratePerNight, startDate, endDate);
        em.persist(roomRate);
        em.flush();
        return roomRate;
    }  
    
    //View Room Details
    @Override
    public Rate viewRoomRateDetails(Long rateId) throws RoomRateNotFoundException {
       Rate roomRate = em.find(Rate.class, rateId);
       if (roomRate == null) {
           throw new RoomRateNotFoundException("Room Rate of Id " + rateId + " cannnot be found");
       } 
       return roomRate;
    }
    
    @Override
    public Rate updateRoomRateDetails(Long rateId, String name, RoomType roomType, RateType rateType, BigDecimal ratePerNight, Date startDate, Date endDate) throws RoomRateNotFoundException {
        Rate rate = em.find(Rate.class, rateId);
        if (rate == null) {
            throw new RoomRateNotFoundException("Rate ID " + rateId + " not found.");
        }

        // If the rate is disabled, update is not allowed (depending on business logic)
        if (rate.getIsDisabled()) {
            throw new IllegalStateException("Cannot update a disabled rate.");
        }

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
    
    //Delete RoomRate details
    @Override
    public void deleteRoomRate(Long rateId) throws RoomRateNotFoundException {
        Rate roomRate = em.find(Rate.class, rateId);
        if (roomRate == null) {
            throw new RoomRateNotFoundException("Room Rate with ID " + rateId + " cannot be found.");
        }
        // Check if the rate is in use
        if (isRateInUse(roomRate)) {
            roomRate.setIsDisabled(true);  // Mark as disabled if it's in use
            em.merge(roomRate);  // Update to save the disabled status
        } else {
            em.remove(roomRate);  // Delete if not in use
        }
    }

    // Helper method to check if the rate is in use
    private boolean isRateInUse(Rate rate) {
        Query query = em.createQuery("SELECT r FROM Reservation r WHERE :rate MEMBER OF r.rates");
        query.setParameter("rate", rate);
        return !query.getResultList().isEmpty();
    }
    
    //View All Room Rates
    @Override
    public List<Rate> retrieveAllRoomRates() {
        Query query = em.createQuery("SELECT r FROM Rate r");
        return query.getResultList();
    }

   
    
}
