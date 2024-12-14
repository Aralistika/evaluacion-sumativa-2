package com.aiep.evsum2.repository.rowmapper;

import com.aiep.evsum2.domain.Departamentos;
import io.r2dbc.spi.Row;
import java.math.BigDecimal;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Departamentos}, with proper type conversions.
 */
@Service
public class DepartamentosRowMapper implements BiFunction<Row, String, Departamentos> {

    private final ColumnConverter converter;

    public DepartamentosRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Departamentos} stored in the database.
     */
    @Override
    public Departamentos apply(Row row, String prefix) {
        Departamentos entity = new Departamentos();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setNombredepartamento(converter.fromRow(row, prefix + "_nombredepartamento", String.class));
        entity.setUbicaciondepartamento(converter.fromRow(row, prefix + "_ubicaciondepartamento", String.class));
        entity.setPresupuestodepartamento(converter.fromRow(row, prefix + "_presupuestodepartamento", BigDecimal.class));
        return entity;
    }
}
