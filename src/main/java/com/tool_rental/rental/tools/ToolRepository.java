package com.tool_rental.rental.tools;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ToolRepository extends CrudRepository<Tool, String> {
    Optional<Tool> findByCode(String code);
}
