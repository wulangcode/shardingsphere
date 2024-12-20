/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.shardingsphere.mode.metadata.refresher.type.index;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import org.apache.shardingsphere.infra.config.props.ConfigurationProperties;
import org.apache.shardingsphere.infra.database.core.type.DatabaseType;
import org.apache.shardingsphere.infra.metadata.database.ShardingSphereDatabase;
import org.apache.shardingsphere.infra.metadata.database.schema.model.ShardingSphereIndex;
import org.apache.shardingsphere.infra.metadata.database.schema.model.ShardingSphereTable;
import org.apache.shardingsphere.infra.metadata.database.schema.pojo.AlterSchemaMetaDataPOJO;
import org.apache.shardingsphere.infra.metadata.database.schema.util.IndexMetaDataUtils;
import org.apache.shardingsphere.mode.metadata.refresher.MetaDataRefresher;
import org.apache.shardingsphere.mode.persist.service.MetaDataManagerPersistService;
import org.apache.shardingsphere.sql.parser.statement.core.statement.ddl.CreateIndexStatement;

import java.util.Collection;
import java.util.LinkedList;

/**
 * Schema refresher for create index statement.
 */
public final class CreateIndexStatementSchemaRefresher implements MetaDataRefresher<CreateIndexStatement> {
    
    @Override
    public void refresh(final MetaDataManagerPersistService metaDataManagerPersistService, final ShardingSphereDatabase database, final Collection<String> logicDataSourceNames,
                        final String schemaName, final DatabaseType databaseType, final CreateIndexStatement sqlStatement, final ConfigurationProperties props) {
        String indexName = null == sqlStatement.getIndex()
                ? IndexMetaDataUtils.getGeneratedLogicIndexName(sqlStatement.getColumns())
                : sqlStatement.getIndex().getIndexName().getIdentifier().getValue();
        Preconditions.checkArgument(!Strings.isNullOrEmpty(indexName), "Index name is not exist.");
        String tableName = sqlStatement.getTable().getTableName().getIdentifier().getValue();
        ShardingSphereTable table = newShardingSphereTable(database.getSchema(schemaName).getTable(tableName));
        table.putIndex(new ShardingSphereIndex(indexName, new LinkedList<>(), false));
        AlterSchemaMetaDataPOJO alterSchemaMetaDataPOJO = new AlterSchemaMetaDataPOJO(database.getName(), schemaName);
        alterSchemaMetaDataPOJO.getAlteredTables().add(table);
        metaDataManagerPersistService.alterSchemaMetaData(alterSchemaMetaDataPOJO);
    }
    
    private ShardingSphereTable newShardingSphereTable(final ShardingSphereTable table) {
        ShardingSphereTable result = new ShardingSphereTable(table.getName(), table.getAllColumns(), table.getAllIndexes(), table.getAllConstraints(), table.getType());
        result.getColumnNames().addAll(table.getColumnNames());
        result.getVisibleColumns().addAll(table.getVisibleColumns());
        result.getPrimaryKeyColumns().addAll(table.getPrimaryKeyColumns());
        return result;
    }
    
    @Override
    public Class<CreateIndexStatement> getType() {
        return CreateIndexStatement.class;
    }
}
