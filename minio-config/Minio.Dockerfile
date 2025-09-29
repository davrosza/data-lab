FROM quay.io/minio/mc:latest
COPY entrypoint-minio.sh /entrypoint.sh
RUN chmod +x /entrypoint.sh
ENTRYPOINT [ "/entrypoint.sh" ]