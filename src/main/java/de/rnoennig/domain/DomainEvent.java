package de.rnoennig.domain;

import de.rnoennig.service.EventService;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.time.Instant;
import java.util.Date;

@MappedSuperclass
public abstract class DomainEvent {

    private Date occured;

    private Date recorded;

    public DomainEvent() {
        this.recorded = Date.from(Instant.now());
    }

    @Column(name = "occured")
    public Date getOccured() {
        return occured;
    }

    public void setOccured(Date occured) {
        this.occured = occured;
    }

    public Date getRecorded() {
        return recorded;
    }

    public void setRecorded(Date recorded) {
        this.recorded = recorded;
    }

    public abstract void process(EventService eventService);
}
