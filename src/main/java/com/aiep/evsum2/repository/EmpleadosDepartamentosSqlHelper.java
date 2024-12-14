package com.aiep.evsum2.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class EmpleadosDepartamentosSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));

        columns.add(Column.aliased("empleados_id", table, columnPrefix + "_empleados_id"));
        columns.add(Column.aliased("departamentos_id", table, columnPrefix + "_departamentos_id"));
        return columns;
    }
}
