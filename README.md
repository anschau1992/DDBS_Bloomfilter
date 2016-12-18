<<<<<<< HEAD
This is the new test repository for the ddbs_bloomfilter 2016, another test for auto-deployment again and againg


Abgabe: 19.12.16



#TODO:
*Aufbau des Experimentes --> Besprechung mit Kevin
*Hashfunctions-Familie --> Kombination möglich? ==> Mirko
*Setup Latex-report + Aufbau des Reports ==> Andy
*Experimentdurchführung (mit k & l spielen)
=======
#UZH - Distributed Database Systems HS2016
####Mirco ??? & Andreas Schaufelbühl


###Setup
* Install local mySQL: https://dev.mysql.com/downloads/installer/
* Integrate test_db containing test Data into mySQL DB: https://github.com/datacharmer/test_db

###Project structure
The project is divided into the three following parts:
* client: contains the client-side code
* server: contains the server-side code
* shared: contains code used by both sides (BloomFilter, Employees,..)


###Start
* Run ApplicationServer.main() first
* Run Client.main()

TODO:
* Integration of two good hash-functions
* Compare size of returning message with BloomFilter vs. classic Join

Links:
* Bloomfilter: http://billmill.org/bloomfilter-tutorial/
* RMI: http://docs.oracle.com/javase/7/docs/technotes/guides/rmi/hello/hello-world.html
* Java SizeOf:
    * GitHub: https://github.com/dweiss/java-sizeof/blob/master/src/main/java/com/carrotsearch/sizeof/RamUsageEstimator.java
    * Maven: https://mvnrepository.com/artifact/com.carrotsearch/java-sizeof
    * Calculates the size of an object in (Bytes?)


>>>>>>> develop
