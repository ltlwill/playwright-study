package org.ltl.demo.web;

import org.ltl.demo.service.CrawlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author Tianlong Liu
 * @date 2023/2/1 15:37
 */
@RestController
@RequestMapping("/crawl")
public class CrawlController {

    @Resource
    private CrawlService crawlService;

    @RequestMapping
    public Object doCrawl() throws Exception{
        return crawlService.crawl();
    }
}
