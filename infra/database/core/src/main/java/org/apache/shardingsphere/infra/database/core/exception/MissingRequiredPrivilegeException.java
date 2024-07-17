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

package org.apache.shardingsphere.infra.database.core.exception;

import org.apache.shardingsphere.infra.exception.core.external.sql.sqlstate.XOpenSQLState;
import org.apache.shardingsphere.infra.exception.core.external.sql.type.kernel.category.MetaDataSQLException;

import java.util.Collection;

/**
 * Missing required privilege exception.
 */
public final class MissingRequiredPrivilegeException extends MetaDataSQLException {
    
    private static final long serialVersionUID = 3755362278200749857L;
    
    public MissingRequiredPrivilegeException(final Collection<String> privileges) {
        super(XOpenSQLState.PRIVILEGE_NOT_GRANTED, 5, "Missing required privilege(s) `%s`.", privileges);
    }
}
