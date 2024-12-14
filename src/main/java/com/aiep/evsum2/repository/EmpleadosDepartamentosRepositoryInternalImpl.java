package com.aiep.evsum2.repository;

import com.aiep.evsum2.domain.EmpleadosDepartamentos;
import com.aiep.evsum2.repository.rowmapper.DepartamentosRowMapper;
import com.aiep.evsum2.repository.rowmapper.EmpleadosDepartamentosRowMapper;
import com.aiep.evsum2.repository.rowmapper.EmpleadosRowMapper;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.convert.R2dbcConverter;
import org.springframework.data.r2dbc.core.R2dbcEntityOperations;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.repository.support.SimpleR2dbcRepository;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Comparison;
import org.springframework.data.relational.core.sql.Condition;
import org.springframework.data.relational.core.sql.Conditions;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Select;
import org.springframework.data.relational.core.sql.SelectBuilder.SelectFromAndJoinCondition;
import org.springframework.data.relational.core.sql.Table;
import org.springframework.data.relational.repository.support.MappingRelationalEntityInformation;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.r2dbc.core.RowsFetchSpec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC custom repository implementation for the EmpleadosDepartamentos entity.
 */
@SuppressWarnings("unused")
class EmpleadosDepartamentosRepositoryInternalImpl
    extends SimpleR2dbcRepository<EmpleadosDepartamentos, Long>
    implements EmpleadosDepartamentosRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final EmpleadosRowMapper empleadosMapper;
    private final DepartamentosRowMapper departamentosMapper;
    private final EmpleadosDepartamentosRowMapper empleadosdepartamentosMapper;

    private static final Table entityTable = Table.aliased("empleados_departamentos", EntityManager.ENTITY_ALIAS);
    private static final Table empleadosTable = Table.aliased("empleados", "empleados");
    private static final Table departamentosTable = Table.aliased("departamentos", "departamentos");

    public EmpleadosDepartamentosRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        EmpleadosRowMapper empleadosMapper,
        DepartamentosRowMapper departamentosMapper,
        EmpleadosDepartamentosRowMapper empleadosdepartamentosMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(EmpleadosDepartamentos.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.empleadosMapper = empleadosMapper;
        this.departamentosMapper = departamentosMapper;
        this.empleadosdepartamentosMapper = empleadosdepartamentosMapper;
    }

    @Override
    public Flux<EmpleadosDepartamentos> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<EmpleadosDepartamentos> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = EmpleadosDepartamentosSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(EmpleadosSqlHelper.getColumns(empleadosTable, "empleados"));
        columns.addAll(DepartamentosSqlHelper.getColumns(departamentosTable, "departamentos"));
        SelectFromAndJoinCondition selectFrom = Select.builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(empleadosTable)
            .on(Column.create("empleados_id", entityTable))
            .equals(Column.create("id", empleadosTable))
            .leftOuterJoin(departamentosTable)
            .on(Column.create("departamentos_id", entityTable))
            .equals(Column.create("id", departamentosTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, EmpleadosDepartamentos.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<EmpleadosDepartamentos> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<EmpleadosDepartamentos> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    @Override
    public Mono<EmpleadosDepartamentos> findOneWithEagerRelationships(Long id) {
        return findById(id);
    }

    @Override
    public Flux<EmpleadosDepartamentos> findAllWithEagerRelationships() {
        return findAll();
    }

    @Override
    public Flux<EmpleadosDepartamentos> findAllWithEagerRelationships(Pageable page) {
        return findAllBy(page);
    }

    private EmpleadosDepartamentos process(Row row, RowMetadata metadata) {
        EmpleadosDepartamentos entity = empleadosdepartamentosMapper.apply(row, "e");
        entity.setEmpleados(empleadosMapper.apply(row, "empleados"));
        entity.setDepartamentos(departamentosMapper.apply(row, "departamentos"));
        return entity;
    }

    @Override
    public <S extends EmpleadosDepartamentos> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
