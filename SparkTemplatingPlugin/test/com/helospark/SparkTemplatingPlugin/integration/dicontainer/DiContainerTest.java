package com.helospark.SparkTemplatingPlugin.integration.dicontainer;

import org.testng.annotations.Test;

import com.helospark.sparktemplatingplugin.DiContainer;

public class DiContainerTest {

    @Test
    public void testDiContainerInitializeShouldNotThrowAnyExceptions() {
        // GIVEN
        DiContainer.clearDiContainer();

        // WHEN
        DiContainer.initializeDiContainer();

        // THEN should not throw
    }
}
