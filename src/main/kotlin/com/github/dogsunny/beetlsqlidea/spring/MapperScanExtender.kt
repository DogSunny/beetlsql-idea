package com.github.dogsunny.beetlsqlidea.spring

import com.intellij.openapi.module.Module
import com.intellij.psi.JavaPsiFacade
import com.intellij.psi.PsiClass
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.search.searches.AnnotatedElementsSearch
import com.intellij.psi.search.searches.ClassInheritorsSearch
import com.intellij.spring.boot.application.yaml.SpringBootApplicationYamlAnnotator
import com.intellij.spring.model.custom.ComponentScanExtender
import com.intellij.spring.model.jam.stereotype.CustomSpringComponent
import com.intellij.spring.model.jam.stereotype.SpringStereotypeElement


class MapperScanExtender : ComponentScanExtender() {
    companion object {
        const val BASE_MAPPER_CLASS_NAME = "org.beetl.sql.core.mapper.BaseMapper"
    }
    override fun getComponents(scope: GlobalSearchScope, module: Module): Collection<SpringStereotypeElement?> {
        val searchScope = GlobalSearchScope.moduleWithDependenciesAndLibrariesScope(module)
        val baseMapperPsiClass: PsiClass = JavaPsiFacade.getInstance(module.project).findClass(BASE_MAPPER_CLASS_NAME, searchScope) ?: return emptyList()
        return ClassInheritorsSearch.search(baseMapperPsiClass).findAll().map { CustomSpringComponent(it) }
    }
}