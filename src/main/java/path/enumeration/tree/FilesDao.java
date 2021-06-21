package path.enumeration.tree;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import org.hibernate.Session;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import tree.dao.TreeDao;

/**
 *
 * @author ivan.yuriev
 */
public class FilesDao implements TreeDao<Files> {

    @Override
    public Optional<Files> get(long id) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        Files files = session.get(Files.class, id);
        session.getTransaction().commit();
        return Optional.ofNullable(files);
    }

    @Override
    public void save(Files files) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        session.save(files);
        session.getTransaction().commit();
    }

    @Override
    public void save(List<Files> files) {
        int batchSize = HibernateUtil.getBatchSize();
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = session.getTransaction();
        transaction.begin();
        for (int i = 0; i < files.size(); i++) {
            session.save(files.get(i));
            if ((i + 1) % batchSize == 0) {
                // Flush and clear the cache every batch
                session.flush();
                session.clear();
            }
        }
        transaction.commit();
    }

    @Override
    public void delete(Files files) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        session.createNamedQuery("delete")
                .setParameter("path", files.getPath())
                .setParameter("delimiter", File.separator)
                .executeUpdate();
        session.getTransaction().commit();
    }

    @Override
    public List<Files> getAll() {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        List<Files> files = session.createQuery("from Files").list();
        session.getTransaction().commit();
        return files;
    }

    @Override
    public List<Files> getByName(String name) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        List<Files> nodes = session.createQuery("from Files where name = :name")
                .setParameter("name", name)
                .list();
        session.getTransaction().commit();
        return nodes;
    }

    @Override
    public Map<Integer, List<Files>> getAllChildren(Files files) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        List<Object[]> children = session.createNamedQuery("getAllСhildren")
                .setParameter("delimiter", File.separator)
                .setParameter("parentPath", files.getPath())
                .getResultList();
        session.getTransaction().commit();
        Map<Integer, List<Files>> result = new HashMap<>();
        children.forEach(rec -> {
            Files child = (Files) rec[0];
            Integer level = (Integer) rec[1];
            List<Files> list = result.computeIfAbsent(level, k -> new ArrayList<>());
            list.add(child);
        });
        return result;
    }

    @Override
    public Map<Integer, Files> getAllParents(Files files) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        List<Object[]> parents = session.createNamedQuery("getAllParents")
                .setParameter("delimiter", File.separator)
                .setParameter("id", files.getId())
                .getResultList();
        session.getTransaction().commit();
        Map<Integer, Files> result = new HashMap<>();
        parents.forEach(rec -> {
            Files parent = (Files) rec[0];
            Integer level = (Integer) rec[1];
            result.put(level, parent);
        });
        return result;
    }

    @Override
    public void add(Files parentNode, List<Files> nodes) {
        if (parentNode == null || CollectionUtils.isEmpty(nodes)) {
            return;
        }
        nodes.forEach(n -> {
            String newPath = String.join(File.separator, parentNode.getPath(), n.getName());
            n.setPath(newPath);
        });
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        nodes.forEach(session::save);
        session.getTransaction().commit();
    }

    @Override
    public void move(Files parentNode, Files subNode) {
        if (parentNode == null || subNode == null) {
            return;
        }
        String subNodeParentPath = StringUtils.removeEnd(subNode.getPath(), File.separator + subNode.getName());
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        session.createNamedQuery("move")
                .setParameter("subNodeParentPath", subNodeParentPath)
                .setParameter("parentPath", parentNode.getPath())
                .setParameter("delimiter", File.separator)
                .executeUpdate();
        session.getTransaction().commit();
    }

    @Override
    public String getPath(Files files) {
        return files.getPath();
    }

    @Override
    public Files getRoot(Files files) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        Query<Files> result = session.createNamedQuery("getRoot")
                .setParameter("path", files.getPath())
                .setParameter("delimiter", File.separator);
        Files root = result.getSingleResult();
        session.getTransaction().commit();
        return root;
    }
}
