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
import stm.bot.modules.pay.domain.TbPayTransaction;
import stm.bot.modules.pay.service.TbPayTransactionService;
import stm.bot.modules.pay.service.dto.TbPayTransactionQueryCriteria;
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
import stm.bot.modules.pay.service.dto.TbPayTransactionDto;

/**
* @website https://awss.vip.com
* @author admin
* @date 2025-04-18
**/
@RestController
@RequiredArgsConstructor
@Api(tags = "pay订单")
@RequestMapping("/api/tbPayTransaction")
public class TbPayTransactionController {

    private final TbPayTransactionService tbPayTransactionService;

    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('tbPayTransaction:list')")
    public void exportTbPayTransaction(HttpServletResponse response, TbPayTransactionQueryCriteria criteria) throws IOException {
        tbPayTransactionService.download(tbPayTransactionService.queryAll(criteria), response);
    }

    @GetMapping
    @ApiOperation("查询pay订单")
    @PreAuthorize("@el.check('tbPayTransaction:list')")
    public ResponseEntity<PageResult<TbPayTransactionDto>> queryTbPayTransaction(TbPayTransactionQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(tbPayTransactionService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增pay订单")
    @ApiOperation("新增pay订单")
    @PreAuthorize("@el.check('tbPayTransaction:add')")
    public ResponseEntity<Object> createTbPayTransaction(@Validated @RequestBody TbPayTransaction resources){
        tbPayTransactionService.create(resources);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改pay订单")
    @ApiOperation("修改pay订单")
    @PreAuthorize("@el.check('tbPayTransaction:edit')")
    public ResponseEntity<Object> updateTbPayTransaction(@Validated @RequestBody TbPayTransaction resources){
        tbPayTransactionService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    @Log("删除pay订单")
    @ApiOperation("删除pay订单")
    @PreAuthorize("@el.check('tbPayTransaction:del')")
    public ResponseEntity<Object> deleteTbPayTransaction(@ApiParam(value = "传ID数组[]") @RequestBody Integer[] ids) {
        tbPayTransactionService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}