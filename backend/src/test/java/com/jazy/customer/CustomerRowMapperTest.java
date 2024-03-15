package com.jazy.customer;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerRowMapperTest {

    @Test
    void mapRow() throws SQLException {
        //Given
        CustomerRowMapper rowMapper = new CustomerRowMapper();

        ResultSet mock = mock(ResultSet.class);
        when(mock.getLong("id")).thenReturn(1L);
        when(mock.getString("name")).thenReturn("foo");
        when(mock.getString("email")).thenReturn("foo@email.com");
        when(mock.getInt("age")).thenReturn(19);
        when(mock.getString("gender")).thenReturn(String.valueOf(Gender.MALE));

        //When
        Customer actual = rowMapper.mapRow(mock, 1);

        //Then
        Customer expected = new Customer(1L,"foo","foo@email.com",19,Gender.MALE);
        assertThat(actual).isEqualTo(expected);
    }
}