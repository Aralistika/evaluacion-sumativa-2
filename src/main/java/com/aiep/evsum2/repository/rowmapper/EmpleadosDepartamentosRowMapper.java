package com.aiep.evsum2.repository.rowmapper;

import com.aiep.evsum2.domain.EmpleadosDepartamentos;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link EmpleadosDepartamentos}, with proper type conversions.
 */
@Service
public class EmpleadosDepartamentosRowMapper implements BiFunction<Row, String, EmpleadosDepartamentos> {

    private final ColumnConverter converter;

    public EmpleadosDepartamentosRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link EmpleadosDepartamentos} stored in the database.
     */
    @Override
    public EmpleadosDepartamentos apply(Row row, String prefix) {
        EmpleadosDepartamentos entity = new EmpleadosDepartamentos();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setEmpleadosId(converter.fromRow(row, prefix + "_empleados_id", Long.class));
        entity.setDepartamentosId(converter.fromRow(row, prefix + "_departamentos_id", Long.class));
        return entity;
    }
}
