/*
 *  Copyright 2025-2030 zj
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package stm.bot.config;

import stm.bot.utils.SecurityUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zj
 */
@Service(value = "el")
public class AuthorityConfig {

    /**
     * 判断接口是否有权限
     * @param permissions 权限
     * @return /
     */
    public Boolean check(String ...permissions){
        // 获取当前用户的所有权限
        List<String> elPermissions = SecurityUtils.getCurrentUser().getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        // 判断当前用户的所有权限是否包含接口上定义的权限
        return elPermissions.contains("admin") || Arrays.stream(permissions).anyMatch(elPermissions::contains);
    }
}
