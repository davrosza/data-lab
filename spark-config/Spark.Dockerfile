FROM bitnami/spark:3.5.4
USER root
COPY ./spark-defaults.conf /opt/bitnami/spark/conf/spark-defaults.conf
RUN apt update && apt install -y openjdk-17-jdk wget
RUN wget -P /opt/bitnami/spark/jars/ https://repo1.maven.org/maven2/org/apache/iceberg/iceberg-spark-runtime-3.5_2.12/1.7.1/iceberg-spark-runtime-3.5_2.12-1.7.1.jar
RUN wget -P /opt/bitnami/spark/jars/ https://repo1.maven.org/maven2/org/apache/iceberg/iceberg-spark-extensions-3.5_2.12/1.7.1/iceberg-spark-extensions-3.5_2.12-1.7.1.jar
RUN wget -P /opt/bitnami/spark/jars/ https://repo1.maven.org/maven2/org/apache/hadoop/hadoop-aws/3.3.1/hadoop-aws-3.3.1.jar
RUN wget -P /opt/bitnami/spark/jars/ https://repo1.maven.org/maven2/com/amazonaws/aws-java-sdk-bundle/1.12.300/aws-java-sdk-bundle-1.12.300.jar
