/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
// MainApp.java
package merlionhotelsystemjpaclient;

import java.util.Scanner;
import entity.Employee;
import ejb.session.stateless.*;
import entity.Partner;
import entity.Rate;
import entity.Reservation;
import entity.ReservationRoom;
import entity.Room;
import entity.RoomType;
import enums.EmployeeRole;
import enums.RateType;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.ejb.Schedule;
import util.exception.*;

public class MainApp {
    private EmployeeSessionBeanRemote employeeSessionBean;
    private RoomSessionBeanRemote roomSessionBean;
    private ReservationSessionBeanRemote reservationSessionBean;
    private RoomAllocationSessionBeanRemote roomAllocationSessionBean;
    private RoomTypeSessionBeanRemote roomTypeSessionBean;
    private RoomRateSessionBeanRemote roomRateSessionBean;
    private PartnerSessionBeanRemote partnerSessionBean;
    private ExceptionReportSessionBeanRemote exceptionReportSessionBeanRemote;
    
    private Employee loggedInEmployee;

    public MainApp(EmployeeSessionBeanRemote employeeSessionBean, RoomSessionBeanRemote roomSessionBean, ReservationSessionBeanRemote reservationSessionBean, 
                   RoomAllocationSessionBeanRemote roomAllocationSessionBean, RoomTypeSessionBeanRemote roomTypeSessionBean, RoomRateSessionBeanRemote roomRateSessionBean,
                   PartnerSessionBeanRemote partnerSessionBean, ExceptionReportSessionBeanRemote exceptionReportSessionBeanRemote) {
        this.employeeSessionBean = employeeSessionBean;
        this.roomSessionBean = roomSessionBean;
        this.reservationSessionBean = reservationSessionBean;
        this.roomAllocationSessionBean = roomAllocationSessionBean;
        this.roomTypeSessionBean = roomTypeSessionBean;
        this.roomRateSessionBean = roomRateSessionBean;
        this.partnerSessionBean = partnerSessionBean;
        this.exceptionReportSessionBeanRemote = exceptionReportSessionBeanRemote;
    }

