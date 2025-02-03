<h2 align="center"> Ways to store trees in relational databases using ORM Hibernate.</h2>
<div align="center"><img src="https://hsto.org/getpro/habr/upload_files/5ce/aba/82c/5ceaba82c9e579300eea7b8ed9e8c2f7.PNG"></div>
</br></br>
<p>A folder structure on the file system is represented as a tree, which is loaded into the database table(s). The nesting level in my SQL queries is a calculated value, meaning that this value is not present in the table(s). This excludes the designs 'Closure Table' and 'Nested Sets', which have two implementations: one with a 'level' column in the DB table and one without it.</p>
<p><b>Basic operations for working with a tree:</b></p>
 <ul>
  <li>Get all descendants with a nesting level (method name: <b><i>getAllChildren</i></b>).</li>
  <li>Get all ancestors with a nesting level (method name: <b><i>getAllParents</i></b>).</li>
  <li>Add a node with its descendants to the tree (method name: <b><i>add</i></b>).</li>
  <li>Delete a node along with its descendants from the tree (method name: <b><i>delete</i></b>).</li>
  <li>Move a node with its descendants within the tree (method name: <b><i>move</i></b>).</li>
  <li>Get the root of the tree (method name: <b><i>getRoot</i></b>).</li>
  <li>Get the full path to a node (method name: <b><i>getPath</i></b>).</li>
  <li>Get a list of nodes by name (method name: <b><i>getByName</i></b>).</li>
</ul>
<p>All of the above operations are contained in the <a href="https://github.com/yurievivan/TreeProject/blob/master/src/main/java/tree/dao/TreeDao.java">TreeDao</a>.</p>
<p>Basic operations not directly related to the tree are contained in the <a href="https://github.com/yurievivan/TreeProject/blob/master/src/main/java/tree/dao/Dao.java">Dao</a> interface, such as <b><i>get</i></b>, <b><i>save</i></b>, and <b><i>delete</i></b>.</p>
<h2>Adjacency List</h2>
<div align="center"><img src="https://hsto.org/getpro/habr/upload_files/b2c/ee6/216/b2cee6216e8a723f590d55d1223261a6.png" alt="Adjacency List"></div>
</br></br>
<p>The idea of the Adjacency List data structure is to store information about the immediate parent of each node in the tree. In a table view, each row in the TREE table contains an additional PARENT_ID field, which specifies the ID of the parent node. This model allows for retrieving both descendants and ancestors based on the identifiers of the node (ID) and its parent (PARENT_ID).</p>
<p><b>Source package: </b><a href="https://github.com/yurievivan/TreeProject/tree/master/src/main/java/adjacency/list/tree">adjacency.list.tree</a>.</p>
<p><b>Entity: </b><a href="https://github.com/yurievivan/TreeProject/blob/master/src/main/java/adjacency/list/tree/Node.java">Node</a>.</p>
<p><b>Data Access Object: </b><a href="https://github.com/yurievivan/TreeProject/blob/master/src/main/java/adjacency/list/tree/NodeDao.java">NodeDao</a>.</p>
<p><b>Named queries: </b><a href="https://github.com/yurievivan/TreeProject/blob/master/src/main/resources/adjacencyListQueries.hbm.xml">adjacencyListQueries.hbm.xml</a>.</p>
<h2>Closure Table</h2>
<div align="center"><img src="https://hsto.org/getpro/habr/upload_files/6a2/74f/627/6a274f627873c43d43b18ca5de82a8c1.png" alt="Closure Table"></div>
</br></br>
<p>The Closure Table solution is one way of storing hierarchies in two tables and querying them efficiently without the need for recursive queries. It involves storing all possible paths between nodes and their corresponding names in separate tables. This method allows for quick retrieval of both descendants and ancestors of a given node, making it particularly useful for complex hierarchical data structures.</p>
<p><b>Source package: </b><a href="https://github.com/yurievivan/TreeProject/tree/master/src/main/java/closure/table/tree">closure.table.tree</a>.</p>
<p><b>Entities: </b><a href="https://github.com/yurievivan/TreeProject/blob/master/src/main/java/closure/table/tree/FileName.java">FileName</a>, <a href="https://github.com/yurievivan/TreeProject/blob/master/src/main/java/closure/table/tree/TreePath.java">TreePath</a>.</p>
<p><b>Data Access Objects: </b><a href="https://github.com/yurievivan/TreeProject/blob/master/src/main/java/closure/table/tree/FileNameDao.java">FileNameDao</a>, <a href="https://github.com/yurievivan/TreeProject/blob/master/src/main/java/closure/table/tree/TreePathDao.java">TreePathDao</a>.</p>
<p><b>Named queries: </b><a href="https://github.com/yurievivan/TreeProject/blob/master/src/main/resources/closureTableQueries.hbm.xml">closureTableQueries.hbm.xml</a>.</p>
<hr>
<p>Implementation with an additional 'level' column in the FILE_NAME table for faster selection of all ancestors and descendants based on their nesting level. In this implementation, the nesting level is recalculated only when the tree structure changes.</p>
<p><b>Source package: </b><a href="https://github.com/yurievivan/TreeProject/tree/master/src/main/java/improved/closure/table/tree">improved.closure.table.tree</a>.</p>
<p><b>Entities: </b><a href="https://github.com/yurievivan/TreeProject/blob/master/src/main/java/improved/closure/table/tree/FileName.java">FileName</a>, <a href="https://github.com/yurievivan/TreeProject/blob/master/src/main/java/improved/closure/table/tree/TreePath.java">TreePath</a>.</p>
<p><b>Data Access Objects: </b><a href="https://github.com/yurievivan/TreeProject/blob/master/src/main/java/improved/closure/table/tree/FileNameDao.java">FileNameDao</a>, <a href="https://github.com/yurievivan/TreeProject/blob/master/src/main/java/improved/closure/table/tree/TreePathDao.java">TreePathDao</a>.</p>
<p><b>Named queries: </b><a href="https://github.com/yurievivan/TreeProject/blob/master/src/main/resources/improvedClosureTableQueries.hbm.xml">improvedClosureTableQueries.hbm.xml</a>.</p>
<p>The main changes affected named queries and data access objects.</p>
<h2>Nested sets</h2>
<div align="center"><img src="https://hsto.org/getpro/habr/upload_files/770/98a/934/77098a9345c609ea0057827bef6c5249.png" alt="Nested sets"></div>
</br></br>
<p>The Nested Sets model is a technique for representing hierarchical data in relational databases, based on the concept of Nested Intervals. This model eliminates the need for recursive queries to retrieve a list of parents or children. By assigning each node a pair of left and right values, the Nested Sets model allows for efficient querying of both descendants and ancestors in a hierarchical structure.</p>
<p>Let's get the descendants and ancestors for node C, whose Left = 2 and Right = 11.</p>
<b>Example SQL query to get descendants:</b>
<div>SELECT * FROM NESTED_SETS WHERE LFT > 2 AND RGT < 11</div>
<p>Result: G, F, H, K.</p>
<b>Example SQL query to get ancestors:</b>
<div>SELECT * FROM NESTED_SETS WHERE LFT < 2 AND RGT > 11</div>
<p>Result: A.</p>
 </br>
