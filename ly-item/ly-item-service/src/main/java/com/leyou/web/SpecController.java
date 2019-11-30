package com.leyou.web;
import com.leyou.pojo.SpecGroup;
import com.leyou.pojo.SpecParam;
import com.leyou.service.SpecService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * author:  niceyoo
 * blog:    https://cnblogs.com/niceyoo
 * desc:
 */
@RestController
@RequestMapping("spec")
public class SpecController {

    @Autowired
    private SpecService specService;

    /**
     * 规则参数主体
     * @param cid 分类id
     * @return
     */
    @GetMapping("groups/{cid}")
    public ResponseEntity<List<SpecGroup>> querySpecGroups(@PathVariable("cid") Long cid) {
        List<SpecGroup> list = this.specService.querySpecGroups(cid);
        if (list == null || list.size() == 0) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(list);
    }

    /**
     * 规格参数主体中的详细属性
     * @param gid 组id
     * @param cid 分类id
     * @param searching
     * @param generic
     * @return
     */
    @GetMapping("params")//params?gid=1
    public ResponseEntity<List<SpecParam>> querySpecParam(
            @RequestParam(value = "gid", required = false) Long gid,
            @RequestParam(value = "cid", required = false) Long cid,
            @RequestParam(value = "searching", required = false) Boolean searching,
            @RequestParam(value = "generic", required = false) Boolean generic
    ) {
        List<SpecParam> paramList = specService.querySpecParams(gid, cid, searching, generic);
        if (paramList != null && paramList.size() > 0) {
            return ResponseEntity.ok(paramList);
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
