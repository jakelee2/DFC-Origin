package org.dbadmin.repository.jdbc;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.dbadmin.model.Route;
import org.dbadmin.repository.RouteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.stereotype.Repository;

/**
 * Created by henrynguyen on 5/7/16.
 */

@Repository
public class JdbcRouteRepositoryImpl implements RouteRepository {

  private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  private SimpleJdbcInsert insertRoute;

  @Autowired
  public JdbcRouteRepositoryImpl(DataSource dataSource,
      NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
    this.insertRoute =
        new SimpleJdbcInsert(dataSource).withTableName("routes").usingGeneratedKeyColumns("id");

    this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
  }

  @Override
  public Collection<Route> findAllRoutes() {
    Map<String, Object> params = new HashMap<>();
    List<Route> routes =
        this.namedParameterJdbcTemplate.query("SELECT id, url, access, priority FROM routes",
            params, BeanPropertyRowMapper.newInstance(Route.class));
    return routes;
  }

  @Override
  public Route findRouteById(int id) {
    Route route;
    try {
      Map<String, Object> params = new HashMap<>();
      params.put("id", id);
      route = this.namedParameterJdbcTemplate.queryForObject(
          "SELECT id, url, access, priority FROM routes WHERE id= :id", params,
          BeanPropertyRowMapper.newInstance(Route.class));
    } catch (EmptyResultDataAccessException ex) {
      throw new ObjectRetrievalFailureException(Route.class, id);
    }
    return route;
  }

  @Override
  public Collection<Route> findRouteByUrl(String url) {
    Map<String, Object> params = new HashMap<>();
    params.put("url", url + "%");
    List<Route> routes = this.namedParameterJdbcTemplate.query(
        "SELECT id, url, access, priority FROM routes WHERE url like :url", params,
        BeanPropertyRowMapper.newInstance(Route.class));
    return routes;
  }

  @Override
  public void saveRoute(Route route) {
    BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(route);

    if (route.isNew()) {
      Number newKey = this.insertRoute.executeAndReturnKey(parameterSource);
      route.setId(newKey.intValue());
    } else {
      this.namedParameterJdbcTemplate.update(
          "UPDATE routes SET url=:url, access=:access, priority=:priority " + " WHERE id=:id",
          parameterSource);

    }
  }

  @Override
  public void deleteRoute(Route route) {
    BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(route);
    this.namedParameterJdbcTemplate.update("DELETE FROM routes" + " WHERE id=:id", parameterSource);
  }
}
