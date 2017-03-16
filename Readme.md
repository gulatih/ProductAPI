Step 1. Install Cassandra on the desired node. DataStax community version could be used for this. It can be downloaded from https://academy.datastax.com/planet-cassandra//cassandra/

Step 2. Create the desired keyspace. For this project target_keyspace needs to be created.

Step 3. Create Products Table to query for.

Step 4. Install the war file to tomcat. Make sure you update the context.xml file for the cassandra node and port.

Step 5. None of the resource I have created are protected so can be reached from a url without any authentication.

These are the queries and their results:

1. http://localhost:8080/services/product/all
Result: [{"id":13860430,"name":"Jade Yoga Mat","current_price":{"value":120.50,"currency_code":"USD"}},{"id":13860429,"name":"Accent Chair","current_price":{"value":240.89,"currency_code":"USD"}},{"id":13860428,"name":"The Big Lebowski(Blue-Ray)(WideScreen)","current_price":{"value":13.49,"currency_code":"USD"}},{"id":13860431,"name":"Tuffed Sofa","current_price":{"value":90.50,"currency_code":"USD"}}]


2. http://localhost:8080/services/product/13860431/
Result: [{"id":13860431,"name":"Tuffed Sofa","current_price":{"value":90.50,"currency_code":"USD"}}]


3. http://localhost:8080/services/product/name
Result: The Big Lebowski (Blu-ray)


4. http://localhost:8080/services/product/13860430/Jade Yoga Mat/price
Result: [{"id":13860430,"name":"Jade Yoga Mat","current_price":{"value":120.50,"currency_code":"USD"}}]


