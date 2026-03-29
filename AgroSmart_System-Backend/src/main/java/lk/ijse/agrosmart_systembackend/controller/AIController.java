package lk.ijse.agrosmart_systembackend.controller;

import lk.ijse.agrosmart_systembackend.dto.AIRequestDto;
import lk.ijse.agrosmart_systembackend.dto.AIResponseDto;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.google.genai.GoogleGenAiChatOptions;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ai")
@CrossOrigin(origins = "*")
public class AIController {

    private final ChatModel chatModel;

    public AIController(ChatModel chatModel) {
        this.chatModel = chatModel;
    }

    @PostMapping("/generate")
    public ResponseEntity<?> getAIResponse(@RequestBody AIRequestDto aiRequestDto){
        try {
            ChatResponse response = chatModel.call(
                    new Prompt(
                            aiRequestDto.getPrompt(),
                            GoogleGenAiChatOptions.builder()
                                    .temperature(0.4)
                                    .model("gemini-2.5-flash")
                                    .build()
                    )
            );

            AIResponseDto aiResponseDto = new AIResponseDto();
            aiResponseDto.setAiResponse(response.getResult().getOutput().getText());
            return ResponseEntity.ok(aiResponseDto);

        } catch (RuntimeException ex) {
            ex.printStackTrace();

            if (ex.getMessage().contains("429") || ex.getMessage().contains("quota")) {
                return ResponseEntity.status(429)
                        .body("{\"error\": \"AI quota exceeded. Please try again later.\"}");
            }

            return ResponseEntity.status(500)
                    .body("{\"error\": \"" + ex.getMessage() + "\"}");
        }
    }
}