package de.rnoennig.dao;

import de.rnoennig.domain.ApproveRequestEvent;
import de.rnoennig.domain.DomainEvent;
import de.rnoennig.domain.RejectRequestEvent;
import de.rnoennig.domain.RequestEvent;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class EventDAOImpl implements EventDAO {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public void save(DomainEvent event) {
        try(Session session = this.sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            session.saveOrUpdate(event);
            tx.commit();
        }
    }

    @Override
    public List<RequestEvent> listRequestEvents() {
        Session session = this.sessionFactory.openSession();
        List<RequestEvent> eventList = session.createQuery("from RequestEvent").list();
        session.close();
        return eventList;
    }

    @Override
    public List<ApproveRequestEvent> listApproveRequestEvents() {
        Session session = this.sessionFactory.openSession();
        List<ApproveRequestEvent> eventList = session.createQuery("from ApproveRequestEvent").list();
        session.close();
        return eventList;
    }

    @Override
    public List<RejectRequestEvent> listRejectRequestEvents() {
        Session session = this.sessionFactory.openSession();
        List<RejectRequestEvent> eventList = session.createQuery("from RejectRequestEvent").list();
        session.close();
        return eventList;
    }
}
