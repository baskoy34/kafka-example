package com.volk.Controller;

import com.volk.service.MessageSenderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/message")
public class MessageSenderController {
    private final MessageSenderService senderService;

    @GetMapping
    public ResponseEntity<String> sendMessageAsync(@RequestParam int count) {
        senderService.sendBulkMessages(count);
        return ResponseEntity.ok("Messages Send.");
    }
}
