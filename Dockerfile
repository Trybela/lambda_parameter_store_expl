FROM gradle:6.7.0-jdk11 as builder
COPY --chown=gradle:gradle . /home/application
WORKDIR /home/application
RUN gradle build --no-daemon
FROM amazonlinux:2.0.20200722.0 as graalvm

ENV LANG=en_US.UTF-8

RUN yum install -y gcc gcc-c++ libc6-dev  zlib1g-dev curl bash zlib zlib-devel zip gzip tar

ENV GRAAL_VERSION 20.2.0
ENV JDK_VERSION java11
ENV GRAAL_FILENAME graalvm-ce-${JDK_VERSION}-linux-amd64-${GRAAL_VERSION}.tar.gz

RUN curl -4 -L https://github.com/graalvm/graalvm-ce-builds/releases/download/vm-${GRAAL_VERSION}/${GRAAL_FILENAME} -o /tmp/${GRAAL_FILENAME}

RUN tar -zxvf /tmp/${GRAAL_FILENAME} -C /tmp \
    && mv /tmp/graalvm-ce-${JDK_VERSION}-${GRAAL_VERSION} /usr/lib/graalvm

RUN rm -rf /tmp/*
CMD ["/usr/lib/graalvm/bin/native-image"]

FROM graalvm
COPY --from=builder /home/application/ /home/application/
WORKDIR /home/application
RUN /usr/lib/graalvm/bin/gu install native-image
RUN /usr/lib/graalvm/bin/native-image -H:Name=lambda_parameter_store_expl \
                                      -H:Class=com.example.ExampleLambdaRuntime \
                                      -H:ResourceConfigurationFiles=src/main/resources/resource-config.json \
                                      -H:DynamicProxyConfigurationFiles=src/main/resources/dynamic-proxies.json \
                                      -H:+TraceClassInitialization \
                                      -H:+ReportExceptionStackTraces \
                                      --report-unsupported-elements-at-runtime --no-fallback --no-server -cp build/libs/lambda_parameter_store_expl-*-all.jar
RUN chmod 777 bootstrap
RUN chmod 777 lambda_parameter_store_expl
RUN zip -j function.zip bootstrap lambda_parameter_store_expl
ENTRYPOINT ["/home/application/lambda_parameter_store_expl"]
