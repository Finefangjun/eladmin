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
import stm.bot.modules.pay.domain.TzPayMethod;
import stm.bot.modules.pay.service.TzPayMethodService;
import stm.bot.modules.pay.service.dto.TzPayMethodQueryCriteria;
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
import stm.bot.modules.pay.service.dto.TzPayMethodDto;

/**
* @website https://awss.vip.com
* @author admin
* @date 2025-04-18
**/
@RestController
@RequiredArgsConstructor
@Api(tags = "支付配置")
@RequestMapping("/api/tzPayMethod")
public class TzPayMethodController {

    private final TzPayMethodService tzPayMethodService;

    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('tzPayMethod:list')")
    public void exportTzPayMethod(HttpServletResponse response, TzPayMethodQueryCriteria criteria) throws IOException {
        tzPayMethodService.download(tzPayMethodService.queryAll(criteria), response);
    }

    @GetMapping
    @ApiOperation("查询支付配置")
    @PreAuthorize("@el.check('tzPayMethod:list')")
    public ResponseEntity<PageResult<TzPayMethodDto>> queryTzPayMethod(TzPayMethodQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(tzPayMethodService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增支付配置")
    @ApiOperation("新增支付配置")
    @PreAuthorize("@el.check('tzPayMethod:add')")
    public ResponseEntity<Object> createTzPayMethod(@Validated @RequestBody TzPayMethod resources){
        tzPayMethodService.create(resources);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改支付配置")
    @ApiOperation("修改支付配置")
    @PreAuthorize("@el.check('tzPayMethod:edit')")
    public ResponseEntity<Object> updateTzPayMethod(@Validated @RequestBody TzPayMethod resources){
        tzPayMethodService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    @Log("删除支付配置")
    @ApiOperation("删除支付配置")
    @PreAuthorize("@el.check('tzPayMethod:del')")
    public ResponseEntity<Object> deleteTzPayMethod(@ApiParam(value = "传ID数组[]") @RequestBody Integer[] ids) {
        tzPayMethodService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}