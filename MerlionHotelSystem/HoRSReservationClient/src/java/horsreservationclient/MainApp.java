/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package horsreservationclient;

import ejb.session.stateless.GuestSessionBeanRemote;
import ejb.session.stateless.ReservationSessionBeanRemote;
import ejb.session.stateless.RoomAllocationSessionBeanRemote;
import ejb.session.stateless.RoomRateSessionBeanRemote;
import entity.Guest;
import entity.Reservation;
import entity.ReservationRoom;
import entity.Room;
import entity.RoomType;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.exception.GuestAlreadyExistException;
import util.exception.GuestNotFoundException;
import util.exception.InvalidLoginCredentialException;
import util.exception.ReservationNotFoundException;
import util.exception.RoomAllocationException;

/**
 *
 * @author jwong
 */

//private 
public class MainApp {
    private GuestSessionBeanRemote guestSessionBeanRemote;
    private ReservationSessionBeanRemote reservationSessionBeanRemote;
    private RoomAllocationSessionBeanRemote roomAllocationSessionBeanRemote;
    private RoomRateSessionBeanRemote roomRateSessionBeanRemote;
    
    private boolean isLoggedIn = false; //The login status
    private Long loggedInGuestId = null;
    private Date checkInDate;
    private Date checkOutDate;
    private int numberOfRooms;
    private List<RoomType> availableRoomTypes = new ArrayList<>();
    private RoomType selectedRoomType;
    
    public MainApp() {
        
    }
    
    public MainApp(GuestSessionBeanRemote guestSessionBeanRemote, ReservationSessionBeanRemote reservationSessionBeanRemote, 
            RoomAllocationSessionBeanRemote roomAllocationSessionBeanRemote, RoomRateSessionBeanRemote roomRateSessionBeanRemote) {
    this.guestSessionBeanRemote = guestSessionBeanRemote;
    this.reservationSessionBeanRemote = reservationSessionBeanRemote;
    this.roomAllocationSessionBeanRemote = roomAllocationSessionBeanRemote;
    this.roomRateSessionBeanRemote = roomRateSessionBeanRemote;
    }
    
