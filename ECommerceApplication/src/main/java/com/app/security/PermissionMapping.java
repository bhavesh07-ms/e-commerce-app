package com.app.security;
import com.app.enums.Permissions;
import com.app.enums.Role;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.app.enums.Permissions.*;
import static com.app.enums.Role.*;


public class PermissionMapping {
    private static final Map<Role, Set<Permissions>> map = Map.of(
            ADMIN, Set.of(USER_VIEW, USER_CREATE,USER_DELETE,PRODUCT_CREATE,PRODUCT_UPDATE,PRODUCT_DELETE,CART_ADD,ORDER_CREATE,ORDER_VIEW,ORDER_UPDATE,ORDER_CANCEL,DELIVERY_VIEW,DELIVERY_UPDATE),
            SHOPKEEPER, Set.of(USER_VIEW, USER_CREATE,PRODUCT_CREATE,PRODUCT_UPDATE,PRODUCT_DELETE,ORDER_VIEW,ORDER_UPDATE,DELIVERY_VIEW,GREEN_PATH_VIEW),
            CUSTOMER, Set.of(USER_VIEW, USER_CREATE, CART_ADD, ORDER_CREATE, ORDER_VIEW, ORDER_CANCEL,DELIVERY_VIEW),
            DELIVERY_AGENT,Set.of(ORDER_VIEW,ORDER_UPDATE,DELIVERY_VIEW,DELIVERY_UPDATE,GREEN_PATH_VIEW),
            SUPPORT_AGENT,Set.of(USER_VIEW,ORDER_VIEW,ORDER_UPDATE,ORDER_CANCEL)
    );

    public static Set<SimpleGrantedAuthority> getAuthoritiesForRole(Role role) {
        return map.get(role).stream()
                .map(permission -> new SimpleGrantedAuthority(permission.name()))
                .collect(Collectors.toSet());
    }



}
