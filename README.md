# icap
This application calculates prime numbers from the first one (2) up to a max range that you would be providing.

1- Build
	mvn clean install
	
2- Run
	java -cp prime.number.jar com.icap.prime.number.Application

3- Eclipse/STS IDE (Developed with STS 3.7.3.RELEASE)
	within Eclipse, you should be able to find an application launcher called PrimeNumberApplication.

This will start the application with Spring Boot using an embedded Tomcat 8.

4- To use the application, use your favorite browser

	4.1- Using response stream output
	http://localhost:8080/stream/<maxRange> where <maxRange> should be replaced by a positive integer.
This url would write the list of prime numbers directly into the output stream.

	4.2- Using a list 
	http://localhost:8080/<maxRange> where <maxRange> should be replaced by a positive integer.
This url would stored the list of prime numbers into a variable and wait the calculation to fully complete before displaying the result.

