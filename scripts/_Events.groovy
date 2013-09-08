import org.apache.catalina.connector.Connector
import org.apache.catalina.startup.Tomcat

eventConfigureTomcat = { Tomcat tomcat ->
	def connector = new Connector("org.apache.coyote.http11.Http11NioProtocol")
	connector.port = System.getProperty("server.port", "8080").toInteger()
	connector.redirectPort = 8443
	connector.protocol = "HTTP/1.1"
	connector.connectionTimeout = 20000
 
	tomcat.connector = connector
	tomcat.service.addConnector connector
	log.debug "Configured tomcat"
}