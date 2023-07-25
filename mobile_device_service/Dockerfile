FROM openjdk:17 as wildfly_with_grpc

# Needed until gRPC is integrated into Wildfly
USER root
ENV GALEON_VERSION 5.2.0.Final
ENV JBOSS_HOME /opt/jboss/wildfly

RUN curl -Ls https://github.com/wildfly/galleon/releases/download/$GALEON_VERSION/galleon-$GALEON_VERSION.zip -o /tmp/galeon.zip
RUN microdnf install unzip
RUN unzip /tmp/galeon.zip -d /tmp
RUN /tmp/galleon*/bin/galleon.sh install wildfly:current --dir=$JBOSS_HOME
RUN /tmp/galleon*/bin/galleon.sh install org.wildfly.extras.grpc:wildfly-grpc-feature-pack:0.1.2.Final --layers=grpc --dir=$JBOSS_HOME

FROM wildfly_with_grpc as wildfly_grpc_mysql

ENV DB_NAME MobileDevice
ENV DB_USER root
ENV DB_PASS admin
ENV DB_URI mysql-mobile-device:3306

ENV MYSQL_VERSION 8.0.30
ENV JBOSS_CLI /opt/jboss/wildfly/bin/jboss-cli.sh

RUN echo "=> Starting WildFly server" && \
      bash -c '$JBOSS_HOME/bin/standalone.sh &' && \
    echo "=> Waiting for the server to boot" && \
      bash -c 'until `$JBOSS_CLI -c ":read-attribute(name=server-state)" 2> /dev/null | grep -q running`; do echo `$JBOSS_CLI -c ":read-attribute(name=server-state)" 2> /dev/null`; sleep 1; done' && \
    echo "=> Downloading MySQL driver" && \
      curl -Ls https://repo1.maven.org/maven2/mysql/mysql-connector-java/${MYSQL_VERSION}/mysql-connector-java-${MYSQL_VERSION}.jar -o /tmp/mysql.jar && \
    echo "=> Adding MySQL module" && \
      $JBOSS_CLI --connect --command="module add --name=com.mysql.driver --resources=/tmp/mysql.jar --dependencies=javax.api,javax.transaction.api" && \
    echo "=> Adding MySQL driver" && \
      $JBOSS_CLI --connect --command="/subsystem=datasources/jdbc-driver=mysql:add(driver-name=mysql,driver-module-name=com.mysql.driver,driver-class-name=com.mysql.jdbc.Driver)" && \
    echo "=> Creating a new datasource" && \
      $JBOSS_CLI --connect --command="data-source add \
        --name=${DB_NAME}DS \
        --jndi-name=java:/jdbc/datasources/${DB_NAME}DS \
        --user-name=${DB_USER} \
        --password=${DB_PASS} \
        --driver-name=mysql \
        --connection-url=jdbc:mysql://${DB_URI}/${DB_NAME}" && \
    echo "=> Shutting down WildFly and Cleaning up" && \
      $JBOSS_CLI --connect --command=":shutdown" && \
      rm -rf $JBOSS_HOME/standalone/configuration/standalone_xml_history/ $JBOSS_HOME/standalone/log/* && \
      rm -rf /tmp/*


FROM wildfly_grpc_mysql

COPY /target/*.war /opt/jboss/wildfly/standalone/deployments

CMD ["/opt/jboss/wildfly/bin/standalone.sh"]