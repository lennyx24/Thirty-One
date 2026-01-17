FROM sbtscala/scala-sbt:eclipse-temurin-24.0.1_9_1.12.0_3.3.7
WORKDIR /thirty-one
ADD . /thirty-one/
RUN apt-get update && apt-get install -y \
    libxext6 \
    libxrender1 \
    libxtst6 \
    libxi6 \
    libxrandr2 \
    && rm -rf /var/lib/apt/lists/*
CMD sbt run

#docker run -it -e DISPLAY=host.docker.internal:0 \
#           -v /tmp/.X11-unix:/tmp/.X11-unix \
#           thirty-one