/*
 * Table Wrapper CSV Impl
 * Copyright (C) 2022  Spacious Team <spacious-team@ya.ru>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package org.spacious_team.table_wrapper.csv;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.spacious_team.table_wrapper.api.InstantParser;

import static java.time.ZoneOffset.UTC;
import static nl.jqno.equalsverifier.Warning.STRICT_INHERITANCE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CsvCellDataAccessObjectTest {

    CsvCellDataAccessObject dao;
    InstantParser instantParser;
    CsvTableRow row;

    void setUp() {
        instantParser = mock(InstantParser.class);
        dao = spy(CsvCellDataAccessObject.of(instantParser));
    }

    void getCellNull() {
        //noinspection ConstantConditions
        assertNull(dao.getCell(row, null));
        assertNull(dao.getCell(row, 1));
    }

    void getCellNonNull() {
        String expected = "data";
        CsvTableRow row = CsvTableRow.of(new String[]{"", expected}, 1);
        assertEquals(expected, dao.getCell(row, 1));
    }

    void getValue() {
        assertEquals("test", dao.getValue("test"));
    }

    void getInstantValueNull() {
        assertThrows(NullPointerException.class, () -> dao.getInstantValue(null));
    }

    void getInstant() {
        String actual = "test";

        dao.getInstantValue(actual);

        verify(dao).getValue(actual);
        verify(instantParser).parseInstant(actual);
    }

    void testEqualsAndHashCode() {
        EqualsVerifier
                .forClass(CsvCellDataAccessObject.class)
                .suppress(STRICT_INHERITANCE) // no subclass for test
                .verify();
    }

    void testToString() {
        InstantParser instantParser = InstantParser.builder().defaultZoneId(UTC).build();
        assertEquals("CsvCellDataAccessObject(instantParser=InstantParser(" +
                        "dateTimeFormatter=null, defaultDate=null, defaultTime=00:00, defaultZoneId=Z))",
                CsvCellDataAccessObject.of(instantParser).toString());
    }
}
