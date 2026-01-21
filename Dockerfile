FROM sbtscala/scala-sbt:eclipse-temurin-24.0.1_9_1.12.0_3.3.7

WORKDIR /thirty-one

RUN apt-get update && apt-get install -y \
    libxext6 \
    libxrender1 \
    libxtst6 \
    libxi6 \
    libxrandr2 \
    libxinerama1 \
    libxcursor1 \
    && rm -rf /var/lib/apt/lists/*

COPY build.sbt /thirty-one/
COPY project/ /thirty-one/project/
RUN sbt -batch update

COPY src/ /thirty-one/src/
RUN sbt -batch compile

CMD sbt run
