package checkers.Dao;

import checkers.entity.Move;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class MoveDao {

    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void saveMove(Move move) {

        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.save(move);
        session.getTransaction().commit();
        session.close();
    }
}