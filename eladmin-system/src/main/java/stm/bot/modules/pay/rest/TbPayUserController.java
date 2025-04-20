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
package stm.bot.modules.pay.rest;

import stm.bot.annotation.Log;
import stm.bot.modules.pay.domain.TbPayUser;
import stm.bot.modules.pay.service.TbPayUserService;
import stm.bot.modules.pay.service.dto.TbPayUserQueryCriteria;
import org.springframework.data.domain.Pageable;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.*;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import stm.bot.utils.PageResult;
import stm.bot.modules.pay.service.dto.TbPayUserDto;

/**
* @website https://awss.vip.com
* @author admin
* @date 2025-04-20
**/
@RestController
@RequiredArgsConstructor
@Api(tags = "支付用户")
@RequestMapping("/api/tbPayUser")
public class TbPayUserController {

    private final TbPayUserService tbPayUserService;

    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('tbPayUser:list')")
    public void exportTbPayUser(HttpServletResponse response, TbPayUserQueryCriteria criteria) throws IOException {
        tbPayUserService.download(tbPayUserService.queryAll(criteria), response);
    }

    @GetMapping
    @ApiOperation("查询支付用户")
    @PreAuthorize("@el.check('tbPayUser:list')")
    public ResponseEntity<PageResult<TbPayUserDto>> queryTbPayUser(TbPayUserQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(tbPayUserService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增支付用户")
    @ApiOperation("新增支付用户")
    @PreAuthorize("@el.check('tbPayUser:add')")
    public ResponseEntity<Object> createTbPayUser(@Validated @RequestBody TbPayUser resources){
        tbPayUserService.create(resources);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改支付用户")
    @ApiOperation("修改支付用户")
    @PreAuthorize("@el.check('tbPayUser:edit')")
    public ResponseEntity<Object> updateTbPayUser(@Validated @RequestBody TbPayUser resources){
        tbPayUserService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    @Log("删除支付用户")
    @ApiOperation("删除支付用户")
    @PreAuthorize("@el.check('tbPayUser:del')")
    public ResponseEntity<Object> deleteTbPayUser(@ApiParam(value = "传ID数组[]") @RequestBody Integer[] ids) {
        tbPayUserService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}