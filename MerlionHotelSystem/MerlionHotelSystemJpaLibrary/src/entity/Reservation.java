/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import enums.ReservationType;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

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
    
    @Temporal(TemporalType.DATE)
    private Date checkInDate;
    
    @Temporal(TemporalType.DATE)
    private Date checkOutDate;
    
    @Column(nullable = false, precision = 11, scale = 2)
    @NotNull
    @DecimalMin("0.00")
    @Digits(integer = 9, fraction = 2) // 11 digits total with 2 after the decimal point
    private BigDecimal totalCost;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReservationType reservationType;
    
    @ManyToMany
    @JoinTable(
        name = "Reservation_Rate",
        joinColumns = @JoinColumn(name = "reservation_id"),
        inverseJoinColumns = @JoinColumn(name = "rate_id")
    )
    private List<Rate> rates;
    
    
    //bidirectional reference to customer
    @ManyToOne
    private Customer customer;

//    unidirectionsal reference to reservation rooms
//    @OneToMany
//    @JoinColumn(nullable = false, name = "reservation_id")
//    private List<ReservationRoom> reservationRooms;
   
    //unidirectionsal reference to reservation rooms
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


    public Reservation(boolean isAllocated, Date reservationDate, Date checkInDate, Date checkOutDate, 
            Customer customer, BigDecimal totalCost, ReservationType reservationType) {
        this();
        this.isAllocated = isAllocated;
        this.reservationDate = reservationDate;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.customer = customer;
        this.totalCost = totalCost;
        this.reservationType = reservationType;
    }
    
    
    

    public Long getReservationId() {
        return reservationId;
    }

    public void setReservationId(Long reservationId) {
        this.reservationId = reservationId;
    }

    /**
     * @return the isAllocated
     */
    public boolean isIsAllocated() {
        return isAllocated;
    }

    /**
     * @param isAllocated the isAllocated to set
     */
    public void setIsAllocated(boolean isAllocated) {
        this.isAllocated = isAllocated;
    }
    
    
    /**
     * @return the reservationDate
     */
    public Date getReservationDate() {
        return reservationDate;
    }

    /**
     * @param reservationDate the reservationDate to set
     */
    public void setReservationDate(Date reservationDate) {
        this.reservationDate = reservationDate;
    }

    /**
     * @return the rates
     */
    public List<Rate> getRates() {
        return rates;
    }

    /**
     * @param rates the rates to set
     */
    public void setRates(List<Rate> rates) {
        this.rates = rates;
    }

    /**
     * @return the customer
     */
    public Customer getCustomer() {
        return customer;
    }

    /**
     * @param customer the customer to set
     */
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    /**
     * @return the reservationRooms
     */
    public List<ReservationRoom> getReservationRooms() {
        return reservationRooms;
    }

    /**
     * @param reservationRooms the reservationRooms to set
     */
    public void setReservationRooms(List<ReservationRoom> reservationRooms) {
        this.reservationRooms = reservationRooms;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getReservationId() != null ? getReservationId().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the reservationId fields are not set
        if (!(object instanceof Reservation)) {
            return false;
        }
        Reservation other = (Reservation) object;
        if ((this.getReservationId() == null && other.getReservationId() != null) || (this.getReservationId() != null && !this.reservationId.equals(other.reservationId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Reservation[ id=" + getReservationId() + " ]";
    }

    /**
     * @return the checkInDate
     */
    public Date getCheckInDate() {
        return checkInDate;
    }

    /**
     * @param checkInDate the checkInDate to set
     */
    public void setCheckInDate(Date checkInDate) {
        this.checkInDate = checkInDate;
    }

    /**
     * @return the checkOutDate
     */
    public Date getCheckOutDate() {
        return checkOutDate;
    }

    /**
     * @param checkOutDate the checkOutDate to set
     */
    public void setCheckOutDate(Date checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    /**
     * @return the totalCost
     */
    public BigDecimal getTotalCost() {
        return totalCost;
    }

    /**
     * @param totalCost the totalCost to set
     */
    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }

    /**
     * @return the reservationType
     */
    public ReservationType getReservationType() {
        return reservationType;
    }

    /**
     * @param reservationType the reservationType to set
     */
    public void setReservationType(ReservationType reservationType) {
        this.reservationType = reservationType;
    }
    
}
