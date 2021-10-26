package com.github.dogsunny.beetlsqlidea.services

import com.intellij.openapi.project.Project
import com.github.dogsunny.beetlsqlidea.MyBundle

class MyProjectService(project: Project) {

    init {
        println(MyBundle.message("projectService", project.name))
    }
}
