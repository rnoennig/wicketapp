package de.rnoennig.service;

import de.rnoennig.dao.EventDAO;
import de.rnoennig.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;

@Service
public class EventService {

    @Autowired
    private EventDAO eventDAO;

    private List<RequestEvent> eventRequests = new ArrayList<>();
    private List<EventDetail> eventDetails = new ArrayList<>();

    @PostConstruct
    private void init() {
        List<DomainEvent> allEvents = new ArrayList<>();
        allEvents.addAll(eventDAO.listRequestEvents());
        allEvents.addAll(eventDAO.listApproveRequestEvents());
        allEvents.addAll(eventDAO.listRejectRequestEvents());
        allEvents.sort(Comparator.comparing(DomainEvent::getOccured));
        allEvents.stream().forEach(this::process);
        //TODO show only future events
    }

    public List<RequestEvent> getEventRequests() {
        return Collections.unmodifiableList(eventRequests);
    }

    public List<EventDetail> getEventsDetails() {
        return Collections.unmodifiableList(eventDetails);
    }

    public void process(DomainEvent event) {
        event.process(this);
        eventDAO.save(event);
    }

    // Repo methods below

    public Optional<RequestEvent> findById(Long eventRequestId) {
        return eventRequests.stream().filter(e -> e.getId().equals(eventRequestId)).findFirst();
    }

    public void addEventRequest(RequestEvent requestEvent) {
        eventRequests.add(requestEvent);
    }

    public void addEventDetail(EventDetail eventDetail) {
        this.eventDetails.add(eventDetail);
    }

    public void removeEventRequest(RequestEvent requestEvent) {
        this.eventRequests.remove(requestEvent);
    }
}
