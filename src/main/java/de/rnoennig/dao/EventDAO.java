package de.rnoennig.dao;

import de.rnoennig.domain.ApproveRequestEvent;
import de.rnoennig.domain.DomainEvent;
import de.rnoennig.domain.RejectRequestEvent;
import de.rnoennig.domain.RequestEvent;

import java.util.List;

public interface EventDAO {
    void save(DomainEvent eventRequestEvent);

    List<RequestEvent> listRequestEvents();
    List<ApproveRequestEvent> listApproveRequestEvents();
    List<RejectRequestEvent> listRejectRequestEvents();
}
