package improved.closure.table.tree;

import java.util.List;
import java.util.Optional;
import org.hibernate.Session;
import org.hibernate.Transaction;
import tree.dao.Dao;

/**
 *
 * @author ivan.yuriev
 */
public class TreePathDao implements Dao<TreePath> {

    @Override
    public Optional<TreePath> get(long id) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        TreePath treePath = session.find(TreePath.class, id);
        session.getTransaction().commit();
        return Optional.ofNullable(treePath);
    }

    @Override
    public List<TreePath> getAll() {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        List<TreePath> treePaths = session.createQuery("from TreePath", TreePath.class).list();
        session.getTransaction().commit();
        return treePaths;
    }

    @Override
    public void save(TreePath t) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        session.persist(t);
        session.getTransaction().commit();
    }

    @Override
    public void save(List<TreePath> treePaths) {
        int batchSize = HibernateUtil.getBatchSize();
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = session.getTransaction();
        transaction.begin();
        for (int i = 0; i < treePaths.size(); i++) {
            session.persist(treePaths.get(i));
            if ((i + 1) % batchSize == 0) {
                // Flush and clear the cache every batch
                session.flush();
                session.clear();
            }
        }
        transaction.commit();
    }

    public void update(TreePath t) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        session.merge(t);
        session.getTransaction().commit();
    }

    @Override
    public void delete(TreePath t) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        session.remove(t);
        session.getTransaction().commit();
    }

}
