package com.locuspark.api.controller;

import com.locuspark.api.dto.response.TicketResponse;
import com.locuspark.api.service.TicketService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/tickets")
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
        TicketResponse response = ticketService.checkOut(companyId, id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<TicketResponse>> getAll(@RequestAttribute("companyId") UUID companyId) {
        return ResponseEntity.ok(ticketService.listAllTicketsByCompany(companyId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TicketResponse> getById(
            @PathVariable UUID id,
            @RequestAttribute("companyId") UUID companyId) {
        return ResponseEntity.ok(ticketService.getTicketByIdAndCompany(id, companyId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable UUID id,
            @RequestAttribute("companyId") UUID companyId) {
        ticketService.deleteTicket(id, companyId);
        return ResponseEntity.noContent().build();
    }

    // Vincula um convênio/parceria a um ticket ativo para aplicação do desconto posterior no checkout
    @PatchMapping("/{id}/partnership")
    public ResponseEntity<TicketResponse> applyPartnership(
            @PathVariable UUID id,
            @RequestAttribute("companyId") UUID companyId,
            @RequestParam UUID partnershipId) {
        return ResponseEntity.ok(ticketService.applyPartnershipToTicket(id, companyId, partnershipId));
    }
}