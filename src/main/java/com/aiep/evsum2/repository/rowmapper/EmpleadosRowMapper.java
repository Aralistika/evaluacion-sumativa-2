package com.aiep.evsum2.repository.rowmapper;

import com.aiep.evsum2.domain.Empleados;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Empleados}, with proper type conversions.
 */
@Service
public class EmpleadosRowMapper implements BiFunction<Row, String, Empleados> {

    private final ColumnConverter converter;

    public EmpleadosRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Empleados} stored in the database.
     */
    @Override
    public Empleados apply(Row row, String prefix) {
        Empleados entity = new Empleados();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setNombreempleado(converter.fromRow(row, prefix + "_nombreempleado", String.class));
        entity.setApellidoempleado(converter.fromRow(row, prefix + "_apellidoempleado", String.class));
        entity.setTelefonoempleado(converter.fromRow(row, prefix + "_telefonoempleado", String.class));
        entity.setCorreoempleado(converter.fromRow(row, prefix + "_correoempleado", String.class));
        return entity;
    }
}
