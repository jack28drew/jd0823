ALTER TABLE tools
    ADD CONSTRAINT fk_tools_type FOREIGN KEY (type) REFERENCES tool_types(type);
