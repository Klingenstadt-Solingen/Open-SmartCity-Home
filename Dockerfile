FROM bellsoft/liberica-openjdk-alpine:21.0.7 AS open-smartcity-home

LABEL \
  maintainer="Nedeco" \
  org.opencontainers.image.title="Open Smart City Home" \
  org.opencontainers.image.description="Open Smart City Home integrates Smart City systems from the German city of Solingen into smart home environments." \
  org.opencontainers.image.authors="Nedeco"

COPY ./build/libs/open-smartcity-home-*.jar /open-smartcity-home.jar

ENTRYPOINT ["java", "-jar", "/open-smartcity-home.jar"]
