package com.health.qigong.controller;

import com.health.qigong.dto.TrainingRoomCompleteDTO;
import com.health.qigong.dto.TrainingRoomEnterDTO;
import com.health.qigong.result.Result;
import com.health.qigong.service.FileStorageService;
import com.health.qigong.service.TrainingRoomService;
import com.health.qigong.utils.UserContext;
import com.health.qigong.vo.TrainingRoomConfigVO;
import com.health.qigong.vo.TrainingRoomHistoryItemVO;
import com.health.qigong.vo.TrainingRoomSessionVO;
import com.health.qigong.vo.TrainingRoomSummaryVO;
import com.health.qigong.vo.UploadFileVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/training-room")
public class TrainingRoomController {

    @Autowired
    private TrainingRoomService trainingRoomService;

    @Autowired
    private FileStorageService fileStorageService;

    @GetMapping("/config")
    public Result<TrainingRoomConfigVO> config() {
        return Result.success(trainingRoomService.getConfig());
    }

    @PostMapping("/upload")
    public Result<UploadFileVO> upload(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "folder", defaultValue = "training-room") String folder
    ) {
        return Result.success(fileStorageService.store(file, folder));
    }

    @PostMapping("/enter")
    public Result<TrainingRoomSessionVO> enter(@RequestBody TrainingRoomEnterDTO dto) {
        Long userId = UserContext.getUserId();
        return Result.success(trainingRoomService.enter(userId, dto));
    }

    @PostMapping("/sessions/{sessionId}/complete")
    public Result<TrainingRoomSummaryVO> complete(@PathVariable("sessionId") Long sessionId,
                                                  @RequestBody TrainingRoomCompleteDTO dto) {
        Long userId = UserContext.getUserId();
        return Result.success(trainingRoomService.complete(userId, sessionId, dto));
    }

    @PostMapping(value = "/sessions/{sessionId}/complete", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Result<TrainingRoomSummaryVO> completeWithFile(
            @PathVariable("sessionId") Long sessionId,
            @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestParam(value = "coverFile", required = false) MultipartFile coverFile,
            @RequestParam(value = "durationSeconds", required = false) Integer durationSeconds,
            @RequestParam(value = "caloriesBurned", required = false) Integer caloriesBurned,
            @RequestParam(value = "postureScore", required = false) Integer postureScore,
            @RequestParam(value = "breathScore", required = false) Integer breathScore,
            @RequestParam(value = "postureIssues", required = false) String postureIssues,
            @RequestParam(value = "breathIssues", required = false) String breathIssues,
            @RequestParam(value = "practiceVideoUrl", required = false) String practiceVideoUrl,
            @RequestParam(value = "practiceVideoCover", required = false) String practiceVideoCover,
            @RequestParam(value = "practiceVideoDurationSeconds", required = false) Integer practiceVideoDurationSeconds
    ) {
        Long userId = UserContext.getUserId();
        TrainingRoomCompleteDTO dto = new TrainingRoomCompleteDTO();
        dto.setDurationSeconds(durationSeconds);
        dto.setCaloriesBurned(caloriesBurned);
        dto.setPostureScore(postureScore);
        dto.setBreathScore(breathScore);
        dto.setPostureIssues(parseIssueText(postureIssues));
        dto.setBreathIssues(parseIssueText(breathIssues));
        dto.setPracticeVideoDurationSeconds(practiceVideoDurationSeconds);

        if (file != null && !file.isEmpty()) {
            dto.setPracticeVideoUrl(fileStorageService.store(file, "training-room").getUrl());
        } else {
            dto.setPracticeVideoUrl(blankToNull(practiceVideoUrl));
        }

        if (coverFile != null && !coverFile.isEmpty()) {
            dto.setPracticeVideoCover(fileStorageService.store(coverFile, "training-room").getUrl());
        } else {
            dto.setPracticeVideoCover(blankToNull(practiceVideoCover));
        }

        return Result.success(trainingRoomService.complete(userId, sessionId, dto));
    }

    @GetMapping("/history")
    public Result<List<TrainingRoomHistoryItemVO>> history() {
        Long userId = UserContext.getUserId();
        return Result.success(trainingRoomService.history(userId));
    }

    @GetMapping("/history/{sessionId}")
    public Result<TrainingRoomSummaryVO> historyDetail(@PathVariable("sessionId") Long sessionId) {
        Long userId = UserContext.getUserId();
        return Result.success(trainingRoomService.getSummary(userId, sessionId));
    }

    private List<String> parseIssueText(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        String normalized = value.trim();
        if (normalized.startsWith("[") && normalized.endsWith("]")) {
            normalized = normalized.substring(1, normalized.length() - 1);
        }
        return Stream.of(normalized.split("[,，]"))
                .map(String::trim)
                .map(item -> item.replace("\"", ""))
                .filter(item -> !item.isBlank())
                .collect(Collectors.toList());
    }

    private String blankToNull(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
    }
}
