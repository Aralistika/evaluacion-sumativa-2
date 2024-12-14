package com.aiep.evsum2.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class DepartamentosSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("nombredepartamento", table, columnPrefix + "_nombredepartamento"));
        columns.add(Column.aliased("ubicaciondepartamento", table, columnPrefix + "_ubicaciondepartamento"));
        columns.add(Column.aliased("presupuestodepartamento", table, columnPrefix + "_presupuestodepartamento"));

        return columns;
    }
}
