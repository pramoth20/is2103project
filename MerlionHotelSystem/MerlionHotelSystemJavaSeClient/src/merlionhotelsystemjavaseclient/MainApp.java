/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package merlionhotelsystemjavaseclient;

import java.util.List;
import java.util.Scanner;
import ws.partner.InvalidPasswordException_Exception;
import ws.partner.Partner;
import ws.partner.PartnerNotFoundException_Exception;
import ws.partner.PartnerWebService;
import ws.partner.PartnerWebService_Service;
import ws.partner.ReservationRoom;
import ws.partner.Room;
import ws.reservation.Reservation;
import ws.reservation.ReservationNotFoundException_Exception;
import ws.reservation.ReservationWebService;
import ws.reservation.ReservationWebService_Service;
import ws.reservation.RoomType;



/**
 *
 * @author jwong
 */
public class MainApp {
    private boolean isLoggedIn = false; // Track login status
    private Long loggedInPartnerId = null;

    public void runApp() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        while (true) {
            System.out.println("*** Welcome to HoRS Partner Reservation Client System ***\n");

            // Display different options based on login status
            if (!isLoggedIn) {
                System.out.println("1: Partner Login");
                System.out.println("2: Search Hotel Room");
                System.out.println("3: Exit\n");
            } else {
                System.out.println("1: Partner Search Room + Reserve Room");
                System.out.println("2: View Partner Reservation Details");
                System.out.println("3: View All Partner Reservations");
                System.out.println("4: Exit\n");
            }

            System.out.print("> ");
            response = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            if (!isLoggedIn) {
                switch (response) {
                    case 1:
                        partnerLogin();
                        break;
                    case 2:
                        searchHotelRoom();
                        break;
                    case 3:
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
                        viewReservationDetails();
                        break;
                    case 3:
                        viewAllReservations();
                        break;
                    case 4:
                        System.exit(0); // Exit the application
                        break;
                    default:
                        System.out.println("Invalid option. Please try again.");
                }
            }
        }
    }
    
    private void partnerLogin() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter companyName > ");
        String companyName = scanner.nextLine().trim();
        System.out.print("Enter password> ");
        String password = scanner.nextLine().trim();

        
        try {
            // Call the web service's login method
            PartnerWebService_Service service = new PartnerWebService_Service();
            PartnerWebService partnerWebService = service.getPartnerWebServicePort();
        
        // Call the login method using the instance
            Partner newPartner = partnerWebService.login(companyName, password);
          
            System.out.println("Login successful!");
            isLoggedIn = true;
        } catch (PartnerNotFoundException_Exception ex) {
            System.out.println("Invalid login: Partner not found - " + ex.getMessage());
        } catch (InvalidPasswordException_Exception ex) {
            System.out.println("Invalid login: Incorrect password - " + ex.getMessage());
        }
    }
    
    private void searchHotelRoom() {
        
    }
    private void reserveHotelRoom() {
      
    }

    private void viewReservationDetails() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter Reservation ID> ");
        Long reservationId = scanner.nextLong();
        
        ReservationWebService_Service service = new ReservationWebService_Service();
        ReservationWebService reservationWebService = service.getReservationWebServicePort();

        
        try {
        // Assuming `reservationWebService` is the instance of `ReservationWebService`
        Reservation reservation = reservationWebService.findReservation(reservationId);
        
        System.out.println("Reservation Details:");
        System.out.println("Reservation ID: " + reservation.getReservationId());
        System.out.println("Check-in Date: " + reservation.getCheckInDate());
        System.out.println("Check-out Date: " + reservation.getCheckOutDate());
        System.out.println("Total Cost: " + reservation.getTotalCost());
        
        // Display details of each room in the reservation
        if (!reservation.getReservationRooms().isEmpty()) {
            System.out.println("Rooms in Reservation:");
            for (ws.reservation.ReservationRoom reservationRoom : reservation.getReservationRooms()) {
                ws.reservation.Room room = reservationRoom.getRoom();
                RoomType roomType = room.getRoomType();
                System.out.println("- Room Type: " + roomType.getName() + ", Room Number: " + room.getRoomNumber());
            }
        } else {
            System.out.println("No rooms assigned to this reservation.");
        }
    } catch (ReservationNotFoundException_Exception ex) {
        System.out.println("Error: " + ex.getMessage());
    }
   
    }

    private void viewAllReservations() {
        try {
        // Assuming `reservationWebService` is already instantiated
        ReservationWebService_Service service = new ReservationWebService_Service();
        ReservationWebService reservationWebService = service.getReservationWebServicePort();
        List<Reservation> reservations = reservationWebService.getAllReservationsForPartner(loggedInPartnerId);
        
        if (reservations.isEmpty()) {
            System.out.println("No reservations found.");
        } else {
            System.out.println("All Reservations:");
            for (Reservation reservation : reservations) {
                System.out.println("Reservation ID: " + reservation.getReservationId());
                System.out.println("Check-in Date: " + reservation.getCheckInDate());
                System.out.println("Check-out Date: " + reservation.getCheckOutDate());
                System.out.println("Total Cost: " + reservation.getTotalCost());
                
                if (!reservation.getReservationRooms().isEmpty()) {
                    System.out.println("Rooms in Reservation:");
                    for (ws.reservation.ReservationRoom reservationRoom : reservation.getReservationRooms()) {
                        ws.reservation.Room room = reservationRoom.getRoom();
                        RoomType roomType = room.getRoomType();
                        System.out.println("- Room Type: " + roomType.getName() + ", Room Number: " + room.getRoomNumber());
                    }
                } else {
                    System.out.println("No rooms assigned to this reservation.");
                }
                System.out.println("-----------------------------------");
            }
        }
    } catch (Exception ex) {
        System.out.println("Error retrieving reservations: " + ex.getMessage());
    }
    }
    

    
    
    
    
    
}




    

