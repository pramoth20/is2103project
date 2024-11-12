/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB31/SingletonEjbClass.java to edit this template
 */
package ejb.session.singleton;

import ejb.session.stateless.EmployeeSessionBeanLocal;
import ejb.session.stateless.RoomTypeSessionBeanLocal;
import ejb.session.stateless.RoomRateSessionBeanLocal;
import ejb.session.stateless.RoomSessionBeanLocal;
import entity.Employee;
import entity.RoomType;
import entity.Rate;
import entity.Room;
import enums.EmployeeRole;
import enums.RateType;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;
import util.exception.RoomTypeNotFoundException;

/**
 *
 * @author pramoth
 */
@Singleton
@LocalBean
@Startup
public class DataInitSessionBean {

    @EJB
    private EmployeeSessionBeanLocal employeeSessionBeanLocal;

    @EJB
    private RoomTypeSessionBeanLocal roomTypeSessionBeanLocal;

    @EJB
    private RoomRateSessionBeanLocal roomRateSessionBeanLocal;

    @EJB
    private RoomSessionBeanLocal roomSessionBeanLocal;

    @PersistenceContext(unitName = "MerlionHotelSystem-ejbPU")
    private EntityManager em;

    @PostConstruct
    public void postConstruct() {
        try {
            initializeEmployeeData();
            initializeRoomTypeData();
            initializeRoomRateData();
            initializeRoomData();
        } catch (Exception e) {
            System.out.println("An error occurred during data initialization: " + e.getMessage());
        }
    }

    private void initializeEmployeeData() {
        employeeSessionBeanLocal.createEmployee("sysadmin", "password", EmployeeRole.SYSTEM_ADMINISTRATOR);
        employeeSessionBeanLocal.createEmployee("opmanager", "password", EmployeeRole.OPERATION_MANAGER);
        employeeSessionBeanLocal.createEmployee("salesmanager", "password", EmployeeRole.SALES_MANAGER);
        employeeSessionBeanLocal.createEmployee("guestrelo", "password", EmployeeRole.GUEST_RELATION_OFFICER);
    }

    private void initializeRoomTypeData() throws RoomTypeNotFoundException { //do we need the throw the exception
        RoomType deluxeRoom = roomTypeSessionBeanLocal.createRoomType("Deluxe Room");
        RoomType premierRoom = roomTypeSessionBeanLocal.createRoomType("Premier Room");
        RoomType familyRoom = roomTypeSessionBeanLocal.createRoomType("Family Room");
        RoomType juniorSuite = roomTypeSessionBeanLocal.createRoomType("Junior Suite");
        RoomType grandSuite = roomTypeSessionBeanLocal.createRoomType("Grand Suite");

        deluxeRoom.setNextRoomType(premierRoom);
        premierRoom.setNextRoomType(familyRoom);
        familyRoom.setNextRoomType(juniorSuite);
        juniorSuite.setNextRoomType(grandSuite);
        grandSuite.setNextRoomType(null); // Grand Suite is the highest

        roomTypeSessionBeanLocal.updateRoomType(deluxeRoom.getRoomTypeId(), deluxeRoom.getName());
        roomTypeSessionBeanLocal.updateRoomType(premierRoom.getRoomTypeId(), premierRoom.getName());
        roomTypeSessionBeanLocal.updateRoomType(familyRoom.getRoomTypeId(), familyRoom.getName());
        roomTypeSessionBeanLocal.updateRoomType(juniorSuite.getRoomTypeId(), juniorSuite.getName());
        roomTypeSessionBeanLocal.updateRoomType(grandSuite.getRoomTypeId(), grandSuite.getName());
    }

