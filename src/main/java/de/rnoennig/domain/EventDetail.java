package de.rnoennig.domain;

import de.rnoennig.component.LeafletMap;

import java.util.Date;

public class EventDetail implements LeafletMap.LatLng {
    private String name;
    private String lat;
    private String lng;
    private Date createdAt;

    public static EventDetail of(RequestEvent requestEvent) {
        EventDetail eventDetails = new EventDetail();
        eventDetails.setName(requestEvent.getName());
        eventDetails.setLat(requestEvent.getLat());
        eventDetails.setLng(requestEvent.getLng());
        eventDetails.setCreatedAt(requestEvent.getOccured());
        return eventDetails;
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

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Event{" +
                "name='" + name + '\'' +
                ", lat='" + lat + '\'' +
                ", lng='" + lng + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
