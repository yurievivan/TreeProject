package improved.closure.table.tree;

import java.util.ArrayList;
import java.util.HashMap;
import org.hibernate.Session;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import tree.dao.TreeDao;

/**
 *
 * @author ivan.yuriev
 */
public class FileNameDao implements TreeDao<FileName> {

    @Override
    public Optional<FileName> get(long id) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        FileName fileName = session.get(FileName.class, id);
        session.getTransaction().commit();
        return Optional.ofNullable(fileName);
    }

    @Override
    public void save(FileName fileName) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        session.persist(fileName);
        session.getTransaction().commit();
    }

    @Override
    public void save(List<FileName> fileNames) {
        int batchSize = HibernateUtil.getBatchSize();
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = session.getTransaction();
        transaction.begin();
        for (int i = 0; i < fileNames.size(); i++) {
            session.persist(fileNames.get(i));
            if ((i + 1) % batchSize == 0) {
                // Flush and clear the cache every batch
                session.flush();
                session.clear();
            }
        }
        transaction.commit();
    }

    @Override
    public void delete(FileName fileName) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        session.createNamedMutationQuery("delete").setParameter("id", fileName.getId()).executeUpdate();
        session.getTransaction().commit();
    }

    @Override
    public List<FileName> getAll() {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        List<FileName> fileNames = session.createQuery("from FileName", FileName.class).list();
        session.getTransaction().commit();
        return fileNames;
    }

    @Override
    public Map<Integer, List<FileName>> getAllChildren(FileName fileName) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        List<FileName> children = session.createNamedQuery("getAllСhildren", FileName.class)
                .setParameter("id", fileName.getId())
                .setParameter("parentLevel", fileName.getLevel())
                .getResultList();
        session.getTransaction().commit();

        Map<Integer, List<FileName>> result = new HashMap<>();
        children.forEach(child -> {
            List<FileName> list = result.computeIfAbsent(child.getLevel(), k -> new ArrayList<>());
            list.add(child);
        });
        return result;
    }

    @Override
    public Map<Integer, FileName> getAllParents(FileName fileName) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        List<FileName> parents = session.createNamedQuery("getAllParents", FileName.class).setParameter("id", fileName.getId()).getResultList();
        session.getTransaction().commit();
        return parents.stream().collect(Collectors.toMap(FileName::getLevel, Function.identity()));
    }

    @Override
    public void add(FileName parentNode, List<FileName> nodes) {
        if (parentNode == null || CollectionUtils.isEmpty(nodes)) {
            return;
        }
        nodes.forEach(n -> n.setLevel(parentNode.getLevel() + 1));
        nodes.forEach(this::save);
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        nodes.forEach(n -> session.createNamedMutationQuery("addChildren")
                .setParameter("parentId", parentNode.getId())
                .setParameter("childId", n.getId())
                .executeUpdate()
        );
        session.getTransaction().commit();
        nodes.forEach(this::updateLevelByParentId);
    }

    @Override
    public void move(FileName parentNode, FileName subNode) {
        if (parentNode == null || subNode == null) {
            return;
        }
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        session.createNamedMutationQuery("move-deleteParents")
                .setParameter("parentId", parentNode.getId())
                .setParameter("childId", subNode.getId())
                .executeUpdate();
        session.createNamedMutationQuery("move-addChildren")
                .setParameter("parentId", parentNode.getId())
                .setParameter("childId", subNode.getId())
                .executeUpdate();
        session.createNamedMutationQuery("update-level-by-parentId")
                .setParameter("id", parentNode.getId())
                .executeUpdate();
        session.getTransaction().commit();
    }

    @Override
    public String getPath(FileName fileName) {
        String delimiter = fileName.getDelimiter();
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        Query result = session.createNamedQuery("getPath", Object[].class)
                .setParameter("id", fileName.getId())
                .setParameter("delimiter", delimiter);
        String path = (String) result.getSingleResult();
        session.getTransaction().commit();
        return path;
    }

    @Override
    public FileName getRoot(FileName fileName) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        Query<FileName> result = session.createNamedQuery("getRoot", FileName.class).setParameter("id", fileName.getId());
        FileName root = result.getResultList().isEmpty() ? null : result.getSingleResult();
        session.getTransaction().commit();
        return root;
    }

    @Override
    public List<FileName> getByName(String name) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        List<FileName> nodes = session.createQuery("from FileName where name = :name", FileName.class).setParameter("name", name).list();
        session.getTransaction().commit();
        return nodes;
    }

    public void updateNestingLevel() {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        session.createNamedMutationQuery("update-nesting-level").executeUpdate();
        session.getTransaction().commit();
    }

    private void updateLevelByParentId(FileName parent) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        session.createNamedMutationQuery("update-level-by-parentId").setParameter("id", parent.getId()).executeUpdate();
        session.getTransaction().commit();
    }
}