    public void runApp()  {
	Scanner scanner = new Scanner(System.in);
	Integer response = 0;

	while (true) {
            System.out.println("*** Welcome to HoRS Reservation Client System ***\n");

            // Display different options based on login status
            if (!isLoggedIn) {
                System.out.println("1: Guest Login");
                System.out.println("2: Register as Guest");
                System.out.println("3: Search Hotel Room");
                System.out.println("4: Exit\n");
            } else {
                System.out.println("1: Search Hotel Room");
                System.out.println("2: Reserve Hotel Room");
                System.out.println("3: View My Reservation Details");
                System.out.println("4: View All My Reservations");
                System.out.println("5: Exit\n");
            }

            System.out.print("> ");
            response = scanner.nextInt();

            if (!isLoggedIn) {
                switch (response) {
                    case 1:
                        guestLogin();
                        break;
                    case 2:
                        registerAsGuest();
                        break;
                    case 3:
                        searchHotelRoom();
                        break;
                    case 4:
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Invalid option. Please try again.");
                }
            } else {
                switch (response) {
                    case 1:
                        searchHotelRoom();
                        break;
                    case 2:
                    {
                        try {
                            reserveHotelRoom();
                        } catch (GuestNotFoundException ex) {
                            Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (ReservationNotFoundException ex) {
                            Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                        break;

                    case 3:
                        viewReservationDetails();
                        break;
                    case 4:
                        viewAllReservations();
                        break;
                    case 5:
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Invalid option. Please try again.");
                }
            }
            
        }
    }

    private void guestLogin()  {
    Scanner scanner = new Scanner(System.in);
    System.out.print("Enter email> ");
    String email = scanner.nextLine().trim();
    System.out.print("Enter password> ");
    String password = scanner.nextLine().trim();

    // Check for missing credentials
    /*if (email.isEmpty() || password.isEmpty()) {
        throw new InvalidLoginCredentialException("Missing login credential!");
    }*/

    try {
        loggedInGuestId = guestSessionBeanRemote.guestLogin(email, password);
        System.out.println("Login successful!");
        isLoggedIn = true;
    } catch (InvalidLoginCredentialException ex) {
        System.out.println("Invalid login: " + ex.getMessage());
    }
}

    private void registerAsGuest() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("*** HoRS Reservation Client :: Register as Guest ***\n");
        System.out.print("Enter Email: ");
        String email = scanner.nextLine().trim();

        System.out.print("Enter First Name> ");
        String firstName = scanner.nextLine().trim();

        System.out.print("Enter Last Name> ");
        String lastName = scanner.nextLine().trim();

        System.out.print("Enter Phone Number> ");
        String phoneNumber = scanner.nextLine().trim();

        System.out.print("Enter Password> ");
        String password = scanner.nextLine().trim();
       
        Guest newGuest = new Guest(email, firstName, lastName, phoneNumber, password);
        try {
            Long guestId = guestSessionBeanRemote.createGuest(newGuest);
            System.out.println("Registration successful! Your Guest ID is " + guestId);
        } catch (GuestAlreadyExistException ex) {
            System.out.println("Registration failed: " + ex.getMessage());
        }
        
    }

    private List<RoomType> searchHotelRoom() {
        Scanner scan = new Scanner(System.in);
        System.out.print("Enter check-in date (yyyy-MM-dd HH:mm)> ");
        String checkIn = scan.nextLine();
        System.out.print("Enter check-out date (yyyy-MM-dd HH:mm)> ");
        String checkOut = scan.nextLine();
        //scan.nextLine();
        System.out.print("Enter the number of rooms to book> ");
        numberOfRooms = scan.nextInt();
        scan.nextLine();
        
        //List<RoomType> availableRoomTypes = new ArrayList<>();
        
        try {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime checkInDateTime = LocalDateTime.parse(checkIn, formatter);
        LocalDateTime checkOutDateTime = LocalDateTime.parse(checkOut, formatter);
          
        checkInDate = Date.from(checkInDateTime.atZone(ZoneId.systemDefault()).toInstant());
        checkOutDate = Date.from(checkOutDateTime.atZone(ZoneId.systemDefault()).toInstant());
        availableRoomTypes = guestSessionBeanRemote.searchHotelRooms(checkInDate, checkOutDate, numberOfRooms);
        //availableRooms = guestSessionBeanRemote.searchHotelRooms(checkIn, c   heckOut);
        System.out.println("Available Room Types>");
        for (RoomType roomType : availableRoomTypes) {
            
            //BigDecimal totalAmount = RoomRateSessionBeanRemote.calculateTotalReservationAmount(roomType.getRoomTypeId(), LocalDate.parse(checkIn), LocalDate.parse(checkOut));
            BigDecimal totalAmount = reservationSessionBeanRemote.calculateTotalCostForOnlineReservation(roomType, checkInDate, checkOutDate, numberOfRooms);
            System.out.println("- " + roomType.getName() + " | Rate: " + roomType.getRoomRate() + " | Total Amount for Stay: " + totalAmount);
        }
        
        if (isLoggedIn) {
        System.out.print("Would you like to reserve a room now? (Type in 1: Yes, 2: No)> ");
        int proceed = scan.nextInt();
        scan.nextLine();
        
        if (proceed == 1) {
            reserveHotelRoom();
        } else {
            System.out.println("Returning to the main menu...");
        }
        }
    } catch (Exception e) {
        System.out.println("Error searching for rooms: " + e.getMessage());
        //e.printStackTrace();
    }
        return availableRoomTypes;
    }

    
    //Reserve hotel room
    private void reserveHotelRoom() throws GuestNotFoundException, ReservationNotFoundException {
         //List<RoomType> availableRoomTypes = searchHotelRoom();
         
         if (availableRoomTypes.isEmpty()) {
             System.out.println("No available rooms for the specified dates.");
             return; // Exit if there are no available rooms
    }
         
         /*List<String> validRoomTypes = Arrays.asList(
        "Deluxe room", "Premier room", "Family room", "Junior suite", "Grand suite"
    );*/
         Scanner scan = new Scanner(System.in);
         String roomTypeChoice;
    
         while (selectedRoomType == null) {
        System.out.print("Enter the room type you want to reserve: ");
        roomTypeChoice = scan.nextLine().trim();

        // Search for the matching RoomType in availableRoomTypes
        for (RoomType roomType : availableRoomTypes) {
            if (roomType.getName().equalsIgnoreCase(roomTypeChoice)) {
                selectedRoomType = roomType;
                break;
            }
        }

        if (selectedRoomType == null) {
            System.out.println("Invalid room type. Please select from the available options:");
            for (RoomType roomType : availableRoomTypes) {
                System.out.println("- " + roomType.getName());
            }
        }
    }

    //System.out.print("Enter number of rooms to reserve: ");
    //int numberOfRooms = scan.nextInt();
    LocalDate today = LocalDate.now();
    LocalTime currentTime = LocalTime.now();
    
    boolean isSameDayCheckIn = today.isEqual(today);
    boolean isAfter2am = currentTime.isAfter(LocalTime.of(2, 0));
    
        try {
            // Example call to reserve the selected room type

            //guestSessionBeanRemote.reserveRoom(roomTypeChoice, numberOfRooms);
            Guest guest = guestSessionBeanRemote.retrieveGuestById(loggedInGuestId);
            Long reservationId = reservationSessionBeanRemote.createReservationForOnline(
                    guest, selectedRoomType, checkInDate, checkOutDate, numberOfRooms);
            System.out.println("Reservation successful! This is your Reservation ID: " + reservationId);

            if (isSameDayCheckIn && isAfter2am) {
                Reservation reservation = reservationSessionBeanRemote.findReservation(reservationId);
                try {
                    roomAllocationSessionBeanRemote.allocateRoomImmediately(reservation);
                    System.out.println("Rooms allocated immediately for same-day check-in.");
                } catch (RoomAllocationException e) {
                    System.out.println("Immediate room allocation failed: " + e.getMessage());

                }
            } else {
            // Call the timer-based allocation method manually
            System.out.println("Triggering scheduled room allocation method for today.");
            try {
                roomAllocationSessionBeanRemote.allocateRoomReservationsToday();
            } catch (Exception e) {
                System.out.println("Error making reservation: " + e.getMessage());
        }



            }  
    } catch (Exception e) {
        System.out.println("An unexpected error occurred: " + e.getMessage());
    }
    }

    private void viewReservationDetails() {
        Scanner scan = new Scanner(System.in);
        System.out.print("Enter Reservation ID> ");
        Long reservationId = scan.nextLong();

        try {
            Reservation reservation = reservationSessionBeanRemote.findReservation(reservationId);
            System.out.println("Reservation Details:");
            System.out.println("Reservation ID: " + reservation.getReservationId());

            // Retrieve RoomType from the first ReservationRoom in the list (or display each if needed)
            if (!reservation.getReservationRooms().isEmpty()) {
                ReservationRoom reservationRoom = reservation.getReservationRooms().get(0);
                Room room = reservationRoom.getRoom();

                if (room != null) {
                    // Room is assigned, print the room number
                    System.out.println("Your room has room number: " + room.getRoomNumber());
                } else {
                    // Room is not assigned yet
                    System.out.println("Room not assigned yet.");
                }

                // Correctly display the room type name instead of the object reference
                RoomType roomType = reservation.getRoomType();
                if (roomType != null) {
                    System.out.println("Room Type: " + roomType.getName());
                } else {
                    System.out.println("Room Type: Not available");
                }
            }

            System.out.println("Check-in Date: " + reservation.getCheckInDate());
            System.out.println("Check-out Date: " + reservation.getCheckOutDate());
            System.out.println("Total Cost: " + reservation.getTotalCost());
        } catch (ReservationNotFoundException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }
    
    private void viewAllReservations() {
        try {
            List<Reservation> reservations = reservationSessionBeanRemote.getAllReservationsForGuest(loggedInGuestId);
            System.out.println("All Reservations:");
            for (Reservation reservation : reservations) {
                System.out.println("Reservation ID: " + reservation.getReservationId());

                // Traverse each ReservationRoom to get Room and RoomType details
                for (ReservationRoom reservationRoom : reservation.getReservationRooms()) {
                    Room room = reservationRoom.getRoom();
                    RoomType roomType = room.getRoomType();

                    // Display room type details properly
                    if (roomType != null) {
                        System.out.println("Room Type: " + roomType.getName());
                        System.out.println("Room Rate: " + roomType.getRoomRate().toString());
                    } else {
                        System.out.println("Room Type: Not available");
                    }
                }

                System.out.println("Check-in Date: " + reservation.getCheckInDate());
                System.out.println("Check-out Date: " + reservation.getCheckOutDate());
                System.out.println("Total Cost: " + reservation.getTotalCost());
                System.out.println("Allocated: " + (reservation.isIsAllocated() ? "Yes" : "No"));
                System.out.println("-----------------------------------");
            }
        } catch (Exception ex) {
            System.out.println("Error retrieving reservations: " + ex.getMessage());
        }
    }
}