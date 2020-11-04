package com.example;

import com.amazonaws.auth.AWS4Signer;
import com.amazonaws.internal.config.*;
import com.amazonaws.partitions.model.*;
import com.oracle.svm.core.annotate.AutomaticFeature;
import lombok.extern.slf4j.Slf4j;
import org.graalvm.nativeimage.hosted.Feature;
import org.graalvm.nativeimage.hosted.RuntimeReflection;

import java.util.List;


@Slf4j
@AutomaticFeature
public class CustomReflectionFeature implements Feature {

    private static final List<Class<?>> reflectionClassesForRegistration = List.of(
            InternalConfigJsonHelper.class, InternalConfig.class, SignerConfigJsonHelper.class, SignerConfig.class,
            JsonIndex.class, HttpClientConfigJsonHelper.class, HttpClientConfig.class,
            HostRegexToRegionMappingJsonHelper.class, HostRegexToRegionMapping.class, AWS4Signer.class,
            Partitions.class, Partition.class, Endpoint.class, Region.class, Service.class, CredentialScope.class);

    @Override
    public void beforeAnalysis(BeforeAnalysisAccess access) {
        reflectionClassesForRegistration.forEach(this::registerReflectionClass);
        log.info("REFLECTION AUTOMATIC DETECTION: {} classes were successfully registered.", reflectionClassesForRegistration.size());
    }

    private void registerReflectionClass(Class<?> clazz) {
        RuntimeReflection.register(clazz);
        RuntimeReflection.register(clazz.getDeclaredFields());
        RuntimeReflection.register(clazz.getDeclaredMethods());
        RuntimeReflection.register(clazz.getDeclaredConstructors());
    }
}
