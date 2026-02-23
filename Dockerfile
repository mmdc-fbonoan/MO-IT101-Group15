FROM ubuntu:24.04

ENV DEBIAN_FRONTEND=noninteractive

# Install Java 25 (make sure 'universe' is enabled)
RUN apt-get update \
  && apt-get install -y --no-install-recommends software-properties-common ca-certificates \
  && add-apt-repository -y universe \
  && apt-get update \
  && apt-get install -y --no-install-recommends openjdk-25-jdk \
  && apt-get clean 
WORKDIR /app

# Nice default: open a shell
CMD ["bash"]
