package com.aiep.evsum2.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class EmpleadosDepartamentosTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static EmpleadosDepartamentos getEmpleadosDepartamentosSample1() {
        return new EmpleadosDepartamentos().id(1L);
    }

    public static EmpleadosDepartamentos getEmpleadosDepartamentosSample2() {
        return new EmpleadosDepartamentos().id(2L);
    }

    public static EmpleadosDepartamentos getEmpleadosDepartamentosRandomSampleGenerator() {
        return new EmpleadosDepartamentos().id(longCount.incrementAndGet());
    }
}
