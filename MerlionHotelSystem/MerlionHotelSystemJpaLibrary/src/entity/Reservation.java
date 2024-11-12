/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author jwong
 */
@Entity
public class Reservation implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reservationId;
    
    private boolean isAllocated;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date reservationDate;
    
    @ManyToMany
    @JoinTable(
        name = "Reservation_Rate",
        joinColumns = @JoinColumn(name = "reservation_id"),
        inverseJoinColumns = @JoinColumn(name = "rate_id")
    )
    private List<Rate> rates;

    @ManyToOne
    private Customer customer;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinTable(
        name = "reservation_reservation_room", // Join table name
        joinColumns = @JoinColumn(name = "reservation_id"), // Foreign key to Reservation
        inverseJoinColumns = @JoinColumn(name = "reservation_room_id") // Foreign key to ReservationRoom
    )
    private List<ReservationRoom> reservationRooms;

    public Reservation() {
        rates = new ArrayList<>();
        reservationRooms = new ArrayList<>();
    }

    public Reservation(boolean isAllocated, Date reservationDate, Customer customer) {
        this();
        this.isAllocated = isAllocated;
        this.reservationDate = reservationDate;
        this.customer = customer;
    }

    // New constructor to handle RoomType parameter
    public Reservation(boolean isAllocated, Date reservationDate, RoomType roomType) {
        this();
        this.isAllocated = isAllocated;
        this.reservationDate = reservationDate;

        // Create a ReservationRoom object and associate it with the reservation
        if (roomType != null) {
            ReservationRoom reservationRoom = new ReservationRoom(roomType);
            this.reservationRooms.add(reservationRoom);
        }
    }

    // Getters and Setters for the fields
    public Long getReservationId() {
        return reservationId;
    }

    public void setReservationId(Long reservationId) {
        this.reservationId = reservationId;
    }

    public boolean isIsAllocated() {
        return isAllocated;
    }

    public void setIsAllocated(boolean isAllocated) {
        this.isAllocated = isAllocated;
    }

    public Date getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(Date reservationDate) {
        this.reservationDate = reservationDate;
    }

    public List<Rate> getRates() {
        return rates;
    }

    public void setRates(List<Rate> rates) {
        this.rates = rates;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public List<ReservationRoom> getReservationRooms() {
        return reservationRooms;
    }

    public void setReservationRooms(List<ReservationRoom> reservationRooms) {
        this.reservationRooms = reservationRooms;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (reservationId != null ? reservationId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Reservation)) {
            return false;
        }
        Reservation other = (Reservation) object;
        return (this.reservationId != null || other.reservationId == null) && (this.reservationId == null || this.reservationId.equals(other.reservationId));
    }

    @Override
    public String toString() {
        return "entity.Reservation[ id=" + reservationId + " ]";
    }
}