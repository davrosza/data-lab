FROM apache/airflow:2.10.1
COPY requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt
USER root
RUN apt update && apt install -y openjdk-17-jdk wget
RUN wget -P /opt/bitnami/spark/jars/ https://repo1.maven.org/maven2/org/apache/iceberg/iceberg-spark-runtime-3.5_2.12/1.7.1/iceberg-spark-runtime-3.5_2.12-1.7.1.jar
RUN wget -P /opt/bitnami/spark/jars/ https://repo1.maven.org/maven2/org/apache/iceberg/iceberg-spark-extensions-3.5_2.12/1.7.1/iceberg-spark-extensions-3.5_2.12-1.7.1.jar
RUN wget -P /opt/bitnami/spark/jars/ https://repo1.maven.org/maven2/org/apache/hadoop/hadoop-aws/3.3.1/hadoop-aws-3.3.1.jar
RUN wget -P /opt/bitnami/spark/jars/ https://repo1.maven.org/maven2/com/amazonaws/aws-java-sdk-bundle/1.12.300/aws-java-sdk-bundle-1.12.300.jar
COPY entrypoint.sh /entrypoint.sh
RUN chmod +x /entrypoint.sh
USER airflow
ENV JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
ENV SPARK_HOME=/home/airflow/.local/lib/pythn3.12/site-package/pyspark
ENTRYPOINT [ "/entrypoint.sh" ]