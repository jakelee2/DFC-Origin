package org.dbadmin.repository;

import java.util.Collection;

import org.dbadmin.model.Route;

/**
 * Created by henrynguyen on 5/7/16.
 */
public interface RouteRepository {
  Collection<Route> findAllRoutes();

  Route findRouteById(int id);

  Collection<Route> findRouteByUrl(String url);

  void saveRoute(Route route);

  void deleteRoute(Route route);

}
