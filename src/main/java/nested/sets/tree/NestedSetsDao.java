package nested.sets.tree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.apache.commons.collections4.CollectionUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import tree.TreeDao;

/**
 *
 * @author ivan.yuriev
 */
public class NestedSetsDao implements TreeDao<NestedSetsTree> {

    private static final String UPDATE_LEFT_DELETE_OP = "UPDATE nested_sets SET lft = lft - %d WHERE lft > %d";
    private static final String UPDATE_RIGHT_DELETE_OP = "UPDATE nested_sets SET rgt = rgt - %d WHERE rgt > %d";  
    private static final String UPDATE_LEFT_ADD_OP = "UPDATE nested_sets SET lft = lft + %d WHERE lft >= %d";
    private static final String UPDATE_RIGHT_ADD_OP = "UPDATE nested_sets SET rgt = rgt + %d WHERE rgt >= %d";
    
    private static final String UPDATE_SUBTREE =
            "UPDATE nested_sets SET lft = lft + %d, rgt = rgt + %d WHERE id in "
            + "(SELECT descendant.id FROM nested_sets parent "
            + "INNER JOIN nested_sets descendant "
            + "ON (descendant.lft BETWEEN parent.lft and parent.rgt) "
            + "WHERE parent.id = %d)";

    @Override
    public Optional<NestedSetsTree> get(long id) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        NestedSetsTree nestedSet = session.get(NestedSetsTree.class, id);
        session.getTransaction().commit();
        return Optional.ofNullable(nestedSet);
    }

    @Override
    public List<NestedSetsTree> getAll() {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        List<NestedSetsTree> nestedSets = session.createQuery("from NestedSetsTree").list();
        session.getTransaction().commit();
        return nestedSets;
    }

    @Override
    public List<NestedSetsTree> getByName(String name) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        List<NestedSetsTree> nodes = session.createQuery("from NestedSetsTree where name = :name").setParameter("name", name).list();
        session.getTransaction().commit();
        return nodes;
    }

    @Override
    public void save(NestedSetsTree nestedSet) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        session.save(nestedSet);
        session.getTransaction().commit();
    }

    @Override
    public void delete(NestedSetsTree nestedSet) {
        if (nestedSet == null) {
            return;
        }
        long left = nestedSet.getLeft();
        long right = nestedSet.getRight();
        long delta = right - left + 1L;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        session.createNamedQuery("delete").setParameter("id", nestedSet.getId()).executeUpdate();
        session.createNativeQuery(String.format(UPDATE_LEFT_DELETE_OP, delta, left)).executeUpdate();
        session.createNativeQuery(String.format(UPDATE_RIGHT_DELETE_OP, delta, right)).executeUpdate();
        session.getTransaction().commit();
    }

    @Override
    public Map<Integer, List<NestedSetsTree>> getAllChildren(NestedSetsTree nestedSet) {
        if ((nestedSet.getRight() - nestedSet.getLeft()) == 1) {
            return Collections.emptyMap();
        }
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        List<Object[]> children = session.createNamedQuery("getAllСhildren").setParameter("id", nestedSet.getId()).getResultList();
        session.getTransaction().commit();
        Map<Integer, List<NestedSetsTree>> result = new HashMap<>();
        children.forEach(record -> {
            NestedSetsTree child = (NestedSetsTree) record[0];
            Integer level = (Integer) record[1];
            List<NestedSetsTree> list = result.computeIfAbsent(level, k -> new ArrayList<>());
            list.add(child);
        });

        return result;
    }

    @Override
    public Map<Integer, NestedSetsTree> getAllParents(NestedSetsTree nestedSet) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        List<Object[]> parents = session.createNamedQuery("getAllParents").setParameter("id", nestedSet.getId()).getResultList();
        session.getTransaction().commit();
        Map<Integer, NestedSetsTree> result = new HashMap<>();
        parents.forEach(record -> {
            NestedSetsTree parent = (NestedSetsTree) record[0];
            Integer level = (Integer) record[1];
            result.put(level, parent);
        });
        return result;
    }

    @Override
    public String getPath(NestedSetsTree nestedSet) {
        String delimiter = nestedSet.getDelimiter();
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        Query<String> result = session.createNamedQuery("getPath")
                .setParameter("delimiter", delimiter)
                .setParameter("id", nestedSet.getId());
        String path = result.getSingleResult();
        session.getTransaction().commit();
        return path;
    }

    @Override
    public NestedSetsTree getRoot(NestedSetsTree nestedSet) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        Query<NestedSetsTree> result = session.createNamedQuery("getRoot", NestedSetsTree.class).setParameter("id", nestedSet.getId());
        NestedSetsTree root = result.getResultList().isEmpty() ? null : result.getSingleResult();
        session.getTransaction().commit();
        return root;
    }

    @Override
    public void save(List<NestedSetsTree> nestedSet) {
        int batchSize = HibernateUtil.getBatchSize();
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = session.getTransaction();
        transaction.begin();
        for (int i = 0; i < nestedSet.size(); i++) {
            session.save(nestedSet.get(i));
            if ((i + 1) % batchSize == 0) {
                // Flush and clear the cache every batch
                session.flush();
                session.clear();
            }
        }
        transaction.commit();
    }

    @Override
    public void add(NestedSetsTree parentNode, List<NestedSetsTree> nodes) {
        if (parentNode == null || CollectionUtils.isEmpty(nodes)) {
            return;
        }
        long delta = nodes.size() * 2L;
        long right = parentNode.getRight();
        updateTreeNodes(nodes, right - 1L);
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        session.createNativeQuery(String.format(UPDATE_LEFT_ADD_OP, delta, right)).executeUpdate();
        session.createNativeQuery(String.format(UPDATE_RIGHT_ADD_OP, delta, right)).executeUpdate();
        nodes.forEach(session::save);
        session.getTransaction().commit();
    }

    @Override
    public void move(NestedSetsTree parentNode, NestedSetsTree subNode) {
        if (parentNode == null || subNode == null) {
            return;
        }
        long parentRight = parentNode.getRight();
        long parentLeft = parentNode.getLeft();
        long subNodeLeft = subNode.getLeft();
        long subNodeRight = subNode.getRight();
        long subNodeSize = subNodeRight - subNodeLeft + 1L;
  
        long subNodeIndex = parentLeft - subNodeLeft + 1L;
        if ((subNodeRight + subNodeSize + subNodeIndex) > subNodeRight && subNodeRight > parentRight) {
            subNodeIndex = subNodeIndex - subNodeSize;
        }
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        session.createNativeQuery(String.format(UPDATE_LEFT_ADD_OP, subNodeSize, parentRight)).executeUpdate();
        session.createNativeQuery(String.format(UPDATE_RIGHT_ADD_OP, subNodeSize, parentRight)).executeUpdate();
        session.createNativeQuery(String.format(UPDATE_SUBTREE, subNodeIndex, subNodeIndex, subNode.getId())).executeUpdate();
        session.createNativeQuery(String.format(UPDATE_LEFT_DELETE_OP, subNodeSize, subNodeRight)).executeUpdate();
        session.createNativeQuery(String.format(UPDATE_RIGHT_DELETE_OP, subNodeSize, subNodeRight)).executeUpdate();
        session.getTransaction().commit();
    }

    private void updateTreeNodes(List<NestedSetsTree> nodes, long index) {
        if (CollectionUtils.isEmpty(nodes)) {
            return;
        }
        nodes.sort((NestedSetsTree ch1, NestedSetsTree ch2)
                -> (int) (ch1.getLeft() - ch2.getLeft())
        );

        NestedSetsTree node = nodes.get(0);
        long delta = node.getLeft() - 1L;
        nodes.forEach(v -> {
            v.setLeft(v.getLeft() - delta + index);
            v.setRight(v.getRight() - delta + index);
        });
    }

    public void inset(String path) {
        Session session = closure.table.tree.HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        session.createNativeQuery("INSERT INTO test(title) VALUES (?)")
                .setParameter(1, path)
                .executeUpdate();
        session.getTransaction().commit();
    }

}
