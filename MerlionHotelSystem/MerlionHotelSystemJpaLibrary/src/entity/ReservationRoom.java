/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 *
 * @author pramoth
 */
@Entity
public class ReservationRoom implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reservationRoomId;
    
//    @ManyToOne(optional = false)
//    private Reservation reservation;
    
    //unidirectional relationship with room
    @ManyToOne(optional = false)
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;
    
    
    @Column(nullable = false)
    private int numberOfNights;
    
    @OneToMany(cascade = {/*CascadeType.PERSIST, */CascadeType.REMOVE}, orphanRemoval = true)
    @JoinTable(
        name = "reservationroom_exceptionreport", // Join table name
        joinColumns = @JoinColumn(name = "reservation_room_id"), // FK column in join table referring to ReservationRoom
        inverseJoinColumns = @JoinColumn(name = "exception_report_id") // FK column in join table referring to ExceptionReport
    )
    private List<ExceptionReport> exceptionReports;
    
    
    public ReservationRoom() {
        exceptionReports = new ArrayList<>();
    }

    public ReservationRoom(Room room, int numberOfNights) {
        this();
        this.room = room;
        this.numberOfNights = numberOfNights;
    }

    // Getters and Setters
    public Long getReservationRoomId() {
        return reservationRoomId;
    }

    public void setReservationRoomId(Long reservationRoomId) {
        this.reservationRoomId = reservationRoomId;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public int getNumberOfNights() {
        return numberOfNights;
    }
    
    public void setNumberOfNights(int numberOfNights) {
        this.numberOfNights = numberOfNights;
    }
    
    
    /**
     * @return the exceptionReports
     */
    public List<ExceptionReport> getExceptionReports() {
        return exceptionReports;
    }

    /**
     * @param exceptionReports the exceptionReports to set
     */
    public void setExceptionReports(List<ExceptionReport> exceptionReports) {
        this.exceptionReports = exceptionReports;
    }
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (reservationRoomId != null ? reservationRoomId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the reservationRoomId fields are not set
        if (!(object instanceof ReservationRoom)) {
            return false;
        }
        ReservationRoom other = (ReservationRoom) object;
        if ((this.reservationRoomId == null && other.reservationRoomId != null) || (this.reservationRoomId != null && !this.reservationRoomId.equals(other.reservationRoomId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.ReservationRoom[ id=" + reservationRoomId + " ]";
    }

}