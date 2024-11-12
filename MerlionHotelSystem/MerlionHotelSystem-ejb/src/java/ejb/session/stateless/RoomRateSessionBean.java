package ejb.session.stateless;

import entity.Rate;
import entity.RoomType;
import enums.RateType;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Set;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.InputDataValidationException;
import util.exception.NoApplicableRateException;
import util.exception.RoomRateNotFoundException;
import util.exception.UpdateRoomRateException;

@Stateless
public class RoomRateSessionBean implements RoomRateSessionBeanRemote, RoomRateSessionBeanLocal {

    @PersistenceContext(unitName = "MerlionHotelSystem-ejbPU")
    private EntityManager em;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;
       

    /*public Rate createRate(String name, RoomType roomType, RateType rateType, BigDecimal ratePerNight, Date startDate, Date endDate) {
        Rate roomRate = new Rate(name, roomType, rateType, ratePerNight, startDate, endDate);
        em.persist(roomRate);
        em.flush();
        return roomRate;
    }  */
    
    public RoomRateSessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }
    
    @Override
    public Rate createRate(Rate rate) {
    em.persist(rate);
    em.flush();  // Ensure the rate is persisted and the ID is generated
    return rate;
}
    
    //View Room Details

    @Override
    public Rate viewRoomRateDetails(Long rateId) throws RoomRateNotFoundException {
        Rate roomRate = em.find(Rate.class, rateId);
        if (roomRate == null) {
            throw new RoomRateNotFoundException("Room Rate with ID " + rateId + " cannot be found.");
        }
        return roomRate;
    }
    
    @Override
    public Rate updateRoomRateDetails(Rate updatedRate) throws RoomRateNotFoundException, UpdateRoomRateException, InputDataValidationException {
        // Step 1: Basic validation to ensure `updatedRate` and its ID are present
        if (updatedRate != null && updatedRate.getRateId() != null) {
            // Step 2: Perform validation using the Validator
            Set<ConstraintViolation<Rate>> constraintViolations = validator.validate(updatedRate);
            
            if (constraintViolations.isEmpty()) {
                // Step 3: Retrieve existing rate entity from the database
                Rate rateToUpdate = em.find(Rate.class, updatedRate.getRateId());
                if (rateToUpdate == null) {
                    throw new RoomRateNotFoundException("Rate with ID " + updatedRate.getRateId() + " not found.");
                }
                
                if (!rateToUpdate.getRoomType().equals(updatedRate.getRoomType())) {
                throw new UpdateRoomRateException("Room type cannot be changed for an existing rate.");
            }

                // Step 4: Update the existing fields with values from updatedRate
                rateToUpdate.setName(updatedRate.getName());
                rateToUpdate.setRoomType(updatedRate.getRoomType());
                rateToUpdate.setRateType(updatedRate.getRateType());
                rateToUpdate.setRatePerNight(updatedRate.getRatePerNight());
                rateToUpdate.setStartDate(updatedRate.getStartDate());
                rateToUpdate.setEndDate(updatedRate.getEndDate());
                rateToUpdate.setIsDisabled(updatedRate.getIsDisabled());
                rateToUpdate.setIsAvailable(updatedRate.getIsAvailable());

                // Step 5: Merge the updated entity
                em.merge(rateToUpdate);
                return rateToUpdate;

            } else {
                // Step 6: Handle constraint violations
                throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
            }
        } else {
            // If updatedRate or its ID is null, throw an exception
            throw new RoomRateNotFoundException("Rate ID not provided for rate to be updated.");
        }
    }
    
    // Helper method to format validation errors into a readable message
    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<Rate>> constraintViolations) {
        StringBuilder msg = new StringBuilder("Input data validation error:");
        for (ConstraintViolation<Rate> violation : constraintViolations) {
            msg.append("\n").append(violation.getPropertyPath()).append(" - ").append(violation.getMessage());
        }
        return msg.toString();
    }
    
    
    
    //Delete RoomRate details
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
    
    //View All Room Rates   

    @Override
    public List<Rate> retrieveAllRoomRates() {
        Query query = em.createQuery("SELECT r FROM Rate r");
        return query.getResultList();
    }
    
    //Reservation rate for each day 
    @Override
    public BigDecimal getReservationRate(RoomType roomType, Date date) throws NoApplicableRateException {
        List<Rate> rates = roomType.getRoomRate();
        BigDecimal reservationRate = null;

        // Priority 1: Promotion Rate
        for (Rate rate : rates) {
            if (rate.getRateType() == RateType.PROMOTION && isDateWithinRange(rate, date)) {
                reservationRate = rate.getRatePerNight();
                break;
            }
        }

        // Priority 2: Peak Rate (if no Promotion Rate found)
        if (reservationRate == null) {
            for (Rate rate : rates) {
                if (rate.getRateType() == RateType.PEAK && isDateWithinRange(rate, date)) {
                    reservationRate = rate.getRatePerNight();
                    break;
                }
            }
        }

        // Priority 3: Normal Rate (if no Promotion or Peak Rate found)
        if (reservationRate == null) {
            for (Rate rate : rates) {
                if (rate.getRateType() == RateType.NORMAL) {
                    reservationRate = rate.getRatePerNight();
                    break;
                }
            }
        }

        // If no rate was found, throw an exception
        if (reservationRate == null) {
            throw new NoApplicableRateException("No applicable rate found for RoomType: " + roomType.getName() + " on date: " + date);
        }

        return reservationRate;
    }
    
    //Walk in rate per night
    @Override
    public BigDecimal getWalkInRate(RoomType roomType) throws RoomRateNotFoundException {
    List<Rate> rates = roomType.getRoomRate();

    for (Rate rate : rates) {
        if (rate.getRateType() == RateType.PUBLISHED) {
            // If a Published Rate is found, return it immediately
            return rate.getRatePerNight();
        }
    }

    // If no Published Rate is found in the list, throw an exception
    throw new RoomRateNotFoundException("No Published Rate found for room type: " + roomType.getName());
}
   
    
    
    private boolean isDateWithinRange(Rate rate, Date date) {
    Date startDate = rate.getStartDate();
    Date endDate = rate.getEndDate();

    // Check if the rate has a defined start and end date
    if (startDate != null && endDate != null) {
        return !date.before(startDate) && !date.after(endDate);  // date >= startDate && date <= endDate
    }

    // If there's no startDate and endDate, consider it invalid
    return false;
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