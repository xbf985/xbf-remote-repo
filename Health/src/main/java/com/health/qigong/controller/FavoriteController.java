package com.health.qigong.controller;

import com.health.qigong.dto.FavoriteDTO;
import com.health.qigong.result.Result;
import com.health.qigong.service.FavoriteService;
import com.health.qigong.utils.UserContext;
import com.health.qigong.vo.FavoritePageVO;
import com.health.qigong.vo.FavoriteTabVO;
import com.health.qigong.vo.HistoryItemVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/favorite")
public class FavoriteController {

    @Autowired
    private FavoriteService favoriteService;

    @PostMapping("/add")
    public Result<Void> add(@RequestBody FavoriteDTO dto) {
        long userId = UserContext.getUserId();
        favoriteService.add(userId, dto);
        return Result.success();
    }

    @PostMapping("/remove")
    public Result<Void> remove(@RequestBody FavoriteDTO dto) {
        long userId = UserContext.getUserId();
        favoriteService.remove(userId, dto);
        return Result.success();
    }

    @GetMapping("/list")
    public Result<List<HistoryItemVO>> list(@RequestParam(value = "tab", required = false) String tab) {
        long userId = UserContext.getUserId();
        return Result.success(filterByTab(favoriteService.list(userId), tab));
    }

    @GetMapping("/page")
    public Result<FavoritePageVO> page(@RequestParam(value = "tab", defaultValue = "all") String tab) {
        long userId = UserContext.getUserId();
        List<HistoryItemVO> allItems = favoriteService.list(userId);

        FavoritePageVO page = new FavoritePageVO();
        page.setCurrentTab(normalizeTab(tab));
        page.setTabs(buildTabs(allItems));
        page.setItems(filterByTab(allItems, tab));
        return Result.success(page);
    }

    private List<FavoriteTabVO> buildTabs(List<HistoryItemVO> items) {
        int videoCount = (int) items.stream().filter(item -> item.getContentType() == 1).count();
        int recipeCount = (int) items.stream().filter(item -> item.getContentType() == 2).count();
        int communityVideoCount = (int) items.stream().filter(item -> "communityVideo".equals(item.getItemSubType())).count();
        int communityPostCount = (int) items.stream().filter(item -> "communityPost".equals(item.getItemSubType())).count();

        List<FavoriteTabVO> tabs = new ArrayList<>();
        tabs.add(new FavoriteTabVO("all", "全部", items.size()));
        tabs.add(new FavoriteTabVO("video", "视频", videoCount));
        tabs.add(new FavoriteTabVO("communityVideo", "社区视频", communityVideoCount));
        tabs.add(new FavoriteTabVO("communityPost", "社区图文", communityPostCount));
        tabs.add(new FavoriteTabVO("recipe", "药膳食疗", recipeCount));
        return tabs;
    }

    private List<HistoryItemVO> filterByTab(List<HistoryItemVO> items, String tab) {
        String normalized = normalizeTab(tab);
        if ("all".equals(normalized)) {
            return items;
        }
        if ("video".equals(normalized)) {
            return items.stream().filter(item -> item.getContentType() == 1).collect(Collectors.toList());
        }
        if ("recipe".equals(normalized)) {
            return items.stream().filter(item -> item.getContentType() == 2).collect(Collectors.toList());
        }
        if ("communityVideo".equals(normalized)) {
            return items.stream().filter(item -> "communityVideo".equals(item.getItemSubType())).collect(Collectors.toList());
        }
        if ("communityPost".equals(normalized)) {
            return items.stream().filter(item -> "communityPost".equals(item.getItemSubType())).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    private String normalizeTab(String tab) {
        if (tab == null || tab.isBlank()) {
            return "all";
        }
        return switch (tab) {
            case "video", "recipe", "communityVideo", "communityPost" -> tab;
            default -> "all";
        };
    }
}