    public void start() throws PartnerExistsException, RoomNotFoundException, UpdateRoomRateException, RoomRateNotFoundException, InputDataValidationException {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("---- Merlion Hotel Management System ----");
            System.out.print("Username: ");
            String username = scanner.nextLine();
            System.out.print("Password: ");
            String password = scanner.nextLine();

            try {
                loggedInEmployee = employeeSessionBean.login(username, password);
                showMenu();
            } catch (InvalidLoginCredentialException ex) {
                System.out.println("Invalid credentials. Please try again.");
            }
        }
    }

    public void showMenu() throws PartnerExistsException, RoomNotFoundException, UpdateRoomRateException, RoomRateNotFoundException, InputDataValidationException {
        EmployeeRole role = loggedInEmployee.getPosition();

        System.out.println("\nWelcome, " + loggedInEmployee.getUsername() + "!");
        System.out.println("Role: " + role);
        System.out.println("Select an action based on your role:");

        switch (role) {
            case SYSTEM_ADMINISTRATOR:
                try {
                    showAdminMenu();
                } catch (EmployeeNotFoundException ex) {
                    System.out.println("Error: " + ex.getMessage());
                }
                break;
            case OPERATION_MANAGER:
                try {
                    showOperationManagerMenu();
                } catch (EmployeeNotFoundException ex) {
                    System.out.println("Error: " + ex.getMessage());
                }
                break;
            case SALES_MANAGER:
                try {
                    showSalesManagerMenu();
                } catch (EmployeeNotFoundException ex) {
                    System.out.println("Error: " + ex.getMessage());
                }
                break;
            case GUEST_RELATION_OFFICER:
                try {
                    showGuestRelationsMenu();
                } catch (EmployeeNotFoundException ex) {
                    System.out.println("Error: " + ex.getMessage());
                }
                break;
            default:
                System.out.println("Invalid role. Access Denied.");
        }
    }
    
    private void showAdminMenu() throws PartnerExistsException, EmployeeNotFoundException {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("1. Create New Employee");
            System.out.println("2. View All Employees");
            System.out.println("3. Create New Partner");
            System.out.println("4. View All Partners");
            System.out.println("5. Employee Logout");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    createNewEmployee();
                    break;
                case 2:
                    viewAllEmployees();
                    break;
                case 3:
                    createNewPartner();
                    break;
                case 4:
                    viewAllPartners();
                    break;
                case 5:
                    employeeSessionBean.logout(loggedInEmployee);
                    System.out.println("You have been logged out.");
                    return;
                default:
                    System.out.println("Invalid option, please try again.");
            }
        }
    }

    private void showOperationManagerMenu() throws RoomNotFoundException, EmployeeNotFoundException {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("---- Operation Manager Menu ----");
            System.out.println("1. Create New Room Type");
            System.out.println("2. View Room Type Details");
            System.out.println("3. Update Room Type");
            System.out.println("4. Delete Room Type");
            System.out.println("5. View All Room Types");
            System.out.println("6. Create New Room");
            System.out.println("7. Update Room");
            System.out.println("8. Delete Room");
            System.out.println("9. View All Rooms");
            System.out.println("10. View Room Allocation Exception Report");
            System.out.println("11. Logout");

            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    createNewRoomType();
                    break;
                case 2:
                    viewRoomTypeDetails();
                    break;
                case 3:
                    updateRoomType();
                    break;
                case 4:
                    deleteRoomType();
                    break;
                case 5:
                    viewAllRoomTypes();
                    break;
                case 6:
                    createNewRoom();
                    break;
                case 7:
                    updateRoom();
                    break;
                case 8:
                    deleteRoom();
                    break;
                case 9:
                    viewAllRooms();
                    break;
                case 10:
                    viewRoomAllocationExceptionReport();
                    break;
                case 11:
                    employeeSessionBean.logout(loggedInEmployee);
                    System.out.println("You have been logged out.");
                    return;
                default:
                    System.out.println("Invalid option, please try again.");
            }
        }
    }

    private void showSalesManagerMenu() throws UpdateRoomRateException, InputDataValidationException, EmployeeNotFoundException {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("1. Create New Room Rate");
            System.out.println("2. Update Room Rate");
            System.out.println("3. Delete Room Rate");
            System.out.println("4. View All Room Rates");
            System.out.println("5. Employee Logout");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    createNewRoomRate();
                    break;
                case 2:
                    updateRoomRate();
                    break;
                case 3:
                    deleteRoomRate();
                    break;
                case 4:
                    viewRoomRateDetails();
                    break;
                case 5:
                    employeeSessionBean.logout(loggedInEmployee);
                    System.out.println("You have been logged out.");
                    return;
                default:
                    System.out.println("Invalid option, please try again.");
            }
        }
    }


    private void showGuestRelationsMenu() throws RoomRateNotFoundException, EmployeeNotFoundException {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("1. Walk-In Search Room");
            System.out.println("2. Walk-In Reserve Room");
            System.out.println("3. Check-In Guest");
            System.out.println("4. Check-Out Guest");
            System.out.println("5. Employee Logout");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    walkInSearchRoom();
                    break;
                case 2:
                    walkInReserveRoom();
                    break;
                case 3:
                    checkInGuest();
                    break;
                case 4:
                    checkOutGuest();
                    break;
                case 5:
                    employeeSessionBean.logout(loggedInEmployee);
                    System.out.println("You have been logged out.");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    
    /* The next 4 methods are for the SYSTEM_ADMINISTRATOR role, on top of each method, i will state the use case number and description*/
    
    //USE CASE 3: CREATE NEW EMPLOYEE
    private void createNewEmployee() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("---- Create New Employee ----");
        System.out.print("Enter Username: ");
        String username = scanner.nextLine();
        System.out.print("Enter Password: ");
        String password = scanner.nextLine();
        System.out.println("Select Employee Role:");
        for (EmployeeRole role : EmployeeRole.values()) {
            System.out.println(role.ordinal() + 1 + ". " + role);
        }

        int roleChoice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        if (roleChoice < 1 || roleChoice > EmployeeRole.values().length) {
            System.out.println("Invalid role choice. Employee not created.");
            return;
        }

        EmployeeRole role = EmployeeRole.values()[roleChoice - 1];
        Employee newEmployee = employeeSessionBean.createEmployee(username, password, role);
        System.out.println("Employee created successfully with ID: " + newEmployee.getEmployeeId());
    }

    //USE CASE 4: VIEW ALL EMPLOYEES
    private void viewAllEmployees() {
        System.out.println("---- View All Employees ----");
        List<Employee> employees = employeeSessionBean.retrieveAllEmployees();
        if (employees.isEmpty()) {
            System.out.println("No employees found.");
        } else {
            for (Employee employee : employees) {
                System.out.println("ID: " + employee.getEmployeeId() +
                                   ", Username: " + employee.getUsername() +
                                   ", Role: " + employee.getPosition());
            }
        }
    }

    //USE CASE 5: CREATE NEW PARTNER
    private void createNewPartner() throws PartnerExistsException {
        Scanner scanner = new Scanner(System.in);

        System.out.println("---- Create New Partner ----");
        System.out.print("Enter Partner Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Partner Email: ");
        String email = scanner.nextLine();
        System.out.print("Enter Partner Phone Number: ");
        String phoneNumber = scanner.nextLine();  
        System.out.print("Enter Partner password: ");
        String password = scanner.nextLine();

        Partner newPartner = new Partner(name, email, phoneNumber, password);
        Long partnerId = partnerSessionBean.createPartner(newPartner);
        System.out.println("Partner created successfully with ID: " + partnerId);
    }

    //USE CASE 6: VIEW ALL PARTNERS
    private void viewAllPartners() {
        System.out.println("---- View All Partners ----");
        List<Partner> partners = partnerSessionBean.retrieveAllPartners();
        if (partners.isEmpty()) {
            System.out.println("No partners found.");
        } else {
            for (Partner partner : partners) {
                System.out.println("ID: " + partner.getPartnerId() +
                                   ", Name: " + partner.getCompanyName() +
                                   ", Email: " + partner.getEmail());
            }
        }
    }
    
    /* The next 10 methods are for the OPERATION_MANAGER role, on top of each method, i will state the use case number and description*/
    
    //USE CASE 7: CREATE NEW ROOM TYPE
    private void createNewRoomType() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("---- Create New Room Type ----");
        System.out.print("Enter Room Type Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Room Description: ");
        String description = scanner.nextLine();
        System.out.print("Enter Room Size (e.g., in square meters): ");
        int size = scanner.nextInt();
        scanner.nextLine(); // consume newline
        System.out.print("Enter Bed Type (e.g., King, Queen): ");
        String bed = scanner.nextLine();
        System.out.print("Enter Room Capacity (number of people): ");
        int capacity = scanner.nextInt();
        scanner.nextLine(); // consume newline
        System.out.print("Enter Amenities (comma separated, e.g., WiFi, TV, Minibar): ");
        String amenities = scanner.nextLine();

        RoomType roomType = new RoomType(name, description, size, bed, capacity, amenities);
        RoomType newRoomType = roomTypeSessionBean.createRoomType(roomType);
        System.out.println("Room Type created successfully with ID: " + newRoomType.getRoomTypeId());
    }
    
    // USE CASE 8: VIEW ROOM TYPE DETAILS
    private void viewRoomTypeDetails() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("---- View Room Type Details ----");
        System.out.print("Enter Room Type ID: ");
        Long roomTypeId = scanner.nextLong();

        try {
            RoomType roomType = roomTypeSessionBean.getRoomTypeDetails(roomTypeId);
            System.out.println("Room Type ID: " + roomType.getRoomTypeId());
            System.out.println("Name: " + roomType.getName());
            System.out.println("Description: " + roomType.getDescription());
            System.out.println("Size: " + roomType.getSize() + " square meters");
            System.out.println("Bed Type: " + roomType.getBed());
            System.out.println("Capacity: " + roomType.getCapacity() + " people");
            System.out.println("Amenities: " + roomType.getAmenities());
            System.out.println("Is Available: " + roomType.getIsAvailable());
        } catch (RoomTypeNotFoundException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }
    
    
    // USE CASE 9: UPDATE ROOM TYPE
    private void updateRoomType() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("---- Update Room Type ----");
        System.out.print("Enter Room Type ID: ");
        Long roomTypeId = scanner.nextLong();
        scanner.nextLine(); // Consume newline

        System.out.print("Enter New Room Type Name (leave empty to keep current): ");
        String name = scanner.nextLine();
        System.out.print("Enter New Room Description (leave empty to keep current): ");
        String description = scanner.nextLine();
        System.out.print("Enter New Room Size (leave empty to keep current): ");
        String sizeInput = scanner.nextLine();
        System.out.print("Enter New Bed Type (leave empty to keep current): ");
        String bed = scanner.nextLine();
        System.out.print("Enter New Capacity (leave empty to keep current): ");
        String capacityInput = scanner.nextLine();
        System.out.print("Enter New Amenities (leave empty to keep current): ");
        String amenities = scanner.nextLine();

        try {
            RoomType roomType = roomTypeSessionBean.getRoomTypeDetails(roomTypeId);

            if (!name.isEmpty()) {
                roomType.setName(name);
            }
            if (!description.isEmpty()) {
                roomType.setDescription(description);
            }
            if (!sizeInput.isEmpty()) {
                roomType.setSize(Integer.parseInt(sizeInput));
            }
            if (!bed.isEmpty()) {
                roomType.setBed(bed);
            }
            if (!capacityInput.isEmpty()) {
                roomType.setCapacity(Integer.parseInt(capacityInput));
            }
            if (!amenities.isEmpty()) {
                roomType.setAmenities(amenities);
            }

            RoomType updatedRoomType = roomTypeSessionBean.updateRoomType(roomType);
            System.out.println("Room Type updated successfully: " + updatedRoomType.getName());
        } catch (RoomTypeNotFoundException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }
    
    // USE CASE 10: DELETE ROOM TYPE
    private void deleteRoomType() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("---- Delete Room Type ----");
        System.out.print("Enter Room Type ID: ");
        Long roomTypeId = scanner.nextLong();

        try {
            roomTypeSessionBean.deleteRoomType(roomTypeId);
            System.out.println("Room Type deleted successfully (or marked as disabled).");
        } catch (RoomTypeNotFoundException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }
    
    // USE CASE 11: VIEW ALL ROOM TYPES
    private void viewAllRoomTypes() {
        System.out.println("---- View All Room Types ----");
        List<RoomType> roomTypes = roomTypeSessionBean.retrieveAllRoomTypes();
        if (roomTypes.isEmpty()) {
            System.out.println("No room types found.");
        } else {
            for (RoomType roomType : roomTypes) {
                System.out.println("ID: " + roomType.getRoomTypeId() +
                                   ", Name: " + roomType.getName() +
                                   ", Description: " + roomType.getDescription() +
                                   ", Size: " + roomType.getSize() + " square meters" +
                                   ", Bed: " + roomType.getBed() +
                                   ", Capacity: " + roomType.getCapacity() +
                                   ", Amenities: " + roomType.getAmenities() +
                                   ", Disabled: " + roomType.getIsDisabled());
            }
        }
    }
    
    // USE CASE 12: CREATE NEW ROOM
    private void createNewRoom() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("---- Create New Room ----");

        // Display all available room types with their IDs
        List<RoomType> roomTypes = roomTypeSessionBean.retrieveAllRoomTypes();
        System.out.println("Available Room Types:");
        for (RoomType roomType : roomTypes) {
            System.out.println("ID: " + roomType.getRoomTypeId() + " - Name: " + roomType.getName());
        }

        System.out.print("Enter Room Type ID: ");
        Long roomTypeId = scanner.nextLong();
        scanner.nextLine(); // Consume newline

        RoomType roomType;
        try {
            roomType = roomTypeSessionBean.getRoomTypeDetails(roomTypeId);
        } catch (RoomTypeNotFoundException ex) {
            System.out.println("Error: " + ex.getMessage());
            return;
        }

        System.out.print("Enter Floor Number: ");
        int floorNumber = scanner.nextInt();
        System.out.print("Enter Sequence Number: ");
        int sequenceNumber = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        Room newRoom = new Room(roomType, true, floorNumber, sequenceNumber, null, false, true);
        try {
            Long roomId = roomSessionBean.createRoom(newRoom); // Capture the generated room ID
            System.out.println("Room created successfully with ID: " + roomId);
        } catch (IllegalArgumentException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }
    
    //USE CASE 13: UPDATE ROOM
    private void updateRoom() throws RoomNotFoundException {
        Scanner scanner = new Scanner(System.in);

        System.out.println("---- Update Room ----");
        System.out.print("Enter Room ID: ");
        Long roomId = scanner.nextLong();
        scanner.nextLine(); // Consume newline

        Room room = roomSessionBean.retrieveRoomById(roomId); //need fill up logic in room session bean
        if (room == null) {
            System.out.println("Room ID not found.");
            return;
        }

        System.out.print("Enter new Floor Number (leave blank to keep current): ");
        String floorInput = scanner.nextLine();
        System.out.print("Enter new Sequence Number (leave blank to keep current): ");
        String sequenceInput = scanner.nextLine();

        if (!floorInput.isEmpty()) {
            room.setFloorNumber(Integer.parseInt(floorInput));
        }
        if (!sequenceInput.isEmpty()) {
            room.setSequenceNumber(Integer.parseInt(sequenceInput));
        }

        roomSessionBean.updateRoom(room);
        System.out.println("Room updated successfully.");
    }
    
    //USE CASE 14: DELETE ROOM
    private void deleteRoom() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("---- Delete Room ----");
        System.out.print("Enter Room ID: ");
        Long roomId = scanner.nextLong();

        roomSessionBean.deleteRoom(roomId);
        System.out.println("Room deleted successfully.");
    }
    
    //USE CASE 15: VIEW ALL ROOMS
    private void viewAllRooms() {
        System.out.println("---- View All Rooms ----");
        List<Room> rooms = roomSessionBean.retrieveAllRooms();
        if (rooms.isEmpty()) {
            System.out.println("No rooms found.");
        } else {
            for (Room room : rooms) {
                System.out.println("ID: " + room.getRoomId() +
                                   ", Room Type: " + room.getRoomType().getName() +
                                   ", Room Number: " + room.getRoomNumber() +
                                   ", Status: " + (room.isRoomStatus() ? "Available" : "Unavailable"));
            }
        }
    }
    
    //USE CASE 16: VIEW ROOM ALLOCATION EXCEPTION REPORT
    private void viewRoomAllocationExceptionReport() {
        System.out.println("---- View Room Allocation Exception Report ----");
        // Logic for viewing room allocation exception report goes here
        System.out.println(".");
    }
    
     /* The next 5 methods are for the SALES_MANAGER role, on top of each method, i will state the use case number and description*/
    
    // USE CASE 17: CREATE NEW ROOM RATE
    private void createNewRoomRate() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("---- Create New Room Rate ----");
        System.out.print("Enter Room Rate Name: ");
        String name = scanner.nextLine();

        // Display all available room types with their IDs
        List<RoomType> roomTypes = roomTypeSessionBean.retrieveAllRoomTypes(); // Assuming there's a method to get all room types
        System.out.println("Available Room Types:");
        for (int i = 0; i < roomTypes.size(); i++) {
            System.out.println((i + 1) + ": " + roomTypes.get(i).getName());
        }

        System.out.print("Select Room Type (by number): ");
        int roomTypeIndex = scanner.nextInt() - 1;
        scanner.nextLine(); // consume newline

        if (roomTypeIndex < 0 || roomTypeIndex >= roomTypes.size()) {
            System.out.println("Invalid selection. Please try again.");
            return;
        }

        RoomType roomType = roomTypes.get(roomTypeIndex);

        // Display rate types
        System.out.println("Available Rate Types:");
        RateType[] rateTypes = RateType.values();
        for (int i = 0; i < rateTypes.length; i++) {
            System.out.println((i + 1) + ": " + rateTypes[i]);
        }

        System.out.print("Select Rate Type (by number): ");
        int rateTypeIndex = scanner.nextInt() - 1;
        scanner.nextLine(); // consume newline

        if (rateTypeIndex < 0 || rateTypeIndex >= rateTypes.length) {
            System.out.println("Invalid selection. Please try again.");
            return;
        }

        RateType rateType = rateTypes[rateTypeIndex];

        System.out.print("Enter Rate Per Night (Decimal Value): ");
        BigDecimal ratePerNight = scanner.nextBigDecimal();
        scanner.nextLine(); // consume newline

        System.out.print("Enter Start Date (yyyy-MM-dd) [Leave blank if not applicable]: ");
        String startDateInput = scanner.nextLine();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = null;

        if (!startDateInput.isEmpty()) {
            try {
                startDate = dateFormat.parse(startDateInput);
            } catch (ParseException e) {
                System.out.println("Invalid date format for start date. Please use yyyy-MM-dd.");
                return; // Exit the method if the date is invalid
            }
        }

        System.out.print("Enter End Date (yyyy-MM-dd) [Leave blank if not applicable]: ");
        String endDateInput = scanner.nextLine();
        Date endDate = null;

        if (!endDateInput.isEmpty()) {
            try {
                endDate = dateFormat.parse(endDateInput);
            } catch (ParseException e) {
                System.out.println("Invalid date format for end date. Please use yyyy-MM-dd.");
                return; // Exit the method if the date is invalid
            }
        }

        try {
            Rate rate = new Rate(name, roomType, rateType, ratePerNight, startDate, endDate);
            Long rateId = roomRateSessionBean.createRate(rate);
            System.out.println("Room Rate created successfully with ID: " + rateId);
        } catch (Exception ex) {
            System.out.println("An error occurred: " + ex.getMessage());
        }
    }
    
    //USE CASE 18: VIEW ROOM RATE DETAILS
    private void viewRoomRateDetails() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("---- View Room Rate Details ----");
        System.out.print("Enter Room Rate ID: ");
        Long rateId = scanner.nextLong();

        try {
            Rate rate = roomRateSessionBean.viewRoomRateDetails(rateId);
            System.out.println("Rate ID: " + rate.getRateId());
            System.out.println("Name: " + rate.getName());
            System.out.println("Rate Type: " + rate.getRateType());
            System.out.println("Rate Per Night: " + rate.getRatePerNight());
            System.out.println("Start Date: " + rate.getStartDate());
            System.out.println("End Date: " + rate.getEndDate());
            System.out.println("Is Disabled: " + rate.getIsDisabled());
        } catch (RoomRateNotFoundException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }
    
    
    // USE CASE 19: UPDATE ROOM RATE
    private void updateRoomRate() throws UpdateRoomRateException, InputDataValidationException {
        Scanner scanner = new Scanner(System.in);

        System.out.println("---- Update Room Rate ----");
        System.out.print("Enter Room Rate ID to Update: ");
        Long rateId = scanner.nextLong();
        scanner.nextLine();  // consume newline

        System.out.print("Enter Updated Room Rate Name [Leave blank if unchanged]: ");
        String name = scanner.nextLine();

        // Display all available room types with their IDs
        List<RoomType> roomTypes = roomTypeSessionBean.retrieveAllRoomTypes(); // Assuming there's a method to get all room types
        System.out.println("Available Room Types:");
        System.out.println("0: No Change");
        for (int i = 0; i < roomTypes.size(); i++) {
            System.out.println((i + 1) + ": " + roomTypes.get(i).getName());
        }

        System.out.print("Select Updated Room Type (by number) [0 if unchanged]: ");
        int roomTypeIndex = scanner.nextInt() - 1;
        scanner.nextLine();  // consume newline

        RoomType roomType = null;
        if (roomTypeIndex >= 0 && roomTypeIndex < roomTypes.size()) {
            roomType = roomTypes.get(roomTypeIndex);
        } else if (roomTypeIndex != -1) {
            System.out.println("Invalid selection. Please try again.");
            return;
        }

        // Display available rate types
        System.out.println("Available Rate Types:");
        System.out.println("0: No Change");
        RateType[] rateTypes = RateType.values();
        for (int i = 0; i < rateTypes.length; i++) {
            System.out.println((i + 1) + ": " + rateTypes[i]);
        }

        System.out.print("Select Updated Rate Type (by number) [0 if unchanged]: ");
        int rateTypeIndex = scanner.nextInt() - 1;
        scanner.nextLine(); // consume newline

        RateType rateType = null;
        if (rateTypeIndex >= 0 && rateTypeIndex < rateTypes.length) {
            rateType = rateTypes[rateTypeIndex];
        } else if (rateTypeIndex != -1) {
            System.out.println("Invalid selection. Please try again.");
            return;
        }

        System.out.print("Enter Updated Rate Per Night (Decimal Value) [Leave blank if unchanged]: ");
        String ratePerNightInput = scanner.nextLine();
        BigDecimal ratePerNight = ratePerNightInput.isEmpty() ? null : new BigDecimal(ratePerNightInput);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = null;
        Date endDate = null;

        System.out.print("Enter Updated Start Date (yyyy-MM-dd) [Leave blank if unchanged]: ");
        String startDateInput = scanner.nextLine();
        if (!startDateInput.isEmpty()) {
            try {
                startDate = dateFormat.parse(startDateInput);
            } catch (ParseException e) {
                System.out.println("Invalid start date format. Please use yyyy-MM-dd.");
                return; // Exit the method if the date is invalid
            }
        }

        System.out.print("Enter Updated End Date (yyyy-MM-dd) [Leave blank if unchanged]: ");
        String endDateInput = scanner.nextLine();
        if (!endDateInput.isEmpty()) {
            try {
                endDate = dateFormat.parse(endDateInput);
            } catch (ParseException e) {
                System.out.println("Invalid end date format. Please use yyyy-MM-dd.");
                return; // Exit the method if the date is invalid
            }
        }

        try {
            Rate existingRate = roomRateSessionBean.viewRoomRateDetails(rateId);

            // Update the attributes only if new values are provided
            if (!name.isEmpty()) {
                existingRate.setName(name);
            }
            if (roomType != null) {
                existingRate.setRoomType(roomType);
            }
            if (rateType != null) {
                existingRate.setRateType(rateType);
            }
            if (ratePerNight != null) {
                existingRate.setRatePerNight(ratePerNight);
            }
            if (startDate != null) {
                existingRate.setStartDate(startDate);
            }
            if (endDate != null) {
                existingRate.setEndDate(endDate);
            }

            // Update the room rate details
            roomRateSessionBean.updateRoomRateDetails(existingRate);
            System.out.println("Room Rate updated successfully: " + existingRate.getName());

        } catch (RoomRateNotFoundException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }
    
    //USE CASE 20: DELETE ROOM RATE
    private void deleteRoomRate() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("---- Delete Room Rate ----");
        System.out.print("Enter Room Rate ID to Delete: ");
        Long rateId = scanner.nextLong();

        try {
            roomRateSessionBean.deleteRoomRate(rateId);
            System.out.println("Room Rate successfully deleted or disabled.");
        } catch (RoomRateNotFoundException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }
    
    //USE CASE 21: VIEW ALL ROOM RATES
    private void viewAllRoomRates() {
        System.out.println("---- View All Room Rates ----");
        List<Rate> rates = roomRateSessionBean.retrieveAllRoomRates();

        for (Rate rate : rates) {
            System.out.println("Rate ID: " + rate.getRateId() + ", Name: " + rate.getName() + ", Rate Type: " + rate.getRateType() + ", Rate Per Night: " + rate.getRatePerNight());
        }
    }
    
    //USE CASE 22: ALLOCATE ROOMS TO RESERVATIONS
    @Schedule(hour = "2", minute = "0", info = "Daily Room Allocation Timer")
    public void allocateRoomReservationsToday() throws RoomAllocationException {
        Date today = new Date();
        roomAllocationSessionBean.allocateRoom(today);
    }
    
    /* The next 10 methods are for the GUEST_RELATION_OFFICER role, on top of each method, i will state the use case number and description*/
    
    //USE CASE 23: WALK IN SEARCH ROOM
    private void walkInSearchRoom() throws RoomRateNotFoundException {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter check-in date (yyyy-MM-dd): ");
        String checkInDateStr = scanner.nextLine();
        System.out.print("Enter check-out date (yyyy-MM-dd): ");
        String checkOutDateStr = scanner.nextLine();

        try {
            // Parse the dates
            Date checkInDate = new SimpleDateFormat("yyyy-MM-dd").parse(checkInDateStr);
            Date checkOutDate = new SimpleDateFormat("yyyy-MM-dd").parse(checkOutDateStr);

            // Retrieve all room types
            List<RoomType> roomTypes = roomTypeSessionBean.retrieveAllRoomTypes();
            boolean availableRoomFound = false;

            for (RoomType roomType : roomTypes) {
                // Search available rooms of the specific room type for given date range
                List<Room> availableRooms = roomSessionBean.searchAvailableRoomsForDates(roomType, checkInDate, checkOutDate);

                if (!availableRooms.isEmpty()) {
                    availableRoomFound = true;

                    // Display the available rooms for the room type
                    System.out.println("Room Type: " + roomType.getName() + ", Available Rooms: ");
                    for (Room room : availableRooms) {
                        System.out.println("- Room Number: " + room.getRoomNumber());

                        // Calculate reservation cost
                        BigDecimal ratePerNight = roomRateSessionBean.getPublishedRateForRoomType(roomType);
                        long numOfNights = (checkOutDate.getTime() - checkInDate.getTime()) / (1000 * 60 * 60 * 24);
                        BigDecimal totalAmount = ratePerNight.multiply(BigDecimal.valueOf(numOfNights));

                        System.out.println("  Rate Per Night: $" + ratePerNight);
                        System.out.println("  Total Amount for Stay: $" + totalAmount);
                    }
                }
            }

            if (!availableRoomFound) {
                System.out.println("No available rooms found for the given date range.");
            }
        } catch (ParseException ex) {
            System.out.println("Invalid date format. Please enter the date in yyyy-MM-dd format.");
        }
    }
    
    //USE CASE 24: WALK IN RESERVATION
    private void walkInReserveRoom() {
        Scanner scanner = new Scanner(System.in);

        try {
            System.out.println("---- Walk-In Reserve Room ----");

            // Get the room type for the reservation
            System.out.print("Enter Room Type Name to Reserve: ");
            String roomTypeName = scanner.nextLine();

            // Find Room Type by name
            RoomType roomType = roomTypeSessionBean.getRoomTypeDetailsByName(roomTypeName);
            if (roomType == null) {
                System.out.println("Room type not found.");
                return;
            }

            // Get reservation details from the user
            System.out.print("Enter check-in date (yyyy-MM-dd): ");
            String checkInDateInput = scanner.nextLine();
            Date checkInDate = new SimpleDateFormat("yyyy-MM-dd").parse(checkInDateInput);

            System.out.print("Enter check-out date (yyyy-MM-dd): ");
            String checkOutDateInput = scanner.nextLine();
            Date checkOutDate = new SimpleDateFormat("yyyy-MM-dd").parse(checkOutDateInput);

            // Get the number of rooms the guest wants to reserve
            System.out.print("Enter number of rooms to reserve: ");
            int numberOfRooms = scanner.nextInt();

            // Create reservation through the session bean
            Long reservationId = reservationSessionBean.createWalkInReservation(roomType, checkInDate, checkOutDate, numberOfRooms);

            // If the reservation is created successfully, print the details
            System.out.println("Reservation created successfully with ID: " + reservationId);
        } catch (RoomTypeNotFoundException e) {
            System.out.println("Room type not found: " + e.getMessage());
        } catch (RoomRateNotFoundException e) {
            System.out.println("Room rate not found: " + e.getMessage());
        } catch (ParseException e) {
            System.out.println("Invalid date format. Please use yyyy-MM-dd.");
        } catch (Exception e) {
            System.out.println("Failed to create reservation. " + e.getMessage());
        }
    }  
    
    //USE CASE 25: CHECK-IN GUEST
    private void checkInGuest() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter reservation ID for check-in: ");
        Long reservationId = scanner.nextLong();

        try {
            // Retrieve the reservation using the reservation ID
            Reservation reservation = reservationSessionBean.findReservation(reservationId);

            if (reservation != null && !reservation.isIsAllocated()) {
                // Check if rooms are allocated for the reservation
                for (ReservationRoom reservationRoom : reservation.getReservationRooms()) {
                    Room room = reservationRoom.getRoom();
                    if (room != null && room.getIsAvailable()) {
                        // Mark room as occupied and unavailable
                        room.setRoomStatus(true);
                        room.setIsAvailable(false);
                        roomSessionBean.updateRoom(room);
                        System.out.println("Checked in successfully. Room number: " + room.getRoomNumber());
                    } else {
                        throw new Exception("Room allocation exception - Unable to allocate room for reservation ID " + reservationId);
                    }
                }

                // Mark reservation as allocated
                reservation.setIsAllocated(true);
                reservationSessionBean.updateReservation(reservation);
            } else {
                System.out.println("Reservation not found or already allocated.");
            }
        } catch (Exception ex) {
            System.out.println("Failed to check in guest. " + ex.getMessage());
        }
    }
    
    //USE CASE 26: CHECK-OUT GUEST
    private void checkOutGuest() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter reservation ID for check-out: ");
        Long reservationId = scanner.nextLong();

        try {
            // Retrieve the reservation using the reservation ID
            Reservation reservation = reservationSessionBean.findReservation(reservationId);

            if (reservation != null && reservation.isIsAllocated()) {
                // Check out all rooms for this reservation
                for (ReservationRoom reservationRoom : reservation.getReservationRooms()) {
                    Room room = reservationRoom.getRoom();
                    if (room != null) {
                        // Mark room as available and not occupied
                        room.setRoomStatus(false);
                        room.setIsAvailable(true);
                        roomSessionBean.updateRoom(room);
                        System.out.println("Checked out successfully. Room number: " + room.getRoomNumber());
                    } else {
                        System.out.println("Room information is missing for reservation ID: " + reservationId);
                    }
                }

                // Mark reservation as no longer allocated
                reservation.setIsAllocated(false);
                reservationSessionBean.updateReservation(reservation);
            } else {
                System.out.println("Reservation not found or has not been checked in.");
            }
        } catch (Exception ex) {
            System.out.println("Failed to check out guest. " + ex.getMessage());
        }
    }
    
    private void logout() {
        try {
            employeeSessionBean.logout(loggedInEmployee);
            loggedInEmployee = null; // Explicitly clear the logged-in employee state
            System.out.println("Logged out successfully.");
        } catch (EmployeeNotFoundException ex) {
            System.out.println("Error during logout: " + ex.getMessage());
        } catch (IllegalStateException ex) {
            System.out.println("Error during logout: " + ex.getMessage());
        }
    }

}