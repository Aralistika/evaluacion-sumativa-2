package com.aiep.evsum2.domain;

import static com.aiep.evsum2.domain.JefesTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.aiep.evsum2.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class JefesTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Jefes.class);
        Jefes jefes1 = getJefesSample1();
        Jefes jefes2 = new Jefes();
        assertThat(jefes1).isNotEqualTo(jefes2);

        jefes2.setId(jefes1.getId());
        assertThat(jefes1).isEqualTo(jefes2);

        jefes2 = getJefesSample2();
        assertThat(jefes1).isNotEqualTo(jefes2);
    }
}
