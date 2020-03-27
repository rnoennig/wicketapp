package de.rnoennig.domain;

import de.rnoennig.service.EventService;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table( name = "APPROVE_REQUEST_EVENTS" )
public class ApproveRequestEvent extends DomainEvent {
    private Long id;
    private Long eventRequestId;

    public ApproveRequestEvent() {}

    public ApproveRequestEvent(Long eventRequestId) {
        super();
        this.eventRequestId = eventRequestId;
    }

    @Id
    @GeneratedValue(generator="increment")
    @GenericGenerator(name="increment", strategy = "increment")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name="event_request_id")
    public Long getEventRequestId() {
        return eventRequestId;
    }

    public void setEventRequestId(Long eventRequestId) {
        this.eventRequestId = eventRequestId;
    }

    @Override
    public String toString() {
        return "ApproveEventRequestEvent{" +
                "id=" + id +
                ", occured=" + getOccured() +
                ", recorded=" + getRecorded() +
                '}';
    }

    @Override
    public void process(EventService eventService) {
        RequestEvent requestEvent = eventService.findById(this.getEventRequestId()).get();
        eventService.addEventDetail(EventDetail.of(requestEvent));
        eventService.removeEventRequest(requestEvent);
    }
}
