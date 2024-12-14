package com.aiep.evsum2.repository.rowmapper;

import com.aiep.evsum2.domain.Jefes;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Jefes}, with proper type conversions.
 */
@Service
public class JefesRowMapper implements BiFunction<Row, String, Jefes> {

    private final ColumnConverter converter;

    public JefesRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Jefes} stored in the database.
     */
    @Override
    public Jefes apply(Row row, String prefix) {
        Jefes entity = new Jefes();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setNombrejefe(converter.fromRow(row, prefix + "_nombrejefe", String.class));
        entity.setTelefonojefe(converter.fromRow(row, prefix + "_telefonojefe", String.class));
        return entity;
    }
}
