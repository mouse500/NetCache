FROM openjdk:8

ENV P_HOME /opt/netcache
ENV JAR_FILE ROOT.jar

RUN set -e \
    && mkdir -p ${P_HOME}

##COPY to the home
COPY . ${P_HOME}/
EXPOSE 8088
WORKDIR ${P_HOME}

RUN ./gradlew build -x test
RUN rm -f ./${JAR_FILE}
RUN mv ./build/libs/${JAR_FILE} .

CMD java -XX:+UseParallelGC -jar ${JAR_FILE}