    private void initializeRoomRateData() {
        RoomType deluxeRoom = roomTypeSessionBeanLocal.createRoomType("Deluxe Room");
        RoomType premierRoom = roomTypeSessionBeanLocal.createRoomType("Premier Room");
        RoomType familyRoom = roomTypeSessionBeanLocal.createRoomType("Family Room");
        RoomType juniorSuite = roomTypeSessionBeanLocal.createRoomType("Junior Suite");
        RoomType grandSuite = roomTypeSessionBeanLocal.createRoomType("Grand Suite");

        roomRateSessionBeanLocal.createRate(new Rate("Deluxe Room Published", deluxeRoom, RateType.PUBLISHED, new BigDecimal("100")));
        roomRateSessionBeanLocal.createRate(new Rate("Deluxe Room Normal", deluxeRoom, RateType.NORMAL, new BigDecimal("50")));
        roomRateSessionBeanLocal.createRate(new Rate("Premier Room Published", premierRoom, RateType.PUBLISHED, new BigDecimal("200")));
        roomRateSessionBeanLocal.createRate(new Rate("Premier Room Normal", premierRoom, RateType.NORMAL, new BigDecimal("100")));
        roomRateSessionBeanLocal.createRate(new Rate("Family Room Published", familyRoom, RateType.PUBLISHED, new BigDecimal("300")));
        roomRateSessionBeanLocal.createRate(new Rate("Family Room Normal", familyRoom, RateType.NORMAL, new BigDecimal("150")));
        roomRateSessionBeanLocal.createRate(new Rate("Junior Suite Published", juniorSuite, RateType.PUBLISHED, new BigDecimal("400")));
        roomRateSessionBeanLocal.createRate(new Rate("Junior Suite Normal", juniorSuite, RateType.NORMAL, new BigDecimal("200")));
        roomRateSessionBeanLocal.createRate(new Rate("Grand Suite Published", grandSuite, RateType.PUBLISHED, new BigDecimal("500")));
        roomRateSessionBeanLocal.createRate(new Rate("Grand Suite Normal", grandSuite, RateType.NORMAL, new BigDecimal("250")));
    }

    private void initializeRoomData() {
        RoomType deluxeRoom = roomTypeSessionBeanLocal.createRoomType("Deluxe Room");
        RoomType premierRoom = roomTypeSessionBeanLocal.createRoomType("Premier Room");
        RoomType familyRoom = roomTypeSessionBeanLocal.createRoomType("Family Room");
        RoomType juniorSuite = roomTypeSessionBeanLocal.createRoomType("Junior Suite");
        RoomType grandSuite = roomTypeSessionBeanLocal.createRoomType("Grand Suite");

        // Creating Rooms
        roomSessionBeanLocal.createRoom(new Room(deluxeRoom, true, 1, 1, "0101", false, true));
        roomSessionBeanLocal.createRoom(new Room(deluxeRoom, true, 2, 1, "0201", false, true));
        roomSessionBeanLocal.createRoom(new Room(deluxeRoom, true, 3, 1, "0301", false, true));
        roomSessionBeanLocal.createRoom(new Room(deluxeRoom, true, 4, 1, "0401", false, true));
        roomSessionBeanLocal.createRoom(new Room(deluxeRoom, true, 5, 1, "0501", false, true));
        roomSessionBeanLocal.createRoom(new Room(premierRoom, true, 1, 2, "0102", false, true));
        roomSessionBeanLocal.createRoom(new Room(premierRoom, true, 2, 2, "0202", false, true));
        roomSessionBeanLocal.createRoom(new Room(premierRoom, true, 3, 2, "0302", false, true));
        roomSessionBeanLocal.createRoom(new Room(premierRoom, true, 4, 2, "0402", false, true));
        roomSessionBeanLocal.createRoom(new Room(premierRoom, true, 5, 2, "0502", false, true));
        roomSessionBeanLocal.createRoom(new Room(familyRoom, true, 1, 3, "0103", false, true));
        roomSessionBeanLocal.createRoom(new Room(familyRoom, true, 2, 3, "0203", false, true));
        roomSessionBeanLocal.createRoom(new Room(familyRoom, true, 3, 3, "0303", false, true));
        roomSessionBeanLocal.createRoom(new Room(familyRoom, true, 4, 3, "0403", false, true));
        roomSessionBeanLocal.createRoom(new Room(familyRoom, true, 5, 3, "0503", false, true));
        roomSessionBeanLocal.createRoom(new Room(juniorSuite, true, 1, 4, "0104", false, true));
        roomSessionBeanLocal.createRoom(new Room(juniorSuite, true, 2, 4, "0204", false, true));
        roomSessionBeanLocal.createRoom(new Room(juniorSuite, true, 3, 4, "0304", false, true));
        roomSessionBeanLocal.createRoom(new Room(juniorSuite, true, 4, 4, "0404", false, true));
        roomSessionBeanLocal.createRoom(new Room(juniorSuite, true, 5, 4, "0504", false, true));
        roomSessionBeanLocal.createRoom(new Room(grandSuite, true, 1, 5, "0105", false, true));
        roomSessionBeanLocal.createRoom(new Room(grandSuite, true, 2, 5, "0205", false, true));
        roomSessionBeanLocal.createRoom(new Room(grandSuite, true, 3, 5, "0305", false, true));
        roomSessionBeanLocal.createRoom(new Room(grandSuite, true, 4, 5, "0405", false, true));
        roomSessionBeanLocal.createRoom(new Room(grandSuite, true, 5, 5, "0505", false, true));
    }
}