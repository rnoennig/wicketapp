package de.rnoennig.domain;

import de.rnoennig.component.LeafletMap;
import de.rnoennig.service.EventService;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.Instant;
import java.util.Date;

// TODO store source ip, location as name, begin and end of event and notes
@Entity
@Table( name = "REQUEST_EVENTS" )
public class RequestEvent extends DomainEvent implements LeafletMap.LatLng {
    private Long id;
    private String name;
    private String lat;
    private String lng;

    @Id
    @GeneratedValue(generator="increment")
    @GenericGenerator(name="increment", strategy = "increment")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getLat() {
        return lat;
    }

    @Override
    public void setLat(String lat) {
        this.lat = lat;
    }

    @Override
    public String getLng() {
        return lng;
    }

    @Override
    public void setLng(String lng) {
        this.lng = lng;
    }

    @Override
    public String toString() {
        return "RequestEvent{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", lat='" + lat + '\'' +
                ", lng='" + lng + '\'' +
                ", occured=" + getOccured() +
                ", recorded=" + getRecorded() +
                '}';
    }

    @Override
    public void process(EventService eventService) {
        eventService.addEventRequest(this);
    }
}
