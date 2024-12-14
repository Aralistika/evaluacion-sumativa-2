package com.aiep.evsum2.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class EmpleadosTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Empleados getEmpleadosSample1() {
        return new Empleados()
            .id(1L)
            .nombreempleado("nombreempleado1")
            .apellidoempleado("apellidoempleado1")
            .telefonoempleado("telefonoempleado1")
            .correoempleado("correoempleado1");
    }

    public static Empleados getEmpleadosSample2() {
        return new Empleados()
            .id(2L)
            .nombreempleado("nombreempleado2")
            .apellidoempleado("apellidoempleado2")
            .telefonoempleado("telefonoempleado2")
            .correoempleado("correoempleado2");
    }

    public static Empleados getEmpleadosRandomSampleGenerator() {
        return new Empleados()
            .id(longCount.incrementAndGet())
            .nombreempleado(UUID.randomUUID().toString())
            .apellidoempleado(UUID.randomUUID().toString())
            .telefonoempleado(UUID.randomUUID().toString())
            .correoempleado(UUID.randomUUID().toString());
    }
}
