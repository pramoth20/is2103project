/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB30/SessionLocal.java to edit this template
 */
package ejb.session.stateless;

import entity.Rate;
import entity.RoomType;
import enums.RateType;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;
import util.exception.RoomRateNotFoundException;

/**
 *
 * @author jwong
 */
@Local
public interface RoomRateSessionBeanLocal {
    public Rate createRoomRate(String name, RoomType roomType, RateType rateType, BigDecimal ratePerNight, Date startDate, Date endDate);
    public Rate viewRoomRateDetails(Long rateId) throws RoomRateNotFoundException;
    public Rate updateRoomRateDetails(Long rateId, String name, RoomType roomType, RateType rateType, BigDecimal ratePerNight, Date startDate, Date endDate) throws RoomRateNotFoundException;
    public void deleteRoomRate(Long rateId) throws RoomRateNotFoundException;
    public List<Rate> retrieveAllRoomRates();
}
