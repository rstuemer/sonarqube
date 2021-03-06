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
package org.sonar.server.search;

import java.io.Serializable;

public class KeyIndexAction<K extends Serializable> extends IndexAction {

  private final K key;

  public KeyIndexAction(String indexName, Method method, K key) {
    super(indexName, method);
    this.key = key;
  }

  @Override
  public void doExecute() {
    try {
      if (this.getMethod().equals(Method.DELETE)) {
        index.deleteByKey(this.key);
      } else if (this.getMethod().equals(Method.INSERT)) {
        index.insertByKey(this.key);
      } else if (this.getMethod().equals(Method.UPDATE)) {
        index.updateByKey(this.key);
      }
    } catch (Exception e) {
      throw new IllegalStateException("Index " + this.getIndexName() + " cannot execute " +
        this.getMethod() + " for " + key);
    }
  }
}
