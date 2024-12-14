package com.aiep.evsum2.repository.rowmapper;

import com.aiep.evsum2.domain.DepartamentosJefes;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link DepartamentosJefes}, with proper type conversions.
 */
@Service
public class DepartamentosJefesRowMapper implements BiFunction<Row, String, DepartamentosJefes> {

    private final ColumnConverter converter;

    public DepartamentosJefesRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link DepartamentosJefes} stored in the database.
     */
    @Override
    public DepartamentosJefes apply(Row row, String prefix) {
        DepartamentosJefes entity = new DepartamentosJefes();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setDepartamentosId(converter.fromRow(row, prefix + "_departamentos_id", Long.class));
        entity.setJefesId(converter.fromRow(row, prefix + "_jefes_id", Long.class));
        return entity;
    }
}
