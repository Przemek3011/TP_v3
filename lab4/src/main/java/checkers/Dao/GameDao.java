package checkers.Dao;

import checkers.entity.Game;
import org.hibernate.SessionFactory;
import org.hibernate.Session;

public class GameDao {

    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void saveGame(Game game) {

        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.save(game);
        session.getTransaction().commit();
        session.close();
    }
}
