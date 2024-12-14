package com.aiep.evsum2.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class DepartamentosJefesTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static DepartamentosJefes getDepartamentosJefesSample1() {
        return new DepartamentosJefes().id(1L);
    }

    public static DepartamentosJefes getDepartamentosJefesSample2() {
        return new DepartamentosJefes().id(2L);
    }

    public static DepartamentosJefes getDepartamentosJefesRandomSampleGenerator() {
        return new DepartamentosJefes().id(longCount.incrementAndGet());
    }
}
