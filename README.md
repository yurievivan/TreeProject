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
<b>📜Example SQL query to get descendants:</b><br><br>
<div><b>WITH RECURSIVE</b> r (id, parent_id, name, level) <b>AS</b></div>
<div>(<b>SELECT</b> id, parent_id, name, 1</div>
<div><b>FROM</b> tree</div>
<div><b>WHERE</b> parent_id = 6</div>
<div><b>UNION ALL</b></div>
<div><b>SELECT</b> t.id, t.parent_id, t.name, tp.level + 1</div>
<div><b>FROM</b> r tp</div>
<div><b>INNER JOIN</b> tree t</div>
<div><b>ON</b> tp.id = t.parent_id)</div>
<div><b>SELECT</b> *</div> 
<div><b>FROM</b> r</div>
<br>
<p>SQL query result:</p>
<table>
  <thead>
    <tr>
      <th align="center">ID</th>
      <th align="center">Name</th>
      <th align="center">Parent_ID</th>
      <th align="center">Level</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td align="center">7</td>
      <td align="center">H</td>
      <td align="center">6</td>
      <td align="center">1</td>
    </tr>
    <tr>
      <td align="center">8</td>
      <td align="center">K</td>
      <td align="center">6</td>
     <td align="center">1</td>
    </tr>
  </tbody>
</table>
<br>
<b>📜Example SQL query to get ancestors:</b><br><br>
<div><b>WITH RECURSIVE</b> r(id, name, parent_id, level) <b>AS</b></div>
<div>(<b>SELECT</b> tr.id, tr.name, tr.parent_id, 1</div>
<div><b>FROM</b> tree tl</div>
<div><b>LEFT JOIN</b> tree tr</div> 
<div><b>ON</b> tl.parent_id = tr.id</div>
<div><b>WHERE</b> tl.id = 6</div>
<div><b>UNION ALL</b></div>
<div><b>SELECT</b> t.id, t.name, t.parent_id, level+1</div>
<div><b>FROM</b> tree t, r</div>
<div><b>WHERE</b> t.id = r.parent_id)</div>
<div><b>SELECT</b> id, name, parent_id, <b>ROW_NUMBER() OVER</b> (<b>ORDER BY</b> level <b>DESC</b>) <b>AS</b> level</div>
<div><b>FROM</b> r</div>
<br>
<p>SQL query result:</p>
<table>
  <thead>
    <tr>
      <th align="center">ID</th>
      <th align="center">Name</th>
      <th align="center">Parent_ID</th>
      <th align="center">Level</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td align="center">1</td>
      <td align="center">A</td>
      <td align="center">NULL</td>
      <td align="center">1</td>
    </tr>
    <tr>
      <td align="center">5</td>
      <td align="center">C</td>
      <td align="center">1</td>
     <td align="center">2</td>
    </tr>
  </tbody>
</table>
<br>
<h2>Closure Table</h2>
<div align="center"><img src="https://hsto.org/getpro/habr/upload_files/6a2/74f/627/6a274f627873c43d43b18ca5de82a8c1.png" alt="Closure Table"></div>
</br></br>
<p>The Closure Table solution is one way of storing hierarchies in two tables and querying them efficiently without the need for recursive queries. It involves storing all possible paths between nodes and their corresponding names in separate tables. This method allows for quick retrieval of both descendants and ancestors of a given node, making it particularly useful for complex hierarchical data structures.</p>

<b>📜Example SQL query to get descendants:</b><br><br>
<div><b>SELECT</b> f.*, t.*</div>
<div><b>FROM</b> file_name f</div>
<div><b>INNER JOIN</b> tree_path t</div> 
<div><b>ON</b> f.id = t.descendant</div>
<div><b>WHERE</b> t.ancestor=5</div>
<div><b>AND</b> t.descendant!=t.ancestor</div>
<br>
<p>SQL query result:</p>
<table>
  <thead>
    <tr>
      <th align="center">ID</th>
      <th align="center">Name</th>
      <th align="center">Ancestor</th>
      <th align="center">Descendant</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td align="center">6</td>
      <td align="center">F</td>
      <td align="center">5</td>
      <td align="center">6</td>
    </tr>
    <tr>
      <td align="center">7</td>
      <td align="center">H</td>
      <td align="center">5</td>
     <td align="center">7</td>
    </tr>
    <tr>
      <td align="center">8</td>
      <td align="center">K</td>
      <td align="center">5</td>
     <td align="center">8</td>
    </tr>
       <tr>
      <td align="center">9</td>
      <td align="center">G</td>
      <td align="center">5</td>
     <td align="center">9</td>
    </tr>
  </tbody>
</table>
<br>
<b>📜Example SQL query to get ancestors:</b><br><br>
<div><b>SELECT</b> f.*, t.*</div>
<div><b>FROM</b> file_name f</div>
<div><b>INNER JOIN</b> tree_path t</div> 
<div><b>ON</b> f.id = t.ancestor</div>
<div><b>WHERE</b> t.descendant=5</div> 
<div><b>AND</b> t.descendant != t.ancestor</div>
<br>
<p>SQL query result:</p>
<table>
  <thead>
    <tr>
     <th align="center">ID</th>
      <th align="center">Name</th>
      <th align="center">Ancestor</th>
      <th align="center">Descendant</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td align="center">1</td>
      <td align="center">A</td>
      <td align="center">1</td>
      <td align="center">5</td>
    </tr>
  </tbody>
