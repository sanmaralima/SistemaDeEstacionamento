package com.locuspark.api.controller;

import com.locuspark.api.dto.response.TicketResponse;
import com.locuspark.api.service.TicketService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PostMapping("/check-in")
    public ResponseEntity<TicketResponse> checkIn(
            @RequestAttribute("companyId") UUID companyId,
            @RequestParam UUID vehicleId) {

        TicketResponse response = ticketService.checkIn(companyId, vehicleId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/{id}/check-out")
    public ResponseEntity<TicketResponse> checkOut(
            @PathVariable UUID id,
            @RequestAttribute("companyId") UUID companyId) {

        TicketResponse response = ticketService.checkOut(id, companyId);
        return ResponseEntity.ok(response);
    }
}