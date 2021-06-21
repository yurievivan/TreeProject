package adjacency.list.tree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import tree.dao.TreeDao;
import org.hibernate.Session;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.apache.commons.collections4.CollectionUtils;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

/**
 *
 * @author ivan.yuriev
 */
public class NodeDao implements TreeDao<Node> {

    @Override
    public Optional<Node> get(long id) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        Node node = session.get(Node.class, id);
        session.getTransaction().commit();
        return Optional.ofNullable(node);
    }

    @Override
    public void save(Node node) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        session.save(node);
        session.getTransaction().commit();
    }

    @Override
    public void save(List<Node> nodes) {
        int batchSize = HibernateUtil.getBatchSize();
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = session.getTransaction();
        transaction.begin();
        for (int i = 0; i < nodes.size(); i++) {
            session.save(nodes.get(i));
            if ((i + 1) % batchSize == 0) {
                // Flush and clear the cache every batch
                session.flush();
                session.clear();
            }
        }
        transaction.commit();
    }

    @Override
    public void delete(Node node) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        session.createNamedQuery("delete").setParameter("id", node.getId()).executeUpdate();
        session.getTransaction().commit();
    }

    @Override
    public List<Node> getAll() {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        List<Node> nodes = session.createQuery("from Node").list();
        session.getTransaction().commit();
        return nodes;
    }

    @Override
    public Map<Integer, List<Node>> getAllChildren(Node node) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        List<Object[]> children = session.createNamedQuery("getAllСhildren").setParameter("id", node.getId()).getResultList();
        session.getTransaction().commit();
        Map<Integer, List<Node>> result = new HashMap<>();
        children.forEach(rec -> {
            Node child = (Node) rec[0];
            Integer level = (Integer) rec[1];
            List<Node> list = result.computeIfAbsent(level, k -> new ArrayList<>());
            list.add(child);
        });
        return result;
    }

    @Override
    public Map<Integer, Node> getAllParents(Node node) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        List<Object[]> parents = session.createNamedQuery("getAllParents").setParameter("id", node.getId()).getResultList();
        session.getTransaction().commit();
        Map<Integer, Node> result = new HashMap<>();
        parents.forEach(rec -> {
            Node parent = (Node) rec[0];
            Integer level = (Integer) rec[1];
            result.put(level, parent);
        });
        return result;
    }

    @Override
    public String getPath(Node node) {
        if (node.getParent() == null) {
            return node.getName();
        }
        String delimiter = node.getDelimiter();
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        Query<String> result = session.createNamedQuery("getPath")
                .setParameter("id", node.getId())
                .setParameter("delimiter", delimiter);
        String path = result.getSingleResult();
        session.getTransaction().commit();
        return path;
    }

    @Override
    public void add(Node parentNode, List<Node> nodes) {
        if (parentNode == null || CollectionUtils.isEmpty(nodes)) {
            return;
        }
        nodes.forEach(n -> n.setParent(parentNode));
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        nodes.forEach(session::merge);
        session.getTransaction().commit();
    }

    @Override
    public void move(Node parentNode, Node subNode) {
        add(parentNode, Collections.singletonList(subNode));
    }

    @Override
    public Node getRoot(Node node) {
        if (node.getParent() == null) {
            return node;
        }
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        Query<Node> result = session.createNamedQuery("getRoot", Node.class).setParameter("id", node.getId());
        Node root = result.getSingleResult();
        session.getTransaction().commit();
        return root;
    }

    @Override
    public List<Node> getByName(String name) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        List<Node> nodes = session.createQuery("from Node where name = :name").setParameter("name", name).list();
        session.getTransaction().commit();
        return nodes;
    }
}