</table>
<br>

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
<b>📜Example SQL query to get descendants:</b><br><br>
<div><b>SELECT</b> *</div>
<div><b>FROM</b> NESTED_SETS</div> 
<div><b>WHERE</b> LFT > 2 <b>AND</b> RGT < 11</div>
<br>
<p>SQL query result:</p>
<table>
  <thead>
    <tr>
      <th align="center">ID</th>
      <th align="center">Name</th>
      <th align="center">LFT</th>
      <th align="center">RGT</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td align="center">4</td>
      <td align="center">F</td>
      <td align="center">5</td>
      <td align="center">10</td>
    </tr>
    <tr>
      <td align="center">5</td>
      <td align="center">G</td>
      <td align="center">3</td>
     <td align="center">4</td>
    </tr>
    <tr>
      <td align="center">6</td>
      <td align="center">H</td>
      <td align="center">6</td>
      <td align="center">7</td>
    </tr>
    <tr>
      <td align="center">7</td>
      <td align="center">K</td>
      <td align="center">8</td>
     <td align="center">9</td>
    </tr>
  </tbody>
</table>
<br>
<b>📜Example SQL query to get ancestors:</b><br><br>
<div><b>SELECT</b> *</div> 
<div><b>FROM</b> NESTED_SETS</div> 
<div><b>WHERE</b> LFT < 2 <b>AND</b> RGT > 11</div>
<br>
<p>SQL query result:</p>
<table>
  <thead>
    <tr>
      <th align="center">ID</th>
      <th align="center">Name</th>
      <th align="center">LFT</th>
      <th align="center">RGT</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td align="center">1</td>
      <td align="center">A</td>
      <td align="center">1</td>
      <td align="center">18</td>
    </tr>
  </tbody>
</table>
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
 <br>
 <p>⚠️ <b>Note:</b> <i>The string functions are taken from PostgreSQL, so they will be different for other databases.</i></p>
 <p><b><i>The SQL query examples consider the node with ID=6.</i></b></p>
 <b>📜Example SQL query to get descendants:</b><br><br>
<div><b>SELECT</b> *, <b>DENSE_RANK() OVER</b> (<b>ORDER BY</b> (<b>length</b>(path) - <b>length</b>(<b>replace</b>(path, '\', ''))) <b>ASC</b>) AS LEVEL</div>
<div><b>FROM</b> files</div>
<div><b>WHERE</b> path <b>like REPLACE</b> ('A\C\F' || '\' ||'%','\','\\')	</div>
<br>
<p>SQL query result:</p>
<table>
  <thead>
    <tr>
      <th align="center">ID</th>
      <th align="center">Name</th>
      <th align="center">Path</th>
      <th align="center">Level</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td align="center">7</td>
      <td align="center">H</td>
      <td align="center">A\C\F\H</td>
      <td align="center">1</td>
    </tr>
    <tr>
      <td align="center">8</td>
      <td align="center">K</td>
      <td align="center">A\C\F\K</td>
     <td align="center">1</td>
    </tr>
  </tbody>
</table>
<br>
<b>📜Example SQL query to get ancestors:</b><br><br>
<p>⚠️ <b>Note:</b> <i>Recursive query is only needed here to split the string and build a table of all parent paths.</i><p>
<div><b>WITH RECURSIVE</b> cte (PATH, LEVEL) <b>AS</b></div>
<div>(<b>SELECT split_part</b>(PATH,'\', 1), 1 <b>AS</b> LEVEL</div>
<div><b>FROM</b> files</div>
<div><b>WHERE</b> id = 6</div>
<div><b>UNION ALL</b></div> 
<div><b>SELECT CONCAT</b> (c.path, '\', <b>split_part</b>(f.path, '\', LEVEL+1)), LEVEL + 1</div>
<div><b>FROM</b> cte c, files f</div>
<div><b>WHERE</b> f.id = 6 AND f.path!=c.path)</div>
<div><b>SELECT</b> f.*, c.level</div>
<div><b>FROM</b> cte c, files f</div>
<div><b>WHERE</b> f.path=c.path</div> 
<div><b>AND</b> c.level !=</div> 
<div>(<b>SELECT MAX</b>(level)</div> 
<div><b>FROM</b> cte)</div>
<br>
<p>SQL query result:</p>
<table>
  <thead>
    <tr>
     <th align="center">ID</th>
      <th align="center">Name</th>
      <th align="center">Path</th>
      <th align="center">Level</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td align="center">1</td>
      <td align="center">A</td>
      <td align="center">A</td>
      <td align="center">1</td>
    </tr>
    <tr>
      <td align="center">5</td>
      <td align="center">C</td>
      <td align="center">A\C</td>
      <td align="center">2</td>
    </tr>
  </tbody>
</table>

<hr>
<h2 align="left">Publishing on Habrahabr: <a href="https://habr.com/ru/post/537062/">https://habr.com/ru/post/537062/</a></h1>
