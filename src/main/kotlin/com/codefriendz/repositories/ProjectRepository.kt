package com.codefriendz.repositories

import com.codefriendz.models.project.Project
import org.springframework.data.r2dbc.repository.R2dbcRepository
import java.util.*

interface ProjectRepository : R2dbcRepository<Project, UUID> {
}