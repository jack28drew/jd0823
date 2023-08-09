package com.tool_rental.rental.tools;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ToolRepositoryTest {

    @Autowired
    private ToolRepository toolRepository;

    @Test
    void testRoundTrip() {
        var toolCode = "ABC";

        var tool = new Tool(
                toolCode,
                new ToolType(
                        "drill",
                        999,
                        true,
                        true,
                        true
                ),
                "DeWalt"
        );

        toolRepository.save(tool);
        var savedTool = toolRepository.findByCode(toolCode);

        assertThat(savedTool)
                .get()
                .usingRecursiveComparison()
                .isEqualTo(tool);
    }

}