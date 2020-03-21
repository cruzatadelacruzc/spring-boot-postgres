package com.example.oauth2.service.dto;

import com.example.oauth2.web.TestUtil;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link UserDTO}.
 */
public class UserDTOTest {
    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserDTO.class);
        UserDTO userDTO1 = new UserDTO();
        userDTO1.setId(1L);
        UserDTO userDTO2 = new UserDTO();
        userDTO2.setId(userDTO1.getId());
        assertThat(userDTO1).isEqualTo(userDTO2);
        userDTO2.setId(2L);
        assertThat(userDTO1).isNotEqualTo(userDTO2);
        userDTO2.setId(null);
        assertThat(userDTO1).isNotEqualTo(userDTO2);
    }
}
