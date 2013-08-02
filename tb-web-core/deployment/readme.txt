
================
Deployment
================

1, use 'mysql-connector-java-5.1.25', copy 'mysql-connector-java-5.1.25-bin.jar' to <jboss_home>/server/default/lib/
2, cp jboss-logging.xml to  <jboss_home>/server/default/deploy/
3, change jaas to support utf-8 encoding, change <jboss_home>/server/default/deploy/jbossweb.sar/context.xml
	add '<Valve className="org.apache.catalina.authenticator.FormAuthenticator" characterEncoding="utf-8" />'
4, upgrade jsf, from 2.0.3(b05) to 2.0.11