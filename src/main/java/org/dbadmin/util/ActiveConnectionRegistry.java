package org.dbadmin.util;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;

import org.dbadmin.model.DbConnection;
import org.springframework.stereotype.Repository;

/**
 * Utility class-singleton which holds current Database Connection properties
 * It is thread-safe.
 * 
 * 
 * Usage: ActiveConnectionRegistry.INSTANCE.<any public method>
 * 
 * @author denys
 *
 */
public enum ActiveConnectionRegistry {

  INSTANCE;
  private final CachedLinkedHashSet<DbConnection> activeDbConnections = new CachedLinkedHashSet<DbConnection>();
  
  // used for UI (jsp) representation of current (= latest) connection
  private String latest_ip = "";
  private String latest_dbname = "";
  private String latest_databaseJpaVendorName = "";
  private String latest_connId = "";


  public ActiveConnectionRegistry getInstance() {
    return INSTANCE;
  }

  public DbConnection getLatestActiveConnection() {
    if (!INSTANCE.activeDbConnections.isEmpty()) 
      return INSTANCE.activeDbConnections.getLast(); 
    return null;
  }
  
  public DbConnection getActiveConnectionById(String id) {
    if (!INSTANCE.activeDbConnections.isEmpty()) {
      for (DbConnection dbc : activeDbConnections) {
        if (Integer.valueOf(id.trim()).equals(dbc.getId())) return dbc;
      }
    }
    return new DbConnection(); //better then null
  }
  
  public List<DbConnection> getAllActiveConnections() {
    List<DbConnection> li = new LinkedList<>();
    li.addAll(activeDbConnections);
    return li;
  }

  public static void addActiveConnection(final DbConnection dbConnection) {
    dbConnection.setStatus("ACTIVE");
    INSTANCE.activeDbConnections.add(dbConnection);
    INSTANCE.latest_ip = RegexExtractor.extractIp(dbConnection.getDatabaseJdbcUrl());
    INSTANCE.latest_databaseJpaVendorName = dbConnection.getDatabaseJpaVendorName();
    INSTANCE.latest_dbname = RegexExtractor.extractDbName(dbConnection.getDatabaseJdbcUrl(),
                      INSTANCE.latest_databaseJpaVendorName);
    INSTANCE.latest_connId = Integer.toString(dbConnection.getId());
  }
  
  /**
   * Delete connection from List of active connections. No DAO deletion here.
   * @param dbConnection
   */
  public void deleteActiveConnection(String id) {
    if (!INSTANCE.activeDbConnections.isEmpty()) {
      boolean deleted = false;
      for (DbConnection dbc : activeDbConnections) {
        if (Integer.valueOf(id.trim()).equals(dbc.getId())) {
          activeDbConnections.remove(dbc);
          deleted = true;
          if (id.equals(INSTANCE.latest_connId)) {
            // clear values to show on sidebar.jsp;
            INSTANCE.latest_ip = "";
            INSTANCE.latest_dbname = "";
            INSTANCE.latest_databaseJpaVendorName = "";
            INSTANCE.latest_connId = "";
          }
          break; 
        } 
      }
      if (!deleted) throw new IllegalArgumentException("Can not find active connection with id= "+id);  
    }
    else
      throw new IllegalArgumentException("No active connections found");      
  }

  /*
   * Used in jsp
   */
  public String getLatestActiveConnectionIp() {
    return INSTANCE.latest_ip;
  }

  /*
   * Used in jsp
   */
  public String getLatestActiveConnectionDbName() {
    return INSTANCE.latest_dbname;
  }

  /*
   * Used in jsp
   */  
  public String getLatestDatabaseJpaVendorName() {
    return latest_databaseJpaVendorName;
  }

  /**
   * Internal linked hash set which has added getLast() method.
   * @author denys
   *
   * @param <E>
   */
  class CachedLinkedHashSet<E> extends LinkedHashSet<E> {
    private static final long serialVersionUID = 1L;
    private E last = null;
    
    public boolean add(E element) {
        last = element;
        return super.add(element);
    }
    public E getLast() {
        return last;
    }

}
}
