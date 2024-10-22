package dev.plotnikov.polystore.entities.DTOs.usersRoles;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.aspectj.runtime.internal.Conversions.longValue;

@Setter
@Getter
@NoArgsConstructor
public class UsersRolesDTO {
    public static final String ID_ALIAS = "u_id";
    public static final String USER_NAME_ALIAS = "u_user_name";
    public static final String LAST_NAME_ALIAS = "u_last_name";
    public static final String FIRST_NAME_ALIAS = "u_first_name";
    public static final String BIRTH_DATE_ALIAS = "u_birth_date";
    public static final String PHONE_ALIAS = "u_phone";
    public static final String GENDER_ALIAS = "u_gender";

    private Long id;
    private String username;
    private String lastname;
    private String firstname;
    private String birthDate;
    private String phone;
    private String gender;
    private Boolean isAdmin;

    private List<RolesDTO> roles = new ArrayList<>();

    public UsersRolesDTO(Object[] tuples, Map<String, Integer> aliasToIndexMap) {
        this.id = longValue(tuples[aliasToIndexMap.get(ID_ALIAS)]);
        this.username = String.valueOf(tuples[aliasToIndexMap.get(USER_NAME_ALIAS)]);
        this.lastname = String.valueOf(tuples[aliasToIndexMap.get(LAST_NAME_ALIAS)]);
        this.firstname = String.valueOf(tuples[aliasToIndexMap.get(FIRST_NAME_ALIAS)]);
        this.birthDate = String.valueOf(tuples[aliasToIndexMap.get(BIRTH_DATE_ALIAS)]);
        this.phone = String.valueOf(tuples[aliasToIndexMap.get(PHONE_ALIAS)]);;
        this.gender = String.valueOf(tuples[aliasToIndexMap.get(GENDER_ALIAS)]);
    }

    @Override
    public String toString() {
        return "UsersRolesDTO{" +
               "id=" + id +
               ", username='" + username + '\'' +
               ", lastname='" + lastname + '\'' +
               ", firstname='" + firstname + '\'' +
               ", birthDate='" + birthDate + '\'' +
               ", phone='" + phone + '\'' +
               ", gender='" + gender + '\'' +
               ", isAdmin=" + isAdmin +
               ", roles=" + roles +
               '}';
    }
}