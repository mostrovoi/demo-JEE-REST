FROM gencatcloud/java:7

VOLUME /tmp

COPY run.sh /entrypoint.sh
COPY ../../../../target/*.war /tmp/app.war
RUN chmod 775 /entrypoint.sh

CMD ["/entrypoint.sh"]