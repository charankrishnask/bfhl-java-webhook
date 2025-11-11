package com.example.webhook.repository;

import com.example.webhook.entity.ResultEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResultRepository extends JpaRepository<ResultEntity, Long> { }
