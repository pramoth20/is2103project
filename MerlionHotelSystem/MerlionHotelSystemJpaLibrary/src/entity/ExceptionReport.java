/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author jwong
 */
@Entity
public class ExceptionReport implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reportId;
    
    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date reportDate;
    
    @Column(nullable = false)
    private Boolean isResolved;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date resolvedDate;
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "reservation_room_id",nullable = false)
    private ReservationRoom reservationRoom;
    
    @Column(nullable = false)
    private String exceptionType;

    public ExceptionReport() {
    }

    public ExceptionReport(Date reportDate, Boolean isResolved, Date resolvedDate, ReservationRoom reservationRoom, String exceptionType) {
        this.reportDate = reportDate;
        this.isResolved = isResolved;
        this.resolvedDate = resolvedDate;
        this.reservationRoom = reservationRoom;
        this.exceptionType = exceptionType;
    }
    
    
    
    public Long getReportId() {
        return reportId;
    }

    public void setReportId(Long id) {
        this.reportId = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (reportId != null ? reportId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the reportId fields are not set
        if (!(object instanceof ExceptionReport)) {
            return false;
        }
        ExceptionReport other = (ExceptionReport) object;
        if ((this.reportId == null && other.reportId != null) || (this.reportId != null && !this.reportId.equals(other.reportId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.ExceptionReport[ id=" + reportId + " ]";
    }

    /**
     * @return the reportDate
     */
    public Date getReportDate() {
        return reportDate;
    }

    /**
     * @param reportDate the reportDate to set
     */
    public void setReportDate(Date reportDate) {
        this.reportDate = reportDate;
    }

    /**
     * @return the isResolved
     */
    public Boolean getIsResolved() {
        return isResolved;
    }

    /**
     * @param isResolved the isResolved to set
     */
    public void setIsResolved(Boolean isResolved) {
        this.isResolved = isResolved;
    }

    /**
     * @return the resolvedDate
     */
    public Date getResolvedDate() {
        return resolvedDate;
    }

    /**
     * @param resolvedDate the resolvedDate to set
     */
    public void setResolvedDate(Date resolvedDate) {
        this.resolvedDate = resolvedDate;
    }

    /**
     * @return the exceptionType
     */
    public String getExceptionType() {
        return exceptionType;
    }

    /**
     * @param exceptionType the exceptionType to set
     */
    public void setExceptionType(String exceptionType) {
        this.exceptionType = exceptionType;
    }
    
}
