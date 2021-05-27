package adjacency.list.tree;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

/**
 *
 * @author ivan.yuriev
 */
public class HibernateUtil {

    private static SessionFactory sessionFactory;
    private static final String SQL_QUERIES = "adjacencyListQueries.hbm.xml";

    private HibernateUtil() {
    }

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            Configuration configuration = new Configuration().configure();
            configuration.addAnnotatedClass(Node.class);
            configuration.addResource(SQL_QUERIES);
            StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties());
            sessionFactory = configuration.buildSessionFactory(builder.build());
        }
        return sessionFactory;
    }

    public static int getBatchSize() {
        Object batchSize = getSessionFactory().getProperties().get("hibernate.jdbc.batch_size");
        if (batchSize == null) {
            return 1;
        } else {
            return Integer.parseInt(batchSize.toString());
        }
    }

    public static void close() {
        if (sessionFactory != null && sessionFactory.isOpen()) {
            sessionFactory.close();
        }
    }

}
