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
package org.sonar.batch.scan.measure;

import com.google.common.base.Preconditions;
import org.sonar.api.BatchComponent;
import org.sonar.api.measures.Measure;
import org.sonar.api.measures.RuleMeasure;
import org.sonar.api.resources.Resource;
import org.sonar.batch.index.Cache;
import org.sonar.batch.index.Cache.Entry;
import org.sonar.batch.index.Caches;

/**
 * Cache of all measures. This cache is shared amongst all project modules.
 */
public class MeasureCache implements BatchComponent {

  private final Cache<Measure> cache;

  public MeasureCache(Caches caches) {
    cache = caches.createCache("measures");
  }

  public Iterable<Entry<Measure>> entries() {
    return cache.entries();
  }

  public Iterable<Measure> byResource(Resource r) {
    return cache.values(r.getEffectiveKey());
  }

  public Iterable<Measure> byMetric(Resource r, String metricKey) {
    return cache.values(r.getEffectiveKey(), metricKey);
  }

  public MeasureCache put(Resource resource, Measure measure) {
    Preconditions.checkNotNull(resource.getEffectiveKey());
    Preconditions.checkNotNull(measure.getMetricKey());
    cache.put(resource.getEffectiveKey(), measure.getMetricKey(), computeMeasureKey(measure), measure);
    return this;
  }

  public boolean contains(Resource resource, Measure measure) {
    Preconditions.checkNotNull(resource.getEffectiveKey());
    Preconditions.checkNotNull(measure.getMetricKey());
    return cache.containsKey(resource.getEffectiveKey(), measure.getMetricKey(), computeMeasureKey(measure));
  }

  private static String computeMeasureKey(Measure m) {
    StringBuilder sb = new StringBuilder();
    if (m.getMetricKey() != null) {
      sb.append(m.getMetricKey());
    }
    sb.append("|");
    if (m.getCharacteristic() != null) {
      sb.append(m.getCharacteristic().key());
    }
    sb.append("|");
    if (m.getPersonId() != null) {
      sb.append(m.getPersonId());
    }
    if (m instanceof RuleMeasure) {
      sb.append("|");
      sb.append(((RuleMeasure) m).ruleKey());
    }
    return sb.toString();
  }
}
