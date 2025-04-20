/*
 *  Copyright 2019-2025 zj
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
package stm.bot.modules.pay.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import stm.bot.modules.pay.domain.TbPayUser;

/**
 * @author admin
 * @website https://awss.vip.com
 * @date 2025-04-20
 **/
public interface TbPayUserRepository extends JpaRepository<TbPayUser, Integer>, JpaSpecificationExecutor<TbPayUser> {
    TbPayUser findBySysMerchant(String sysMerchant);
}