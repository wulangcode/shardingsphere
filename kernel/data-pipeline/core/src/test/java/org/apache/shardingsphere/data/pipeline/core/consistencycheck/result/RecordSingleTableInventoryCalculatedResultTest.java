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

package org.apache.shardingsphere.data.pipeline.core.consistencycheck.result;

import org.apache.shardingsphere.data.pipeline.core.consistencycheck.ConsistencyCheckDataBuilder;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;

class RecordSingleTableInventoryCalculatedResultTest {
    
    @Test
    void assertNotEqualsWithNull() {
        assertFalse(new RecordSingleTableInventoryCalculatedResult(0, Collections.emptyList()).equals(null));
    }
    
    @Test
    void assertEqualsWithSameObject() {
        RecordSingleTableInventoryCalculatedResult calculatedResult = new RecordSingleTableInventoryCalculatedResult(0, Collections.emptyList());
        assertThat(calculatedResult, is(calculatedResult));
    }
    
    @Test
    void assertNotEqualsWithDifferentClassType() {
        RecordSingleTableInventoryCalculatedResult actual = new RecordSingleTableInventoryCalculatedResult(0, Collections.emptyList());
        Object expected = new Object();
        assertThat(actual, not(expected));
    }
    
    @Test
    void assertEqualsWithEmptyRecords() {
        RecordSingleTableInventoryCalculatedResult actual = new RecordSingleTableInventoryCalculatedResult(0, Collections.emptyList());
        RecordSingleTableInventoryCalculatedResult expected = new RecordSingleTableInventoryCalculatedResult(0, Collections.emptyList());
        assertThat(actual, is(expected));
    }
    
    @Test
    void assertEqualsWithFullTypeRecords() {
        RecordSingleTableInventoryCalculatedResult actual = new RecordSingleTableInventoryCalculatedResult(1000, Arrays.asList(buildFixedFullTypeRecord(), buildFixedFullTypeRecord()));
        RecordSingleTableInventoryCalculatedResult expected = new RecordSingleTableInventoryCalculatedResult(1000, Arrays.asList(buildFixedFullTypeRecord(), buildFixedFullTypeRecord()));
        assertThat(actual, is(expected));
    }
    
    @Test
    void assertFullTypeRecordsEqualsWithDifferentDecimalScale() {
        RecordSingleTableInventoryCalculatedResult expected = new RecordSingleTableInventoryCalculatedResult(1000, Collections.singletonList(buildFixedFullTypeRecord()));
        Map<String, Object> recordMap = buildFixedFullTypeRecord();
        recordMap.forEach((key, value) -> {
            if (value instanceof BigDecimal) {
                BigDecimal decimal = (BigDecimal) value;
                recordMap.put(key, decimal.setScale(decimal.scale() + 1, RoundingMode.CEILING));
            }
        });
        RecordSingleTableInventoryCalculatedResult actual = new RecordSingleTableInventoryCalculatedResult(1000, Collections.singletonList(recordMap));
        assertThat(actual, is(expected));
    }
    
    @Test
    void assertNotEqualsWithDifferentRecordsCount() {
        assertThat(new RecordSingleTableInventoryCalculatedResult(1000, Collections.singletonList(buildFixedFullTypeRecord())),
                not(new RecordSingleTableInventoryCalculatedResult(1000, Collections.emptyList())));
    }
    
    @Test
    void assertNotEqualsWithDifferentMaxUniqueKeyValue() {
        assertThat(new RecordSingleTableInventoryCalculatedResult(1000, Collections.singletonList(buildFixedFullTypeRecord())),
                not(new RecordSingleTableInventoryCalculatedResult(1001, Collections.singletonList(buildFixedFullTypeRecord()))));
    }
    
    @Test
    void assertNotEqualsWithDifferentRandomColumnValue() {
        Map<String, Object> record = buildFixedFullTypeRecord();
        RecordSingleTableInventoryCalculatedResult result1 = new RecordSingleTableInventoryCalculatedResult(1000, Collections.singletonList(record));
        record.forEach((key, value) -> {
            RecordSingleTableInventoryCalculatedResult result2 = new RecordSingleTableInventoryCalculatedResult(1000,
                    Collections.singletonList(modifyColumnValueRandomly(buildFixedFullTypeRecord(), key)));
            assertThat(result1, not(result2));
        });
    }
    
    private Map<String, Object> buildFixedFullTypeRecord() {
        return ConsistencyCheckDataBuilder.buildFixedFullTypeRecord(1);
    }
    
    private Map<String, Object> modifyColumnValueRandomly(final Map<String, Object> record, final String key) {
        return ConsistencyCheckDataBuilder.modifyColumnValueRandomly(record, key);
    }
    
    @Test
    void assertHashcode() {
        assertThat(new RecordSingleTableInventoryCalculatedResult(1000, Collections.emptyList()).hashCode(),
                is(new RecordSingleTableInventoryCalculatedResult(1000, Collections.emptyList()).hashCode()));
    }
}
