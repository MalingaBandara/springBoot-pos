package com.bitlord.pos.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table( name = "user_role" )
public class UserRole {

    // role id (primary key)
    @Id
    @Column(name = "role_id", length = 45)
    private String roleId;

    // role name
    @Column(name = "role_name", length = 45, unique = true)
    private String roleName;

    // role description
    @Column(name = "description", length = 250)
    private String description;


    @OneToMany(mappedBy = "userRole", fetch = FetchType.LAZY)
    private Set<UserRoleHasUser> userRoleHasUsers;

}