<p><b>Source package: </b><a href="https://github.com/yurievivan/TreeProject/tree/master/src/main/java/nested/sets/tree">nested.sets.tree</a>.</p>
<p><b>Entity: </b><a href="https://github.com/yurievivan/TreeProject/blob/master/src/main/java/nested/sets/tree/NestedSetsTree.java">NestedSetsTree</a>.</p>
<p><b>Data Access Object: </b><a href="https://github.com/yurievivan/TreeProject/blob/master/src/main/java/nested/sets/tree/NestedSetsDao.java">NestedSetsDao</a>.</p>
<p><b>Named queries: </b><a href="https://github.com/yurievivan/TreeProject/blob/master/src/main/resources/nestedSetsQueries.hbm.xml">nestedSetsQueries.hbm.xml</a>.</p>
<hr>
<p>Implementation with an additional 'level' column in the NESTED_SETS table for faster selection of all ancestors and descendants based on their nesting level. In this implementation, the nesting level is recalculated only when the tree structure changes.</p>
<p><b>Source package: </b><a href="https://github.com/yurievivan/TreeProject/tree/master/src/main/java/improved/nested/sets/tree">improved.nested.sets.tree</a>.</p>
<p><b>Entity: </b><a href="https://github.com/yurievivan/TreeProject/blob/master/src/main/java/improved/nested/sets/tree/NestedSetsTree.java">NestedSetsTree</a>.</p>
<p><b>Data Access Object: </b><a href="https://github.com/yurievivan/TreeProject/blob/master/src/main/java/improved/nested/sets/tree/NestedSetsDao.java">NestedSetsDao</a>.</p>
<p><b>Named queries: </b><a href="https://github.com/yurievivan/TreeProject/blob/master/src/main/resources/improvedNestedSetsQueries.hbm.xml">improvedNestedSetsQueries.hbm.xml</a>.</p>
<p>The main changes affected named queries and data access objects.</p>
<h2>Materialized Path</h2>
<div align="center"><img src="https://hsto.org/getpro/habr/upload_files/454/89c/f90/45489cf9091a01a561fbfda628825e4c.png" alt="Materialized Path"></div>
</br></br>
<p>The figure shows an example of the 'Materialized Path' model. The idea behind this model is to store the full path for each node in the tree. The path contains a chain of all ancestors for each node, typically separated by a delimiter (e.g., a slash or comma). By counting the number of separators in the path, you can determine the nesting depth of the node. The Materialized Path model offers a clear and intuitive way to represent tree structures, making it easy to retrieve the full path or identify parent-child relationships.</p>
<p><b>Source package: </b><a href="https://github.com/yurievivan/TreeProject/tree/master/src/main/java/path/enumeration/tree">path.enumeration.tree</a>.</p>
<p><b>Entity: </b><a href="https://github.com/yurievivan/TreeProject/blob/master/src/main/java/path/enumeration/tree/Files.java">Files</a>.</p>
<p><b>Data Access Object: </b><a href="https://github.com/yurievivan/TreeProject/blob/master/src/main/java/path/enumeration/tree/FilesDao.java">FilesDao</a>.</p>
<p><b>Named queries: </b><a href="https://github.com/yurievivan/TreeProject/blob/master/src/main/resources/pathEnumerationQueries.hbm.xml">pathEnumerationQueries.hbm.xml</a>.</p>
</br>
<hr>
<h2 align="left">Publishing on Habrahabr: <a href="https://habr.com/ru/post/537062/">https://habr.com/ru/post/537062/</a></h1>
