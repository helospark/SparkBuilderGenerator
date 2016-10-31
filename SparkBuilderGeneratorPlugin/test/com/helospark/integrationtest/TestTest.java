package com.helospark.integrationtest;

import static org.mockito.MockitoAnnotations.initMocks;

import org.eclipse.ui.activities.ICategory;
import org.junit.Test;
import org.mockito.Mock;

public class TestTest {

    @Mock
    private ICategory asd;

    @Test
    public void doesThisWork() {
        initMocks(this);
    }
}
