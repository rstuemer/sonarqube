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

package org.sonar.server.rule2;

import com.google.common.collect.Lists;
import org.apache.ibatis.session.SqlSession;
import org.hibernate.cfg.NotYetImplementedException;
import org.sonar.api.ServerComponent;
import org.sonar.core.persistence.DbSession;
import org.sonar.core.persistence.MyBatis;
import org.sonar.core.qualityprofile.db.ActiveRuleDto;
import org.sonar.core.qualityprofile.db.ActiveRuleKey;
import org.sonar.core.qualityprofile.db.ActiveRuleMapper;
import org.sonar.core.qualityprofile.db.ActiveRuleParamDto;
import org.sonar.core.rule.RuleConstants;
import org.sonar.server.db.BaseDao;

import javax.annotation.CheckForNull;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class ActiveRuleDao extends BaseDao<ActiveRuleMapper, ActiveRuleDto, ActiveRuleKey>
  implements ServerComponent {

  public ActiveRuleDao(MyBatis mybatis) {
    super(ActiveRuleMapper.class, mybatis);
  }

  @Override
  protected String getIndexName() {
    return RuleConstants.INDEX_NAME;
  }

  @Override
  public Iterable<ActiveRuleKey> keysOfRowsUpdatedAfter(long timestamp) {
    throw new NotYetImplementedException("Need to implement ActiveRuleDto.doGetByKey() method");
  }

  @Override
  protected ActiveRuleDto doGetByKey(ActiveRuleKey key, DbSession session) {
    //return getMapper(session).selectByKey(key);
    throw new NotYetImplementedException("Need to implement ActiveRuleDto.doGetByKey() method");
  }

  @Override
  protected ActiveRuleDto doInsert(ActiveRuleDto item, DbSession session) {
    getMapper(session).insert(item);
    return item;
  }

  @Override
  protected ActiveRuleDto doUpdate(ActiveRuleDto item, DbSession session) {
    getMapper(session).update(item);
    return item;
  }

  @Override
  protected void doDelete(ActiveRuleDto item, DbSession session) {
    getMapper(session).delete(item.getId());
  }

  @Override
  protected void doDeleteByKey(ActiveRuleKey key, DbSession session) {
    throw new NotYetImplementedException("Need to implement ActiveRuleDto.doDeleteByKey() method");
  }



  public void delete(int activeRuleId, SqlSession session) {
    session.getMapper(ActiveRuleMapper.class).delete(activeRuleId);
  }

  public void delete(int activeRuleId) {
    SqlSession session = mybatis.openSession(false);
    try {
      delete(activeRuleId, session);
      session.commit();
    } finally {
      MyBatis.closeQuietly(session);
    }
  }

  public void deleteFromRule(int ruleId, SqlSession session) {
    session.getMapper(ActiveRuleMapper.class).deleteFromRule(ruleId);
  }

  public void deleteFromRule(int ruleId) {
    SqlSession session = mybatis.openSession(false);
    try {
      deleteFromRule(ruleId, session);
      session.commit();
    } finally {
      MyBatis.closeQuietly(session);
    }
  }

  public void deleteFromProfile(int profileId, SqlSession session) {
    session.getMapper(ActiveRuleMapper.class).deleteFromProfile(profileId);
  }

  public void deleteFromProfile(int profileId) {
    SqlSession session = mybatis.openSession(false);
    try {
      deleteFromProfile(profileId, session);
      session.commit();
    } finally {
      MyBatis.closeQuietly(session);
    }
  }

  public List<ActiveRuleDto> selectByIds(List<Integer> ids) {
    SqlSession session = mybatis.openSession(false);
    try {
      return selectByIds(ids, session);
    } finally {
      MyBatis.closeQuietly(session);
    }
  }

  public List<ActiveRuleDto> selectByIds(Collection<Integer> ids, SqlSession session) {
    if (ids.isEmpty()) {
      return Collections.emptyList();
    }
    List<ActiveRuleDto> dtosList = newArrayList();
    List<List<Integer>> idsPartitionList = Lists.partition(newArrayList(ids), 1000);
    for (List<Integer> idsPartition : idsPartitionList) {
      List<ActiveRuleDto> dtos = session.selectList("org.sonar.core.qualityprofile.db.ActiveRuleMapper.selectByIds", newArrayList(idsPartition));
      dtosList.addAll(dtos);
    }
    return dtosList;
  }

  public List<ActiveRuleDto> selectAll() {
    SqlSession session = mybatis.openSession(false);
    try {
      return selectAll(session);
    } finally {
      MyBatis.closeQuietly(session);
    }
  }

  public List<ActiveRuleDto> selectAll(SqlSession session) {
    return session.getMapper(ActiveRuleMapper.class).selectAll();
  }

  public List<ActiveRuleDto> selectByRuleId(int ruleId) {
    SqlSession session = mybatis.openSession(false);
    try {
      return selectByRuleId(ruleId, session);
    } finally {
      MyBatis.closeQuietly(session);
    }
  }

  public List<ActiveRuleDto> selectByRuleId(int ruleId, SqlSession session) {
    return session.getMapper(ActiveRuleMapper.class).selectByRuleId(ruleId);
  }

  public List<ActiveRuleDto> selectByProfileId(int profileId) {
    SqlSession session = mybatis.openSession(false);
    try {
      return selectByProfileId(profileId, session);
    } finally {
      MyBatis.closeQuietly(session);
    }
  }

  public List<ActiveRuleDto> selectByProfileId(int profileId, SqlSession session) {
    return session.getMapper(ActiveRuleMapper.class).selectByProfileId(profileId);
  }


  @CheckForNull
  public ActiveRuleDto selectById(int id) {
    SqlSession session = mybatis.openSession(false);
    try {
      return selectById(id, session);
    } finally {
      MyBatis.closeQuietly(session);
    }
  }

  @CheckForNull
  public ActiveRuleDto selectById(int id, SqlSession session) {
    return session.getMapper(ActiveRuleMapper.class).selectById(id);
  }

  @CheckForNull
  public ActiveRuleDto selectByProfileAndRule(int profileId, int ruleId) {
    SqlSession session = mybatis.openSession(false);
    try {
      return selectByProfileAndRule(profileId, ruleId, session);
    } finally {
      MyBatis.closeQuietly(session);
    }
  }

  @CheckForNull
  public ActiveRuleDto selectByProfileAndRule(int profileId, int ruleId, SqlSession session) {
    return session.getMapper(ActiveRuleMapper.class).selectByProfileAndRule(profileId, ruleId);
  }

  public void insert(ActiveRuleParamDto dto, SqlSession session) {
    session.getMapper(ActiveRuleMapper.class).insertParameter(dto);
  }

  public void insert(ActiveRuleParamDto dto) {
    SqlSession session = mybatis.openSession(false);
    try {
      insert(dto, session);
      session.commit();
    } finally {
      MyBatis.closeQuietly(session);
    }
  }

  public void update(ActiveRuleParamDto dto, SqlSession session) {
    session.getMapper(ActiveRuleMapper.class).updateParameter(dto);
  }

  public void update(ActiveRuleParamDto dto) {
    SqlSession session = mybatis.openSession(false);
    try {
      update(dto, session);
      session.commit();
    } finally {
      MyBatis.closeQuietly(session);
    }
  }


  public void deleteParameter(int activeRuleParamId, SqlSession session) {
    session.getMapper(ActiveRuleMapper.class).deleteParameter(activeRuleParamId);
  }

  public void deleteParameter(int activeRuleParamId) {
    SqlSession session = mybatis.openSession(false);
    try {
      deleteParameter(activeRuleParamId, session);
      session.commit();
    } finally {
      MyBatis.closeQuietly(session);
    }
  }

  public void deleteParameters(int activeRuleId, SqlSession session) {
    session.getMapper(ActiveRuleMapper.class).deleteParameters(activeRuleId);
  }

  public void deleteParameters(int activeRuleId) {
    SqlSession session = mybatis.openSession(false);
    try {
      deleteParameters(activeRuleId, session);
      session.commit();
    } finally {
      MyBatis.closeQuietly(session);
    }
  }

  public void deleteParametersWithParamId(int id, SqlSession session) {
    session.getMapper(ActiveRuleMapper.class).deleteParametersWithParamId(id);
  }

  public void deleteParametersFromProfile(int profileId) {
    SqlSession session = mybatis.openSession(false);
    try {
      deleteParametersFromProfile(profileId, session);
      session.commit();
    } finally {
      MyBatis.closeQuietly(session);
    }
  }

  public void deleteParametersFromProfile(int profileId, SqlSession session) {
    session.getMapper(ActiveRuleMapper.class).deleteParametersFromProfile(profileId);
  }

  public ActiveRuleParamDto selectParamById(Integer activeRuleParamId) {
    SqlSession session = mybatis.openSession(false);
    try {
      return session.getMapper(ActiveRuleMapper.class).selectParamById(activeRuleParamId);
    } finally {
      MyBatis.closeQuietly(session);
    }
  }

  public ActiveRuleParamDto selectParamByActiveRuleAndKey(int activeRuleId, String key) {
    SqlSession session = mybatis.openSession(false);
    try {
      return selectParamByActiveRuleAndKey(activeRuleId, key, session);
    } finally {
      MyBatis.closeQuietly(session);
    }
  }

  public ActiveRuleParamDto selectParamByActiveRuleAndKey(int activeRuleId, String key, SqlSession session) {
    return session.getMapper(ActiveRuleMapper.class).selectParamByActiveRuleAndKey(activeRuleId, key);
  }

  public List<ActiveRuleParamDto> selectParamsByActiveRuleId(int activeRuleId) {
    SqlSession session = mybatis.openSession(false);
    try {
      return selectParamsByActiveRuleId(activeRuleId, session);
    } finally {
      MyBatis.closeQuietly(session);
    }
  }

  public List<ActiveRuleParamDto> selectParamsByActiveRuleId(int activeRuleId, SqlSession session) {
    return session.getMapper(ActiveRuleMapper.class).selectParamsByActiveRuleId(activeRuleId);
  }

  public List<ActiveRuleParamDto> selectParamsByActiveRuleIds(List<Integer> activeRuleIds) {
    SqlSession session = mybatis.openSession(false);
    try {
      return selectParamsByActiveRuleIds(activeRuleIds, session);
    } finally {
      MyBatis.closeQuietly(session);
    }
  }

  public List<ActiveRuleParamDto> selectParamsByActiveRuleIds(Collection<Integer> activeRuleIds, SqlSession session) {
    if (activeRuleIds.isEmpty()) {
      return Collections.emptyList();
    }
    List<ActiveRuleParamDto> dtosList = newArrayList();
    List<List<Integer>> idsPartitionList = Lists.partition(newArrayList(activeRuleIds), 1000);
    for (List<Integer> idsPartition : idsPartitionList) {
      List<ActiveRuleParamDto> dtos = session.selectList("org.sonar.core.qualityprofile.db.ActiveRuleMapper.selectParamsByActiveRuleIds", newArrayList(idsPartition));
      dtosList.addAll(dtos);
    }
    return dtosList;
  }

  public List<ActiveRuleParamDto> selectAllParams() {
    SqlSession session = mybatis.openSession(false);
    try {
      return selectAllParams(session);
    } finally {
      MyBatis.closeQuietly(session);
    }
  }

  public List<ActiveRuleParamDto> selectAllParams(SqlSession session) {
    return session.getMapper(ActiveRuleMapper.class).selectAllParams();
  }

  public List<ActiveRuleParamDto> selectParamsByProfileId(int profileId) {
    SqlSession session = mybatis.openSession(false);
    try {
      return session.getMapper(ActiveRuleMapper.class).selectParamsByProfileId(profileId);
    } finally {
      MyBatis.closeQuietly(session);
    }
  }
}
