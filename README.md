<h2 align="center"> Ways to store trees in relational databases using ORM Hibernate.</h1>
<div align="center"><img src="https://hsto.org/getpro/habr/upload_files/5ce/aba/82c/5ceaba82c9e579300eea7b8ed9e8c2f7.PNG"></div>
</br></br>
<p><b>Basic operations for working with tree:</b></p>
 <ul>
  <li>get all descendants with a nesting level (method name getAllChildren);</li>
  <li>get all ancestors with a nesting level (method name getAllParents);</li>
  <li>add a node with descendants in the tree (method name add);</li>
  <li>delete a node with descendants in the tree (method name delete);</li>
  <li>move a node with descendants in the tree (method name move);</li>
  <li>get a root of a tree (method name getRoot);</li>
  <li>get a full path to the node (method name getPath);</li>
  <li>get a list of nodes by name (method name getByName).</li>
</ul>
<p>All of the above operations are contained in interface <a href="https://github.com/yurievivan/TreeProject/blob/master/src/main/java/tree/dao/TreeDao.java">TreeDao</a>.</p>
<p>Basic operations not only related to the tree are contained in interface <a href="https://github.com/yurievivan/TreeProject/blob/master/src/main/java/tree/dao/Dao.java">Dao</a>. For example, such operations as get, save, delete.</p>
<h2>Adjacency List</h2>
<div align="center"><img src="https://hsto.org/getpro/habr/upload_files/b2c/ee6/216/b2cee6216e8a723f590d55d1223261a6.png" alt="Adjacency List"></div>
</br></br>
<p><b>Source package: </b><a href="https://github.com/yurievivan/TreeProject/tree/master/src/main/java/adjacency/list/tree">adjacency.list.tree</a>.</p>
<p><b>Entity: </b><a href="https://github.com/yurievivan/TreeProject/blob/master/src/main/java/adjacency/list/tree/Node.java">Node</a>.</p>
<p><b>Data Access Object: </b><a href="https://github.com/yurievivan/TreeProject/blob/master/src/main/java/adjacency/list/tree/NodeDao.java">NodeDao</a>.</p>
<p><b>Named queries: </b><a href="https://github.com/yurievivan/TreeProject/blob/master/src/main/resources/adjacencyListQueries.hbm.xml">adjacencyListQueries.hbm.xml</a>.</p>
<h2>Closure Table</h2>
<div align="center"><img src="https://hsto.org/getpro/habr/upload_files/6a2/74f/627/6a274f627873c43d43b18ca5de82a8c1.png" alt="Closure Table"></div>
</br></br>
<p><b>Source package: </b><a href="https://github.com/yurievivan/TreeProject/tree/master/src/main/java/closure/table/tree">closure.table.tree</a>.</p>
<p><b>Entities: </b><a href="https://github.com/yurievivan/TreeProject/blob/master/src/main/java/closure/table/tree/FileName.java">FileName</a>, </b><a href="https://github.com/yurievivan/TreeProject/blob/master/src/main/java/closure/table/tree/TreePath.java">TreePath</a>.</p>
<p><b>Data Access Objects: </b><a href="https://github.com/yurievivan/TreeProject/blob/master/src/main/java/closure/table/tree/FileNameDao.java">FileNameDao</a>, </b><a href="https://github.com/yurievivan/TreeProject/blob/master/src/main/java/closure/table/tree/TreePathDao.java">TreePathDao</a>.</p>
<p><b>Named queries: </b><a href="https://github.com/yurievivan/TreeProject/blob/master/src/main/resources/closureTableQueries.hbm.xml">closureTableQueries.hbm.xml</a>.</p>
<h2>Nested sets</h2>
<div align="center"><img src="https://hsto.org/getpro/habr/upload_files/770/98a/934/77098a9345c609ea0057827bef6c5249.png" alt="Nested sets"></div>
</br></br>
<p><b>Source package: </b><a href="https://github.com/yurievivan/TreeProject/tree/master/src/main/java/nested/sets/tree">nested.sets.tree</a>.</p>
<p><b>Entity: </b><a href="https://github.com/yurievivan/TreeProject/blob/master/src/main/java/nested/sets/tree/NestedSetsTree.java">NestedSetsTree</a>.</p>
<p><b>Data Access Object: </b><a href="https://github.com/yurievivan/TreeProject/blob/master/src/main/java/nested/sets/tree/NestedSetsDao.java">NestedSetsDao</a>.</p>
<p><b>Named queries: </b><a href="https://github.com/yurievivan/TreeProject/blob/master/src/main/resources/nestedSetsQueries.hbm.xml">nestedSetsQueries.hbm.xml</a>.</p>
<h2>Materialized Path</h2>
<div align="center"><img src="https://hsto.org/getpro/habr/upload_files/454/89c/f90/45489cf9091a01a561fbfda628825e4c.png" alt="Materialized Path"></div>
</br></br>
<p><b>Source package: </b><a href="https://github.com/yurievivan/TreeProject/tree/master/src/main/java/path/enumeration/tree">path.enumeration.tree</a>.</p>
<p><b>Entity: </b><a href="https://github.com/yurievivan/TreeProject/blob/master/src/main/java/path/enumeration/tree/Files.java">Files</a>.</p>
<p><b>Data Access Object: </b><a href="https://github.com/yurievivan/TreeProject/blob/master/src/main/java/path/enumeration/tree/FilesDao.java">FilesDao</a>.</p>
<p><b>Named queries: </b><a href="https://github.com/yurievivan/TreeProject/blob/master/src/main/resources/pathEnumerationQueries.hbm.xml">pathEnumerationQueries.hbm.xml</a>.</p>
</br>
<hr>
<h2 align="left">Publishing on Habrahabr: <a href="https://habr.com/ru/post/537062/">https://habr.com/ru/post/537062/</a></h1>
