package com.locuspark.api.controller;

import com.locuspark.api.dto.response.ReportResponse;
import com.locuspark.api.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @GetMapping
    public ResponseEntity<ReportResponse> getReport(@RequestAttribute("companyId") UUID companyId) {
        ReportResponse response = reportService.getCompanyReport(companyId);
        return ResponseEntity.ok(response);
    }
}
