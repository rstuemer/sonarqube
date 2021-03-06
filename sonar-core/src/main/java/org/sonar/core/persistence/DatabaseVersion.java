/*
 * SonarQube, open source software quality management tool.
 * Copyright (C) 2008-2014 SonarSource
 * mailto:contact AT sonarsource DOT com
 *
 * SonarQube is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * SonarQube is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.sonar.core.persistence;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.ImmutableList;
import org.apache.ibatis.session.SqlSession;
import org.sonar.api.BatchComponent;
import org.sonar.api.ServerComponent;

import java.util.Collections;
import java.util.List;

/**
 * @since 3.0
 */
public class DatabaseVersion implements BatchComponent, ServerComponent {

  public static final int LAST_VERSION = 531;

  public static enum Status {
    UP_TO_DATE, REQUIRES_UPGRADE, REQUIRES_DOWNGRADE, FRESH_INSTALL
  }

  /**
   * List of all the tables.
   * This list is hardcoded because we didn't succeed in using java.sql.DatabaseMetaData#getTables() in the same way
   * for all the supported databases, particularly due to Oracle results.
   */
  public static final List<String> TABLES = ImmutableList.of(
    "action_plans",
    "active_dashboards",
    "active_rules",
    "active_rule_changes",
    "active_rule_parameters",
    "active_rule_param_changes",
    "authors",
    "characteristics",
    "dashboards",
    "dependencies",
    "duplications_index",
    "events",
    "graphs",
    "groups",
    "groups_users",
    "group_roles",
    "issues",
    "issue_changes",
    "issue_filters",
    "issue_filter_favourites",
    "loaded_templates",
    "manual_measures",
    "measure_filters",
    "measure_filter_favourites",
    "metrics",
    "notifications",
    "permission_templates",
    "perm_templates_users",
    "perm_templates_groups",
    "quality_gates",
    "quality_gate_conditions",
    "projects",
    "project_links",
    "project_measures",
    "properties",
    "resource_index",
    "rules",
    "rules_parameters",
    "rules_profiles",
    "rule_tags",
    "rules_rule_tags",
    "semaphores",
    "schema_migrations",
    "snapshots",
    "snapshot_sources",
    "snapshot_data",
    "users",
    "user_roles",
    "widgets",
    "widget_properties"
    );

  private MyBatis mybatis;

  public DatabaseVersion(MyBatis mybatis) {
    this.mybatis = mybatis;
  }

  public Integer getVersion() {
    SqlSession session = mybatis.openSession(false);
    try {
      List<Integer> versions = session.getMapper(SchemaMigrationMapper.class).selectVersions();
      if (!versions.isEmpty()) {
        Collections.sort(versions);
        return versions.get(versions.size() - 1);
      }
      return null;
    } catch (RuntimeException e) {
      // The table SCHEMA_MIGRATIONS does not exist.
      // Ignore this exception -> it will be created by Ruby on Rails migrations.
      return null;

    } finally {
      MyBatis.closeQuietly(session);
    }
  }

  public Status getStatus() {
    return getStatus(getVersion(), LAST_VERSION);
  }

  @VisibleForTesting
  static Status getStatus(Integer currentVersion, int lastVersion) {
    Status status = Status.FRESH_INSTALL;
    if (currentVersion != null) {
      if (currentVersion == lastVersion) {
        status = Status.UP_TO_DATE;
      } else if (currentVersion > lastVersion) {
        status = Status.REQUIRES_DOWNGRADE;
      } else {
        status = Status.REQUIRES_UPGRADE;
      }
    }
    return status;
  }
}
